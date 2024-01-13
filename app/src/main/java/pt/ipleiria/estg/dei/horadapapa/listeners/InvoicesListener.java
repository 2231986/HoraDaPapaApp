package pt.ipleiria.estg.dei.horadapapa.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.models.Invoice;

public interface InvoicesListener {
    void onRefreshInvoices(ArrayList<Invoice> list);
}