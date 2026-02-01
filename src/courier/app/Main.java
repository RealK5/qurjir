package courier.app;

import courier.domain.*;
import courier.service.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   SYSTEM ZARZĄDZANIA PRZESYŁKAMI QURJIR   ║");
        System.out.println("╚════════════════════════════════════════════╝\n");

        // Initialize services
        RoutingService routingService = new RoutingService();
        TrackingService trackingService = new TrackingService();

        // Create addresses
        Address warsawHub = new Address("ul. Logistyki 1", "Warszawa", "02-000", "Polska");
        Address krakowHub = new Address("ul. Kurierska 5", "Kraków", "31-000", "Polska");

        Address client1 = new Address("ul. Puławskiego 10", "Warszawa", "02-000", "Polska");
        Address client2 = new Address("ul. Długa 20", "Kraków", "31-000", "Polska");
        Address client3 = new Address("al. Jerozolimskie 30", "Warszawa", "02-000", "Polska");

        System.out.println("1. TWORZENIE PACZEK\n" + "-".repeat(50));

        // Create standard parcel
        StandardParcel parcel1 = new StandardParcel(
                "PKG001",
                2.5,
                "20x15x10 cm",
                warsawHub,
                client1
        );
        System.out.println("✓ " + parcel1);
        System.out.println("  Koszt dostawy: " + String.format("%.2f zł", parcel1.calculateShippingCost()));
        System.out.println("  Wymagania: " + parcel1.getHandlingRequirements());

        // Create fragile parcel
        FragileParcel parcel2 = new FragileParcel(
                "PKG002",
                5.0,
                "25x20x15 cm",
                warsawHub,
                client2
        );
        System.out.println("\n✓ " + parcel2);
        System.out.println("  Koszt dostawy: " + String.format("%.2f zł", parcel2.calculateShippingCost()));
        System.out.println("  Wymagania:\n" + parcel2.getHandlingRequirements());

        // Create another standard parcel
        StandardParcel parcel3 = new StandardParcel(
                "PKG003",
                1.2,
                "15x10x8 cm",
                krakowHub,
                client3
        );
        System.out.println("\n✓ " + parcel3);
        System.out.println("  Koszt dostawy: " + String.format("%.2f zł", parcel3.calculateShippingCost()));

        // Register all parcels
        trackingService.registerParcel(parcel1);
        trackingService.registerParcel(parcel2);
        trackingService.registerParcel(parcel3);

        System.out.println("\n2. TWORZENIE TRAS\n" + "-".repeat(50));

        Route route1 = routingService.createRoute("ROUTE001", "COURIER001", warsawHub);
        System.out.println("✓ Trasa " + route1.getId() + " stworzona (kurier: " + route1.getCourierId() + ")");

        Route route2 = routingService.createRoute("ROUTE002", "COURIER002", krakowHub);
        System.out.println("✓ Trasa " + route2.getId() + " stworzona (kurier: " + route2.getCourierId() + ")");

        System.out.println("\n3. PRZYPISYWANIE PACZEK DO TRAS\n" + "-".repeat(50));

        routingService.assignParcelToRoute("ROUTE001", parcel1);
        System.out.println("✓ Paczka PKG001 przypisana do ROUTE001");

        routingService.assignParcelToRoute("ROUTE001", parcel2);
        System.out.println("✓ Paczka PKG002 przypisana do ROUTE001");

        routingService.assignParcelToRoute("ROUTE002", parcel3);
        System.out.println("✓ Paczka PKG003 przypisana do ROUTE002");

        System.out.println("\n4. INFORMACJE O TRASACH\n" + "-".repeat(50));

        for (Route route : routingService.getAllRoutes()) {
            System.out.printf("%s: %d paczek, waga: %.2f kg%n",
                    route.getId(),
                    route.getParcelCount(),
                    route.getTotalWeight());
        }

        System.out.println("\n5. ROZPOCZĘCIE TRAS\n" + "-".repeat(50));

        route1.start();
        System.out.println("✓ Trasa ROUTE001 rozpoczęta");

        route2.start();
        System.out.println("✓ Trasa ROUTE002 rozpoczęta");

        System.out.println("\n6. SYMULACJA DOSTARCZEŃ\n" + "-".repeat(50));

        // Simulate deliveries
        route1.logDeliveryAttempt(parcel1, true);
        System.out.println("✓ Dostarczono PKG001");

        route1.logDeliveryAttempt(parcel2, false);
        System.out.println("✗ Nie udało się dostarczyć PKG002 (brak osoby do odebrania)");

        route2.logDeliveryAttempt(parcel3, true);
        System.out.println("✓ Dostarczono PKG003");

        System.out.println("\n7. RAPORT ŚLEDZENIA\n" + "-".repeat(50));

        trackingService.printStatusReport("PKG001");
        trackingService.printStatusReport("PKG002");

        System.out.println("\n8. STATYSTYKI\n" + "-".repeat(50));

        System.out.println("Łączna liczba paczek: " + routingService.getTotalParcelsCount());
        System.out.println("Łączna waga: " + String.format("%.2f kg", routingService.getTotalParcelsWeight()));
        System.out.println("Dystrybucja statusów:");

        trackingService.getStatusDistribution().forEach((status, count) ->
                System.out.println("  - " + status.getDisplayName() + ": " + count));

        System.out.println("\n9. EKSPORT DO CSV\n" + "-".repeat(50));

        try {
            String csv = route1.exportToCSV();
            System.out.println("CSV Route 1:\n" + csv);

            saveToFile("route1_report.csv", csv);
            System.out.println("✓ Raport zapisany do route1_report.csv");
        } catch (Exception e) {
            System.err.println("✗ Błąd: " + e.getMessage());
        }

        System.out.println("\n10. CENNIK\n" + "-".repeat(50));

        PricingStrategy basePrice = new BasePricingStrategy();
        PricingStrategy discount = new DiscountPricingStrategy(10);

        System.out.println("PKG001 (" + basePrice.getStrategyName() + "): " +
                String.format("%.2f zł", basePrice.calculatePrice(parcel1)));
        System.out.println("PKG001 (" + discount.getStrategyName() + "): " +
                String.format("%.2f zł", discount.calculatePrice(parcel1)));

        System.out.println("\n11. POWIADOMIENIA\n" + "-".repeat(50));

        Notifier emailNotifier = new EmailNotifier("client@example.com");
        Notifier smsNotifier = new SMSNotifier("+48123456789");

        emailNotifier.notifyStatusChange(parcel1, Status.PENDING, Status.PICKED_UP);
        smsNotifier.notifyStatusChange(parcel1, Status.PICKED_UP, Status.IN_TRANSIT);

        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║            DEMO ZAKOŃCZONE                ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
    }

    private static void saveToFile(String filename, String content) throws java.io.IOException {
        try (java.io.FileWriter writer = new java.io.FileWriter(filename)) {
            writer.write(content);
        }
    }
}
