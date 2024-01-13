package pt.ipleiria.estg.dei.horadapapa.activities.invoice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.R;
import pt.ipleiria.estg.dei.horadapapa.adapters.InvoiceListAdapter;
import pt.ipleiria.estg.dei.horadapapa.listeners.InvoicesListener;
import pt.ipleiria.estg.dei.horadapapa.models.Invoice;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InvoicesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvoicesListFragment extends Fragment implements InvoicesListener {

    private ListView lvInvoices;

    public InvoicesListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invoices_list, container, false);

        //setHasOptionsMenu(true);

        lvInvoices = view.findViewById(R.id.lvInvoices);
        Singleton.getInstance(getContext()).setInvoicesListener(this);
        Singleton.getInstance(getContext()).requestInvoiceGetAll(getContext());

        return view;
    }

    @Override
    public void onRefreshInvoices(ArrayList<Invoice> list) {
        if (list != null) {
            lvInvoices.setAdapter(new InvoiceListAdapter(getContext(), list));
        }
    }

}