package pt.ipleiria.estg.dei.horadapapa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.R;
import pt.ipleiria.estg.dei.horadapapa.models.Plate;

public class PlateListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Plate> plates;


    public PlateListAdapter(Context context, ArrayList<Plate> plates) {
        this.context = context;
        this.plates = plates;
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
        if (layoutInflater==null)
            layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null )
            view = layoutInflater.inflate(R.layout.fragment_plate,null);

        ViewHolderLista viewHolderLista = (ViewHolderLista)  view.getTag();
        if(viewHolderLista == null) {
            viewHolderLista = new ViewHolderLista(view);
            view.setTag(viewHolderLista);
        }
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
            tvPrice.setText(""+plate.getPrice());//Concatenar uma string porque value é integer
            imgCapa.setImageResource(plate.getCapa());
        }
    }
}
