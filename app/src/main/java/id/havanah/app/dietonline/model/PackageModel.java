package id.havanah.app.dietonline.model;

/**
 * Created by farhan at 21:08
 * on 12/04/2019.
 * Havanah Team, ID.
 */
public class PackageModel {
    private String name;
    private int image;

    public PackageModel(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }
}
