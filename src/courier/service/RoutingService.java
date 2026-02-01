package courier.service;

import courier.domain.Parcel;
import courier.domain.Route;
import courier.domain.Address;

import java.util.*;

public class RoutingService {
    private final Map<String, Route> routes;
    private final Set<Parcel> unassignedParcels;

    public RoutingService() {
        this.routes = new HashMap<>();
        this.unassignedParcels = new HashSet<>();
    }

    public Route createRoute(String id, String courierId, Address startPoint) {
        if (routes.containsKey(id)) {
            throw new IllegalArgumentException("Trasa z ID " + id + " już istnieje");
        }
        Route route = new Route(id, courierId, startPoint);
        routes.put(id, route);
        return route;
    }

    public void assignParcelToRoute(String routeId, Parcel parcel) {
        Route route = getRoute(routeId);
        route.addParcel(parcel);
        unassignedParcels.remove(parcel);
    }

    public void registerUnassignedParcel(Parcel parcel) {
        unassignedParcels.add(parcel);
    }

    public Route getRoute(String routeId) {
        Route route = routes.get(routeId);
        if (route == null) {
            throw new IllegalArgumentException("Trasa " + routeId + " nie istnieje");
        }
        return route;
    }

    public List<Route> getAllRoutes() {
        return Collections.unmodifiableList(new ArrayList<>(routes.values()));
    }

    public List<Route> getActiveRoutes() {
        return routes.values().stream()
                .filter(Route::isActive)
                .toList();
    }

    public List<Parcel> getUnassignedParcels() {
        return Collections.unmodifiableList(new ArrayList<>(unassignedParcels));
    }

    public double getTotalParcelsWeight() {
        return routes.values().stream()
                .flatMap(r -> r.getParcels().stream())
                .mapToDouble(Parcel::getWeight)
                .sum();
    }

    public int getTotalParcelsCount() {
        return (int) routes.values().stream()
                .flatMap(r -> r.getParcels().stream())
                .count();
    }
}
