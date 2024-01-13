package pt.ipleiria.estg.dei.horadapapa.models;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

public class Review {


    private int id, value;
    private String description;

    public Review(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
            this.description = jsonObject.getString("description");
            this.value = jsonObject.getInt("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Review(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        this.description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        this.value = cursor.getInt(cursor.getColumnIndexOrThrow("value"));

    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public int getValue() {return value;}

    public void setValue(int value) {this.value = value;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}
}
