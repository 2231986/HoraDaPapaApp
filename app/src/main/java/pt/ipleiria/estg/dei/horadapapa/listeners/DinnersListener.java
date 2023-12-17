package pt.ipleiria.estg.dei.horadapapa.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.models.Dinner;
import pt.ipleiria.estg.dei.horadapapa.models.Plate;

public interface DinnersListener {
    void onRefreshDinners(ArrayList<Dinner> list);
}
