package pt.ipleiria.estg.dei.horadapapa;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pt.ipleiria.estg.dei.horadapapa.models.Plate;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

public class PlateDetailsActivity extends AppCompatActivity {
    public static final String ID_PLATE = "ID_PLATE";
    Plate plate;

    private EditText etTitle, etDesc, etPrice;

    private ImageView imgCover;

    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_details);

        int id = getIntent().getIntExtra(ID_PLATE,0);
        //TODO: tem de se implementar ou listener ou metodos na bd para obter os dados
        //plate = Singleton.getInstance(this).requestPlateGetAll(this).get(id);

        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        etPrice = findViewById(R.id.etPrice);
        imgCover = findViewById(R.id.imgCover);
        floatingActionButton=findViewById(R.id.floatingActionButton);
        
        loadPlate();
    }

    private void loadPlate() {
        etTitle.setText(plate.getTitle());
        etDesc.setText(plate.getDescription());
        etPrice.setText(plate.getPrice()+"");
        Glide.with(this)
                .load(plate.getImage())
                .placeholder(R.drawable.img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgCover);
    }
}