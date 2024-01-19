package pt.ipleiria.estg.dei.horadapapa.activities.meal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.R;
import pt.ipleiria.estg.dei.horadapapa.adapters.PlateListAdapter;
import pt.ipleiria.estg.dei.horadapapa.listeners.PlatesListener;
import pt.ipleiria.estg.dei.horadapapa.models.Plate;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealListFragment extends Fragment implements PlatesListener {

    private SearchView searchView;

    private final boolean isMealStarted = false; // Variable to track meal status


    private ListView lvPlates;

    public MealListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_cart, container, false);
        setHasOptionsMenu(true);


        FloatingActionButton fab_createPayment = view.findViewById(R.id.btnCartPay);
        fab_createPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Singleton.getInstance(getContext()).requestMealInvoice(getContext());
            }
        });

        lvPlates = view.findViewById(R.id.lvCartPlates);
        Singleton.getInstance(getContext()).setPlatesListener(this);
        Singleton.getInstance(getContext()).requestMealRequests(getContext());

        return view;
    }

    @Override
    public void onRefreshPlates(ArrayList<Plate> list) {
        if (list != null) {
            lvPlates.setAdapter(new PlateListAdapter(getContext(), list));
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem itemPesquisa = menu.findItem(R.id.itemSearch);
        searchView = (SearchView) itemPesquisa.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // Call function from Singleton and pass the entered text
                ArrayList<Plate> filteredPlates = Singleton.getInstance(getContext()).filterPlatesByContent(newText);

                // Update your ListView with the filtered plates
                if (filteredPlates != null) {
                    lvPlates.setAdapter(new PlateListAdapter(getContext(), filteredPlates));
                }
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

}