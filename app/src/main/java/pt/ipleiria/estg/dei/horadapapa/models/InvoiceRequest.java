package pt.ipleiria.estg.dei.horadapapa.models;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

public class InvoiceRequest {

    int id;
    int plate_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlate_id() {
        return plate_id;
    }

    public void setPlate_id(int plate_id) {
        this.plate_id = plate_id;
    }

    public int getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(int invoice_id) {
        this.invoice_id = invoice_id;
    }

    int invoice_id;

    public InvoiceRequest(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
            this.plate_id = jsonObject.getInt("plate_id");
            this.invoice_id = jsonObject.getInt("invoice_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public InvoiceRequest(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        this.plate_id = cursor.getInt(cursor.getColumnIndexOrThrow("plate_id"));
        this.invoice_id = cursor.getInt(cursor.getColumnIndexOrThrow("invoice_id"));
    }
}
