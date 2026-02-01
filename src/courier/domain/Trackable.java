package courier.domain;

public interface Trackable {
    void start();
    void stop();
    boolean isActive();
}
