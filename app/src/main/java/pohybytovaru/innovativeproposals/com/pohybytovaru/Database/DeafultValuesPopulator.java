package pohybytovaru.innovativeproposals.com.pohybytovaru.Database;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.App;

import java.sql.SQLException;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.AktualneMnozstvo;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Miestnost;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Tovar;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.TypTransakcie;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;

/**
 * Created by Robert on 21.01.2018.
 */

public class DeafultValuesPopulator {

    public static void PopulateDefaultValues(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        PopulateTypTransakcie(context, databaseHelper);
        populateMiestnosti(context, databaseHelper);
        populateTovar(context, databaseHelper);
        populateAKtualneMnozstvo(context, databaseHelper);
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
                add.setINTERNAL_NAME(context.getString(R.string.TransactionType_Add));
                add.setAssignedResourceId_Black(R.drawable.ic_add_black_24dp);
                add.setAssignedResourceId_White(R.drawable.ic_add_white_24dp);

                TypTransakcie move = new TypTransakcie();
                move.setNazov(context.getString(R.string.TransactionType_Move));
                move.setINTERNAL_NAME(context.getString(R.string.TransactionType_Move));
                move.setAssignedResourceId_Black(R.drawable.ic_swap_horiz_black_24dp);
                move.setAssignedResourceId_White(R.drawable.ic_swap_horiz_white_24dp);

                //no icon for presun necessary

//                TypTransakcie remove = new TypTransakcie();
//                remove.setNazov(context.getString(R.string.TransactionType_Remove));
//                remove.setINTERNAL_NAME(context.getString(R.string.TransactionType_Remove));
//                remove.setAssignedResourceId_Black(R.drawable.ic_remove_black_24dp);
//                remove.setAssignedResourceId_White(R.drawable.ic_remove_white_24dp);

                TypTransakcie delete = new TypTransakcie();
                delete.setNazov(context.getString(R.string.TransactionType_Delete));
                delete.setINTERNAL_NAME(context.getString(R.string.TransactionType_Delete));
                delete.setAssignedResourceId_Black(R.drawable.ic_delete_black_24dp);
                delete.setAssignedResourceId_White(R.drawable.ic_delete_white_24dp);


                transakcieDao.create(add);
                transakcieDao.create(move);
//                transakcieDao.create(remove);
                transakcieDao.create(delete);
            }
        } catch (SQLException ex) {
            Log.e("DefaultValuesPopulator", "Cannot create default TransactionTypes: " + ex.getMessage());
        }
    }

    private static void populateTovar(Context context, DatabaseHelper databaseHelper) {
        Dao<Tovar, Integer> tovarDao = databaseHelper.TovarDAO();
        //lubos test comment -> zmenil som koment
        try {
            List<Tovar> tovarList = tovarDao.queryForAll();
            if (tovarList.size() == 0) {
                Tovar pocitac = new Tovar();
                pocitac.setNazov("Pocitac");
                pocitac.setMinimalneMnozstvo(0);

                Tovar stolicka = new Tovar();
                stolicka.setNazov("Stolicka");
                stolicka.setMinimalneMnozstvo(0);

                tovarDao.create(pocitac);
                tovarDao.create(stolicka);
            }

        } catch (SQLException ex) {
            Log.e("DefaultValuesPopulator", "Cannot create default TransactionTypes: " + ex.getMessage());
        }
    }

    private static void populateMiestnosti(Context context, DatabaseHelper databaseHelper) {
        Dao<Miestnost, Integer> miestnostsDao = databaseHelper.MiestnostDAO();

        try {
            List<Miestnost> miestnostList = miestnostsDao.queryForAll();

            if (miestnostList.size() == 0) {
                Miestnost obyvacka = new Miestnost();
                obyvacka.setNazov("Obyvacka");

                Miestnost kuchyna = new Miestnost();
                kuchyna.setNazov("Kuchyna");

                Miestnost spajza = new Miestnost();
                spajza.setNazov("Spajza");
                spajza.setJeSklad(true);

                miestnostsDao.create(obyvacka);
                miestnostsDao.create(kuchyna);
                miestnostsDao.create(spajza);
            }

        } catch (SQLException ex) {
            Log.e("DefaultValuesPopulator", "Cannot create default TransactionTypes: " + ex.getMessage());
        }
    }


    private static void populateAKtualneMnozstvo(Context context, DatabaseHelper databaseHelper) {
        Dao<AktualneMnozstvo, Integer> aktualneMnozstvoDao = databaseHelper.AktualneMnozstvoDAO();
        Dao<Miestnost, Integer> miestnostsDao = databaseHelper.MiestnostDAO();
        Dao<Tovar, Integer> tovarDao = databaseHelper.TovarDAO();

        try {
            List<AktualneMnozstvo> mnozstvoList = aktualneMnozstvoDao.queryForAll();

            Miestnost firstMiestnost = miestnostsDao.queryForId(1);
            Tovar firstTovar = tovarDao.queryForId(1);

            if(mnozstvoList.size() == 0){
                AktualneMnozstvo mnozstvo = new AktualneMnozstvo();
                mnozstvo.setMiestnost(firstMiestnost);
                mnozstvo.setTovar(firstTovar);
                mnozstvo.setMnozstvo(5);

                aktualneMnozstvoDao.create(mnozstvo);
            }

        } catch (SQLException ex) {
            Log.e("DefaultValuesPopulator", "Cannot create default TransactionTypes: " + ex.getMessage());
        }
    }
}
