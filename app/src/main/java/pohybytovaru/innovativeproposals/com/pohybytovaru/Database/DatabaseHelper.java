package pohybytovaru.innovativeproposals.com.pohybytovaru.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.AktualneMnozstvo;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Miestnost;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Osoba;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Pohyb;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Tovar;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.TypTransakcie;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;

/**
 * Created by Robert on 28.12.2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    // name of the database file for your application -- change to something appropriate for your app
    public static final String DATABASE_NAME = "pohybtovaru.db";
    // any time you make changes to your database objects, you may have to increase the database version
    public static final int DATABASE_VERSION = 9;
    // ver 4 - pridany pohyb inventura
    // ver 6 - pridane skartovanie do pohybov
    // ver 7 - test - pozor, prepisana funkcia onUpgrade - nedropuje tabulky, len maze pohyby


    private Dao<Miestnost, Integer> mMiestnostDAO = null;
    private Dao<Osoba, Integer> mOsobaDAO = null;
    private Dao<Pohyb, Integer> mPohybDAO = null;
    private Dao<Tovar, Integer> mTovarDAO = null;
    private Dao<TypTransakcie, Integer> mTypTransakcieDAO = null;
    private Dao<AktualneMnozstvo, Integer> mAktualneMnozstvoDAO = null;

    private static DatabaseHelper sDatabaseHelper;

    public static DatabaseHelper getInstance() {
        return sDatabaseHelper;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Tovar.class);
            TableUtils.createTable(connectionSource, Osoba.class);
            TableUtils.createTable(connectionSource, Miestnost.class);
            TableUtils.createTable(connectionSource, Pohyb.class);
            TableUtils.createTable(connectionSource, TypTransakcie.class);
            TableUtils.createTable(connectionSource, AktualneMnozstvo.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        // toto odremuj
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");

            TableUtils.dropTable(connectionSource, Tovar.class, true);
            TableUtils.dropTable(connectionSource, Osoba.class, true);
            TableUtils.dropTable(connectionSource, Miestnost.class, true);
            TableUtils.dropTable(connectionSource, Pohyb.class, true);
            TableUtils.dropTable(connectionSource, TypTransakcie.class, true);
            TableUtils.dropTable(connectionSource, AktualneMnozstvo.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(database, connectionSource);

            // napln typy transakcii
            // DeafultValuesPopulator.PopulateDefaultValues(this.getApplicationContext());


        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }

        // potialto
        // mazaniePohybov(connectionSource);

    }

    private void mazaniePohybov(ConnectionSource connectionSource) {
        try {
            TableUtils.clearTable(connectionSource,Pohyb.class);
            TableUtils.clearTable(connectionSource,AktualneMnozstvo.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<Tovar, Integer> TovarDAO() {
        try {
            if (mTovarDAO == null)
                mTovarDAO = getDao(Tovar.class);
        } catch (SQLException ex) {
            Log.e("TOVAR_DAO", "Error occured while fetching DAO: " + ex.getMessage());
        }
        return mTovarDAO;
    }

    public Dao<Osoba, Integer> OsobaDAO() {
        try {
            if (mOsobaDAO == null)
                mOsobaDAO = getDao(Osoba.class);
        } catch (SQLException ex) {
            Log.e("OSOBA_DAO", "Error occured while fetching DAO: " + ex.getMessage());
        }
        return mOsobaDAO;
    }

    public Dao<Miestnost, Integer> MiestnostDAO() {
        try {
            if (mMiestnostDAO == null)
                mMiestnostDAO = getDao(Miestnost.class);
        } catch (SQLException ex) {
            Log.e("MIESTNOST_DAO", "Error occured while fetching DAO: " + ex.getMessage());
        }
        return mMiestnostDAO;
    }

    public Dao<Pohyb, Integer> PohybDAO() {
        try {
            if (mPohybDAO == null)
                mPohybDAO = getDao(Pohyb.class);
        } catch (SQLException ex) {
            Log.e("POHYB_DAO", "Error occured while fetching DAO: " + ex.getMessage());
        }
        return mPohybDAO;
    }

    public Dao<TypTransakcie, Integer> TypTransakcieDAO() {
        try {
            if (mTypTransakcieDAO == null)
                mTypTransakcieDAO = getDao(TypTransakcie.class);
        } catch (SQLException ex) {
            Log.e("TYPTRANSAKCIE_DAO", "Error occured while fetching DAO: " + ex.getMessage());
        }
        return mTypTransakcieDAO;
    }

    public Dao<AktualneMnozstvo, Integer> AktualneMnozstvoDAO() {
        try {
            if (mAktualneMnozstvoDAO == null)
                mAktualneMnozstvoDAO = getDao(AktualneMnozstvo.class);
        } catch (SQLException ex) {
            Log.e("AktualneMnozstvoDAO", "Error occured while fetching DAO: " + ex.getMessage());
        }
        return mAktualneMnozstvoDAO;
    }

    @Override
    public void close() {
        super.close();
        mTovarDAO = null;
        mOsobaDAO = null;
        mMiestnostDAO = null;
        mPohybDAO = null;
        mTypTransakcieDAO = null;
        mAktualneMnozstvoDAO = null;
    }
}
