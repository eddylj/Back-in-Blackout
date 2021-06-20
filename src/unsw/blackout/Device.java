package unsw.blackout;

import java.time.LocalTime;
import java.util.ArrayList;

public class Device implements Comparable<Device> {
    private String id;
    private String type;
    private double position;
    private int delay;
    private ArrayList<ActivationTime> activationTimes;
    private Boolean connected;

    public Device(String id, String type, double position) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.connected = false;
        activationTimes = new ArrayList<>();
    }

    public void intialiseType() {
        if (type.equals("HandheldDevice")) {
            delay = 1;
        }
        else if (type.equals("LaptopDevice")) {
            delay = 2;
        }
        else if (type.equals("DesktopDevice")) {
            delay = 5;
        }
        return;
    }    
    
    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public double getPosition() {
        return this.position;
    }

    public int getDelay() {
        return this.delay;
    }

    public Boolean getConnected() {
        return this.connected;
    }

    public ArrayList<ActivationTime> getActivationTimes() {
        return this.activationTimes;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public void schedule(LocalTime start, int durationInMinutes) {
            ActivationTime activationTime = new ActivationTime(start, start.plusMinutes(durationInMinutes));
            activationTimes.add(activationTime);
    }

    public Boolean isValidTime(LocalTime time) {
        for (ActivationTime activationTime: this.activationTimes) {
                return activationTime.isValidTime(time);
        }
        return false;
    }

    @Override
    public int compareTo(Device device) {
        if (getId() == null || device.getId() == null) {
            return 0;
        }
        return getId().compareTo(device.getId());
    }
}
