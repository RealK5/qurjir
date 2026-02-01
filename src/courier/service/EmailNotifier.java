package courier.service;

import courier.domain.Status;
import courier.domain.Parcel;

public class EmailNotifier extends Notifier {
    private final String recipientEmail;

    public EmailNotifier(String recipientEmail) {
        super("Email");
        this.recipientEmail = recipientEmail;
    }

    @Override
    protected void send(String message) {
        System.out.println("[EMAIL] → " + recipientEmail + ": " + message);
    }
}
