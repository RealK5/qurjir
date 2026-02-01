package courier.service;

import courier.domain.Parcel;
import courier.domain.Status;

public abstract class Notifier {
    private final String name;

    protected Notifier(String name) {
        this.name = name;
    }

    public final void notifyStatusChange(Parcel parcel, Status oldStatus, Status newStatus) {
        String message = formatMessage(parcel, oldStatus, newStatus);
        send(message);
    }

    protected String formatMessage(Parcel parcel, Status oldStatus, Status newStatus) {
        return String.format("Paczka %s: %s → %s",
                parcel.getId(),
                oldStatus.getDisplayName(),
                newStatus.getDisplayName());
    }

    protected abstract void send(String message);

    public String getName() {
        return name;
    }
}
