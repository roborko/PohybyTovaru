package pohybytovaru.innovativeproposals.com.pohybytovaru.Miestnosti;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Database.DatabaseHelper;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.OrmLiteAppCompatActivity;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Miestnost;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.ISimpleRowClickListener;

@EActivity(R.layout.activity_list_miestnosti)
public class ListMiestnostiActivity extends OrmLiteAppCompatActivity<DatabaseHelper> implements ISimpleRowClickListener<Miestnost> {
    public final static int MIESTNOST_REQUEST_CODE = 19;

    @ViewById
    Toolbar toolbar;

    @ViewById
    RecyclerView miestnostiList;

    private List<Miestnost> miestnosti = new ArrayList<>();
    private List<Miestnost> selectedItems = new ArrayList<>(); //selected list by users

    private ListMiestnostiAdapter dataAdapter;

    @AfterViews
    void bindAdapter() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GetData();//fetch data

        //create new adapter
        dataAdapter = new ListMiestnostiAdapter(R.layout.activity_list_miestnosti_row, this, miestnosti);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        miestnostiList.setLayoutManager(layoutManager);
        miestnostiList.setItemAnimator(new DefaultItemAnimator());
        miestnostiList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        miestnostiList.setAdapter(dataAdapter);
    }

    private void GetData() {
        try {
            Dao<Miestnost, Integer> miestnostDao = getHelper().MiestnostDAO();
            miestnosti = miestnostDao.queryForAll();

        } catch (SQLException ex) {
            Log.e(this.getClass().getName(), "Unable to fetch miestnosti data: " + ex.getMessage());
        }
    }

    @OnActivityResult(MIESTNOST_REQUEST_CODE)
    void onResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data.hasExtra("EXTRA_MIESTNOST")) {
            //deserialize object
            Miestnost result = data.getParcelableExtra("EXTRA_MIESTNOST");

            //TODO Add new miestnost / Update already existing object
            if (result.getId() == 0)
                AddNewMiestnost(result);
            else
                UpdateExistingMiestnost(result);
        }
    }

    @Click(R.id.fab_newMiestnost)
    public void AddNewItem() {
        Intent intent = new Intent(this, DetailMiestnostiActivity_.class);
        intent.putParcelableArrayListExtra("EXTRA_LIST_MIESTNOST", new ArrayList<Parcelable>(miestnosti));
        startActivityForResult(intent, MIESTNOST_REQUEST_CODE);
    }

    @Override
    public void onItemClick(Miestnost item) {
        Intent intent = new Intent(this, DetailMiestnostiActivity_.class);
        intent.putExtra("EXTRA_MIESTNOST", item);
        intent.putParcelableArrayListExtra("EXTRA_LIST_MIESTNOST", new ArrayList<Parcelable>(miestnosti));
        startActivityForResult(intent, MIESTNOST_REQUEST_CODE);
    }


    @Override
    public boolean onItemLongClick(Miestnost item) {
        //uzivatel stlacil dlho nejaku polozku
        //treba spravit to, ze ju ulozime/vymazeme zo zoznamu existujucich oznacenych poloziek

        //TODO enable selection mode in activity
        if(item.isSelected()){
            selectedItems.remove(item);
            item.setSelected(false);

            if(selectedItems.size() == 0){
                this.dataAdapter.disableSelectionMode();
            }

        } else {

            //check if we need to enable selection mode
            if(selectedItems.size() == 0){
                this.dataAdapter.startSelectionMode();
            }

            selectedItems.add(item);
            item.setSelected(true);
        }

        return true;
    }


    private void AddNewMiestnost(Miestnost item) {
        try {
            Dao<Miestnost, Integer> miestnostDao = getHelper().MiestnostDAO();
            int newIndex = miestnostDao.create(item);
            item.setId(newIndex);

            //add item to adapter
            dataAdapter.AddItem(item);
        } catch (SQLException ex) {
            Log.e("LIST_MIESTNOSTI", "Cannot create new miestnost. " + ex.getMessage());
        }

    }

    private void UpdateExistingMiestnost(Miestnost item) {
        Dao<Miestnost, Integer> miestnostDao = getHelper().MiestnostDAO();
        try {
            Miestnost databaseItem = miestnostDao.queryForId(item.getId());
            databaseItem.CopyData(item);
            miestnostDao.update(databaseItem);


            //update collection
            for (Miestnost miestnost : miestnosti) {
                if (miestnost.getId() == item.getId()) {
                    miestnost.CopyData(item);
                    break;
                }
            }

            this.dataAdapter.data = miestnosti;
            this.dataAdapter.notifyDataSetChanged();

        } catch (SQLException ex) {
            Log.e("LIST_MIESTNOSTI", "Cannot update miestnost. " + ex.getMessage());
        }
    }
}
