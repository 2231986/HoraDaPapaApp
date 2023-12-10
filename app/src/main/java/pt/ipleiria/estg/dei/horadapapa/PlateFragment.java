package pt.ipleiria.estg.dei.horadapapa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.models.Plate;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PlateFragment extends Fragment {

    TextView tvTitle, tvDesc, tvPrice;
    ImageView imgCapa;

     public PlateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_plate, container, false);
        tvTitle=view.findViewById(R.id.textView);
        tvDesc=view.findViewById(R.id.textView2);
        tvPrice=view.findViewById(R.id.textView3);
        imgCapa=view.findViewById(R.id.imageView);
        //LoadPlate();
        return view;
    }
}