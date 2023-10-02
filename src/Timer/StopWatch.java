package Timer;


import java.time.Duration;
import java.time.Instant;

public class StopWatch {

    private Instant start;
    private Instant pause;
    private Duration totalPauseDuration = Duration.ZERO;

    public void start() {
        if (isPaused()) {  // Unpause
            totalPauseDuration = totalPauseDuration.plus(
                    Duration.between(pause, Instant.now()));
            pause = null;
        }
        if (notStarted()){  // New start
            start = Instant.now();
        }
    }

    public void startFrom(Duration startFrom){
        reset();
        start = start.minus(startFrom);
    }

    public boolean isPaused(){
        return pause != null;
        // If the timer is paused, pause = null;
        // Otherwise, pause = the instant the timer is paused
    }

    public boolean notStarted(){
        return start == null;
    }

    public void pause() {
        pause = Instant.now();
    }

    public void reset(){
        // restart the timer
        start = Instant.now();
        // Stay paused/unpaused
        if (isPaused()) pause();
        totalPauseDuration = Duration.ZERO;
    }

    public Duration elapsedTime() {
        if (notStarted())
            return Duration.ZERO;
        return Duration.between(start,
                        isPaused()? pause : Instant.now())
                .minus(totalPauseDuration);
    }

    public static String formatDuration(Duration duration) {
        int hours = (int) duration.toHours();
        int minutes = (int) duration.minusHours(hours).toMinutes();
        int seconds = (int) duration.minusHours(hours).minusMinutes(minutes).getSeconds();
        if (hours == 0)
            return String.format("%02d:%02d", minutes, seconds);
        else return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static Duration toDuration(int hour, int minute, int second){
        return Duration.ofHours(hour)
                .plusMinutes(minute).plusSeconds(second);
    }

    @Override
    public String toString() {
        return StopWatch.formatDuration(elapsedTime());
    }
}