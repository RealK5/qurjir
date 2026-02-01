package courier.domain;

import java.util.List;

public interface Trackable
{
    void start();
    void stop();
    boolean isActive();
    List<String> getActivityLog();
}
