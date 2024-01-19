package pt.ipleiria.estg.dei.horadapapa.activities.extra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pt.ipleiria.estg.dei.horadapapa.R;
import pt.ipleiria.estg.dei.horadapapa.models.HelpTicket;
import pt.ipleiria.estg.dei.horadapapa.models.Singleton;

public class HelpTicketDetailsActivity extends AppCompatActivity {

    public static final String ID_TICKET = "ID_TICKET";
    public FloatingActionButton fabTicket;
    public Button btndelete;

    private boolean hideUiRequest = false;

    private HelpTicket ticket;

    private EditText txtDescription;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_ticket_details);
        setTitle("HelpTicket Details");

        int id = getIntent().getIntExtra(ID_TICKET, 0);
        ticket = Singleton.getInstance(getApplicationContext()).dbGetTicket(id);




        fabTicket = findViewById(R.id.fabTicket);
        btndelete = findViewById(R.id.btnDeleteTicket);
        txtDescription=findViewById(R.id.et_ticket);


        if(ticket!=null)
        {
            loadTicket();
            fabTicket.setVisibility(View.GONE);
            btndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // DELETE
                    Toast.makeText(HelpTicketDetailsActivity.this, "Processing delete", Toast.LENGTH_SHORT).show();

                    // Use the review.getId() directly in the requestReviewDelete method
                    Singleton.getInstance(getApplicationContext()).requestTicketDelete(
                            HelpTicketDetailsActivity.this,
                            ticket.getId()
                    );

                    finish();

                }
            });

        }
        else
        {
            btndelete.setVisibility(View.GONE);
            fabTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String description = txtDescription.getText().toString();

                    Singleton.getInstance(getApplicationContext()).requestTicketAdd(HelpTicketDetailsActivity.this, description);
                    finish();

                }
            });

        }



    }

    private void loadTicket() {
        setTitle("Ticket nº - " + ticket.getId());
        txtDescription.setText(ticket.getDescription());
    }
}