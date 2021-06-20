package unsw.blackout;

import java.time.LocalTime;

public class ActivationTime {
    private LocalTime startTime;
    private LocalTime endTime;

    public ActivationTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public LocalTime getEndTime() {
        return this.endTime;
    }

    public Boolean isValidTime(LocalTime time) {
        if (time.compareTo(startTime) >= 0 && time.compareTo(endTime) <= 0) {
            return true;
        }
        return false;
    }

}
