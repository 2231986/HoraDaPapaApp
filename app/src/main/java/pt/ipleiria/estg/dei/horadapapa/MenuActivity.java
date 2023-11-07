package pt.ipleiria.estg.dei.horadapapa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class MenuActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private FragmentManager fragmentManager;
    private NavigationView navigationView;

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

        //navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);




    }

    /*public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        if (item.getItemId()==R.id.navMeal){
            //System.out.println("---> Nav Estático");
            //fragment = new EstaticoFragment();
            //fragment = new PlateListFragment();
            setTitle(item.getTitle());
        }

        if (fragment != null){
            fragmentManager.beginTransaction().replace(R.id.contentLayout, fragment).commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }*/
}