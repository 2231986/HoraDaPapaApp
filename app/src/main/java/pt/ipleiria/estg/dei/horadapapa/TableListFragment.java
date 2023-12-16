package pt.ipleiria.estg.dei.horadapapa;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TableListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableListFragment extends Fragment {

    public TableListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table_list, container, false);


        //mapeamento do botão e setup de listener para metodo onClick
        Button navigateButton = view.findViewById(R.id.button3);
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MealListFragment é instanciada e guardada como fragment b (destino)
                MealListFragment fragmentB = new MealListFragment();

                // Chamada do Fragment manager
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                // Substituimos o content_layout pelo fragmento que queremos chamar
                fragmentManager.beginTransaction()
                        .replace(R.id.contentLayout, fragmentB) // É aqui que se dá a substituição
                        .addToBackStack(null) // Opcional: adiciona a transição em memória
                        .commit();
            }
        });

        return view;
    }
}