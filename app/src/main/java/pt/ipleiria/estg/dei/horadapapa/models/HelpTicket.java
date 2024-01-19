package pt.ipleiria.estg.dei.horadapapa.models;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

public class HelpTicket {

    private int id;
    private int user_id;

    private int needHelp;
    private String description;



    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public HelpTicket(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
            this.description = jsonObject.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public HelpTicket(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        this.description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
    }
}
