package courier.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Route implements Trackable, Exportable
{
    private final String id;
    private final String courierId;
    private final Address startPoint;
    private final List<Parcel> parcels;
    private final List<String> activityLog;
    private boolean active;

    public Route(String id, String courierId, Address startPoint)
    {
        this.id = Objects.requireNonNull(id, "ID trasy nie może być null");
        this.courierId = Objects.requireNonNull(courierId, "ID kuriera nie może być null");
        this.startPoint = Objects.requireNonNull(startPoint, "Punkt początkowy nie może być null");
        this.parcels = new ArrayList<>();
        this.activityLog = new ArrayList<>();
        this.active = false;
    }

    public String getId()
    {
        return id;
    }
    public String getCourierId()
    {
        return courierId;
    }
    public Address getStartPoint()
    {
        return startPoint;
    }

    public List<Parcel> getParcels()
    {
        return Collections.unmodifiableList(parcels);
    }

    public void addParcel(Parcel parcel)
    {
        if (parcel == null)
        {
            throw new IllegalArgumentException("Paczka nie może być null");
        }
        if (!parcel.canBeAddedToRoute())
        {
            throw new IllegalStateException("Paczka " + parcel.getId() + " nie może być dodana do trasy " +  "(status: " + parcel.getCurrentStatus().getDisplayName() + ")");
        }
        if (parcels.contains(parcel))
        {
            throw new IllegalStateException("Paczka już znajduje się na tej trasie");
        }
        parcels.add(parcel);
        addLog("Paczka " + parcel.getId() + " dodana do trasy");
    }

    public void removeParcel(Parcel parcel)
    {
        if (parcels.remove(parcel))
        {
            addLog("Paczka " + parcel.getId() + " usunięta z trasy");
        }
    }

    public double getTotalWeight()
    {
        return parcels.stream().mapToDouble(Parcel::getWeight).sum();
    }

    public int getParcelCount()
    {
        return parcels.size();
    }

    @Override
    public void start()
    {
        if (parcels.isEmpty())
        {
            throw new IllegalStateException("Nie można rozpocząć trasy bez paczek");
        }
        this.active = true;
        addLog("Trasa rozpoczęta - " + parcels.size() + " paczek, waga całkowita: " +
                String.format("%.2f kg", getTotalWeight()));

        // Update all parcel statuses
        parcels.forEach(p -> p.updateStatus(Status.PICKED_UP));
    }

    @Override
    public void stop()
    {
        this.active = false;
        addLog("Trasa zakończona");
    }

    @Override
    public boolean isActive()
    {
        return active;
    }

    @Override
    public List<String> getActivityLog()
    {
        return Collections.unmodifiableList(activityLog);
    }

    private void addLog(String message)
    {
        String logEntry = String.format("[%s] %s",
                new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date()),
                message);
        activityLog.add(logEntry);
    }

    public void logDeliveryAttempt(Parcel parcel, boolean successful)
    {
        if (!parcels.contains(parcel))
        {
            throw new IllegalArgumentException("Paczka nie należy do tej trasy");
        }

        if (successful)
        {
            parcel.updateStatus(Status.DELIVERED);
            addLog("✓ Paczka " + parcel.getId() + " dostarczona");
        }
        else
        {
            parcel.updateStatus(Status.FAILED_DELIVERY);
            addLog("✗ Dostarczenie paczki " + parcel.getId() + " nieudane");
        }
    }

    @Override
    public String exportToCSV()
    {
        StringBuilder csv = new StringBuilder();
        csv.append("ID Trasy,ID Kuriera,Liczba Paczek,Całkowita Waga (kg),Status\n");
        csv.append(id).append(",")
                .append(courierId).append(",")
                .append(parcels.size()).append(",")
                .append(String.format("%.2f", getTotalWeight())).append(",")
                .append(active ? "Aktywna" : "Zakończona").append("\n\n");

        csv.append("ID Paczki,Typ,Waga (kg),Status,Historia\n");
        for (Parcel p : parcels) {
            csv.append(p.getId()).append(",")
                    .append(p.getClass().getSimpleName()).append(",")
                    .append(String.format("%.2f", p.getWeight())).append(",")
                    .append(p.getCurrentStatus().getDisplayName()).append(",");

            // Status history
            List<Parcel.StatusHistory> history = p.getStatusHistory();
            for (int i = 0; i < history.size(); i++) {
                Parcel.StatusHistory sh = history.get(i);
                csv.append(sh.getStatus().getDisplayName());
                if (i < history.size() - 1) csv.append(" -> ");
            }
            csv.append("\n");
        }

        return csv.toString();
    }

    @Override
    public List<String> getExportableData()
    {
        List<String> data = new ArrayList<>();
        data.add("Route ID: " + id);
        data.add("Courier ID: " + courierId);
        data.addAll(activityLog);
        return Collections.unmodifiableList(data);
    }

    @Override
    public String toString()
    {
        return String.format("Trasa{id='%s', kurier='%s', paczek=%d, waga=%.2f kg, aktywna=%b}",
                id, courierId, parcels.size(), getTotalWeight(), active);
    }
}
