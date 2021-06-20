package unsw.blackout;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Connection implements Comparable<Connection>{
    private Device device;
    private Satellite satellite;
    private String deviceId;
    private String satelliteId;
    private LocalTime startTime;
    private LocalTime endTime;
    private int minutesActive;
    private int delay;

    public Connection(Device device, Satellite satellite) {
        this.deviceId = device.getId();
        this.satelliteId = satellite.getId();
        this.device = device;
        this.satellite = satellite;
    }

    public Device getDevice() {
        return this.device;
    }

    public Satellite getSatellite() {
        return this.satellite;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getSatelliteId() {
        return this.satelliteId;
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public LocalTime getEndTime() {
        return this.endTime;
    }

    public int getMinutesActive() {
        return this.minutesActive;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setMinutesActive(int minutesActive) {
        this.minutesActive = minutesActive;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void updateMinutesActive(LocalTime currTime) {
        if (endTime == null) {
            minutesActive = ((int) startTime.until(currTime, ChronoUnit.MINUTES)) - delay;
            if (minutesActive < 0) {
                minutesActive = 0;
            }
        }
    }

    public void updateConnection(Boolean connected) {
        device.setConnected(connected);
    }

    public Boolean isValidTime(LocalTime currTime) {
        if (device.isValidTime(currTime)) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Connection connection) {
        if (getStartTime() == null || connection.getStartTime() == null) {
            return 0;
        }
        if (getStartTime().compareTo(connection.getStartTime()) == 0) {
            if (getDeviceId() == null || connection.getDeviceId() == null) {
                return 0;
            }
            return getDeviceId().compareTo(connection.getDeviceId());
        }

        return getStartTime().compareTo(connection.getStartTime());
    }
}