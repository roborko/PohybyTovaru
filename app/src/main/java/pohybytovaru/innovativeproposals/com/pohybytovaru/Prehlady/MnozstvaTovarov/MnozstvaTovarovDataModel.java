package pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.MnozstvaTovarov;

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



/*
select  tovar.nazov as Tovar, miestnost.nazov as Miestnost, aktualneMnozstvo.mnozstvo
        from aktualneMnozstvo
        join miestnost on miestnost.id = aktualneMnozstvo.miestnost
        join tovar on tovar.id = aktualneMnozstvo.tovar
        order by  tovar.nazov, miestnost.nazov


select tovar.id, tovar.fotografia,  tovar.nazov as Tovar, sum(aktualneMnozstvo.mnozstvo)
from aktualneMnozstvo
join tovar on tovar.id = aktualneMnozstvo.tovar
group by  tovar.id
order by  tovar.id

*/

public class MnozstvaTovarovDataModel extends SQLiteOpenHelper {

    private static final String DB_DATABAZA =  DATABASE_NAME;
    private static final int DB_VERZIA = DATABASE_VERSION;
  //  private static final String DB_TABULKA = "majetok";

//public class MnozstvaTovarovAdapter extends DataBoundAdapter<ActivityListTovarRowBinding, Tovar> {
  //  private final ISimpleRowClickListener actionCallback;

/*    public MnozstvaTovarovAdapter(@NonNull Context context, int layoutId, ISimpleRowClickListener actionCallback, List<Tovar> data) {
        super(layoutId);
        this.context = context;
        this.actionCallback = actionCallback;
        this.data = data;
        this.filterView.dataSourceChanged(this.data);
    }
*/

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


    public MnozstvaTovarovDataModel(Context ctx) {
        // super(context, name, factory, version);
        super(ctx, DB_DATABAZA, null, DB_VERZIA);
    }

    /*
    @Override
    protected void bindItem(DataBoundViewHolder<ActivityListTovarRowBinding> holder, int position, List<Object> payloads) {
        holder.binding.setObj(this.filterView.get(position));
        holder.binding.setCallback(actionCallback);
    }

    @Override
    public int getItemCount() {
        try {
            return this.filterView.size();
        } catch (Exception ex) {
            return 0;
        }
    } */

    public List<MnozstvaTovaru> getTovarTotalList(String myFilter) throws URISyntaxException {

        ArrayList<MnozstvaTovaru> results = new ArrayList<>();
        String sSQL;
        String myNazov2 = "%"+myFilter+"%";

          sSQL = "SELECT  tovar.Id, tovar.fotografia, tovar.nazov, sum(aktualneMnozstvo.mnozstvo) " +
                 "FROM aktualneMnozstvo " +
                 "JOIN tovar on tovar.id = aktualneMnozstvo.tovar " +
                  "GROUP BY  tovar.id " +
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
                newItem.setTovar(cursor.getString(2));
             //   newItem.setMiestnost(cursor.getString(2));
                newItem.setMnozstvo(cursor.getDouble(3));
                results.add(newItem);

            } while (cursor.moveToNext()); // kurzor na dalsi zaznam
        }
        cursor.close();
        return results;
    }

    public List<MnozstvaTovaru> getMnozstvaVMiestnostiach(int myId) throws URISyntaxException {

        ArrayList<MnozstvaTovaru> results = new ArrayList<>();
        String sSQL;
        //String myTovarId = myFilter+"
        //   String myKod2 =  myFilter + "%";

      /*  sSQL = "SELECT aa.Id,aa.itemdescription,aa.itembarcode,aa.status,aa.datum,bb.obrazok, aa.datumzaradenia, aa.serialnr, aa.datumvyradenia, aa.zodpovednaosoba, aa.typmajetku," +
                "aa.obstaravaciacena, aa.extranotice, aa.datumReal " +
                "FROM majetok aa left join  MajetokObrazky bb on bb.itembarcode = aa.itembarcode " +
                "WHERE aa.itembarcode like '" + myKod2 +"' or aa.serialnr like '" + myKod2 +"' or aa.itemdescription like '" + myNazov2 +
                "' or aa.extranotice like '" + myNazov2 + "' order by aa.datumREAL asc limit 100";*/

        sSQL = "SELECT  miestnost.Id, tovar.fotografia, tovar.nazov, miestnost.nazov, aktualneMnozstvo.mnozstvo " +
                "FROM aktualneMnozstvo " +
                "JOIN miestnost on miestnost.id = aktualneMnozstvo.miestnost " +
                "JOIN tovar on tovar.id = aktualneMnozstvo.tovar " +
                "WHERE tovar.id = " + myId +
                " ORDER BY miestnost.nazov COLLATE NOCASE";


        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement selectStmt  =   db.compileStatement(sSQL);

        Cursor cursor = db.rawQuery(sSQL, null);

        //kurzor na prvy zaznam
        if (cursor.moveToFirst()) {
            do {

                MnozstvaTovaru newItem = new MnozstvaTovaru();
                newItem.setId(cursor.getInt(0));
                newItem.setFotografia(cursor.getBlob(1));
                newItem.setTovar(cursor.getString(2));
                newItem.setMiestnost(cursor.getString(3));
                newItem.setMnozstvo(cursor.getDouble(4));
                results.add(newItem);

            } while (cursor.moveToNext()); // kurzor na dalsi zaznam
        }
        cursor.close();
        return results;
    }

    public List<Miestnost> getMiestnostSInventarom(int tovarId )  {

        ArrayList<Miestnost> results = new ArrayList<>();
        String sSQL;
        //String myTovarId = myFilter+"
        //   String myKod2 =  myFilter + "%";

        if(tovarId>0)

            sSQL = "SELECT distinct miestnost.nazov, miestnost.id " +
                    "FROM aktualneMnozstvo " +
                    "JOIN miestnost on miestnost.id = aktualneMnozstvo.miestnost " +
                    "WHERE aktualneMnozstvo.tovar = " + tovarId + " AND aktualneMnozstvo.Mnozstvo > 0 " +
                    " ORDER BY miestnost.nazov COLLATE NOCASE";
        else

         /*   sSQL = "SELECT distinct miestnost.nazov, miestnost.id " +  //  aktualneMnozstvo.mnozstvo
                    "FROM aktualneMnozstvo " +
                    "LEFT OUTER JOIN miestnost on miestnost.id = aktualneMnozstvo.miestnost" +
                    " ORDER BY miestnost.nazov COLLATE NOCASE"; */

            sSQL = "SELECT  miestnost.nazov, miestnost.id FROM miestnost ORDER BY miestnost.nazov COLLATE NOCASE";


        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement selectStmt  =   db.compileStatement(sSQL);

        Cursor cursor = db.rawQuery(sSQL, null);

        //kurzor na prvy zaznam
        if (cursor.moveToFirst()) {
            do {

                Miestnost myMiestnost = new Miestnost();

                myMiestnost.setNazov(cursor.getString(0));
                myMiestnost.setId(cursor.getInt(1));

                //myMiestnost.se AktualneMnozstvo
             //   myMiestnost.AktualneMnozstvo(cursor.getDouble(1));
                results.add(myMiestnost);

            } while (cursor.moveToNext()); // kurzor na dalsi zaznam
        }
        cursor.close();
        return results;
    }

    public AktualneMnozstvo getAktualneMnozstvoTovaruZMiestnosti(int tovarId, int miestnostId )  {

        String sSQL;
     //   ArrayList<AktualneMnozstvo> results = new ArrayList<>();

        AktualneMnozstvo myMnozstvo = new AktualneMnozstvo();

        if((tovarId>0) && (miestnostId>0)) {

            sSQL = "SELECT aktualneMnozstvo.miestnost, miestnost.nazov, aktualneMnozstvo.tovar, tovar.nazov, aktualneMnozstvo.mnozstvo  " +
                    "FROM aktualneMnozstvo " +
                    "JOIN miestnost on miestnost.id = aktualneMnozstvo.miestnost " +
                    "JOIN tovar on tovar.id = aktualneMnozstvo.tovar " +
            "WHERE aktualneMnozstvo.tovar = " + tovarId + " AND aktualneMnozstvo.miestnost = " + miestnostId;


            SQLiteDatabase db = this.getWritableDatabase();
            SQLiteStatement selectStmt = db.compileStatement(sSQL);

            Cursor cursor = db.rawQuery(sSQL, null);

            //kurzor na prvy zaznam
            if (cursor.moveToFirst()) {
                do {

               //     AktualneMnozstvo myMnozstvo = new AktualneMnozstvo();
                    Miestnost myMiestnost = new Miestnost();
                    Tovar myTovar = new Tovar();

                    myMiestnost.setId(cursor.getInt(0));
                    myMiestnost.setNazov(cursor.getString(1));

                    myTovar.setId(cursor.getInt(2));
                    myTovar.setNazov(cursor.getString(3));

                    myMnozstvo.setMiestnost(myMiestnost);
                    myMnozstvo.setTovar(myTovar);
                    myMnozstvo.setMnozstvo(cursor.getDouble(4));

                  //  results.add(myMnozstvo);

                } while (cursor.moveToNext()); // kurzor na dalsi zaznam
            }
            cursor.close();
        }
        return myMnozstvo;
    }

    public int setMnozstvoTovaruVMiestnosti(int tovarId, int miestnostId, double myMnozstvo )  {

        int result = 0;

        // POZOR - pri vybere  presunu to sem nema co ist - vratil som sa z detailu bez zapisu

            if((tovarId>0) && (miestnostId>0)) {

            SQLiteDatabase db = this.getWritableDatabase();
            String sUpdateSQL = "INSERT INTO  aktualneMnozstvo (Mnozstvo,tovar,miestnost) VALUES(?,?,?)";

            //String sSQL = "SELECT id  FROM aktualneMnozstvo WHERE tovar = '" + tovarId + "' AND miestnost = " + miestnostId ;
            String sSQL = "SELECT id  FROM aktualneMnozstvo WHERE tovar = " + tovarId + " AND miestnost = " + miestnostId ;
            Cursor cursor = db.rawQuery(sSQL, null);

            //kurzor na prvy zaznam
            if (cursor.moveToFirst()) {
                do {
                  //  existuje = true;
                    sUpdateSQL = "UPDATE  aktualneMnozstvo SET Mnozstvo = ? WHERE tovar = ? AND miestnost = ? ";
                } while (cursor.moveToNext()); // kurzor na dalsi zaznam
            }

            cursor.close();
            SQLiteStatement updateInstertStmt = db.compileStatement(sUpdateSQL);

            updateInstertStmt.clearBindings();
            // insertStmt.bindString(1, Integer.toString(this.accId));
            updateInstertStmt.bindDouble(1, myMnozstvo) ;
            updateInstertStmt.bindLong(2,tovarId) ;
            updateInstertStmt.bindLong(3,miestnostId) ;

            Log.d("update","Query: "+updateInstertStmt.toString());
            updateInstertStmt.executeInsert();
            db.close();
        }
       return result;
    }

}



