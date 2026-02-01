package courier.domain;

public enum Status {

    PENDING("Oczekująca"),
    PICKED_UP("Odebrana"),
    IN_TRANSIT("W drodze"),
    OUT_FOR_DELIVERY("Do dostarczenia"),
    DELIVERED("Dostarczona"),
    FAILED_DELIVERY("Dostarczenie nieudane"),
    RETURNED("Zwrócona");

    private final String displayName;

    Status(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }
}
