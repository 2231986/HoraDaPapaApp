package pt.ipleiria.estg.dei.horadapapa.activities.extra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import pt.ipleiria.estg.dei.horadapapa.R;

public class HelpTicketDetailsActivity extends AppCompatActivity {

    public static final String ID_TICKET = "ID_TICKET";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_ticket_details);
        setTitle("HelpTicket Details");
    }
}