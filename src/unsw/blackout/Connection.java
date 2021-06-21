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

    public double getDevicePosition() {
        return this.device.getPosition();
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

    /**
     * Checks if the connection is active by checking endtime.
     * 
     * @return true if the connection is active and endtime is null, otherwise false.
     */
    public Boolean isActive() {
        if (endTime == null) {
            return true;
        }
        return false;
    }

    /**
     * Connects a satellite to a device.
     * 
     * @param time
     */
    public void connect(LocalTime time) {
        startTime = time;
        satellite.setActiveConnections(satellite.getActiveConnections() + 1);
        device.addConnection(this);
        device.addConnectionNo();
        if (device instanceof AWSCloudServer && device.getConnectionNo() < 2) {
            device.setConnected(false);
        }
        else {
            device.setConnected(true);
        }
        return;
    }

    /**
     * Disconnects a satellite from a device by setting the endtime.
     * 
     * @param time
     */
    public void disconnect(LocalTime time) {
        endTime = time;
        device.minusConnectionNo();
        device.setConnected(false);
        satellite.setActiveConnections(satellite.getActiveConnections() - 1);
        return;
    }

    /**
     * Updates the minutes active of the connection.
     * 
     * @param currTime
     */
    public void updateMinutesActive(LocalTime currTime) {
        if (endTime == null) {
            minutesActive = ((int) startTime.until(currTime, ChronoUnit.MINUTES)) - delay;
            if (minutesActive < 0) {
                minutesActive = 0;
            }
        }
        return;
    }

    /**
     * Updates connections for a new time by disconnecting the connection if 
     * it is not eligible and updating the minutes active of the connection.
     * 
     * @param currentTime
     */
    public void updateConnection(LocalTime currentTime) {
        if (isActive() && minutesActive > 0) {
            if (!device.isValidTime(currentTime) ||
                !MathsHelper.satelliteIsVisibleFromDevice(satellite.getPosition()
                , satellite.getHeight(), device.getPosition())) {
                disconnect(currentTime);
            }
        }
        updateMinutesActive(currentTime);
        return;
    }

    // Sorts connections by starttime and if equal, then deviceid id.
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