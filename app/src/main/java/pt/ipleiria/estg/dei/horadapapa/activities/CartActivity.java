package pt.ipleiria.estg.dei.horadapapa.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import pt.ipleiria.estg.dei.horadapapa.R;
import pt.ipleiria.estg.dei.horadapapa.activities.extra.MenuActivity;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
    }

    public void Purchase(View view) {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
    }
}