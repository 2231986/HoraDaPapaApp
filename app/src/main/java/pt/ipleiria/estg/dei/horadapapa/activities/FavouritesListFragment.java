package pt.ipleiria.estg.dei.horadapapa.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.R;
import pt.ipleiria.estg.dei.horadapapa.adapters.PlateListAdapter;
import pt.ipleiria.estg.dei.horadapapa.listeners.FavoritesListener;
import pt.ipleiria.estg.dei.horadapapa.models.Plate;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouritesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouritesListFragment extends Fragment implements FavoritesListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView lvFavorites;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavouritesListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouritesListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouritesListFragment newInstance(String param1, String param2) {
        FavouritesListFragment fragment = new FavouritesListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourites_list, container, false);

        lvFavorites = view.findViewById(R.id.lvFavorites);
        Singleton.getInstance(getContext()).setFavoritesListener(this);
        Singleton.getInstance(getContext()).requestFavoritesGetAll(getContext());

        return view;
    }

    @Override
    public void onRefreshFavorites(ArrayList<Plate> list) {
        if (list != null) {
            lvFavorites.setAdapter(new PlateListAdapter(getContext(), list));
        }
    }
}