package pt.ipleiria.estg.dei.horadapapa.adapters;

import static pt.ipleiria.estg.dei.horadapapa.activities.extra.review.ReviewDetailsActivity.ID_REVIEW;
import static pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper.BetterToast;

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
import pt.ipleiria.estg.dei.horadapapa.activities.extra.review.ReviewDetailsActivity;
import pt.ipleiria.estg.dei.horadapapa.models.Plate;
import pt.ipleiria.estg.dei.horadapapa.models.Review;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

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
                intent.putExtra(ID_REVIEW, currentReview.getId());

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
        private final TextView tvDesc, tvValue, tvPlateID;

        public ViewHolderLista(View view) {
            tvDesc = view.findViewById(R.id.tv_Des);
            tvValue = view.findViewById(R.id.textView15);
            tvPlateID = view.findViewById(R.id.textView11);
        }

        public void update(Review review) {
            tvDesc.setText(review.getDescription());
            tvValue.setText(String.valueOf(review.getValue()));

            int plateID = review.getPlate_id();

            if (plateID > 0) {
                Plate plate = Singleton.getInstance(context).dbGetPlate(plateID);

                if (plate != null) {
                    tvPlateID.setText(String.valueOf(plate.getTitle()));
                } else {
                    BetterToast(context, "Os pratos ainda não foram carregados!");
                }
            } else {
                BetterToast(context, "Os pratos ainda não foram carregados!");
            }

        }

    }
}
