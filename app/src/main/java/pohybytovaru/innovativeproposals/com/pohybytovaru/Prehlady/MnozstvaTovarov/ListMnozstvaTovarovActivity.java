package pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.MnozstvaTovarov;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.MnozstvaTovaru;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Tovar;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.MinimalneMnozstva.MinimalneMnozstvaTovarovDataModel;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;

public class ListMnozstvaTovarovActivity  extends AppCompatActivity  {

    private String myTovarName;
    TextView tovarnazovTV;
    TextView mnozstvoTV;
    //ImageView image = null;

    List<MnozstvaTovaru > zoznamHM = null;

    MnozstvaTovarovDataModel dm = new MnozstvaTovarovDataModel(this); // pri kopirovani do inej triedy zmen
    MnozstvaTovarovAdapter mnozstvaTovaruAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //enable search within activity

        myTovarName = "";

        setContentView(R.layout.activity_mnozstvo_tovaru_total); // list_mnozstva_tovaru_total
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            // zoznam inventarov v miestnosti
            zoznamHM = dm.getTovarTotalList(myTovarName);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        ListView lw = (ListView) findViewById(R.id.list_mnozstva_tovaru_total);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //kliknutie na polozku zoznamu
            @SuppressLint("RestrictedApi")
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
             //   tovarnazovTV = (TextView) view.findViewById(R.id.tovarnazovTV);
             //   mnozstvoTV = (TextView) view.findViewById(R.id.mnozstvoTV);
                int tovarID = zoznamHM.get(position).getTovar();

                Tovar myTovar = dm.getTovar(tovarID);

                Intent theIndent = new Intent(getApplication(),
                        //ViewInventarDetail.class);
                        ListMnozstvaTovarovVMiestnostiach.class);

                //theIndent.putExtra("myId",TovarID);
                //theIndent.putExtra("myTovarName",zoznamHM.get(position).getTovarName());

                theIndent.putExtra("myTovar",myTovar);


                startActivity(theIndent);
            }
        });
        mnozstvaTovaruAdapter = new MnozstvaTovarovAdapter(this, R.layout.activity_mnozstvo_tovaru_total_row, zoznamHM);
        lw.setAdapter(mnozstvaTovaruAdapter);

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

    }

    private void ExportUdajov() throws IOException {

        // Creating a folder in the data/data/pkg/files directory
        // https://infinum.co/the-capsized-eight/share-files-using-fileprovider

        SimpleDateFormat s = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        String format = s.format(new Date());

        File filePath = new File(String.valueOf(getBaseContext().getFilesDir()));
        File yourFile = new File(filePath + File.separator + "aktualneMnozstvo.csv");
        yourFile.createNewFile(); // vytvorenie !!!


        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(yourFile, false),
                "windows-1250");  // "windows-1252" , "UTF-8", "ISO-8859-2"

        writer.append("Vytvorené: "+ format +" \n\n");
        writer.append("Prehľad aktuálnych množstiev tovarov\n\n");


        writer.append("Tovar;Aktuálne množstvo\n");

        int kolko = zoznamHM.size();
        MnozstvaTovaru xx;
        String tovar = "";
        String osoba, datum, miestnostOd, miestostDo;
        int aktMnozstvo, minimMnozstvo;

        for (int iItem = 0; iItem < kolko; iItem++) {

            xx = zoznamHM.get(iItem);
            tovar = xx.getTovarName();

            aktMnozstvo = (int) xx.getMnozstvo(); // setMnozstvo
          //  minimMnozstvo = (int) xx.getLimitne_mnozstvo();

           // writer.write((convertText4Windows(tovar) + ";"+String.valueOf(aktMnozstvo)+"\n").getBytes());
            writer.append(tovar + ";"+String.valueOf(aktMnozstvo)+"\n");

        }

        writer.close();

        //zle  Uri uri = FileProvider.getUriForFile(this, "${applicationId}", new File(yourFile.toString()));
        Uri uri = FileProvider.getUriForFile(this, "pohybytovaru.innovativeproposals.com.FileProvider", yourFile);

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(uri);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"lubos.jokl@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Aktualne množstvá tovarov ");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Zoznam aktuálnych množstiev tovarov");

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


    public static String convertText4Windows (String input){

        String output = "";
        try {
        /* From ISO-8859-1 to UTF-8 */
            //output = new String(input.getBytes("Windows-1252"), "UTF-8");  // "UTF-8"
        /* From UTF-8 to ISO-8859-1 */
         //   output = new String(input.getBytes("UTF-8"), "Windows-1252");

           // output = new String(input.getBytes("UTF-8"), Charset.forName("Windows-1252"));
           // output = new String(input.getBytes("Windows-1252"), Charset.forName("UTF-8"));

//            output = new String(input.getBytes(), "ISO-8859-1");
           // output = new String(input.getBytes(), "Windows-1250");
          //  output = new String(input.getBytes(), "ISO-8859-2"); // Latin-2
          //  output = new String(input.getBytes(), Charset.forName("ISO-8859-2")); // Latin-2

        output = new String(input.getBytes("ISO-8859-2"), Charset.forName("UTF-8"));

         //   output = new String(input.getBytes("x-iscii-be"));

           // output = new String (input.getBytes("UTF-8"),"UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return output;
    }



}
