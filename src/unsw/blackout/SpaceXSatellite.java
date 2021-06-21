package unsw.blackout;

public class SpaceXSatellite extends Satellite {

    public SpaceXSatellite(String id, String type, double height, double position) {
        super(id, type, height, position);
    }

    public void updateVelocity() {
        super.setVelocity(3330/(double)60);
    }
}
