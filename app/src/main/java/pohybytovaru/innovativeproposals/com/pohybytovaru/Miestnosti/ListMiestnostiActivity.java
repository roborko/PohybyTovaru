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

    @AfterViews
    void bindAdapter() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GetData();//fetch data

        ListMiestnostiAdapter newAdapter = new ListMiestnostiAdapter(R.layout.activity_list_miestnosti_row, this, miestnosti);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        miestnostiList.setLayoutManager(layoutManager);
        miestnostiList.setItemAnimator(new DefaultItemAnimator());
        miestnostiList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        miestnostiList.setAdapter(newAdapter);
    }

    private void GetData() {
        try {
            Dao<Miestnost, Integer> miestnostDao = getHelper().MiestnostDAO();
            miestnosti = miestnostDao.queryForAll();


            for (int i = 0; i < 100; i++) {
                Miestnost newItem = new Miestnost();
                newItem.setNazov("Item" + String.valueOf(i));
                miestnosti.add(newItem);
            }

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
        startActivityForResult(intent,MIESTNOST_REQUEST_CODE);
    }
}
