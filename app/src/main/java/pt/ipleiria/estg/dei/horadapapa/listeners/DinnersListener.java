package pt.ipleiria.estg.dei.horadapapa.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.models.Dinner;

public interface DinnersListener {
    void onRefreshDinners(ArrayList<Dinner> list);
}
