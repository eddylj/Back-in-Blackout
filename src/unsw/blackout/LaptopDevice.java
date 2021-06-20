package unsw.blackout;

public class LaptopDevice extends Device {
    public LaptopDevice(String id, String type, double position) {
        super(id, type, position);
    }

    public void updateDelay() {
        super.setDelay(2);
    }
}
