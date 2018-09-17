package pohybytovaru.innovativeproposals.com.pohybytovaru;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.net.URISyntaxException;
import java.util.List;


import pohybytovaru.innovativeproposals.com.pohybytovaru.Budovy.ListBudovaActivity_;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Database.DeafultValuesPopulator;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.MnozstvaTovaru;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.MinimalneMnozstva.MinimalneMnozstvaTovarovActivity;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.MinimalneMnozstva.MinimalneMnozstvaTovarovAdapter;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.MinimalneMnozstva.MinimalneMnozstvaTovarovDataModel;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.MnozstvaTovarov.ListMnozstvaTovarovActivity;
//import pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.MnozstvaTovarov.ListMnozstvaTovarovActivity_;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.PohybTovarov.PohybTovarActivity_;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Tovary.ListTovarActivity_;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Tovary.ImportTovarov;


@EActivity
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    @ViewById
    Toolbar toolbar;

    @ViewById(R.id.drawer_layout)
    DrawerLayout drawer;

    @ViewById(R.id.nav_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN); // stale necha otvorene navigation bar

        //populate default values
         DeafultValuesPopulator.PopulateDefaultValues(this);  // toto presun do vytvorenia databazy

        //        showLimitneMnozstva();

        /* odtialto

        MinimalneMnozstvaTovarovDataModel dm = new MinimalneMnozstvaTovarovDataModel(this); // pri kopirovani do inej triedy zmen
        MinimalneMnozstvaTovarovAdapter minimalneMnozstvaTovaruAdapter;

        List<MnozstvaTovaru> zoznamHM = null;

        // xx    setContentView(R.layout.activity_minimalne_mnozstvo_tovaru); // list_mnozstva_tovaru_total
        //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            // zoznam inventarov v miestnosti
            zoznamHM = dm.getPrekroceneMinimalneMnozstvo();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //   if (zoznamHM.size() != 0) {
        ListView lw = (ListView) findViewById(R.id.list_min_mnozstva_tovaru);

        minimalneMnozstvaTovaruAdapter = new MinimalneMnozstvaTovarovAdapter(this, R.layout.activity_minimalne_mnozstvo_tovaru_row, zoznamHM);
        lw.setAdapter(minimalneMnozstvaTovaruAdapter);

potialto

        */
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.nav_miestnosti:
                //intent = new Intent(this, ListMiestnostiActivity_.class); // tu bol podciarkovnik
                intent = new Intent(this, ListBudovaActivity_.class); // tu bol podciarkovnik
                startActivity(intent);
                break;
            case R.id.nav_tovary:
                intent = new Intent(this, ListTovarActivity_.class); // tu bol podciarkovnik
                startActivity(intent);
                break;
            case R.id.nav_pohybTovaru:
                intent = new Intent(this, PohybTovarActivity_.class); // tu bol podciarkovnik
                startActivity(intent);
                break;
            case R.id.nav_InventarVMiestnosti:
                intent = new Intent(this, ListMnozstvaTovarovActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_MinimalneMnozstva:
                intent = new Intent(this, MinimalneMnozstvaTovarovActivity.class);
                startActivity(intent);
                break;

         /*   case R.id.nav_voziky:
                intent = new Intent(this, VozikListActivity_.class);
                startActivity(intent);
                break; */

          /*  case R.id.nav_pokus_three:
                intent = new Intent(this, ThreeLevelExpandableListView.class);
                startActivity(intent);
                break; */

            case R.id.nav_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_import:
                intent = new Intent(this, ImportTovarov.class);
                startActivity(intent);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void showLimitneMnozstva() {

        MinimalneMnozstvaTovarovDataModel dm = new MinimalneMnozstvaTovarovDataModel(this); // pri kopirovani do inej triedy zmen
        MinimalneMnozstvaTovarovAdapter minimalneMnozstvaTovaruAdapter;

        List<MnozstvaTovaru> zoznamHM = null;

        setContentView(R.layout.activity_minimalne_mnozstvo_tovaru); // list_mnozstva_tovaru_total
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            // zoznam inventarov v miestnosti
            zoznamHM = dm.getPrekroceneMinimalneMnozstvo();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //   if (zoznamHM.size() != 0) {
        ListView lw = (ListView) findViewById(R.id.list_min_mnozstva_tovaru);

        minimalneMnozstvaTovaruAdapter = new MinimalneMnozstvaTovarovAdapter(this, R.layout.activity_minimalne_mnozstvo_tovaru_row, zoznamHM);
        lw.setAdapter(minimalneMnozstvaTovaruAdapter);

    }
}
