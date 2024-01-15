package pt.ipleiria.estg.dei.horadapapa.activities.extra;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.R;
import pt.ipleiria.estg.dei.horadapapa.activities.PlateListFragment;
import pt.ipleiria.estg.dei.horadapapa.adapters.PlateSpinnerAdapter;
import pt.ipleiria.estg.dei.horadapapa.listeners.PlatesListener;
import pt.ipleiria.estg.dei.horadapapa.models.Plate;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

public class ReviewDetailsActivity extends AppCompatActivity implements PlatesListener {

    Spinner reviewSpinnerPlate;
    PlateSpinnerAdapter spinnerAdapter;

    Plate selectedPlate;

    private EditText txtDescription;

    private RatingBar stars;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);

        // Retrieve review details from the intent
        Intent intent = getIntent();
        int plateId = intent.getIntExtra("PlateId", 0); // 0 is the default value
        String descriptionn = intent.getStringExtra("Description");
        int valuen = intent.getIntExtra("Value", 0); // 0 is the default value


        EditText editTextDescription = findViewById(R.id.editTextDescription);
        editTextDescription.setText(descriptionn);

        // Initialize and set the RatingBar
        RatingBar starsn = findViewById(R.id.ratingBar);
        starsn.setRating(valuen);



        reviewSpinnerPlate = findViewById(R.id.reviewSpinnerPlate);
        spinnerAdapter = new PlateSpinnerAdapter(getApplicationContext(), new ArrayList<>());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reviewSpinnerPlate.setAdapter(spinnerAdapter);

        reviewSpinnerPlate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Store the selected Dinner object
                selectedPlate = spinnerAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        Singleton.getInstance(getApplicationContext()).setPlatesListener(this);
        Singleton.getInstance(getApplicationContext()).requestPlateGetAll(getApplicationContext());

        Button btnStartReview = findViewById(R.id.btnSubmitReview);
        btnStartReview.setOnClickListener(v -> {
            // ver se prato foi selecionado
            if (selectedPlate != null) {
                // Use selectedDinner.getId()
                int selectedPlateId = selectedPlate.getId();
                //Descriçao
                txtDescription = findViewById(R.id.editTextDescription);
                String description = txtDescription.getText().toString();
                //Rating
                stars = findViewById(R.id.ratingBar);
                // buscar valor float
                float rating = stars.getRating();
                // conversao para int
                int value = Math.round(rating);
                //Faz request à API para guardar review
                Singleton.getInstance(getApplicationContext()).requestReviewAdd(this, selectedPlateId, value, description);
                finish();
            } else {
                Toast.makeText(this, "Please select a plate before sending a review!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefreshPlates(ArrayList<Plate> list) {
        if (list != null) {
            spinnerAdapter.clear();
            spinnerAdapter.addAll(list);
        }
    }
}