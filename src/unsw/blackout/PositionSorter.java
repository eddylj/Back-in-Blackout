package unsw.blackout;

import java.util.Comparator;

public class PositionSorter implements Comparator<Satellite> {
    @Override
    public int compare(Satellite sat1, Satellite sat2) {
        if (sat2.getPosition() < sat1.getPosition()) {
            return 1;
        }
        else if (sat2.getPosition() > sat1.getPosition()) {
            return -1;
        }
        return 0;
    }
}
