package id.havanah.app.dietonline.model;

/**
 * Created by farhan at 11:05
 * on 11/04/2019.
 * Havanah Team, ID.
 */
public class ProductModel {
    private String product_id;
    private String name;
    private int price;
    private int image;
    private String content;

    public ProductModel(String product_id, String name, int price, int image, String content) {
        this.product_id = product_id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.content = content;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getImage() {
        return image;
    }

    public String getContent() {
        return content;
    }
}
