package unsw.blackout;

public class BlueOriginSatellite extends Satellite {

    public BlueOriginSatellite(String id, String type, double height, double position) {
        super(id, type, height, position);
    }

    public void updateVelocity() {
        super.setVelocity(8500/(double)60);
    }
}
