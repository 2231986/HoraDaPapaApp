package pt.ipleiria.estg.dei.horadapapa.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Meal {
    private int id, dinnerID;
    private boolean checkout;

    public Meal(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
            this.dinnerID = jsonObject.getInt("id");
            this.checkout = jsonObject.getBoolean("checkout");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDinnerID() {
        return dinnerID;
    }

    public void setDinnerID(int dinnerID) {
        this.dinnerID = dinnerID;
    }

    public boolean isCheckout() {
        return checkout;
    }

    public void setCheckout(boolean checkout) {
        this.checkout = checkout;
    }
}
