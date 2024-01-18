package pt.ipleiria.estg.dei.horadapapa.adapters;

import static pt.ipleiria.estg.dei.horadapapa.activities.invoice.InvoiceDetailsActivity.ID_INVOICE;

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
import pt.ipleiria.estg.dei.horadapapa.activities.invoice.InvoiceDetailsActivity;
import pt.ipleiria.estg.dei.horadapapa.models.Invoice;

public class InvoiceListAdapter extends BaseAdapter {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final ArrayList<Invoice> invoices;


    public InvoiceListAdapter(Context context, ArrayList<Invoice> invoices) {
        this.context = context;
        this.invoices = invoices;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return invoices.size();
    }

    @Override
    public Object getItem(int i) {
        return invoices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return invoices.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolderLista viewHolderLista;
        final Invoice currentInvoice = invoices.get(i);

        if (view == null) {
            view = layoutInflater.inflate(R.layout.fragment_invoice, null);
            viewHolderLista = new ViewHolderLista(view);
            view.setTag(viewHolderLista);
        } else {
            viewHolderLista = (ViewHolderLista) view.getTag();
        }

        view.setOnClickListener(v -> {
            Toast.makeText(context, "Detalhe aberto!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, InvoiceDetailsActivity.class);
            intent.putExtra(ID_INVOICE, currentInvoice.getId());
            context.startActivity(intent);
        });

        viewHolderLista.update(currentInvoice);

        return view;
    }

    private class ViewHolderLista {
        private final TextView tvInvoiceID;
        private final TextView tvMealID;
        private final TextView tvTotal;

        public ViewHolderLista(View view) {
            tvInvoiceID = view.findViewById(R.id.tv_invoiceID);
            tvMealID = view.findViewById(R.id.tv_invoice_meal_id);
            tvTotal = view.findViewById(R.id.tv_invoicePrice);
        }

        public void update(Invoice invoice) {
            tvInvoiceID.setText(invoice.getId() + "");
            tvMealID.setText(invoice.getMeal_id() + "");
            tvTotal.setText(invoice.getPriceFormatted() + "");
        }
    }
}
