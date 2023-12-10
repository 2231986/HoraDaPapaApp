package pt.ipleiria.estg.dei.horadapapa;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pt.ipleiria.estg.dei.horadapapa.models.Plate;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;
import pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper;

public class PlateDetailsActivity extends AppCompatActivity {
    public static final String ID_PLATE = "ID_PLATE";
    Plate plate;

    private EditText etTitle, etDesc, etPrice;

    private ImageView imgCover;

    private FloatingActionButton fabTooglePlateHasFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_details);

        int id = getIntent().getIntExtra(ID_PLATE, 0);

        if (id == 0) {
            ProjectHelper.BetterToast(this, "Prato inválido!");
            finish();
        } else {
            plate = Singleton.getInstance(this).dbGetPlate(id);
        }

        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        etPrice = findViewById(R.id.etPrice);
        imgCover = findViewById(R.id.imgCover);
        fabTooglePlateHasFavorite = findViewById(R.id.fab_TooglePlateHasFavorite);

        loadPlate();

        fabTooglePlateHasFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlateDetailsActivity.this, "ADD 1", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPlate() {
        etTitle.setText(plate.getTitle());
        etDesc.setText(plate.getDescription());
        etPrice.setText(plate.getPrice());
        Glide.with(this)
                .load(plate.getImage())
                .placeholder(R.drawable.img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgCover);
    }
}