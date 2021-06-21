package unsw.blackout;

public class NasaSatellite extends Satellite {

    public NasaSatellite(String id, String type, double height, double position) {
        super(id, type, height, position);
    }

    public void updateVelocity() {
        super.setVelocity(5100/(double)60);
    }
}
