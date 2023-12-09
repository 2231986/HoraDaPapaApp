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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_meal_list, container, false);
        setHasOptionsMenu(true);
        Log.d("MealListFragment", "onCreateView called");

        lvPlates=view.findViewById(R.id.lvPlates);

        Singleton.getInstance(getContext()).setProdutoListener(this);
        Singleton.getInstance(getContext()).requestPlateGetAll(getContext());

       /* lvPlates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override //mostra detalhes
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "LISTENER OK", Toast.LENGTH_SHORT).show();
                Log.d("MealListFragment", "onItemClick called");



                Toast.makeText(getContext(), plates.get(i).getTitle(), Toast.LENGTH_SHORT).show();

                //Intent intent = new Intent(getContext(), PlateDetailsActivity.class);
                //intent.putExtra(ID_PLATE, (int)l);
                //startActivity(intent);
                //startActivityForResult(intent, MenuActivity.EDIT);

            }
        });
        lvPlates.setClickable(true);
        lvPlates.setFocusable(true);

        lvPlates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Assuming PlateDetailsActivity is the target activity
                Log.d("MealListFragment", "Item clicked"); // Add this line

                Intent intent = new Intent(getContext(), PlateDetailsActivity.class);
                intent.putExtra(ID_PLATE, plates.get(position).getId());
                startActivity(intent);
            }
        });
       */

        return view;
    }

    @Override
    public void onRefreshPlates(ArrayList<Plate> list) {
        if (list != null) {
            lvPlates.setAdapter(new PlateListAdapter(getContext(), list));
        }
    }
}