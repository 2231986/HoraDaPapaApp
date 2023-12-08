package pt.ipleiria.estg.dei.horadapapa;

import static pt.ipleiria.estg.dei.horadapapa.PlateDetailsActivity.ID_PLATE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.adapters.PlateListAdapter;
import pt.ipleiria.estg.dei.horadapapa.models.Plate;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealListFragment extends Fragment {

    private ListView lvPlates;
    private ArrayList<Plate> plates;

    public MealListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_meal_list, container, false);

        lvPlates=view.findViewById(R.id.lvPlates);
        plates= Singleton.getInstance(getContext()).getPlates();

        lvPlates.setAdapter(new PlateListAdapter(getContext(),plates));

        lvPlates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override //mostra detalhes
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), plates.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), PlateDetailsActivity.class);
                intent.putExtra(ID_PLATE, (int)l);
                //startActivity(intent);
                startActivityForResult(intent, MenuActivity.EDIT);

            }
        });

    return view;
    }



}