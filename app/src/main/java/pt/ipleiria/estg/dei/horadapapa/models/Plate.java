package pt.ipleiria.estg.dei.horadapapa.models;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

public class Plate {
    private int id;
    private String title, image, price,description;

    public Plate(JSONObject jsonObject)
    {
        try {
            this.id = jsonObject.getInt("id");
            this.image = jsonObject.getString("image_name");
            this.price = jsonObject.getString("price");
            this.title = jsonObject.getString("title");
            this.description = jsonObject.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Plate(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        this.image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
        this.price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
        this.title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        this.description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public String getPriceFormatted() {
      return price + " €"; //TODO: trocar na API de String para Double
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
