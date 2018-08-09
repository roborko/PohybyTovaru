package pohybytovaru.innovativeproposals.com.pohybytovaru.Budovy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Database.DatabaseHelper;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.OrmLiteAppCompatActivity;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Budova;
import pohybytovaru.innovativeproposals.com.pohybytovaru.MyAlertDialogFragmentOK;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.ISimpleRowClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;



// google : android expandablelistview three levels from sqlite
// https://stackoverflow.com/questions/32880281/how-to-add-three-level-listview-in-expandablelistview-in-android
// https://gist.github.com/st-f/2b2a838d3f0258c5c33f
// http://www.androidcodec.com/android-expandablelistview-multi-level/
// https://stackoverflow.com/questions/27030763/sqlite-displayed-in-expandablelistview
// https://github.com/talhahasanzia/Three-Level-Expandable-Listview/blob/master/ThreeLevelListview/app/src/main/java/com/talhahasanzia/threelevellistview/MainActivity.java

// https://www.loginworks.com/blogs/work-expandablelistview-android/

@EActivity(R.layout.activity_list_budova)
public class ListBudovaActivity extends OrmLiteAppCompatActivity<DatabaseHelper> implements ISimpleRowClickListener<Budova> {

    public final static int BUDOVA_REQUEST_CODE = 21;
    public final static String CODE_INTENT_BUDOVA = "EXTRA_BUDOVA";

    @ViewById
    Toolbar toolbar;

    @ViewById
    RecyclerView recyclerView;

    private List<Budova> data_list = new ArrayList<>();
    private ListBudovyAdapter dataAdapter;
    ListBudovaDataModel dm = new ListBudovaDataModel(this);
    private MyAlertDialogFragmentOK editNameDialogFragment;
    private int idVybratejBudovy;

    @AfterViews
    void bindAdapter() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            // zoznam inventarov v miestnosti
            data_list = dm.getZoznamBudov();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //create new adapter
        dataAdapter = new ListBudovyAdapter(this, R.layout.activity_list_budova_row, this, data_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(dataAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

/*
        if (editNameDialogFragment.stlacenyButton == 1) {
            // stlacil OK
            dataAdapter.RemoveItemById(idVybratejBudovy);
            // TODO sem este daj sql kontrolu, ci sa Idcko nenachadza v poschodiach
            dm.deleteRowFromTable("Budova", idVybratejBudovy);
        }
*/

    }

    @OnActivityResult(BUDOVA_REQUEST_CODE)
    void onResult(int resultCode, Intent data) {

        // toto refreshuje list
        if (resultCode == Activity.RESULT_OK && data.hasExtra(CODE_INTENT_BUDOVA)) {
            //deserialize object

            Budova result = data.getParcelableExtra("EXTRA_BUDOVA");
            if (result.getId() == 0) {
              //  int aa = dm.dajMaxId("Budova");
                result.setId(dm.dajMaxId("Budova"));
                dataAdapter.AddItem(result);
            }
            else
                dataAdapter.UpdateItem(result);
        }
    }

    @Click(R.id.fab_newBudova)
    public void AddNewItem() {
        Intent intent = new Intent(this, DetailBudovaActivity_.class);
        startActivityForResult(intent, BUDOVA_REQUEST_CODE);
    }

    @Override
    public void onItemClick(Budova item) {

        Intent intent = new Intent(this, DetailBudovaActivity_.class);
        intent.putExtra("EXTRA_BUDOVA", item);
        intent.putParcelableArrayListExtra("EXTRA_LIST_BUDOVA", new ArrayList<Parcelable>(data_list));
        startActivityForResult(intent, BUDOVA_REQUEST_CODE);
    }

    @SuppressLint("RestrictedApi")
    public void onItemClick(View view, Budova item) {

        /* sem asi nejde
        Intent intent = new Intent(this, DetailBudovaActivity_.class);
        intent.putParcelableArrayListExtra("EXTRA_LIST_BUDOVA", new ArrayList<Parcelable>(data_list));
        startActivityForResult(intent, BUDOVA_REQUEST_CODE);
    */
    }

    //uzivatel stlacil dlho nejaku polozku

    @Override
    public boolean onItemLongClick(View view, Budova item) {

        idVybratejBudovy = item.getId();
      //  sendEmail();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle("Skutočne chcete zmazať budovu ?");

        // set dialog message
        alertDialogBuilder
                .setMessage("Pokiaľ áno, stlačte ZMAZAŤ")
                .setCancelable(false)
                .setPositiveButton("ZMAZAŤ",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        dataAdapter.RemoveItemById(idVybratejBudovy);
                        // TODO sem este daj sql kontrolu, ci sa Idcko nenachadza v poschodiach
                        dm.deleteRowFromTable("Budova", idVybratejBudovy);

                        dialog.cancel();
                     //xx   MainActivity.this.finish();
                    }
                })
                .setNegativeButton("NIE",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        return true;
    }

    //Method which triggers when user clicks 'trash' icon from app menu (erase selected items)
    private void DeleteSelectedMiestnosti() {
        //retrieve list of all selected items
        List<Integer> itemsToRemove = dataAdapter.getSelectedItemsId();

    }

    //MENU OPTIONS MANIPULATIONS - get access to menu object
    private Menu savedMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.savedMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete_or_search, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.search);
    /*    final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
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

        */
        return true;
    }

    private Integer selectedListItems = 0;

    private void clearSelectedItems() {
        //set original title
        getSupportActionBar().setTitle(R.string.activity_ListBudov);

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
            menu_search.setVisible(false);
            menu_delete.setVisible(false); // bolo false
        } else {
            menu_search.setVisible(false);
            menu_delete.setVisible(false);
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
