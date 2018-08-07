package pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.MnozstvaTovarov;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Database.DatabaseHelper;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.ImageHelpers;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.OrmLiteAppCompatActivity;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.MnozstvaTovaru;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Tovar;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.MinimalneMnozstva.MinimalneMnozstvaTovarovDataModel;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.ISimpleRowClickListener;

public class ListMnozstvaTovarovVMiestnostiach extends AppCompatActivity  {

   // private int myTovarId;
    TextView tovarnazovTV;
    TextView mnozstvoTV;
    ImageView myObrazok;

    List<MnozstvaTovaru > zoznamHM = null;

    MnozstvaTovarovDataModel dm = new MnozstvaTovarovDataModel(this);
    MnozstvaTovarovVMiestnostiachAdapter mnozstvaTovaruVMiestnostiachAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tovar myTovar = null;
       // String myTovarName = "";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            myTovar = getIntent().getParcelableExtra("myTovar");
        }
        setContentView(R.layout.activity_mnozstvo_tovaru_vmiestnostiach);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView tovarImage = findViewById(R.id.tovarImage);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(myTovar.getNazov());

        if (myTovar.getFotografia() != null && myTovar.getFotografia().length > 1) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(myTovar.getFotografia());
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            tovarImage.setImageBitmap(theImage);
        } else {
            tovarImage.setImageResource(R.drawable.not_disturb);
        }

        try {
            // zoznam inventarov v miestnosti
            zoznamHM = dm.getMnozstvaVMiestnostiach(myTovar.getId());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        View exportnyButton = findViewById(R.id.fab_Export);
        exportnyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    ExportUdajov();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        ListView lw = (ListView) findViewById(R.id.list_mnozstva_tovaru_vmiestnostiach);

        mnozstvaTovaruVMiestnostiachAdapter = new MnozstvaTovarovVMiestnostiachAdapter(this, R.layout.activity_mnozstvo_tovaru_vmiestnostiach_row , zoznamHM);
        lw.setAdapter(mnozstvaTovaruVMiestnostiachAdapter);

    }

    private void ExportUdajov() throws IOException {

        // Creating a folder in the data/data/pkg/files directory
        // https://infinum.co/the-capsized-eight/share-files-using-fileprovider

        SimpleDateFormat s = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        String format = s.format(new Date());

        File filePath = new File(String.valueOf(getBaseContext().getFilesDir()));
        File yourFile = new File(filePath + File.separator + "mnozstvaTovaruVMiestnostiach.csv");
        yourFile.createNewFile(); // vytvorenie !!!

        //FileOutputStream writer  = new FileOutputStream(yourFile, false);

        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(yourFile, false),
                "windows-1250");  // "windows-1252" , "UTF-8", "ISO-8859-2"

       // writer.write(("Dňa: "+ format +" \n\n").getBytes());
        writer.append("Vytvorené: "+ format +" \n\n");

        writer.append("Prehľad množstiev vybratého tovaru v jednotlivých miestnostiach \n\n");


      //  writer.write(("tovar;aktualne mnozstvo;minimalne mnozstvo\n").getBytes());
        writer.append("Názov;Množstvo;Miestnosť\n");

        int kolko = zoznamHM.size();
        MnozstvaTovaru xx;
        String tovar = "";
        String osoba, datum;
        int aktMnozstvo;
        int minimMnozstvo;
        String miestnostFull;
       // String[] umiestnenie;


        for (int iItem = 0; iItem < kolko; iItem++) {

            xx = zoznamHM.get(iItem);
            tovar = xx.getTovarName();

            //umiestnenie = dm.getKoordinatyMiestnosti(xx.getMiestnost());
            miestnostFull = xx.getMiestnostFullName();  //umiestnenie[0] + "-" + umiestnenie[1] + "-" + umiestnenie[2];
            aktMnozstvo = (int) xx.getMnozstvo();

            writer.append(tovar+";"+String.valueOf(aktMnozstvo)+";"+miestnostFull+"\n"); // pridane miestnostFullName xxx

        }

        writer.close();

        Uri uri = FileProvider.getUriForFile(this, "pohybytovaru.innovativeproposals.com.FileProvider", yourFile);

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(uri);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"lubos.jokl@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Množstvá v miestnostiach pre tovar " + tovar);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Množstvá v miestnostiach pre tovar " + tovar);

        startActivity(Intent.createChooser(emailIntent, "Share"));

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