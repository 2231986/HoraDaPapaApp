package pt.ipleiria.estg.dei.horadapapa.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.models.Plate;

public interface FavoritesListener {
    void onRefreshFavorites(ArrayList<Plate> list);
}
