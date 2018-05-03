package pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.PohybTovarov;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Database.DatabaseHelper;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.OrmLiteAppCompatActivity;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Miestnosti.DetailMiestnostiActivity_;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Miestnosti.ListMiestnostiAdapter;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Miestnost;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Pohyb;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Tovar;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.TypTransakcie;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.ISimpleRowClickListener;

// https://developer.android.com/guide/topics/ui/layout/recyclerview#java

@EActivity(R.layout.activity_pohyb_tovar)
public class PohybTovarActivity extends OrmLiteAppCompatActivity<DatabaseHelper> implements ISimpleRowClickListener<Pohyb> {
    public final static int POHYB_REQUEST_CODE = 19;

    @ViewById
    Toolbar toolbar;

    @ViewById
    RecyclerView recyclerView;

    private List<Pohyb> data_list = new ArrayList<>();
    private PohybTovarAdapter dataAdapter;


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

//            Dao<Miestnost, Integer> miestnostsDAO = getHelper().MiestnostDAO();
//            Dao<Tovar, Integer> tovarDAO = getHelper().TovarDAO();
//            Dao<TypTransakcie, Integer> typTransakciesDao = getHelper().TypTransakcieDAO();
//
//            List<Miestnost> miestnosti = miestnostsDAO.queryForAll();
//            List<Tovar> tovary = tovarDAO.queryForAll();
//            List<TypTransakcie> transakcieList = typTransakciesDao.queryForAll();
//
//
//            Pohyb add = new Pohyb();
//            add.setDatum(new Date());
//            add.setMiestnostFrom(miestnosti.get(0));
//            add.setTovar(tovary.get(0));
//            add.setPocetKusov(5);
//            add.setTypPohybu(transakcieList.get(0));
//
//            Pohyb remove = new Pohyb();
//            remove.setDatum(new Date());
//            remove.setMiestnostFrom(miestnosti.get(0));
//            remove.setTovar(tovary.get(0));
//            remove.setPocetKusov(5);
//            remove.setTypPohybu(transakcieList.get(2));
//
//            Pohyb delete = new Pohyb();
//            delete.setDatum(new Date());
//            delete.setMiestnostFrom(miestnosti.get(0));
//            delete.setTovar(tovary.get(0));
//            delete.setPocetKusov(5);
//            delete.setTypPohybu(transakcieList.get(3));
//
//
//            data_list.add(add);
//            data_list.add(remove);
//            data_list.add(delete);

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
            int pocet = data_list.size();

            // refreshni screen ??
            dataAdapter.clearSelectedItems();
            dataAdapter.data = data_list;


            dataAdapter.notifyDataSetChanged();

            // DiffUtil ??
            // https://android.jlelse.eu/smart-way-to-update-recyclerview-using-diffutil-345941a160e0

            //    recyclerView. ?
           // recyclerView.invalidate();
           // recyclerView.setAdapter(dataAdapter);

            }
        }

    @Click(R.id.fab_newMiestnost)
    public void AddNewItem() {
        Intent intent = new Intent(this, PohybTovarActivityDetail_.class);
//        intent.putParcelableArrayListExtra("EXTRA_LIST_MIESTNOST", new ArrayList<Parcelable>(data_list));
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

        dataAdapter.clearSelectedItems();
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
                return false;

            case R.id.delete:
                DeleteSelectedPohyby();
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
