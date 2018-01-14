package pohybytovaru.innovativeproposals.com.pohybytovaru.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.ShoppingItem;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Tovar;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;

/**
 * Created by Robert on 28.12.2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "pohybtovaru.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private Dao<ShoppingItem, Integer> mShoppingItemDao = null;
    private Dao<Tovar, Integer> mTovarDAO = null;


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
            TableUtils.createTable(connectionSource, ShoppingItem.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Tovar.class, true);
            TableUtils.dropTable(connectionSource, ShoppingItem.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<Tovar, Integer> TovarDAO() {
        try{
            if(mTovarDAO == null)
                mTovarDAO = getDao(Tovar.class);
        } catch (SQLException ex){
            Log.e("TOVAR_DAO", "Error occured while fetching DAO: " + ex.getMessage());
        }
        return mTovarDAO;
    }


    public Dao<ShoppingItem, Integer> getShoppingItemsDao() {
        try {
            if (mShoppingItemDao == null)
                mShoppingItemDao = getDao(ShoppingItem.class);

        } catch (SQLException ex) {
            Log.e("DAO_EXEPTION", "Error occured while fethcing Shopping Items: " + ex.getMessage());
        }
        return mShoppingItemDao;
    }

    @Override
    public void close() {
        super.close();
        mShoppingItemDao = null;
        mCategoryDao = null;
    }
}
