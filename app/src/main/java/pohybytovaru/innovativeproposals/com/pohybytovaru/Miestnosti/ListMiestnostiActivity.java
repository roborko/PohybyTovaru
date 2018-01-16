package pohybytovaru.innovativeproposals.com.pohybytovaru.Miestnosti;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Database.DatabaseHelper;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.OrmLiteAppCompatActivity;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Miestnost;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.ISimpleRowClickListener;

@EActivity
public class ListMiestnostiActivity extends OrmLiteAppCompatActivity<DatabaseHelper> {
    @ViewById
    Toolbar toolbar;

    @ViewById
    RecyclerView miestnostiList;


    @AfterViews
    void bindAdapter() {
        GetData();//fetch data

        ISimpleRowClickListener clickListener = new ISimpleRowClickListener<Miestnost>() {
            @Override
            public void onItemClick(Miestnost item) {
                Log.i("item selected", "onItemClick: ");
            }
        };

        ListMiestnostiAdapter newAdapter = new ListMiestnostiAdapter(R.layout.activity_list_miestnosti_row, clickListener, miestnosti);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        miestnostiList.setLayoutManager(layoutManager);
        miestnostiList.setItemAnimator(new DefaultItemAnimator());
        miestnostiList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        miestnostiList.setAdapter(newAdapter);


    }

    private List<Miestnost> miestnosti = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_miestnosti);

        setSupportActionBar(toolbar);
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
}
