package Timer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventLogger {
    List<LocalDateTime> timestamp;
    List<Duration> timerAt;
    List<Duration> restartFrom;
    List<EventType> event;
    // Best practice here should be defining a LogEntry class inside
    // EventLogger and store a List<LogEntry>, meanwhile making every
    // variables private and give getters without setters. But that
    // would double the code here of this simple app.

    public enum EventType {
        START_TIMER, PAUSE_TIMER, RESET_TIMER, START_FROM
    }


    public EventLogger() {
        timestamp = new ArrayList<>();
        timerAt = new ArrayList<>();
        event = new ArrayList<>();
        restartFrom = new ArrayList<>();
    }

    public void logEvent(EventType eventKey, Duration timerElapsed, Duration timerChangedTo){
        event.add(eventKey);
        timestamp.add(LocalDateTime.now());
        timerAt.add(timerElapsed);
        restartFrom.add(timerChangedTo);

        if (event.size() > 30){
            event.remove(0);
            timestamp.remove(0);
            timerAt.remove(0);
            restartFrom.remove(0);
        }
    }

    public void logEvent(EventType eventKey, Duration timerElapsed){
        logEvent(eventKey, timerElapsed, null);
    }
}
