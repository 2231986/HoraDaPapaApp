package pt.ipleiria.estg.dei.horadapapa.activities.extra.review;

import static pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper.BetterToast;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.R;
import pt.ipleiria.estg.dei.horadapapa.adapters.PlateSpinnerAdapter;
import pt.ipleiria.estg.dei.horadapapa.listeners.PlatesListener;
import pt.ipleiria.estg.dei.horadapapa.models.Plate;
import pt.ipleiria.estg.dei.horadapapa.models.Review;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;
import pt.ipleiria.estg.dei.horadapapa.utilities.AppPreferences;

public class ReviewDetailsActivity extends AppCompatActivity implements PlatesListener {

    public static final String ID_REVIEW = "ID_REVIEW";
    Spinner reviewSpinnerPlate;
    PlateSpinnerAdapter spinnerAdapter;
    Plate selectedPlate;
    private EditText txtDescription;
    private TextView tvplatename;
    private RatingBar stars;
    private FloatingActionButton fabReview;
    private Review review; // This will hold the existing review data if editing
    private Button Deletebtn;

    private ImageView plateimg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);

        Singleton.getInstance(getApplicationContext()).setPlatesListener(this);
        Singleton.getInstance(getApplicationContext()).requestPlateGetAll(getApplicationContext());

        int id = getIntent().getIntExtra(ID_REVIEW, 0);
        review = Singleton.getInstance(getApplicationContext()).dbGetReview(id);


        Deletebtn = findViewById(R.id.btnDeleteReview);
        txtDescription = findViewById(R.id.editTextDescription);
        stars = findViewById(R.id.ratingBar);
        fabReview = findViewById(R.id.fabReview);
        reviewSpinnerPlate = findViewById(R.id.reviewSpinnerPlate);
        plateimg = findViewById(R.id.imageView3);

        if (review != null) {
            loadReview();
            stars.setNumStars(10);

            Deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // DELETE
                    Toast.makeText(ReviewDetailsActivity.this, "Processing delete", Toast.LENGTH_SHORT).show();

                    // Use the review.getId() directly in the requestReviewDelete method
                    Singleton.getInstance(getApplicationContext()).requestReviewDelete(
                            ReviewDetailsActivity.this,
                            review.getId()
                    );

                    finish();
                }
            });
            fabReview.setImageResource(R.drawable.ic_edit);
            reviewSpinnerPlate.setVisibility(View.GONE);

        } else {
            setTitle("Add Review");
            fabReview.setImageResource(R.drawable.ic_add);
            Deletebtn.setVisibility(View.GONE);
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

        fabReview.setOnClickListener(v -> {
            if (review != null) {
                // EDITAR
                Toast.makeText(ReviewDetailsActivity.this, "Processing update", Toast.LENGTH_SHORT).show();

                // Descrição
                String description = txtDescription.getText().toString();

                // Buscar valor float
                float rating = stars.getRating();

                // Conversão para int
                int value = Math.round(rating);
                // Usamos review.getId() diretamente no request
                Singleton.getInstance(getApplicationContext()).requestReviewEdit(
                        ReviewDetailsActivity.this,
                        review.getId(),
                        value,
                        description
                );

                finish();
            } else {
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
        });

    }

    private void loadReview() {
        setTitle("Review nº - " + review.getId());
        reviewSpinnerPlate.setSelection(review.getPlate_id());
        txtDescription.setText(review.getDescription());
        stars.setRating(review.getValue());

        int plateID = review.getPlate_id();

        if (plateID > 0) {
            Plate plate = Singleton.getInstance(getApplicationContext()).dbGetPlate(plateID);

            if (plate != null) {
                AppPreferences appPreferences = new AppPreferences(getApplicationContext());

                Glide.with(getApplicationContext())
                        .load("http://" + appPreferences.getApiIP() + plate.getImage())
                        .placeholder(R.drawable.img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(plateimg);
            } else {
                BetterToast(getApplicationContext(), "Os pratos ainda não foram carregados!");
            }
        } else {
            BetterToast(getApplicationContext(), "Os pratos ainda não foram carregados!");
        }
    }

    @Override
    public void onRefreshPlates(ArrayList<Plate> list) {
        if (list != null) {
            spinnerAdapter.clear();
            spinnerAdapter.addAll(list);
        }
    }
}