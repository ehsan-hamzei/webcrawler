package au.edu.unimelb.student.ehamzei.db.walkingmaps;

import java.io.Serializable;

/**
 * Created by ehamzei on 11/07/2017.
 */
public class MarkerModel extends PathDetailModel  implements Serializable {
    private int markerId;
    private boolean snapToRoad;

    public boolean isSnapToRoad() {
        return snapToRoad;
    }

    public void setSnapToRoad(boolean snapToRoad) {
        this.snapToRoad = snapToRoad;
    }

    public int getMarkerId() {
        return markerId;
    }

    public void setMarkerId(int markerId) {
        this.markerId = markerId;
    }
}
