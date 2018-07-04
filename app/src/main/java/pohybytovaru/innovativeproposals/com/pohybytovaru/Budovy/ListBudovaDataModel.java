package pohybytovaru.innovativeproposals.com.pohybytovaru.Budovy;

import android.content.Context;
import java.util.List;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Budova;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Miestnost;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Poschodie;

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

public class ListBudovaDataModel extends SQLiteOpenHelper {

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

    public ListBudovaDataModel(Context ctx) {
        // super(context, name, factory, version);
        super(ctx, DB_DATABAZA, null, DB_VERZIA);
    }

    public List<Budova> getZoznamBudov() throws URISyntaxException {

        ArrayList<Budova> results = new ArrayList<>();
        String sSQL;

        /*
        sSQL = "SELECT  tovar.Id, tovar.fotografia, tovar.nazov, sum(aktualneMnozstvo.mnozstvo), tovar.MinimalneMnozstvo  " +
                "FROM aktualneMnozstvo " +
                "JOIN tovar on tovar.id = aktualneMnozstvo.tovar " +
                "WHERE tovar.MinimalneMnozstvo > 0 " +
                "GROUP BY  tovar.id " +
                "HAVING tovar.MinimalneMnozstvo > sum(aktualneMnozstvo.mnozstvo) " +
                "ORDER BY  tovar.id"; */


        sSQL = "SELECT id, nazov  " +
                "FROM Budova " ;
                /*"JOIN tovar on tovar.id = aktualneMnozstvo.tovar " +
                "WHERE tovar.MinimalneMnozstvo > 0 " +
                "GROUP BY  tovar.id " +
                "HAVING tovar.MinimalneMnozstvo > sum(aktualneMnozstvo.mnozstvo) " + */

        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement selectStmt  =   db.compileStatement(sSQL);

        Cursor cursor = db.rawQuery(sSQL, null);

        //kurzor na prvy zaznam
        if (cursor.moveToFirst()) {
            do {

                Budova newItem = new Budova();
                newItem.setId(cursor.getInt(0));
                newItem.setNazov(cursor.getString(1));
               // newItem.setTovar(cursor.getString(2));
               // newItem.setMnozstvo(cursor.getDouble(3));
               // newItem.setLimitne_mnozstvo(cursor.getDouble(4));
                results.add(newItem);

            } while (cursor.moveToNext()); // kurzor na dalsi zaznam
        }
        cursor.close();
        return results;
    }

    public List<Poschodie> getZoznamPoschodi(int budovaID) throws URISyntaxException {

        ArrayList<Poschodie> results = new ArrayList<>();
        String sSQL;

        sSQL = "SELECT Poschodie.id, Poschodie.IDBUDOVA, Poschodie.nazov  " +
                "FROM Poschodie " +
               // "JOIN Budova on Budova.id = Poschodie.IDBUDOVA " +
                "WHERE Poschodie.IDBUDOVA = " + budovaID ;

        /*"GROUP BY  tovar.id " +
                "HAVING tovar.MinimalneMnozstvo > sum(aktualneMnozstvo.mnozstvo) " ; */

        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement selectStmt  =   db.compileStatement(sSQL);

        Cursor cursor = db.rawQuery(sSQL, null);

        //kurzor na prvy zaznam
        if (cursor.moveToFirst()) {
            do {

                Poschodie newItem = new Poschodie();
                newItem.setId(cursor.getInt(0));
                newItem.setIdBudova(cursor.getInt(1));
                newItem.setNazov(cursor.getString(2));
                // newItem.setMnozstvo(cursor.getDouble(3));
                // newItem.setLimitne_mnozstvo(cursor.getDouble(4));
                results.add(newItem);

            } while (cursor.moveToNext()); // kurzor na dalsi zaznam
        }
        cursor.close();
        return results;
    }

    public List<Miestnost> getZoznamMiestnosti(int poschodieID) throws URISyntaxException {

        ArrayList<Miestnost> results = new ArrayList<>();
        String sSQL;

        sSQL = "SELECT id, IDBUDOVA, IDPOSCHODIE, NAZOV  " +
                "FROM Miestnost " +
                "WHERE IDPOSCHODIE = " + poschodieID +
                " ORDER BY NAZOV COLLATE NOCASE"; // zohladnuje radenie podla slovenciny

        /*"GROUP BY  tovar.id " +
                "HAVING tovar.MinimalneMnozstvo > sum(aktualneMnozstvo.mnozstvo) " ; */

        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement selectStmt  =   db.compileStatement(sSQL);

        Cursor cursor = db.rawQuery(sSQL, null);

        //kurzor na prvy zaznam
        if (cursor.moveToFirst()) {
            do {

                Miestnost newItem = new Miestnost();
                newItem.setId(cursor.getInt(0));
                newItem.setIdBudova(cursor.getInt(1));
                newItem.setIdPoschodie(cursor.getInt(2));
                newItem.setNazov(cursor.getString(3));
                // newItem.setMnozstvo(cursor.getDouble(3));
                // newItem.setLimitne_mnozstvo(cursor.getDouble(4));
                results.add(newItem);

            } while (cursor.moveToNext()); // kurzor na dalsi zaznam
        }
        cursor.close();
        return results;
    }


/*
    public ArrayList getZoznamMiestnostiSBudovami() {
    } */

    public  String [][][][] getZoznamMiestnostiSBudovami()  {

        int maxBudov = dajMaxId("Budova");
        int maxPoschodi = dajMaxId("Poschodie");
        int maxMiestnosti = dajMaxId("Miestnost");

        String results[][][][] = new String[maxBudov][maxPoschodi][maxMiestnosti][2];

        String sSQL;

        sSQL = "select budova.nazov,poschodie.nazov, miestnost.nazov, budova.id, poschodie.id, miestnost.id from miestnost " +
                "join poschodie on poschodie.id = miestnost.idposchodie " +
                "join budova on budova.id = miestnost.idbudova" +
                " ORDER BY budova.nazov COLLATE NOCASE, poschodie.nazov COLLATE NOCASE, miestnost.nazov COLLATE NOCASE"; // zohladnuje radenie podla slovenciny

        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement selectStmt  =   db.compileStatement(sSQL);

        Cursor cursor = db.rawQuery(sSQL, null);
        String tempBudova = "";
        String tempPoschodie = "";
        int budovaId = -1;
        int poschodieId = -1;
        int miestnostId = 0;
        int kolkyRetazec = 0;
        String tempArray = "";

        //kurzor na prvy zaznam
        if (cursor.moveToFirst()) {
            do {

                if(!tempBudova.equals(cursor.getString(0))) {

                    if(!tempBudova.isEmpty()) {

                        // zaloz string
                        int counter = 0;
                        for( int i=0; i<tempArray.length(); i++ ) {
                            if( tempArray.charAt(i) == '^' ) {
                                counter++;
                            }
                        }

                        tempArray = "";
                    }

                    tempBudova =cursor.getString(0);
                    ++budovaId;
                    poschodieId = -1;
                  //  kolkyRetazec = 0;
                 //   kolkoMiestnostiVArray = 0;
                }

                if(!tempPoschodie.equals(cursor.getString(1))) {
                    tempPoschodie =cursor.getString(1);
                    ++poschodieId;

                    //maxPoschodi = dajPocet("Poschodie","IdBudova =" + cursor.getString(3));

               /*     tempArray = tempArray + (cursor.getString(0);
                    tempArray = tempArray + "^";
                    tempArray = tempArray + (cursor.getString(1);
                    tempArray = tempArray + "^"; */

                    results[budovaId][poschodieId][0][0] = cursor.getString(0); // budova je v nulke na poslednej pozicii
                    results[budovaId][poschodieId][0][1] = cursor.getString(1); // poschodie je v jednotke na poslednej pozicii
                    miestnostId = 0;

                }

                ++miestnostId;  // 1, 0, 2
                results[budovaId][poschodieId][miestnostId][1] = cursor.getString(2); // misetnosti zacinaju od jednotky
                //tempArray = tempArray + (cursor.getString(2);
                //tempArray = tempArray + "^";
                //++kolkoMiestnostiVArray;


            } while (cursor.moveToNext()); // kurzor na dalsi zaznam
        }
        cursor.close();

        // este dopln posledny array

        return results;
    }


    public long ulozPoschodie(Poschodie myPoschodie) {

        boolean existuje = false;
        long vysledok = 0;
        String commandSql = "INSERT INTO Poschodie ( IDBUDOVA, NAZOV) VALUES ( ? ,? )";

        String sSQL = "SELECT id  FROM Poschodie where id = " + myPoschodie.getId();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sSQL, null);

        //kurzor na prvy zaznam
        if (cursor.moveToFirst()) {
            do {
                existuje = true;
                commandSql = "UPDATE Poschodie SET NAZOV = ?  WHERE id = ?";
            } while (cursor.moveToNext()); // kurzor na dalsi zaznam
        }

        cursor.close(); // je to tu dobre?
        try
        {
            // https://stackoverflow.com/questions/7331310/how-to-store-image-as-blob-in-sqlite-how-to-retrieve-it
            SQLiteStatement insertStmt      =   db.compileStatement(commandSql);
            insertStmt.clearBindings();

            if(existuje) {
                insertStmt.bindString(1, myPoschodie.getNazov());
                insertStmt.bindString(2, Integer.toString(myPoschodie.getId()));

            } else {
                insertStmt.bindString(1, Integer.toString(myPoschodie.getIdBudova()));
                insertStmt.bindString(2, myPoschodie.getNazov());

            }

            Log.d("update","Query: "+insertStmt.toString());
            insertStmt.executeInsert();
            db.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return vysledok;
    }

    public long ulozMiestnost(Miestnost myMiestnost) {

        boolean existuje = false;
        long vysledok = 0;
        String commandSql = "INSERT INTO Miestnost ( IDBUDOVA, IDPOSCHODIE, NAZOV, JeSklad) VALUES ( ?,?,?,? )";

        String sSQL = "SELECT id  FROM Miestnost where id = " + myMiestnost.getId();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sSQL, null);

        //kurzor na prvy zaznam
        if (cursor.moveToFirst()) {
            do {
                existuje = true;
                commandSql = "UPDATE Miestnost SET NAZOV = ?, JeSklad = ? WHERE id = ?";
            } while (cursor.moveToNext()); // kurzor na dalsi zaznam
        }

        cursor.close(); // je to tu dobre?
        try
        {
            // https://stackoverflow.com/questions/7331310/how-to-store-image-as-blob-in-sqlite-how-to-retrieve-it
            SQLiteStatement insertStmt      =   db.compileStatement(commandSql);
            insertStmt.clearBindings();

            if(existuje) {
                insertStmt.bindString(1, myMiestnost.getNazov());

                if(myMiestnost.isJeSklad())
                    insertStmt.bindLong(2,1);
                else
                    insertStmt.bindLong(2,0);

                insertStmt.bindString(3, Integer.toString(myMiestnost.getId()));

            }
         else {
            insertStmt.bindString(1, Integer.toString(myMiestnost.getIdBudova()));
            insertStmt.bindString(2, Integer.toString(myMiestnost.getIdPoschodie()));
            insertStmt.bindString(3, myMiestnost.getNazov());
            if(myMiestnost.isJeSklad())
                insertStmt.bindLong(4,1);
            else
                insertStmt.bindLong(4,0);

            }
            Log.d("update","Query: "+insertStmt.toString());
            insertStmt.executeInsert();

            db.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return vysledok;
    }

    public long ulozBudova(Budova myBudova) {

        boolean existuje = false;
        long vysledok = 0;
        String commandSql = "INSERT INTO Budova ( NAZOV) VALUES ( ?  )";

        String sSQL = "SELECT id  FROM Budova where id = " + myBudova.getId();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sSQL, null);

        //kurzor na prvy zaznam
        if (cursor.moveToFirst()) {
            do {
                existuje = true;
                commandSql = "UPDATE Budova SET NAZOV = ?  WHERE id = ?";
            } while (cursor.moveToNext()); // kurzor na dalsi zaznam
        }

        cursor.close(); // je to tu dobre?
        try
        {
            // https://stackoverflow.com/questions/7331310/how-to-store-image-as-blob-in-sqlite-how-to-retrieve-it
            SQLiteStatement insertStmt      =   db.compileStatement(commandSql);
            insertStmt.clearBindings();

            if(existuje) {
                insertStmt.bindString(1, myBudova.getNazov());
                insertStmt.bindString(2, Integer.toString(myBudova.getId()));

            } else {
                insertStmt.bindString(1, myBudova.getNazov());
            }

            Log.d("update","Query: "+insertStmt.toString());
            insertStmt.executeInsert();

            db.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return vysledok;
    }

    public int dajMaxId(String tableName) {

        int result = 0;
        String sSQL = "SELECT max(id) FROM '" + tableName   + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sSQL, null);

        //kurzor na prvy zaznam
        if (cursor.moveToFirst()) {
            do {

                result = cursor.getInt(0);

            } while (cursor.moveToNext()); // kurzor na dalsi zaznam
        }
        cursor.close();
        return result;
    }

    public int dajPocet(String tableName, String podmienka) {

        int result = 0;
        String sSQL = "SELECT count(*) FROM '" + tableName   + "' WHERE " + podmienka;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sSQL, null);

        //kurzor na prvy zaznam
        if (cursor.moveToFirst()) {
            do {

                result = cursor.getInt(0);

            } while (cursor.moveToNext()); // kurzor na dalsi zaznam
        }
        cursor.close();
        return result;
    }

    public void deleteRowFromTable(String tableName, int myID) {

        String sSQL = "delete FROM '" + tableName   + "' WHERE id = " + myID;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sSQL);
    }

    public String dajNazovByID(String tableName, int myID) {

        String result = "";
        String sSQL = "SELECT nazov FROM '" + tableName   +  "' WHERE id = " + myID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sSQL, null);

        //kurzor na prvy zaznam
        if (cursor.moveToFirst()) {
            do {

                result = cursor.getString(0);

            } while (cursor.moveToNext()); // kurzor na dalsi zaznam
        }
        cursor.close();
        return result;
    }


}





