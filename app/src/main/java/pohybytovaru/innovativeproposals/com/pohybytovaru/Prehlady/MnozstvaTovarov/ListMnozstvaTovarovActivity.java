package pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.MnozstvaTovarov;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
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

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Database.DatabaseHelper;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.OrmLiteAppCompatActivity;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.MnozstvaTovaru;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Tovar;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.ISimpleRowClickListener;


public class ListMnozstvaTovarovActivity  extends AppCompatActivity  {

    private String myTovarName;
    TextView tovarnazovTV;
    TextView mnozstvoTV;



  //  TextView statusET;
  //  TextView datumET;
    List<MnozstvaTovaru > zoznamHM = null;

    MnozstvaTovarovDataModel dm = new MnozstvaTovarovDataModel(this); // pri kopirovani do inej triedy zmen
   //xx  CustomListInventoryAdapter customListAdapter;
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

        //   if (zoznamHM.size() != 0) {
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
                //  theIndent.putExtra(Constants.INTENT_INVENTORY, inventar);

            /*    View imageView = view.findViewById(R.id.detailView_Image);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                        //ListInventarVMiestnosti.this, imageView, "detailView_Image");
                        ListMnozstvaTovarovActivity.this, imageView, "detailView_Image");*/

                //startActivityForResult(theIndent, 1, options.toBundle());
                startActivity(theIndent);
            }
        });
        mnozstvaTovaruAdapter = new MnozstvaTovarovAdapter(this, R.layout.activity_mnozstvo_tovaru_total_row, zoznamHM);
        lw.setAdapter(mnozstvaTovaruAdapter);

        //  }
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
