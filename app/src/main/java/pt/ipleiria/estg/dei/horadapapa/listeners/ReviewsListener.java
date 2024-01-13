package pt.ipleiria.estg.dei.horadapapa.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.models.Dinner;
import pt.ipleiria.estg.dei.horadapapa.models.Review;

public interface ReviewsListener {
    void onRefreshReviews(ArrayList<Review> list);
}
