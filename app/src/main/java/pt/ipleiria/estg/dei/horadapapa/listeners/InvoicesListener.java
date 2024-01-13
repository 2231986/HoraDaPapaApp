package pt.ipleiria.estg.dei.horadapapa.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.models.Invoice;
import pt.ipleiria.estg.dei.horadapapa.models.Plate;

public interface InvoiceListener {
    void onRefreshInvoices(ArrayList<Invoice> list);
}