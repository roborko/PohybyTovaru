package pohybytovaru.innovativeproposals.com.pohybytovaru.Tovary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Parcelable;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Database.DatabaseHelper;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.OrmLiteAppCompatActivity;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Miestnosti.DetailMiestnostiActivity_;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Tovar;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.ISimpleRowClickListener;

@EActivity(R.layout.activity_list_tovar)
public class ListTovarActivity extends OrmLiteAppCompatActivity<DatabaseHelper> implements ISimpleRowClickListener<Tovar> {
    public final static int TOVAR_REQUEST_CODE = 19;
    public final static String CODE_INTENT_TOVAR = "EXTRA_TOVAR";

    @ViewById
    Toolbar toolbar;

    @ViewById
    RecyclerView recyclerView;

    private List<Tovar> data_list = new ArrayList<>();
    private ListTovarAdapter dataAdapter;

    @AfterViews
    void bindAdapter() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GetData();//fetch data

        //create new adapter
        dataAdapter = new ListTovarAdapter(this, R.layout.activity_list_tovar_row, this, data_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(dataAdapter);
    }

    private void GetData() {
        try {
            Dao<Tovar, Integer> tovarDao = getHelper().TovarDAO();
            data_list = tovarDao.queryForAll();

            Collections.sort(data_list, new Comparator<Tovar>() {
                @Override
                public int compare(Tovar lhs, Tovar rhs) {
                    return lhs.getNazov().compareTo(rhs.getNazov());
                }
            });


        } catch (SQLException ex) {
            Log.e(this.getClass().getName(), "Unable to fetch data_list data: " + ex.getMessage());
        }
    }

    @OnActivityResult(TOVAR_REQUEST_CODE)
    void onResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data.hasExtra(CODE_INTENT_TOVAR)) {
            //deserialize object
            Tovar result = data.getParcelableExtra(CODE_INTENT_TOVAR);
            if (result.getId() == 0)
                AddNewItem(result);
            else
                UpdateExistingItem(result);
        }
    }

    @Click(R.id.fab_newMiestnost)
    public void AddNewItem() {
        Intent intent = new Intent(this, DetailTovarActivity_.class);
        startActivityForResult(intent, TOVAR_REQUEST_CODE);
    }

    @Override
    public void onItemClick(Tovar item) {
        //not implemented here
    }

    @SuppressLint("RestrictedApi")
    public void onItemClick(View view, Tovar item) {
        Intent intent = new Intent(this, DetailTovarActivity_.class);
        intent.putExtra(CODE_INTENT_TOVAR, item);
        View imageView = view.findViewById(R.id.detailView_Image); // ma natvrdo v layoute devinovany src
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                ListTovarActivity.this, imageView, "detailView_Image");

        startActivityForResult(intent, TOVAR_REQUEST_CODE, options.toBundle());
    }

    //uzivatel stlacil dlho nejaku polozku
    @Override
    public boolean onItemLongClick(View view, Tovar item) {
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


    private void AddNewItem(Tovar item) {
        try {
            Dao<Tovar, Integer> tovarDao = getHelper().TovarDAO();
            tovarDao.create(item);

            //add item to adapter
            dataAdapter.AddItem(item);
        } catch (SQLException ex) {
            Log.e("LIST_MIESTNOSTI", "Cannot create new miestnost. " + ex.getMessage());
        }
    }

    private void UpdateExistingItem(Tovar item) {
        Dao<Tovar, Integer> tovarDao = getHelper().TovarDAO();
        try {
            tovarDao.update(item);

            //update collection
            this.dataAdapter.UpdateItem(item);

        } catch (SQLException ex) {
            Log.e("LIST_MIESTNOSTI", "Cannot update miestnost. " + ex.getMessage());
        }
    }

    //Method which triggers when user clicks 'trash' icon from app menu (erase selected items)
    private void DeleteSelectedMiestnosti() {
        //retrieve list of all selected items
        List<Integer> itemsToRemove = dataAdapter.getSelectedItemsId();

        try {

            //iterate from last to first element in order to keep correct positions of selected items under sparse boolean array
            for (int iItem = itemsToRemove.size() - 1; iItem >= 0; iItem--) {
                Dao<Tovar, Integer> tovarDao = getHelper().TovarDAO();
                tovarDao.deleteById(itemsToRemove.get(iItem));
                dataAdapter.RemoveItemById(itemsToRemove.get(iItem));
            }

            //recreate top menu
            clearSelectedItems();
        } catch (SQLException ex) {
            Log.e("LIST_MIESTNOSTI", "Cannot delete miestnost. " + ex.getMessage());
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
        getSupportActionBar().setTitle(R.string.activity_ListMiestnosti);

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
                DeleteSelectedMiestnosti();
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
