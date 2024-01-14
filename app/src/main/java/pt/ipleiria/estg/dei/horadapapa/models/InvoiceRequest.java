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

    public int getMeal_id() {
        return meal_id;
    }

    int meal_id;

    public InvoiceRequest(int mealID, JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
            this.plate_id = jsonObject.getInt("plate_id");
            this.meal_id = mealID;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public InvoiceRequest(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        this.plate_id = cursor.getInt(cursor.getColumnIndexOrThrow("plate_id"));
        this.meal_id = cursor.getInt(cursor.getColumnIndexOrThrow("meal_id"));
    }
}
