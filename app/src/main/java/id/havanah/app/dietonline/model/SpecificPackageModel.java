package id.havanah.app.dietonline.model;

/**
 * Created by farhan at 11:36
 * on 23/04/2019.
 * Havanah Team, ID.
 */
public class SpecificPackageModel extends ProductModel {

    private String desc1, desc2, desc3, desc4, desc5, desc6, desc7, desc8, desc9;

    public SpecificPackageModel(String product_id, String name, int price, int image, String content,
                                String desc1, String desc2, String desc3, String desc4, String desc5,
                                String desc6, String desc7, String desc8, String desc9) {
        super(product_id, name, price, image, content);
        this.desc1 = desc1;
        this.desc2 = desc2;
        this.desc3 = desc3;
        this.desc4 = desc4;
        this.desc5 = desc5;
        this.desc6 = desc6;
        this.desc7 = desc7;
        this.desc8 = desc8;
        this.desc9 = desc9;
    }

    public String getDesc1() {
        return desc1;
    }

    public String getDesc2() {
        return desc2;
    }

    public String getDesc3() {
        return desc3;
    }

    public String getDesc4() {
        return desc4;
    }

    public String getDesc5() {
        return desc5;
    }

    public String getDesc6() {
        return desc6;
    }

    public String getDesc7() {
        return desc7;
    }

    public String getDesc8() {
        return desc8;
    }

    public String getDesc9() {
        return desc9;
    }
}
