package pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.MinimalneMnozstva;

        import android.content.Context;
        import android.support.annotation.NonNull;

        import java.util.List;

        import pohybytovaru.innovativeproposals.com.pohybytovaru.Adapters.DataBoundAdapter;
        import pohybytovaru.innovativeproposals.com.pohybytovaru.Adapters.DataBoundViewHolder;
        import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.AktualneMnozstvo;
        import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Miestnost;
        import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.MnozstvaTovaru;
        import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Tovar;
        import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.ISimpleRowClickListener;
        import pohybytovaru.innovativeproposals.com.pohybytovaru.databinding.ActivityListTovarRowBinding;


        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteStatement;
        import android.util.Log;
        import java.net.URISyntaxException;
        import java.util.ArrayList;
        import java.util.List;

        import static pohybytovaru.innovativeproposals.com.pohybytovaru.Database.DatabaseHelper.DATABASE_NAME;
        import static pohybytovaru.innovativeproposals.com.pohybytovaru.Database.DatabaseHelper.DATABASE_VERSION;


public class MinimalneMnozstvaTovarovDataModel extends SQLiteOpenHelper {

    private static final String DB_DATABAZA =  DATABASE_NAME;
    private static final int DB_VERZIA = DATABASE_VERSION;

    // zaklad
    @Override
    public void onCreate(SQLiteDatabase db) {
      /*         db.execSQL("CREATE TABLE " + DB_TABULKA
                + " (" + ATR_ID     + " INTEGER PRIMARY KEY,"
                + ATR_DIVIZIA  + " TEXT"
                + ");");*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     /*   String query = "DROP TABLE IF EXISTS " + DB_TABULKA;
        db.execSQL(query);
        onCreate(db);*/
    }

    public MinimalneMnozstvaTovarovDataModel(Context ctx) {
        // super(context, name, factory, version);
        super(ctx, DB_DATABAZA, null, DB_VERZIA);
    }

    public List<MnozstvaTovaru> getPrekroceneMinimalneMnozstvo() throws URISyntaxException {

        ArrayList<MnozstvaTovaru> results = new ArrayList<>();
        String sSQL;

        // pocitane pre vsetky miestnosti
/*        sSQL = "SELECT  aktualneMnozstvo.Id, tovar.fotografia, tovar.id, tovar.nazov, sum(aktualneMnozstvo.mnozstvo), tovar.MinimalneMnozstvo  " +
                "FROM aktualneMnozstvo " +
                "JOIN tovar on tovar.id = aktualneMnozstvo.tovar " +
                "WHERE tovar.MinimalneMnozstvo > 0 " +
                "GROUP BY  tovar.id " +
                "HAVING tovar.MinimalneMnozstvo > sum(aktualneMnozstvo.mnozstvo) " +
                "ORDER BY  tovar.id"; */

        // pocitane len pre sklady
        sSQL = "SELECT  aktualneMnozstvo.Id, tovar.fotografia, tovar.id, tovar.nazov, sum(aktualneMnozstvo.mnozstvo), tovar.MinimalneMnozstvo  " +
                "FROM tovar " +
                "left outer join aktualneMnozstvo on aktualneMnozstvo.tovar = tovar.id " +
                "JOIN miestnost on miestnost.id = aktualneMnozstvo.miestnost " +
                "WHERE (tovar.MinimalneMnozstvo > 0 and miestnost.JeSklad = 1) " +
                "GROUP BY  tovar.id " +
                "HAVING (tovar.MinimalneMnozstvo > sum(aktualneMnozstvo.mnozstvo) OR  sum(aktualneMnozstvo.mnozstvo) is NULL) " +
                "ORDER BY  tovar.id";


        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement selectStmt  =   db.compileStatement(sSQL);

        Cursor cursor = db.rawQuery(sSQL, null);

        //kurzor na prvy zaznam
        if (cursor.moveToFirst()) {
            do {

                MnozstvaTovaru newItem = new MnozstvaTovaru();
                newItem.setId(cursor.getInt(0));
                newItem.setFotografia(cursor.getBlob(1));
                newItem.setTovar(cursor.getInt(2));
                newItem.setTovarName(cursor.getString(3));
                newItem.setMnozstvo(cursor.getDouble(4));
                newItem.setLimitne_mnozstvo(cursor.getDouble(5));
                results.add(newItem);

            } while (cursor.moveToNext()); // kurzor na dalsi zaznam
        }
        cursor.close();
        return results;
    }

    /*
    public String getTovarName (int myTovarId) {

        String result = "";
        String sSQL;

        sSQL = "SELECT  tovar.nazov " +
                "FROM tovar " +
                "WHERE id = " + myTovarId;

        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement selectStmt  =   db.compileStatement(sSQL);

        Cursor cursor = db.rawQuery(sSQL, null);

        //kurzor na prvy zaznam
        if (cursor.moveToFirst()) {
            do {

               // MnozstvaTovaru newItem = new MnozstvaTovaru();
               // newItem.setId(cursor.getInt(0));
                //newItem.setFotografia(cursor.getBlob(1));
                //newItem.setTovar(cursor.getInt(2));
                //newItem.setMnozstvo(cursor.getDouble(3));
                //newItem.setLimitne_mnozstvo(cursor.getDouble(4));
                result = cursor.getString(0);

            } while (cursor.moveToNext()); // kurzor na dalsi zaznam
        }
        cursor.close();
        return result;

    }
*/

}




