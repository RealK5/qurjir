package courier.service;

import courier.domain.Parcel;
import courier.domain.Status;

public class SMSNotifier extends Notifier {
    private final String phoneNumber;

    public SMSNotifier(String phoneNumber) {
        super("SMS");
        this.phoneNumber = phoneNumber;
    }

    @Override
    protected void send(String message) {
        System.out.println("[SMS] → " + phoneNumber + ": " + message);
    }
}
