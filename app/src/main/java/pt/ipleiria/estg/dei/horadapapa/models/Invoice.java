package pt.ipleiria.estg.dei.horadapapa.models;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Invoice {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<InvoiceRequest> getPlateRequests() {
        return PlateRequests;
    }

    public void setPlateRequests(ArrayList<InvoiceRequest> plateRequests) {
        PlateRequests = plateRequests;
    }

    private String price;
    private ArrayList<InvoiceRequest> PlateRequests;

    public Invoice(JSONObject jsonObject)
    {
        try {
            this.id = jsonObject.getInt("id");
            this.price = jsonObject.getString("price");

            JSONArray requestsArray = jsonObject.getJSONArray("requests");

            for (int i = 0; i < requestsArray.length(); i++) {
                JSONObject requestObject = requestsArray.getJSONObject(i);

                this.PlateRequests.add(new InvoiceRequest(requestObject));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Invoice(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        this.price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
    }
}
