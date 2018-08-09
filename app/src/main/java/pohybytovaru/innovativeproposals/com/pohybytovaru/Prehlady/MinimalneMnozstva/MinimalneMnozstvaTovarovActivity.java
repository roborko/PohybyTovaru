package pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.MinimalneMnozstva;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
//import java.sql.SQLException;
//import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Constants;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.MnozstvaTovaru;

import pohybytovaru.innovativeproposals.com.pohybytovaru.R;


//@EActivity(R.layout.activity_minimalne_mnozstvo_tovaru)
public class MinimalneMnozstvaTovarovActivity extends AppCompatActivity {

    List<MnozstvaTovaru> zoznamHM = null;

    MinimalneMnozstvaTovarovDataModel dm = new MinimalneMnozstvaTovarovDataModel(this); // pri kopirovani do inej triedy zmen
    MinimalneMnozstvaTovarovAdapter minimalneMnozstvaTovaruAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //enable search within activity

        setContentView(R.layout.activity_minimalne_mnozstvo_tovaru); // list_mnozstva_tovaru_total
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        View exportnyButton = findViewById(R.id.fab_newExport);
        exportnyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    ExportTovarov();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void ExportTovarov() throws IOException {

        // Creating a folder in the data/data/pkg/files directory
        // https://infinum.co/the-capsized-eight/share-files-using-fileprovider

        SimpleDateFormat s = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        String format = s.format(new Date());


        File filePath = new File(String.valueOf(getBaseContext().getFilesDir()));
        File yourFile = new File(filePath + File.separator + "chybajuceMnozstva.csv");
        yourFile.createNewFile(); // vytvorenie !!!

        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(yourFile, false),
                "windows-1250");  // "windows-1252" , "UTF-8", "ISO-8859-2"

        writer.append("Vytvorené: "+ format +" \n\n");
        writer.append("Zoznam tovarov s množstvom nižším než minimálnym \n\n");

     //   writer.write(("tovar;aktualne mnozstvo;minimalne mnozstvo\n").getBytes());
        writer.append("Názov;Aktuálne množstvo;Minimálne množstvo\n");

        int kolko = zoznamHM.size();
        MnozstvaTovaru xx;
        String tovar = "";
        String osoba, datum, miestnostOd, miestostDo;
        int aktMnozstvo, minimMnozstvo;

        for (int iItem = 0; iItem < kolko; iItem++) {

            xx = zoznamHM.get(iItem);
            tovar = xx.getTovarName();

            aktMnozstvo = (int) xx.getMnozstvo();
            minimMnozstvo = (int) xx.getLimitne_mnozstvo();

            writer.append(tovar+";"+String.valueOf(aktMnozstvo)+";"+String.valueOf(minimMnozstvo)+"\n");

        }

        writer.close();



      //zle  Uri uri = FileProvider.getUriForFile(this, "${applicationId}", new File(yourFile.toString()));
       Uri uri = FileProvider.getUriForFile(this, "pohybytovaru.innovativeproposals.com.FileProvider", yourFile);

       Intent emailIntent = new Intent(Intent.ACTION_SEND);
       emailIntent.setData(uri);
       emailIntent.setType("text/plain");
       emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
       emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName() + Constants.PREF_FILE_NAME, MODE_PRIVATE);
        String address = sharedPreferences.getString(Constants.MAIL_ADDRESS, "zadajte mail do nastaveni"); // druhy parameter je defaultna hodnota

       emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {address});
       emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Chybajúce množstvá tovarov ");
       emailIntent.putExtra(Intent.EXTRA_TEXT, "Zoznam tovarov s množstvom nižším než zadefinovaným");

       startActivity(Intent.createChooser(emailIntent, "Share"));

    }

    private MnozstvaTovaru findInventarById(Integer itemId) {
        for (MnozstvaTovaru item : zoznamHM) {
            if (item.getId() == itemId)
                return item;
        }

        //toto by sa nikdy nemalo stat
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Called when the activity is becoming visible to the user.
        //startScan(); // aktivuj hw tlacidlo skeneru
    }
}
