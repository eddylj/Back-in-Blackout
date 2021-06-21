package unsw.blackout;

public class SovietSatellite extends Satellite {

    public SovietSatellite(String id, String type, double height, double position) {
        super(id, type, height, position);
    }

    public void updateVelocity() {
        super.setVelocity(6000/(double)60);
    }
}
