package pt.ipleiria.estg.dei.horadapapa.activities.extra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

        /*Button btnStartMeal = view.findViewById(R.id.btn_StartMeal);
        btnStartMeal.setOnClickListener(v -> {
            // Check if a dinner is selected
            if (selectedDinner != null) {
                // Use selectedDinner.getId() or any other relevant method
                int selectedDinnerId = selectedDinner.getId();

                // Perform actions with the selected dinner ID
                Toast.makeText(getContext(), "Selected Dinner ID: " + selectedDinnerId, Toast.LENGTH_SHORT).show();

                //Faz um request à API para começar a refeição
                Singleton.getInstance(getContext()).requestDinnerStart(getContext(), selectedDinnerId);

                // MealListFragment é instanciada e guardada como fragment b (destino)
                PlateListFragment fragmentB = new PlateListFragment();

                // Chamada do Fragment manager
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                // Substituimos o content_layout pelo fragmento que queremos chamar
                fragmentManager.beginTransaction()
                        .replace(R.id.contentLayout, fragmentB) // É aqui que se dá a substituição
                        .addToBackStack(null) // Opcional: adiciona a transição em memória
                        .commit();
            } else {
                Toast.makeText(getContext(), "Please select a dinner before starting a meal", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public void onRefreshPlates(ArrayList<Plate> list) {
        if (list != null) {
            spinnerAdapter.clear();
            spinnerAdapter.addAll(list);
        }
    }
}