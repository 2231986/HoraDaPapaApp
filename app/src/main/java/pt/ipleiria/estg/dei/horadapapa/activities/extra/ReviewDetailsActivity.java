package pt.ipleiria.estg.dei.horadapapa.activities.extra;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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







        /*EditText editTextDescription = findViewById(R.id.editTextDescription);
        String description = editTextDescription.getText().toString();

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        int value = (int) ratingBar.getRating();*/



        Button btnStartReview = findViewById(R.id.btnSubmitReview);
        btnStartReview.setOnClickListener(v -> {
            // Check if a plate is selected
            if (selectedPlate != null) {
                // Use selectedDinner.getId() or any other relevant method
                int selectedPlateId = selectedPlate.getId();

                //Descriçao
                txtDescription = findViewById(R.id.editTextDescription);
                String description = txtDescription.getText().toString();

                //Rating
                stars = findViewById(R.id.ratingBar);
                // Get the float value from the RatingBar
                float rating = stars.getRating();
                // Convert the float rating to an integer using Math.round()
                int value = Math.round(rating);


                //Faz um request à API para guardar review
                //Singleton.getInstance(getContext()).requestReviewAdd(getContext(), selectedPlateId);

                Singleton.getInstance(getApplicationContext()).requestReviewAdd(this, selectedPlateId, value, description);


                // Perform actions with the selected dinner ID
                //Toast.makeText(this, "Review for " + selectedPlateId + " sent!", Toast.LENGTH_SHORT).show();

                /*
                // MealListFragment é instanciada e guardada como fragment b (destino)
                PlateListFragment fragmentB = new PlateListFragment();

                // Chamada do Fragment manager
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                // Substituimos o content_layout pelo fragmento que queremos chamar
                fragmentManager.beginTransaction()
                        .replace(R.id.contentLayout, fragmentB) // É aqui que se dá a substituição
                        .addToBackStack(null) // Opcional: adiciona a transição em memória
                        .commit();

                 */
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