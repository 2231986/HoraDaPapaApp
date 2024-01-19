package pt.ipleiria.estg.dei.horadapapa.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pt.ipleiria.estg.dei.horadapapa.R;
import pt.ipleiria.estg.dei.horadapapa.models.Plate;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;
import pt.ipleiria.estg.dei.horadapapa.utilities.AppPreferences;
import pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper;

public class PlateDetailsActivity extends AppCompatActivity {
    public static final String ID_PLATE = "ID_PLATE";
    Plate plate;

    private TextView tvTitle, tvDesc, tvPrice;

    private ImageView imgCover;

    private FloatingActionButton fabTooglePlateHasFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_details);
        setTitle("Plate Details");


        int id = getIntent().getIntExtra(ID_PLATE, 0);

        if (id == 0) {
            ProjectHelper.BetterToast(this, "Prato inválido!");
            finish();
        } else {
            plate = Singleton.getInstance(this).dbGetPlate(id);
        }

        tvTitle = findViewById(R.id.tvTitle);
        tvDesc = findViewById(R.id.tvDesc);
        tvPrice = findViewById(R.id.tvPrice);
        imgCover = findViewById(R.id.imgCover);
        fabTooglePlateHasFavorite = findViewById(R.id.fab_TooglePlateHasFavorite);

        loadPlate();

        fabTooglePlateHasFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Plate plate = Singleton.getInstance(getApplicationContext()).dbGetFavorite(id);
                if (plate == null) {
                    Singleton.getInstance(getApplicationContext()).requestPlateAddFavorite(getApplicationContext(), id);
                } else {
                    Singleton.getInstance(getApplicationContext()).requestPlateRemoveFavorite(getApplicationContext(), id);
                }
            }
        });
    }

    private void loadPlate() {
        tvTitle.setText(plate.getTitle());
        tvDesc.setText(plate.getDescription());
        tvPrice.setText(plate.getPriceFormatted());

        AppPreferences appPreferences = new AppPreferences(this);

        Glide.with(this)
                .load("http://" + appPreferences.getApiIP() + plate.getImage())
                .placeholder(R.drawable.img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgCover);
    }
}