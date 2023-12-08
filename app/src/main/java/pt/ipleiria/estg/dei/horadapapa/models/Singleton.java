package pt.ipleiria.estg.dei.horadapapa.models;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.R;

public class Singleton {
    private static Singleton singleton_instance = null;
    public static RequestQueue volleyQueue = null;

    private ArrayList<Plate> plates;

    public static synchronized Singleton getInstance(Context context) {
        if (singleton_instance == null) {
            singleton_instance = new Singleton();
            volleyQueue = Volley.newRequestQueue(context);
        }

        return singleton_instance;
    }

    public static boolean isConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private Singleton(){
        generateData();
    }

    private void generateData() {
        plates=new ArrayList<>();
        plates.add(new Plate(1, R.drawable.droid,12,"Bolonhesa","Massa com carne"));
    }

    public ArrayList<Plate> getPlates() {
        return plates;
    }
}