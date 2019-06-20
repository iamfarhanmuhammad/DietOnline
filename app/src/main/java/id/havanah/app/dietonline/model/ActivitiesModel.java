package id.havanah.app.dietonline.model;

/**
 * Created by farhan at 12:41
 * on 04/06/2019.
 * Havanah Team, ID.
 */
public class ActivitiesModel {
    private String title;
    private String subtitle;

    public ActivitiesModel(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
