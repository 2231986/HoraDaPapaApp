package pt.ipleiria.estg.dei.horadapapa.models;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Invoice {
    private int id;
    private int meal_id;
    private String price;
    private ArrayList<InvoiceRequest> invoiceRequests;

    public Invoice(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
            this.price = jsonObject.getString("price");
            this.invoiceRequests = new ArrayList<>();

            JSONObject mealObject = jsonObject.getJSONObject("meal");
            this.meal_id = mealObject.getInt("id");
            JSONArray requestsArray = mealObject.getJSONArray("requests");

            for (int i = 0; i < requestsArray.length(); i++) {
                JSONObject requestObject = requestsArray.getJSONObject(i);
                this.invoiceRequests.add(new InvoiceRequest(this.meal_id, requestObject));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Invoice(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        this.meal_id = cursor.getInt(cursor.getColumnIndexOrThrow("meal_id"));
        this.price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMeal_id() {
        return meal_id;
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

    public ArrayList<InvoiceRequest> getInvoiceRequests() {
        return invoiceRequests;
    }

    public void setInvoiceRequests(ArrayList<InvoiceRequest> invoiceRequests) {
        this.invoiceRequests = invoiceRequests;
    }
}
