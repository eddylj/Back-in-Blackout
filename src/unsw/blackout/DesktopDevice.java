package unsw.blackout;

public class DesktopDevice extends Device{
    public DesktopDevice(String id, String type, double position) {
        super(id, type, position);
    }

    public void updateDelay() {
        super.setDelay(5);
    }
}
