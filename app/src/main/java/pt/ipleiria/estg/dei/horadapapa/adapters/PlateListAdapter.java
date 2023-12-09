package pt.ipleiria.estg.dei.horadapapa.adapters;

import static java.security.AccessController.getContext;
import static pt.ipleiria.estg.dei.horadapapa.PlateDetailsActivity.ID_PLATE;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.PlateDetailsActivity;
import pt.ipleiria.estg.dei.horadapapa.R;
import pt.ipleiria.estg.dei.horadapapa.models.Plate;

public class PlateListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Plate> plates;


    public PlateListAdapter(Context context, ArrayList<Plate> plates) {
        this.context = context;
        this.plates = plates;
        this.layoutInflater = LayoutInflater.from(context);
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

        if (view == null) {
            view = layoutInflater.inflate(R.layout.fragment_plate, null);
            viewHolderLista = new ViewHolderLista(view);
            view.setTag(viewHolderLista);
        } else {
            viewHolderLista = (ViewHolderLista) view.getTag();
        }

        // Assuming this is where you set the click listener for the PlateFragment
        final Plate currentPlate = plates.get(i);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click here, for example, start PlateDetailsActivity

                Toast.makeText(context, "AQUI", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, PlateDetailsActivity.class);
                intent.putExtra(ID_PLATE, currentPlate.getId());
                context.startActivity(intent);
            }
        });

        viewHolderLista.update(plates.get(i));
        return view;
    }


    private class ViewHolderLista{
        private TextView tvTitle, tvDesc, tvPrice;
        private ImageView imgCapa;

        public ViewHolderLista(View view){
            tvTitle = view.findViewById(R.id.textView);
            tvDesc = view.findViewById(R.id.textView2);
            tvPrice = view.findViewById(R.id.textView3);
            imgCapa = view.findViewById(R.id.imageView);

        }

        public void update (Plate plate){
            tvTitle.setText(plate.getTitle());
            tvDesc.setText(plate.getDescription());
            tvPrice.setText(plate.getPrice());
            Glide.with(context)
                    .load(plate.getImage())
                    .placeholder(R.drawable.img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCapa);
        }
    }
}
