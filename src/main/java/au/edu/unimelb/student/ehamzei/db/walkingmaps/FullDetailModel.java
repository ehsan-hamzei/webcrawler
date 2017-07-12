package au.edu.unimelb.student.ehamzei.db.walkingmaps;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ehamzei on 12/07/2017.
 */
public class FullDetailModel implements Serializable {
    private List<MarkerModel> markers;
    private List<PathDetailModel> pathDetails;
    private List<POIModel> pois;
    private String title;

    public List<MarkerModel> getMarkers() {
        return markers;
    }

    public void setMarkers(List<MarkerModel> markers) {
        this.markers = markers;
    }

    public List<PathDetailModel> getPathDetails() {
        return pathDetails;
    }

    public void setPathDetails(List<PathDetailModel> pathDetails) {
        this.pathDetails = pathDetails;
    }

    public List<POIModel> getPois() {
        return pois;
    }

    public void setPois(List<POIModel> pois) {
        this.pois = pois;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;
}
