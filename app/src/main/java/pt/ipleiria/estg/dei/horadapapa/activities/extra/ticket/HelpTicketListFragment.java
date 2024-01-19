package pt.ipleiria.estg.dei.horadapapa.activities.extra.ticket;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.R;
import pt.ipleiria.estg.dei.horadapapa.activities.extra.MenuActivity;
import pt.ipleiria.estg.dei.horadapapa.adapters.TicketsListAdapter;
import pt.ipleiria.estg.dei.horadapapa.models.HelpTicket;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HelpTicketListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpTicketListFragment extends Fragment {

    private ListView lvHelpTicket;
    private FloatingActionButton fabadd;


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

        fabadd = view.findViewById(R.id.fabticketlistadd);
        fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HelpTicketDetailsActivity.class);
                startActivityForResult(intent, MenuActivity.ADD);
            }
        });


        return view;
    }

    public void onRefreshTickets(ArrayList<HelpTicket> list) {
        if (list != null) {
            lvHelpTicket.setAdapter(new TicketsListAdapter(getContext(), list));
        }
    }
}