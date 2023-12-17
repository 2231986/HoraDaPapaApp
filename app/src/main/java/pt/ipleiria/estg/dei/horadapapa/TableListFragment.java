package pt.ipleiria.estg.dei.horadapapa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.adapters.DinnerSpinnerAdapter;
import pt.ipleiria.estg.dei.horadapapa.listeners.DinnersListener;
import pt.ipleiria.estg.dei.horadapapa.models.Dinner;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TableListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableListFragment extends Fragment implements DinnersListener {

    private Spinner spinner_SelectTable;
    private DinnerSpinnerAdapter spinnerAdapter;
    private Dinner selectedDinner;  // Change the type to Dinner


    public TableListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table_list, container, false);

        spinner_SelectTable = view.findViewById(R.id.spinner_SelectTable);

        spinnerAdapter = new DinnerSpinnerAdapter(getContext(), new ArrayList<>());

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_SelectTable.setAdapter(spinnerAdapter);
        spinner_SelectTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Store the selected Dinner object
                selectedDinner = spinnerAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        Singleton.getInstance(getContext()).setDinnersListener(this);
        Singleton.getInstance(getContext()).requestDinnerGetAll(getContext());

        //mapeamento do botão e setup de listener para metodo onClick
        Button btnStartMeal = view.findViewById(R.id.btn_StartMeal);
        btnStartMeal.setOnClickListener(v -> {
            // Check if a dinner is selected
            if (selectedDinner != null) {
                // Use selectedDinner.getId() or any other relevant method
                int selectedDinnerId = selectedDinner.getId();

                // Perform actions with the selected dinner ID
                Toast.makeText(getContext(), "Selected Dinner ID: " + selectedDinnerId, Toast.LENGTH_SHORT).show();

                // Example: Request to start a meal with the selected dinner ID
                Singleton.getInstance(getContext()).requestDinnerStart(getContext(), selectedDinnerId);

                // MealListFragment é instanciada e guardada como fragment b (destino)
                MealListFragment fragmentB = new MealListFragment();

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
        });

        return view;
    }

    @Override
    public void onRefreshDinners(ArrayList<Dinner> list) {
        if (list != null) {
            // Clear existing data
            spinnerAdapter.clear();

            // Add new data
            spinnerAdapter.addAll(list);

            // Notify the adapter that the data set has changed
            spinnerAdapter.notifyDataSetChanged();
        }
    }
}