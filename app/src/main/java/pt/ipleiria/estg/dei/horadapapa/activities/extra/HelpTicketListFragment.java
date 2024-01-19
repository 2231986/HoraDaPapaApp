package pt.ipleiria.estg.dei.horadapapa.activities.extra;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.R;
import pt.ipleiria.estg.dei.horadapapa.adapters.ReviewsListAdapter;
import pt.ipleiria.estg.dei.horadapapa.adapters.TicketsListAdapter;
import pt.ipleiria.estg.dei.horadapapa.listeners.TicketsListener;
import pt.ipleiria.estg.dei.horadapapa.models.HelpTicket;
import pt.ipleiria.estg.dei.horadapapa.models.Review;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HelpTicketListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpTicketListFragment extends Fragment {

    private ListView lvHelpTicket;


    public HelpTicketListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help_ticket_list, container, false);

        //setHasOptionsMenu(true);

        lvHelpTicket = view.findViewById(R.id.lvTickets);
        Singleton.getInstance(getContext()).setTicketsListener(this::onRefreshTickets);
        Singleton.getInstance(getContext()).requestTicketGetAll(getContext());

        return view;
    }

    public void onRefreshTickets(ArrayList<HelpTicket> list) {
        if (list != null) {
            lvHelpTicket.setAdapter(new TicketsListAdapter(getContext(), list));
        }
    }
}