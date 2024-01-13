package pt.ipleiria.estg.dei.horadapapa.models;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

public class Favorite {

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

    public Favorite(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
            this.plate_id = jsonObject.getInt("plate_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Favorite(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        this.plate_id = cursor.getInt(cursor.getColumnIndexOrThrow("plate_id"));
    }

    private int id;

    private int plate_id;
}
