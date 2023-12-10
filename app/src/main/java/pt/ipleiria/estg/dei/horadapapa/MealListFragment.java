package pt.ipleiria.estg.dei.horadapapa;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

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

    private ListView lvPlates;

    public MealListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meal_list, container, false);
        setHasOptionsMenu(true);
        Log.d("MealListFragment", "onCreateView called");

        lvPlates = view.findViewById(R.id.lvPlates);

        Singleton.getInstance(getContext()).setProdutoListener(this);
        Singleton.getInstance(getContext()).requestPlateGetAll(getContext());

        return view;
    }

    @Override
    public void onRefreshPlates(ArrayList<Plate> list) {
        if (list != null) {
            lvPlates.setAdapter(new PlateListAdapter(getContext(), list));
        }
    }
}