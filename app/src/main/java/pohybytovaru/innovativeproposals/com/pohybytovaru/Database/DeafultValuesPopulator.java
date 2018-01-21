package pohybytovaru.innovativeproposals.com.pohybytovaru.Database;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.App;

import java.sql.SQLException;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.TypTransakcie;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;

/**
 * Created by Robert on 21.01.2018.
 */

public class DeafultValuesPopulator {

    public static void PopulateDefaultValues(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        PopulateTypTransakcie(context, databaseHelper);

        databaseHelper.close();
        databaseHelper = null;
        context = null;
    }

    private static void PopulateTypTransakcie(Context context, DatabaseHelper databaseHelper) {
        Dao<TypTransakcie, Integer> transakcieDao = databaseHelper.TypTransakcieDAO();
        try {
            List<TypTransakcie> transakcieList = transakcieDao.queryForAll();
            if (transakcieList.size() == 0) {
                TypTransakcie add = new TypTransakcie();
                add.setNazov(context.getString(R.string.TransactionType_Add));
                add.setAssignedResourceId_Black(R.drawable.ic_add_black_24dp);
                add.setAssignedResourceId_White(R.drawable.ic_add_white_24dp);

                TypTransakcie move = new TypTransakcie();
                move.setNazov(context.getString(R.string.TransactionType_Move));
                //no icon for presun necessary

                TypTransakcie remove = new TypTransakcie();
                remove.setNazov(context.getString(R.string.TransactionType_Remove));
                remove.setAssignedResourceId_Black(R.drawable.ic_remove_black_24dp);
                remove.setAssignedResourceId_White(R.drawable.ic_remove_white_24dp);

                TypTransakcie delete = new TypTransakcie();
                delete.setNazov(context.getString(R.string.TransactionType_Delete));
                delete.setAssignedResourceId_Black(R.drawable.ic_delete_black_24dp);
                delete.setAssignedResourceId_White(R.drawable.ic_delete_white_24dp);


                transakcieDao.create(add);
                transakcieDao.create(move);
                transakcieDao.create(remove);
                transakcieDao.create(delete);
            }
        } catch (SQLException ex) {
            Log.e("DefaultValuesPopulator", "Cannot create default TransactionTypes: " + ex.getMessage() );
        }
    }
}
