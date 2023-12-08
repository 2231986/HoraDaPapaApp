package pt.ipleiria.estg.dei.horadapapa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.models.Plate;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlateFragment#newInstance} factory method to
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
        LoadPlate();
        return view;
    }

    private void LoadPlate() {
        ArrayList<Plate> plates = Singleton.getInstance(getContext()).getPlates();
        if (plates.size()>0){
            Plate plate = plates.get(0);
            tvTitle.setText(plate.getTitle());
            tvDesc.setText(plate.getDescription());
            tvPrice.setText(""+plate.getPrice()); //Concatenar uma string porque value é integer
            imgCapa.setImageResource(plate.getCapa());
        }
    }
}