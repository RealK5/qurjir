package courier.service;

import courier.domain.*;
import java.util.*;

public class TrackingService {
    private final Map<String, Parcel> trackedParcels;

    public TrackingService() {
        this.trackedParcels = new HashMap<>();
    }

    public void registerParcel(Parcel parcel) {
        if (parcel == null) {
            throw new IllegalArgumentException("Paczka nie może być null");
        }
        trackedParcels.put(parcel.getId(), parcel);
    }

    public Parcel getParcelStatus(String parcelId) {
        Parcel parcel = trackedParcels.get(parcelId);
        if (parcel == null) {
            throw new IllegalArgumentException("Paczka " + parcelId + " nie znaleziona");
        }
        return parcel;
    }

    public Status getCurrentStatus(String parcelId) {
        return getParcelStatus(parcelId).getCurrentStatus();
    }

    public List<Parcel.StatusHistory> getStatusHistory(String parcelId) {
        return getParcelStatus(parcelId).getStatusHistory();
    }

    public void updateParcelStatus(String parcelId, Status newStatus) {
        Parcel parcel = getParcelStatus(parcelId);
        parcel.updateStatus(newStatus);
    }

    public List<Parcel> getParcelsByStatus(Status status) {
        return trackedParcels.values().stream()
                .filter(p -> p.getCurrentStatus() == status)
                .toList();
    }

    public void printStatusReport(String parcelId) {
        Parcel parcel = getParcelStatus(parcelId);
        System.out.println("\n" + "=".repeat(60));
        System.out.println("RAPORT ŚLEDZENIA: " + parcelId);
        System.out.println("=".repeat(60));
        System.out.println("Od: " + parcel.getSenderAddress());
        System.out.println("Do:  " + parcel.getRecipientAddress());
        System.out.println("Typ: " + parcel.getClass().getSimpleName());
        System.out.println("Waga: " + String.format("%.2f kg", parcel.getWeight()));
        System.out.println("Wymiary: " + parcel.getDimensions());
        System.out.println("\nObsługa:\n" + parcel.getHandlingRequirements());
        System.out.println("\nHistoria Statusów:");
        System.out.println("-".repeat(60));

        for (Parcel.StatusHistory history : parcel.getStatusHistory()) {
            System.out.printf("  [%s] %s%n",
                    history.getFormattedTimestamp(),
                    history.getStatus().getDisplayName());
        }
        System.out.println("=".repeat(60) + "\n");
    }

    public int getTotalTrackedParcels() {
        return trackedParcels.size();
    }

    public Map<Status, Long> getStatusDistribution() {
        return trackedParcels.values().stream()
                .collect(java.util.stream.Collectors
                        .groupingByConcurrent(
                                Parcel::getCurrentStatus,
                                java.util.stream.Collectors.counting()));
    }
}
