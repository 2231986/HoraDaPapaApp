package pt.ipleiria.estg.dei.horadapapa.models;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

public class Dinner {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int id;
    private String name;

    public Dinner(JSONObject jsonObject)
    {
        try {
            this.id = jsonObject.getInt("id");
            this.name = jsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
