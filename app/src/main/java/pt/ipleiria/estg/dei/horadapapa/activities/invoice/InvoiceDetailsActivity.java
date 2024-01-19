package pt.ipleiria.estg.dei.horadapapa.activities.invoice;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.horadapapa.R;
import pt.ipleiria.estg.dei.horadapapa.adapters.PlateListAdapter;
import pt.ipleiria.estg.dei.horadapapa.models.Invoice;
import pt.ipleiria.estg.dei.horadapapa.models.InvoiceRequest;
import pt.ipleiria.estg.dei.horadapapa.models.Plate;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;
import pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper;

public class InvoiceDetailsActivity extends AppCompatActivity {

    public static final String ID_INVOICE = "ID_INVOICE";
    Invoice invoice;

    TextView tvInvoiceTotalPrice, tvInvoiceID, tvInvoiceMealID;

    ListView lvInvoiceRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);
        setTitle("Invoice Details");


        int id = getIntent().getIntExtra(ID_INVOICE, 0);

        if (id == 0) {
            ProjectHelper.BetterToast(this, "Fatura inválida!");
            finish();
        } else {
            invoice = Singleton.getInstance(this).dbGetInvoice(id);
            if (invoice == null)
                ProjectHelper.BetterToast(this, "Fatura não encontrada!");
        }

        tvInvoiceTotalPrice = findViewById(R.id.tv_InvoiceTotalPrice);
        tvInvoiceID = findViewById(R.id.textView19);
        tvInvoiceMealID = findViewById(R.id.textView20);
        lvInvoiceRequests = findViewById(R.id.lv_InvoiceRequests);

        loadUI();
    }

    private void loadUI() {

        ArrayList<Plate> plates = new ArrayList<>();
        ArrayList<InvoiceRequest> invoiceRequests = invoice.getInvoiceRequests();

        for (InvoiceRequest request : invoiceRequests) {

            Plate plate = Singleton.getInstance(this).dbGetPlate(request.getPlate_id());

            if (plate != null) {
                plates.add(plate);
            }
        }

        if (plates.size() > 0) {
            lvInvoiceRequests.setAdapter(new PlateListAdapter(this, plates, true));
            tvInvoiceTotalPrice.setText(invoice.getPriceFormatted() + "");
            tvInvoiceID.setText(invoice.getId() + "");
            tvInvoiceMealID.setText(invoice.getMeal_id() + "");
        } else {
            ProjectHelper.BetterToast(this, "Os Pratos ainda não foram carregados!");
        }
    }
}