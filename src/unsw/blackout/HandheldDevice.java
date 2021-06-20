package unsw.blackout;

public class HandheldDevice extends Device {

    public HandheldDevice(String id, String type, double position) {
        super(id, type, position);
    }

    public void updateDelay() {
        super.setDelay(1);
    }

}
