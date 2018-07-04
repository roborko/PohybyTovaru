package pohybytovaru.innovativeproposals.com.pohybytovaru.Budovy;

import android.app.Activity;
import android.content.Intent;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Budova;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Poschodie;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.ISimpleRowClickListener;

@EActivity(R.layout.activity_list_budova_detail)
//public class DetailBudovaActivity extends AppCompatActivity {
public class DetailBudovaActivity extends AppCompatActivity implements ISimpleRowClickListener<Poschodie> {

    public final static int BUDOVA_REQUEST_CODE = 21; // TODO preverit ci existuje
    public final static String CODE_INTENT_BUDOVA = "EXTRA_BUDOVA";


    public final static int POSCHODIE_REQUEST_CODE = 22;

    @Extra("EXTRA_BUDOVA")
    Budova budova;

    @Extra("EXTRA_LIST_BUDOVA")
    ArrayList<Budova> list_budova = new ArrayList<>();

    @ViewById
    Toolbar toolbar;

    @ViewById
    RecyclerView recyclerView;

    @ViewById(R.id.inputLayout_BudovaName)
    TextInputLayout textInputLayout;

    @ViewById(R.id.txt_BudovaName)
    EditText txt_Budova;

    @ViewById(R.id.btn_Save)
    Button btn_Save;

    private List<Poschodie> data_list = new ArrayList<>();
 //   private ListBudovyAdapter dataAdapter;
    private ListPoschodieAdapter dataAdapterPoschodie;
    ListBudovaDataModel dm = new ListBudovaDataModel(this);


    @AfterViews
    void ProcessAfterViews() {
        if(budova != null && budova.getId() > 0){

            txt_Budova.setText(budova.getNazov());

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            try {
                // zoznam inventarov v miestnosti
                data_list = dm.getZoznamPoschodi(budova.getId());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            //create new adapter
            dataAdapterPoschodie = new ListPoschodieAdapter(this, R.layout.activity_list_poschodie_row, this, data_list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(dataAdapterPoschodie);

            // vyhod focus pre existujucu budovu
            txt_Budova.clearFocus();
            recyclerView.requestFocus();
            btn_Save.setVisibility(View.INVISIBLE);
        }
    }

    @Click(R.id.fab_newPoschodie)
    public void AddNewItem() {

        // pridam nove poschodie
        Intent intent = new Intent(this, DetailPoschodieActivity_.class);
        intent.putExtra("budovaID", budova.getId());
        //startActivity(intent);
        startActivityForResult(intent, POSCHODIE_REQUEST_CODE); // aby som to dokazal odchytit v OnActivityResult
    }

    @OnActivityResult(POSCHODIE_REQUEST_CODE)
    void onResult(int resultCode, Intent data) {

        // toto refreshuje list

        if (resultCode == Activity.RESULT_OK && data.hasExtra("EXTRA_POSCHODIE")) {
            //deserialize object

            Poschodie result = data.getParcelableExtra("EXTRA_POSCHODIE");
            if (result.getId() == 0) {
               // int aa = dm.dajMaxId("Poschodie");
                result.setId(dm.dajMaxId("Poschodie"));
                dataAdapterPoschodie.AddItem(result);
            }
            else
                dataAdapterPoschodie.UpdateItem(result);
        }
    }

    @TextChange(R.id.txt_BudovaName)
    void onBudovaTextChange(){

        textInputLayout.setError(null);
        // todo sem daj ukazanie buttonu save

        boolean visible = (btn_Save.getVisibility() == View.VISIBLE);
        btn_Save.setVisibility(View.VISIBLE);
    }

    @Click(R.id.btn_Save)
    void SaveChanges() {
        String itemName = txt_Budova.getText().toString().trim();

        //validuj miestnost, tj ci sa uz rovnaka nenachadza v databaze
        if(duplicateExists(itemName)){
            textInputLayout.setError(getString(R.string.lbl_duplicate_budova));
            return;
        }

        //no duplicate, create new budova
        Budova resultBudova;
        if(budova == null)
            resultBudova = new Budova();
        else
            resultBudova = budova;

        resultBudova.setNazov(itemName);
       // long aa = dm.ulozBudova(resultBudova);

        Intent intent = new Intent();
        intent.putExtra("EXTRA_BUDOVA", resultBudova);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }

    @Click(R.id.btn_Cancel)
    void CloseWindow() {
        this.finish();
    }

     private boolean duplicateExists(String itemName){

        if(budova != null && budova.getId() > 0){
            //skontroluj ci sa nazov zhoduje
            if(budova.getNazov().equals(itemName)){
                //uzivatel nezmenil nazov
                return false;
            }
        }

        for (int i = 0; i < list_budova.size(); i++) {
            if(list_budova.get(i).getNazov().equals(itemName)) {
                //name is the same; check if it is is sklad
                return true;
            }
        }

        //no match found
        return  false;
    }

    @Override
    public void onItemClick(Poschodie item) {

        // stlacil som poschodie na liste poschodi pod nazvom budovy  - je to zmenene?

        Intent intent = new Intent(this, DetailPoschodieActivity_.class);
        intent.putExtra("budovaID", budova.getId());
        intent.putExtra("poschodieID", item.getId());
        intent.putExtra("EXTRA_POSCHODIE", item);
        startActivityForResult(intent, POSCHODIE_REQUEST_CODE); // aby som to dokazal odchytit v OnActivityResult

    }

    @Override
    public void onItemClick(View view, Poschodie item) {
        int aa = 0;
        ++aa;
    }

    @Override
    public boolean onItemLongClick(View view, Poschodie item) {
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

                dataAdapterPoschodie.FilterArray(s);

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

        dataAdapterPoschodie.clearSelectedItems();
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

