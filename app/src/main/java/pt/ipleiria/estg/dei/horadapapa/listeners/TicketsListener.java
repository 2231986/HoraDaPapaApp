package pt.ipleiria.estg.dei.horadapapa.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.models.HelpTicket;


public interface TicketsListener {
    void onRefreshTickets(ArrayList<HelpTicket> list);

}
