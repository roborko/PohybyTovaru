package pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.MnozstvaTovarov;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.net.URISyntaxException;
import java.util.List;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.MnozstvaTovaru;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;

public class ListMnozstvaTovarovActivity  extends AppCompatActivity  {

    private String myTovarName;
    TextView tovarnazovTV;
    TextView mnozstvoTV;

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
                tovarnazovTV = (TextView) view.findViewById(R.id.tovarnazovTV);
                mnozstvoTV = (TextView) view.findViewById(R.id.mnozstvoTV);
                int TovarID = (int) view.getTag();
                MnozstvaTovaru myTovar = findInventarById(TovarID);

                Intent theIndent = new Intent(getApplication(),
                        //ViewInventarDetail.class);
                        ListMnozstvaTovarovVMiestnostiach.class);

                theIndent.putExtra("myId",TovarID);
                theIndent.putExtra("myTovarName",myTovar.getTovar());

                startActivity(theIndent);
            }
        });
        mnozstvaTovaruAdapter = new MnozstvaTovarovAdapter(this, R.layout.activity_mnozstvo_tovaru_total_row, zoznamHM);
        lw.setAdapter(mnozstvaTovaruAdapter);
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
