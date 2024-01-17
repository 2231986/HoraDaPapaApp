package pt.ipleiria.estg.dei.horadapapa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import pt.ipleiria.estg.dei.horadapapa.activities.extra.MenuActivity;
import pt.ipleiria.estg.dei.horadapapa.activities.extra.ReviewDetailsActivity;
import pt.ipleiria.estg.dei.horadapapa.adapters.PlateListAdapter;
import pt.ipleiria.estg.dei.horadapapa.adapters.ReviewsListAdapter;
import pt.ipleiria.estg.dei.horadapapa.listeners.ReviewsListener;
import pt.ipleiria.estg.dei.horadapapa.models.Plate;
import pt.ipleiria.estg.dei.horadapapa.models.Review;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewListFragment extends Fragment implements ReviewsListener {

    private SearchView searchView;
    private ListView lvReviews;

    private FloatingActionButton fabadd;


    public ReviewListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review_list, container, false);
        setHasOptionsMenu(true);
        Log.d("ReviewListFragment", "onCreateView called");

        lvReviews = view.findViewById(R.id.lvReviews);
        Singleton.getInstance(getContext()).setReviewsListener(this);
        Singleton.getInstance(getContext()).requestReviewGetAll(getContext());

        fabadd = view.findViewById(R.id.fabReviewAdd);
        fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReviewDetailsActivity.class);
                startActivityForResult(intent, MenuActivity.ADD);
            }
        });
        return view;
    }

    @Override
    public void onRefreshReviews(ArrayList<Review> list) {
        if (list != null) {
            lvReviews.setAdapter(new ReviewsListAdapter(getContext(), list));
        }
    }

}