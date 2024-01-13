package pt.ipleiria.estg.dei.horadapapa.activities.invoice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import pt.ipleiria.estg.dei.horadapapa.R;
import pt.ipleiria.estg.dei.horadapapa.models.Invoice;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;
import pt.ipleiria.estg.dei.horadapapa.utilities.ProjectHelper;

public class InvoiceDetailsActivity extends AppCompatActivity {

    public static final String ID_INVOICE = "ID_INVOICE";
    Invoice invoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);

        int id = getIntent().getIntExtra(ID_INVOICE, 0);

        if (id == 0) {
            ProjectHelper.BetterToast(this, "Fatura inválida!");
            finish();
        } else {
            invoice = Singleton.getInstance(this).dbGetInvoice(id);
        }
    }
}