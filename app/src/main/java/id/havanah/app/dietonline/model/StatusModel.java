package id.havanah.app.dietonline.model;

/**
 * Created by farhan at 21:23
 * on 20/04/2019.
 * Havanah Team, ID.
 */
public class StatusModel {
    private String id, invoice, product_id, date, time, notes, status;

    public StatusModel(String id, String invoice, String product_id, String date, String time, String notes, String status) {
        this.id = id;
        this.invoice = invoice;
        this.product_id = product_id;
        this.date = date;
        this.time = time;
        this.notes = notes;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getInvoice() {
        return invoice;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getNotes() {
        return notes;
    }

    public String getStatus() {
        return status;
    }
}
