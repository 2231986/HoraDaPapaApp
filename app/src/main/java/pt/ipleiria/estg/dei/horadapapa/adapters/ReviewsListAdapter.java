package pt.ipleiria.estg.dei.horadapapa.adapters;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.R;
import pt.ipleiria.estg.dei.horadapapa.activities.extra.MenuActivity;
import pt.ipleiria.estg.dei.horadapapa.activities.extra.ReviewDetailsActivity;
import pt.ipleiria.estg.dei.horadapapa.models.Review;

public class ReviewsListAdapter extends BaseAdapter {



    private final Context context;
    private final LayoutInflater layoutInflater;

    private final ArrayList<Review> reviews;

    public ReviewsListAdapter(Context context, ArrayList<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reviews.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReviewsListAdapter.ViewHolderLista viewHolderLista;
        final Review currentReview = reviews.get(position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.fragment_review, null);
            viewHolderLista = new ViewHolderLista(convertView);
            convertView.setTag(viewHolderLista);
        } else {
            viewHolderLista = (ViewHolderLista) convertView.getTag();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Detalhe aberto!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ReviewDetailsActivity.class);
                intent.putExtra("PlateId", currentReview.getPlate_id());
                intent.putExtra("Description", currentReview.getDescription());
                intent.putExtra("Value", currentReview.getValue());
                context.startActivity(intent);
            }
        });
/*
        Button btn_addRequest = convertView.findViewById(R.id.btn_addRequest);
        View finalView = convertView;
        btn_addRequest.setOnClickListener(v -> {
            int reviewId = currentReview.getId();

            String observation = ""; //TODO: Implementar este campo

            Singleton.getInstance(context).requestRequestPlate(context, reviewId, observation);
        });*/

        viewHolderLista.update(currentReview);
        return convertView;
    }


    private class ViewHolderLista {
        private final TextView tvDesc;
        private final TextView tvValue;

        public ViewHolderLista(View view) {
            tvDesc = view.findViewById(R.id.textView12);
            tvValue = view.findViewById(R.id.textView15);
            //imgCapa = view.findViewById(R.id.imageView);

        }

        public void update(Review review) {
            tvDesc.setText(review.getDescription());
            tvValue.setText(String.valueOf(review.getValue()));

            //Glide.with(context);
                    //.load(plate.getImage())
                    //.placeholder(R.drawable.img)
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                   // .into(imgCapa);
        }

    }
}
