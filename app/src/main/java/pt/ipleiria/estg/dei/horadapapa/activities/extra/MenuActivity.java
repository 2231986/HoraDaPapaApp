package pt.ipleiria.estg.dei.horadapapa.activities.extra;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import pt.ipleiria.estg.dei.horadapapa.R;
import pt.ipleiria.estg.dei.horadapapa.activities.FavouritesListFragment;
import pt.ipleiria.estg.dei.horadapapa.activities.MealListFragment;
import pt.ipleiria.estg.dei.horadapapa.activities.PlateListFragment;
import pt.ipleiria.estg.dei.horadapapa.activities.ReviewListFragment;
import pt.ipleiria.estg.dei.horadapapa.activities.TableListFragment;
import pt.ipleiria.estg.dei.horadapapa.activities.invoice.InvoicesListFragment;

public class MenuActivity extends AppCompatActivity {

    public static final int ADD = 100, EDIT = 200, DELETE = 300;


    private DrawerLayout drawerLayout;

    private FragmentManager fragmentManager;
    private NavigationView navigationView;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentLayout, new TableListFragment()).commit();

        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        if (item.getItemId() == R.id.navMeal) {
            fragment = new MealListFragment();
            setTitle(item.getTitle());
        } else if (item.getItemId() == R.id.navPlate) {
            fragment = new PlateListFragment();
            setTitle(item.getTitle());
        } else if (item.getItemId() == R.id.navReview) {
            fragment = new ReviewListFragment();
            setTitle(item.getTitle());
        } else if (item.getItemId() == R.id.navFavourites) {
            fragment = new FavouritesListFragment();
            setTitle(item.getTitle());
        } else if (item.getItemId() == R.id.navInvoice) {
            fragment = new InvoicesListFragment();
            setTitle(item.getTitle());
        } else if (item.getItemId() == R.id.navTable) {
            fragment = new TableListFragment();
            setTitle(item.getTitle());
        } else if (item.getItemId() == R.id.navEmail) {
            enviarEmail();
        }

        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.contentLayout, fragment).commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void enviarEmail() {
        String subject = "PSI 2023/2024";
        String message = "Olá" + email + ", isto é uma mensagem de teste, enviado na app";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Erro no email", Toast.LENGTH_SHORT).show();
        }
    }
}