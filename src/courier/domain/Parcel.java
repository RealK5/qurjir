package courier.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Parcel
{
    private final String id;
    private final double weight;  // kg
    private final String dimensions;  // format: LxWxH cm
    private final Address senderAddress;
    private final Address recipientAddress;
    private final List<StatusHistory> statusHistory;

    protected Parcel(String id, double weight, String dimensions,
                     Address senderAddress, Address recipientAddress)
    {
        this.id = validateParcelId(id);
        this.weight = validateWeight(weight);
        this.dimensions = validateDimensions(dimensions);
        this.senderAddress = Objects.requireNonNull(senderAddress, "Adres nadawcy nie może być null");
        this.recipientAddress = Objects.requireNonNull(recipientAddress, "Adres odbiorcy nie może być null");
        this.statusHistory = new ArrayList<>();
        this.statusHistory.add(new StatusHistory(Status.PENDING));
    }

    private String validateParcelId(String id)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException("ID paczki nie może być puste");
        }
        return id.trim();
    }

    private double validateWeight(double weight)
    {
        if (weight <= 0)
        {
            throw new IllegalArgumentException("Waga musi być dodatnia");
        }
        return weight;
    }

    private String validateDimensions(String dimensions)
    {
        if (dimensions == null || dimensions.trim().isEmpty())
        {
            throw new IllegalArgumentException("Wymiary nie mogą być puste");
        }
        return dimensions.trim();
    }

    public String getId() { return id; }
    public double getWeight() { return weight; }
    public String getDimensions() { return dimensions; }
    public Address getSenderAddress() { return senderAddress; }
    public Address getRecipientAddress() { return recipientAddress; }

    public Status getCurrentStatus()
    {
        return statusHistory.get(statusHistory.size() - 1).getStatus();
    }

    public List<StatusHistory> getStatusHistory()
    {
        return Collections.unmodifiableList(statusHistory);
    }

    public void updateStatus(Status newStatus)
    {
        if (newStatus == null)
        {
            throw new IllegalArgumentException("Status nie może być null");
        }
        statusHistory.add(new StatusHistory(newStatus));
    }

    public boolean canBeAddedToRoute() {
        return getCurrentStatus() == Status.PENDING ||
                getCurrentStatus() == Status.PICKED_UP;
    }

    public abstract String getHandlingRequirements();

    public abstract double calculateShippingCost();

    @Override
    public String toString()
    {
        return String.format("Paczka{id='%s', waga=%.2f kg, wymiary='%s', status=%s}",
                id, weight, dimensions, getCurrentStatus().getDisplayName());
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Parcel)) return false;
        Parcel parcel = (Parcel) o;
        return Objects.equals(id, parcel.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }


    public static class StatusHistory
    {
        private final Status status;
        private final long timestamp;

        public StatusHistory(Status status)
        {
            this.status = Objects.requireNonNull(status);
            this.timestamp = System.currentTimeMillis();
        }

        public Status getStatus()
        {
            return status;
        }
        public long getTimestamp()
        {
            return timestamp;
        }

        public String getFormattedTimestamp()
        {
            return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new java.util.Date(timestamp));
        }
    }
}
