package au.edu.unimelb.student.ehamzei.db.walkingmaps;

import java.io.Serializable;

/**
 * Created by ehamzei on 11/07/2017.
 */
public class PathDetailModel  implements Serializable {
    private double lat;
    private double lng;
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


}
