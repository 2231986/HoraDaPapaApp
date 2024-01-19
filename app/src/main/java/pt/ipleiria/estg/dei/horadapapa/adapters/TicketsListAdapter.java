package pt.ipleiria.estg.dei.horadapapa.adapters;

import static pt.ipleiria.estg.dei.horadapapa.activities.extra.HelpTicketDetailsActivity.ID_TICKET;
import static pt.ipleiria.estg.dei.horadapapa.activities.extra.ReviewDetailsActivity.ID_REVIEW;
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
import pt.ipleiria.estg.dei.horadapapa.activities.extra.HelpTicketDetailsActivity;
import pt.ipleiria.estg.dei.horadapapa.activities.extra.ReviewDetailsActivity;
import pt.ipleiria.estg.dei.horadapapa.models.HelpTicket;
import pt.ipleiria.estg.dei.horadapapa.models.Plate;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

public class TicketsListAdapter extends BaseAdapter {

    private final Context context;
    private final LayoutInflater layoutInflater;

    private final ArrayList<HelpTicket> tickets;
    public TicketsListAdapter(Context context, ArrayList<HelpTicket> tickets) {
        this.context = context;
        this.tickets = tickets;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {return tickets.size();}

    @Override
    public Object getItem(int position) {return tickets.get(position);}

    @Override
    public long getItemId(int position) {return tickets.get(position).getId();}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TicketsListAdapter.ViewHolderLista viewHolderLista;
        final HelpTicket currentTicket = tickets.get(position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.fragment_ticket, null);
            viewHolderLista = new TicketsListAdapter.ViewHolderLista(convertView);
            convertView.setTag(viewHolderLista);
        } else {
            viewHolderLista = (TicketsListAdapter.ViewHolderLista) convertView.getTag();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Detalhe aberto!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, HelpTicketDetailsActivity.class);
                intent.putExtra(ID_TICKET, currentTicket.getId());

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

        viewHolderLista.update(currentTicket);
        return convertView;
    }

    private class ViewHolderLista {
        private final TextView tvDesc, tvTicketID;

        public ViewHolderLista(View view) {
            tvDesc = view.findViewById(R.id.tv_Descr);
            tvTicketID = view.findViewById(R.id.tv_ticketID);
        }

        public void update(HelpTicket ticket)
        {
            tvTicketID.setText(String.valueOf(ticket.getId()));

            tvDesc.setText(String.valueOf(ticket.getDescription()));

        }

    }
}
