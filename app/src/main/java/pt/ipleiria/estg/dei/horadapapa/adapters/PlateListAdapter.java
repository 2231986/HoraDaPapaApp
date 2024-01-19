package pt.ipleiria.estg.dei.horadapapa.adapters;

import static pt.ipleiria.estg.dei.horadapapa.activities.PlateDetailsActivity.ID_PLATE;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.R;
import pt.ipleiria.estg.dei.horadapapa.activities.PlateDetailsActivity;
import pt.ipleiria.estg.dei.horadapapa.models.Plate;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;
import pt.ipleiria.estg.dei.horadapapa.utilities.AppPreferences;

public class PlateListAdapter extends BaseAdapter {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final ArrayList<Plate> plates;
    private boolean hideUiRequest = false;


    public PlateListAdapter(Context context, ArrayList<Plate> plates) {
        this.context = context;
        this.plates = plates;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public PlateListAdapter(Context context, ArrayList<Plate> plates, boolean hideUiRequest) {
        this.context = context;
        this.plates = plates;
        this.layoutInflater = LayoutInflater.from(context);
        this.hideUiRequest = hideUiRequest;
    }

    @Override
    public int getCount() {
        return plates.size();
    }

    @Override
    public Object getItem(int i) {
        return plates.get(i);
    }

    @Override
    public long getItemId(int i) {
        return plates.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolderLista viewHolderLista;
        final Plate currentPlate = plates.get(i);

        if (view == null) {
            view = layoutInflater.inflate(R.layout.fragment_plate, null);
            viewHolderLista = new ViewHolderLista(view);
            view.setTag(viewHolderLista);
        } else {
            viewHolderLista = (ViewHolderLista) view.getTag();
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Detalhe aberto!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, PlateDetailsActivity.class);
                intent.putExtra(ID_PLATE, currentPlate.getId());
                context.startActivity(intent);
            }
        });


        EditText et_RequestQuantity = view.findViewById(R.id.et_RequestQuantity);
        EditText et_plateObs = view.findViewById(R.id.et_plateObs);
        TextView textView18 = view.findViewById(R.id.textView18);


        Button btn_addRequest = view.findViewById(R.id.btn_addRequest);

        // Mostrar UI Request
        if (
                (Singleton.getInstance(context.getApplicationContext()).getCurrentMealID() == 0 && !this.hideUiRequest) || //Regra de Pratos
                        (this.hideUiRequest) //Regra de Invoices
        ) {
            btn_addRequest.setVisibility(View.GONE);
            et_RequestQuantity.setVisibility(View.GONE);
            et_plateObs.setVisibility(View.GONE);
            textView18.setVisibility(View.GONE);
        }

        View finalView = view;
        btn_addRequest.setOnClickListener(v -> {
            EditText etRequestQuantity = finalView.findViewById(R.id.et_RequestQuantity);
            int quantity = Integer.parseInt(etRequestQuantity.getText().toString());

            EditText etRequestObs = finalView.findViewById(R.id.et_plateObs);
            String observation = etRequestObs.getText().toString();

            int plateID = currentPlate.getId();
            Singleton.getInstance(context).requestRequestPlate(context, plateID, quantity, observation);
        });

        viewHolderLista.update(currentPlate);
        return view;
    }


    private class ViewHolderLista {
        private final TextView tvTitle;
        private final TextView tvDesc;
        private final TextView tvPrice;
        private final ImageView imgCapa, favoriteStar;

        public ViewHolderLista(View view) {
            tvTitle = view.findViewById(R.id.textView);
            tvDesc = view.findViewById(R.id.textView2);
            tvPrice = view.findViewById(R.id.textView3);
            imgCapa = view.findViewById(R.id.imageView);
            favoriteStar = view.findViewById(R.id.plateDetailFavoriteStar);
        }

        public void update(Plate plate) {
            tvTitle.setText(plate.getTitle());
            tvDesc.setText(plate.getDescription());
            tvPrice.setText(plate.getPriceFormatted());

            Plate favoritePlate = Singleton.getInstance(context).dbGetFavorite(plate.getId());

            if (favoritePlate != null) {
                favoriteStar.setImageResource(R.drawable.ic_favourite_full);
            } else {
                favoriteStar.setImageResource(R.drawable.ic_favourite_empty);
            }

            AppPreferences appPreferences = new AppPreferences(context);

            Glide.with(context)
                    .load("http://" + appPreferences.getApiIP() + plate.getImage())
                    .placeholder(R.drawable.img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCapa);
        }
    }
}
