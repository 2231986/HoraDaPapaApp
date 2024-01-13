package pt.ipleiria.estg.dei.horadapapa.models;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

public class PlateRequest {
    private int id;
    private int mealId;
    private String observation;
    private int plateId;
    private int isCooked;
    private int isDelivered;
    private int userId;
    private String dateTime;
    private String price;
    private int quantity;

    public PlateRequest(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
            this.mealId = jsonObject.getInt("meal_id");
            this.observation = jsonObject.isNull("observation") ? null : jsonObject.getString("observation");
            this.plateId = jsonObject.getInt("plate_id");
            this.isCooked = jsonObject.getInt("isCooked");
            this.isDelivered = jsonObject.getInt("isDelivered");
            this.userId = jsonObject.getInt("user_id");
            this.dateTime = jsonObject.getString("date_time");
            this.price = jsonObject.getString("price");
            this.quantity = jsonObject.getInt("quantity");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public int getMealId() {
        return mealId;
    }

    public String getObservation() {
        return observation;
    }

    public int getPlateId() {
        return plateId;
    }

    public int getIsCooked() {
        return isCooked;
    }

    public int getIsDelivered() {
        return isDelivered;
    }

    public int getUserId() {
        return userId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
