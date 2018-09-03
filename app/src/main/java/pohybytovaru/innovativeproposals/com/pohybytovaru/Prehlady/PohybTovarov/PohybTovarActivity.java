package pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.PohybTovarov;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
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
import android.widget.SearchView;
//import android.widget.TextView;

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
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Constants;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Database.DatabaseHelper;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.OrmLiteAppCompatActivity;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Pohyb;
import pohybytovaru.innovativeproposals.com.pohybytovaru.MyAlertDialogFragmentOK;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.MnozstvaTovarov.MnozstvaTovarovDataModel;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.ISimpleRowClickListener;


// https://developer.android.com/guide/topics/ui/layout/recyclerview#java

@EActivity(R.layout.activity_pohyb_tovar)
public class PohybTovarActivity extends OrmLiteAppCompatActivity<DatabaseHelper> implements ISimpleRowClickListener<Pohyb> {

    public final static int POHYB_REQUEST_CODE = 19;
    private String filtrovanyVyraz = "";

    @ViewById
    Toolbar toolbar;

    @ViewById
    RecyclerView recyclerView;

    private List<Pohyb> data_list = new ArrayList<>();
    private PohybTovarAdapter dataAdapter;
    MnozstvaTovarovDataModel dm = new MnozstvaTovarovDataModel(this);

    @AfterViews
    void bindAdapter() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GetData();//fetch data

        //create new adapter
        dataAdapter = new PohybTovarAdapter(this, R.layout.activity_pohyb_tovar_row, this, data_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(dataAdapter);
    }

    private void GetData() {
        try {
            Dao<Pohyb, Integer> pohybDao = getHelper().PohybDAO();
            data_list = pohybDao.queryForAll();

            Collections.sort(data_list, new Comparator<Pohyb>() {
                @Override
                public int compare(Pohyb lhs, Pohyb rhs) {
                    return  rhs.getId() - lhs.getId(); // Ascending
                }
            });


        } catch (SQLException ex) {
            Log.e(this.getClass().getName(), "Unable to fetch data_list data: " + ex.getMessage());
        }
    }
    @OnActivityResult(POHYB_REQUEST_CODE)
    // tu refreshni ked prida novy pohyb -  neprejde cez "EXTRA_MIESTNOST xx GetData();//fetch data
    void onResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data.hasExtra("EXTRA_MIESTNOST")) {

            //deserialize object
            Pohyb result = data.getParcelableExtra("EXTRA_MIESTNOST");

            //TODO Add new miestnost / Update already existing object
            if (result.getId() == 0)
                AddNewItem(result);
            else
                UpdateExistingItem(result);
        }
        else {
            GetData();  // aktualizuje data_list
            dataAdapter.AddNewCollection(data_list);

            //   int pocet = data_list.size();

            // refreshni screen ??
        /*    dataAdapter.clearSelectedItems();
            dataAdapter.data = data_list;
            selectedListItems = pocet;
            dataAdapter.notifyDataSetChanged();*/

            // DiffUtil ??
            // https://android.jlelse.eu/smart-way-to-update-recyclerview-using-diffutil-345941a160e0

            //    recyclerView. ?
           // recyclerView.invalidate();
           // recyclerView.setAdapter(dataAdapter);

            }
        }

    @Click(R.id.fab_newExport)
    public void exportZoznam() {

        try {

            if(!kontrolaUdajov()) return;

            ExportSelectedPohyby();
        } catch (IOException e) {
            e.printStackTrace();

            showDialogFragment("Žiadne údaje");
        }
    }

    public boolean kontrolaUdajov() {

        boolean jeOK = false;
        if(!dm.jeZaznamVTabulke("miestnost")) {
            showDialogFragment("Definujte aspoň jednu miestnosť");
            return jeOK;
        }

        if(!dm.jeZaznamVTabulke("tovar")) {
            showDialogFragment("Definujte aspoň jednu miestnosť");
            return jeOK;
        }
        return true;
    }

    @Click(R.id.fab_newMiestnost)
    public void AddNewItem() {

        // kontrola ci existuje tovar a miestnost
        // boolean ako = dm.jeZaznamVTabulke("miestnost");

        if(!kontrolaUdajov()) return;

        /*
        if(!dm.jeZaznamVTabulke("miestnost")) {
            showDialogFragment("Definujte aspoň jednu miestnosť");
            return;
        }

        if(!dm.jeZaznamVTabulke("tovar")) {
            showDialogFragment("Definujte aspoň jednu miestnosť");
            return;
        }
*/
        Intent intent = new Intent(this, PohybTovarActivityDetail_.class);
        startActivityForResult(intent, POHYB_REQUEST_CODE);
    }

    @Override
    public void onItemClick(Pohyb item) {
//        Intent intent = new Intent(this, DetailMiestnostiActivity_.class);
//        intent.putExtra("EXTRA_MIESTNOST", item);
//        intent.putParcelableArrayListExtra("EXTRA_LIST_MIESTNOST", new ArrayList<Parcelable>(data_list));
//        startActivityForResult(intent, POHYB_REQUEST_CODE);
    }

    public void onItemClick(View view, Pohyb item) {
        if (dataAdapter.getSelectedItemsCount() > 0) {
            //at least one item is selected, trigger select event instead of click event
            this.onItemLongClick(view,item);
        }
    }

    //uzivatel stlacil dlho nejaku polozku
    @Override
    public boolean onItemLongClick(View view, Pohyb item) {
        int position = (int) view.getTag();

        dataAdapter.toggleItemSelection(position);
        selectedListItems = dataAdapter.getSelectedItemsCount();
        if (selectedListItems > 0) {
            getSupportActionBar().setTitle(String.valueOf(dataAdapter.getSelectedItemsCount()) + " " + getResources().getString(R.string.action_hint_selecteditems));
        } else {
            getSupportActionBar().setTitle(R.string.activity_ListMiestnosti);
        }

        this.onPrepareOptionsMenu(savedMenu);
        return true;
    }

    private void AddNewItem(Pohyb item) {

        // zapis miestnosti?

        try {
            Dao<Pohyb, Integer> pohybDao = getHelper().PohybDAO();
            pohybDao.create(item);

            //add item to adapter
            dataAdapter.AddItem(item);
        } catch (SQLException ex) {
            Log.e("POHYB TOVARU", "Cannot create new pohyb tovaru. " + ex.getMessage());
        }
    }

    private void UpdateExistingItem(Pohyb item) {
        Dao<Pohyb, Integer> pohybDao = getHelper().PohybDAO();
        try {
            pohybDao.update(item);

            //update collection
            this.dataAdapter.UpdateItem(item);

        } catch (SQLException ex) {
            Log.e("POHYB TOVARU", "Cannot update pohyb tovaru. " + ex.getMessage());
        }
    }

    //Method which triggers when user clicks 'trash' icon from app menu (erase selected items)
    private void DeleteSelectedPohyby() {
        //retrieve list of all selected items
        List<Integer> itemsToRemove =  dataAdapter.getSelectedItemsId();
        try{

            //iterate from last to first element in order to keep correct positions of selected items under sparse boolean array
            for (int iItem = itemsToRemove.size()-1; iItem >= 0; iItem--) {
                Dao<Pohyb, Integer> pohybDao = getHelper().PohybDAO();
                pohybDao.deleteById(itemsToRemove.get(iItem));
                dataAdapter.RemoveItemById(itemsToRemove.get(iItem));
            }

            //recreate top menu
            clearSelectedItems();
        }catch (SQLException ex){
            Log.e("POHYB TOVARU", "Cannot delete pohyb tovaru. " + ex.getMessage());
        }
    }
    //MENU OPTIONS MANIPULATIONS

    //get access to menu object
    private Menu savedMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.savedMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete_or_search, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //tu sa odohrava event, kde sa zmenil search text

                filtrovanyVyraz = s;
                dataAdapter.FilterArray(s);

                Log.i("QUERY CHANGED", "Search Text: " + s);
                return false;
            }
        });

        return true;
    }

    private Integer selectedListItems = 0;

    private void clearSelectedItems() {
        //set original title
        getSupportActionBar().setTitle(R.string.activity_PohybTovarov);
        filtrovanyVyraz = ""; // dobre ?
        dataAdapter.clearSelectedItems();
        selectedListItems = 0;

        //update menu layout as delete icon now should disappear
        this.onPrepareOptionsMenu(savedMenu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menu_delete = menu.findItem(R.id.delete);
        MenuItem menu_search = menu.findItem(R.id.search);
      //  MenuItem menu_export = menu.findItem(R.id.export);

        if (selectedListItems == 0) {
            //hide trash icon
            menu_search.setVisible(true);
          //  menu_export.setVisible(true);
            menu_delete.setVisible(false);
        } else {
            menu_search.setVisible(false);
          //  menu_export.setVisible(false);
            menu_delete.setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //back button pressed on navigation bar
                if (selectedListItems > 0) {
                    //user wants to clear selected items array
                    clearSelectedItems();

                    return true;
                }
                //regular back button, user wants to navigate back
                return false; // TODO nemoze mat 2 rozne stave

           /* case R.id.export:
                try {
                    ExportSelectedPohyby();
                } catch (IOException e) {
                    e.printStackTrace();

                    showDialogFragment(e.toString());

                }
                break; */

            case R.id.delete:
           //     DeleteSelectedPohyby(); // zakazane, opravit cez inventuru !!!
                break;

        }

        this.finish();
        return super.onOptionsItemSelected(item);
    }

    private void ExportSelectedPohyby() throws IOException {

        SimpleDateFormat s = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        SimpleDateFormat kratsiDatum = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");


        String format = s.format(new Date());

        File filePath = new File(String.valueOf(getBaseContext().getFilesDir()));
        File yourFile ;

        if(filtrovanyVyraz=="")
            yourFile = new File(filePath + File.separator + "prehladPohybov_vsetky.csv");
        else
            yourFile = new File(filePath + File.separator + "prehladPohybov_" + filtrovanyVyraz +".csv");

        yourFile.createNewFile(); // vytvorenie !!!

        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(yourFile, false),
                "windows-1250");  // "windows-1252" , "UTF-8", "ISO-8859-2"

        writer.append("Vytvorené : "+ format +" \n\n");

        if(filtrovanyVyraz=="")
          //  myArtikel = " vsetky tovary";
            writer.append("Prehľad pohybov všetkých tovarov \n\n");
        else
           // myArtikel = " tovar " + filtrovanyVyraz;
            writer.append("Prehľad pohybov tovarov začínajúcich reťazcom: " + filtrovanyVyraz + " \n\n");


        writer.append("Názov;Pohyb;Počet;Dátum;Z miestnosti;Do miestnosti\n");

        //int kolko = data_list.size();
        int kolko = dataAdapter.filterView.size();

        // dataAdapter.FilterArray(s);

        Pohyb xx;
        String tovar = "";

        String osoba, datum, miestnostOd, miestostDo,typPohybu, kod;
        String[] umiestnenie;
        int pocet;

        for (int iItem = 0; iItem < kolko; iItem++) {

            miestnostOd = "";
            miestostDo = "";
           // xx = data_list.get(iItem);
            xx = dataAdapter.filterView.get(iItem);
            tovar = xx.getTovar().toString();
            typPohybu = xx.getTypPohybu().toString();

            kod = "";
            if(xx.getTovar().getKodTovaru()!=null)
                kod = xx.getTovar().getKodTovaru();

            osoba = "";
            if(xx.getOsoba() != null)
                osoba = xx.getOsoba().toString();
            //datum = xx.getDatum().toString();
            datum = kratsiDatum.format(xx.getDatum());

            if(xx.getMiestnostFrom() != null) {
                umiestnenie = dm.getKoordinatyMiestnosti(xx.getMiestnostFrom().getId());
                miestnostOd = umiestnenie[0] + "-" + umiestnenie[1] + "-" + umiestnenie[2];
            }
            if(xx.getMiestnostTo() != null) {
                umiestnenie = dm.getKoordinatyMiestnosti(xx.getMiestnostTo().getId());
                miestostDo = umiestnenie[0] + "-" + umiestnenie[1] + "-" + umiestnenie[2];
            }
            pocet = (int) xx.getPocetKusov();

            writer.append(tovar+";"+typPohybu+";"+String.valueOf(pocet)+";"+datum+";"+miestnostOd+";"+miestostDo+"\n");

        }

        writer.close();

        String myArtikel = "";

        if(filtrovanyVyraz=="")
            myArtikel = " vsetky tovary";
        else
            myArtikel = " tovar začínajúci reťazcom : " + filtrovanyVyraz;

        Uri uri = FileProvider.getUriForFile(this, "pohybytovaru.innovativeproposals.com.FileProvider", yourFile);

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(uri);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName() + Constants.PREF_FILE_NAME, MODE_PRIVATE);
        String address = sharedPreferences.getString(Constants.MAIL_ADDRESS, "zadajte mail do nastaveni"); // druhy parameter je defaultna hodnota


        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {address});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Export pohybov pre " + myArtikel);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Prehľad pohybov tovarov");

        startActivity(Intent.createChooser(emailIntent, "Share"));

    }

    @Override
    public void onBackPressed() {
        if (selectedListItems > 0) {
            clearSelectedItems();
            return;
        }
        super.onBackPressed();
    }

    private void showDialogFragment(String Mymessage) {

        FragmentManager fm = getSupportFragmentManager();
        MyAlertDialogFragmentOK editNameDialogFragment = MyAlertDialogFragmentOK.newInstance(Mymessage);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    public void sendEmail(String myFilePath, String myArtikel)
    {

        // prerob takto ??
        // https://stackoverflow.com/questions/2020088/sending-email-in-android-using-javamail-api-without-using-the-default-built-in-a?rq=1

        if(myArtikel=="")
            myArtikel = " vsetky pohyby";
        else
            myArtikel = " invetar " + myArtikel;
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"lubos.jokl@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Export pohybov pre " + myArtikel);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Vyexportovane pohyby");

        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + myFilePath));

        startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));

    }


}
