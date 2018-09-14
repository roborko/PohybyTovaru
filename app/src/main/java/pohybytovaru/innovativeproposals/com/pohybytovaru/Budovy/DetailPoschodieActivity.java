package pohybytovaru.innovativeproposals.com.pohybytovaru.Budovy;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Miestnosti.DetailMiestnostiActivity_;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Miestnost;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Poschodie;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;
import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Budova;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Poschodie;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Miestnost;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.ISimpleRowClickListener;

@EActivity(R.layout.activity_list_poschodie_detail)

public class DetailPoschodieActivity extends AppCompatActivity implements ISimpleRowClickListener<Miestnost> {

    public final static int MIESTNOST_REQUEST_CODE = 23;

    @Extra("EXTRA_POSCHODIE")
    Poschodie poschodie;

    @Extra("EXTRA_LIST_POSCHODIE")
    ArrayList<Budova> list_poschodie = new ArrayList<>();

    @ViewById
    Toolbar toolbar;

    @ViewById
    RecyclerView recyclerView;

    @ViewById(R.id.inputLayout_PoschodieName)
    TextInputLayout textInputLayout;

    @ViewById(R.id.txt_PoschodieName)
    EditText txt_Poschodie;

    @ViewById(R.id.btn_Save)
    Button btn_Save;

    @ViewById(R.id.fab_newMiestnost)
    FloatingActionButton fab_btn_Miestnost;


    private List<Miestnost> data_list = new ArrayList<>();
    private ListMiestnostiAdapter dataAdapterMiestnosti;
    ListBudovaDataModel dm = new ListBudovaDataModel(this);
    int myBudovaId = 0;
    int myPoschodieId = 0;

    @AfterViews
    void ProcessAfterViews() {


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            myBudovaId = getIntent().getIntExtra("budovaID",0);
            myPoschodieId = getIntent().getIntExtra("poschodieID",0);

            String menoBudova = dm.dajNazovByID("Budova",myBudovaId);
           // toolbar.setTitle(menoBudova);
            getSupportActionBar().setTitle(menoBudova);

        }

        if(poschodie != null && poschodie.getId() > 0){

            txt_Poschodie.setText(poschodie.getNazov());

            try {
                // zoznam inventarov v miestnosti
                data_list = dm.getZoznamMiestnosti(poschodie.getId());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            //create new adapter
            dataAdapterMiestnosti = new ListMiestnostiAdapter(this, R.layout.activity_list_miestnosti_row, this, data_list);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(dataAdapterMiestnosti);

            // vyhod focus pre existujucu budovu
            //txt_Budova.clearFocus();
            recyclerView.requestFocus();
            btn_Save.setVisibility(View.INVISIBLE);

        } // else
          //  fab_btn_Miestnost.setVisibility(View.INVISIBLE);

    }

    @FocusChange(R.id.txt_PoschodieName)
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            fab_btn_Miestnost.setVisibility(View.INVISIBLE);
        } else {
            fab_btn_Miestnost.setVisibility(View.VISIBLE);
        }
    }


    @OnActivityResult(MIESTNOST_REQUEST_CODE)
    void onResult(int resultCode, Intent data) {

        // toto refreshuje list miestnosti na poschodi

        if (resultCode == Activity.RESULT_OK && data.hasExtra("EXTRA_MIESTNOST")) {
            //deserialize object

            Miestnost result = data.getParcelableExtra("EXTRA_MIESTNOST");
            if (result.getId() == 0) {
             //   int aa = dm.dajMaxId("Miestnost");
                result.setId(dm.dajMaxId("Miestnost"));
                dataAdapterMiestnosti.AddItem(result);
            }
            else
                dataAdapterMiestnosti.UpdateItem(result);
        }
    }

    @Click(R.id.fab_newMiestnost)
    public void AddNewItem() {

        // pridam novu miestnost
        Intent intent = new Intent(this, DetailMiestnostiActivity_.class);
        intent.putExtra("budovaID", myBudovaId);
        intent.putExtra("poschodieID", myPoschodieId);
        startActivityForResult(intent, MIESTNOST_REQUEST_CODE); // aby som to dokazal odchytit v OnActivityResult

    }

    @TextChange(R.id.txt_PoschodieName)
    void onPoschodieTextChange(){

       // fab_btn_Miestnost.setVisibility(View.INVISIBLE);
        textInputLayout.setError(null);
        btn_Save.setVisibility(View.VISIBLE);

    }

    @Click(R.id.btn_Save)
    void SaveChanges() {

       // ulozenie mena poschodia
        fab_btn_Miestnost.setVisibility(View.VISIBLE);
        String itemName = txt_Poschodie.getText().toString().trim();

        //validuj miestnost, tj ci sa uz rovnaka nenachadza v databaze
        if(duplicateExists(itemName)){
            textInputLayout.setError(getString(R.string.lbl_duplicate_poschodie));
            return;
        }

        //no duplicate, create new budova
        Poschodie resultPoschodie;

        if(poschodie == null) {
            resultPoschodie = new Poschodie();
            resultPoschodie.setNazov(itemName);
            resultPoschodie.setIdBudova(myBudovaId);
        }
        else
            resultPoschodie = poschodie;

        resultPoschodie.setNazov(itemName);
        long xx = dm.ulozPoschodie(resultPoschodie);

        // posielma udaje na refresh zoznamu
        Intent intent = new Intent();
        intent.putExtra("EXTRA_POSCHODIE", resultPoschodie);
        setResult(Activity.RESULT_OK, intent);


        this.finish();
    }

    @Click(R.id.btn_Cancel)
    void CloseWindow() {
        this.finish();
    }

    private boolean duplicateExists(String itemName){

        if(poschodie != null && poschodie.getId() > 0){
            //skontroluj ci sa nazov zhoduje
            if(poschodie.getNazov().equals(itemName)){
                //uzivatel nezmenil nazov
                return false;
            }
        }

        for (int i = 0; i < list_poschodie.size(); i++) {
            if(list_poschodie.get(i).getNazov().equals(itemName)) {
                //name is the same; check if it is is sklad

                return true;
            }
        }

        //no match found
        return  false;
    }

    @Override
    public void onItemClick(Miestnost item) {

        // stlacil som miestnost na poschodi  - je to zmenene?
        Intent intent = new Intent(this, DetailMiestnostiActivity_.class);
        //intent.putExtra("budovaID", budova.getId());

        //intent.putExtra("budovaID", myBudovaId);
        //intent.putExtra("poschodieID", myPoschodieId);

        intent.putExtra("EXTRA_MIESTNOST", item);
       // startActivityForResult(intent, MIESTNOST_REQUEST_CODE);
        //startActivity(intent);
        startActivityForResult(intent, MIESTNOST_REQUEST_CODE); // aby som to dokazal odchytit v OnActivityResult


    }

    @Override
    public void onItemClick(View view, Miestnost item) {

    }

    @Override
    public boolean onItemLongClick(View view, Miestnost item) {
        return false;
    }

    //MENU OPTIONS MANIPULATIONS - get access to menu object. Aby mi fungovalo back na toolbare
    private Menu savedMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.savedMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete_or_search, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.search);
        final android.widget.SearchView searchView = (android.widget.SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
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

                dataAdapterMiestnosti.FilterArray(s);

                Log.i("QUERY CHANGED", "Search Text: " + s);
                return false;
            }
        });
        return true;
    }

    private Integer selectedListItems = 0;

    private void clearSelectedItems() {
        //set original title
        getSupportActionBar().setTitle(R.string.activity_ListMiestnosti);

        dataAdapterMiestnosti.clearSelectedItems();
        selectedListItems = 0;

        //update menu layout as delete icon now should disappear
        this.onPrepareOptionsMenu(savedMenu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem menu_delete = menu.findItem(R.id.delete);
        MenuItem menu_search = menu.findItem(R.id.search);

        if (selectedListItems == 0) {
            //hide trash icon
            menu_search.setVisible(true);
            menu_delete.setVisible(false);
        } else {
            menu_search.setVisible(false);
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
                finish(); // navrat na predchadzajucu activity cez toolbar back
                return false;

            case R.id.delete:
                //     DeleteSelectedMiestnosti();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (selectedListItems > 0) {
            clearSelectedItems();
            return;
        }
        super.onBackPressed();
    }

}


