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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.R;
import pt.ipleiria.estg.dei.horadapapa.activities.PlateListFragment;
import pt.ipleiria.estg.dei.horadapapa.adapters.PlateSpinnerAdapter;
import pt.ipleiria.estg.dei.horadapapa.listeners.PlatesListener;
import pt.ipleiria.estg.dei.horadapapa.models.Plate;
import pt.ipleiria.estg.dei.horadapapa.models.Review;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

public class ReviewDetailsActivity extends AppCompatActivity implements PlatesListener {

    Spinner reviewSpinnerPlate;
    PlateSpinnerAdapter spinnerAdapter;

    Plate selectedPlate;

    private EditText txtDescription;

    private RatingBar stars;

    private FloatingActionButton fabReview;
    private Review review; // This will hold the existing review data if editing

    public static final String ID_REVIEW = "ID_REVIEW";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);

        int id = getIntent().getIntExtra(ID_REVIEW, 0);
        review = Singleton.getInstance(getApplicationContext()).dbGetReview(id);



        txtDescription = findViewById(R.id.editTextDescription);
        stars = findViewById(R.id.ratingBar);
        fabReview = findViewById(R.id.fabReview);
        reviewSpinnerPlate = findViewById(R.id.reviewSpinnerPlate);

        if(review !=null) {
            loadReview();
            fabReview.setImageResource(R.drawable.ic_edit);
        }else{
            setTitle("Adicionar Livro");
            fabReview.setImageResource(R.drawable.ic_add);
        }

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

        fabReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(review!=null)
                {
                    //EDITAR
                    Toast.makeText(ReviewDetailsActivity.this, "TOSTA", Toast.LENGTH_SHORT).show();

                } else
                {
                    //ADICIONAR

                    // ver se prato foi selecionado
                    if (selectedPlate != null) {
                        // Use selectedDinner.getId()
                        int selectedPlateId = selectedPlate.getId();
                        //Descriçao
                        String description = txtDescription.getText().toString();
                        // buscar valor float
                        float rating = stars.getRating();
                        // conversao para int
                        int value = Math.round(rating);
                        //Faz request à API para guardar review
                        Singleton.getInstance(getApplicationContext()).requestReviewAdd(ReviewDetailsActivity.this, selectedPlateId, value, description);
                        finish();
                    } else {
                        Toast.makeText(ReviewDetailsActivity.this, "Please select a plate before sending a review!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void loadReview() {
        setTitle("Review nº - " + review.getId());
        reviewSpinnerPlate.setSelection(review.getPlate_id());
        txtDescription.setText(review.getDescription());
        stars.setNumStars(review.getValue());
    }

    @Override
    public void onRefreshPlates(ArrayList<Plate> list) {
        if (list != null) {
            spinnerAdapter.clear();
            spinnerAdapter.addAll(list);
        }
    }
}