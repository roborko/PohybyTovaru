package pohybytovaru.innovativeproposals.com.pohybytovaru.Tovary;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Database.DatabaseHelper;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Tovar;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;


//  Import .csv file to Sqlite in Android

public class ImportTovarov extends AppCompatActivity implements View.OnClickListener {

    // Progress Dialog
    public ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    public Button myBt;
    private ProgressBar progressBar;
    //  private Context thisContext;
    Toolbar toolbar;

    // File url to download
    //private static String file_url = "https://www.dropbox.com/s/tyahxlx7gqia7bk/importTovarov.csv?dl=0
    // Now , Simply Replace the String “www.dropbox.com” with “dl.dropboxusercontent.com” And your Direct Download Link is Ready
    private static String file_url = "https://dl.dropboxusercontent.com/s/tyahxlx7gqia7bk/importTovarov.csv?dl=0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_import);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //   thisContext = getApplicationContext();

        myBt = findViewById(R.id.btImport);
        progressBar = (ProgressBar) findViewById(R.id.progressBarMain);
        progressBar.setMax(0);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    @Override
    public void onClick(View v) {

        //boolean jeOK = false;
        if (jeZaznamVTabulke("tovar")) {
            //showDialogFragment("Tovary su uz definovane");
            Toast.makeText(getApplicationContext(), "Tovary su uz definovane", Toast.LENGTH_LONG).show();
        } else {

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setMax(2);

            AsyncTask<String, Integer, String> execute = new DownloadFileFromURL().execute(file_url);

        }
    }

    public boolean jeZaznamVTabulke (String myTable){

            boolean isOK = false;

            DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
            // Gets the data repository in write mode
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String sSQL = "SELECT id FROM " + myTable;
            // db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(sSQL, null);

            if (cursor.moveToFirst()) {
                do {

                    int aa = cursor.getInt(0);
                    if (aa > 0)
                        isOK = true;

                } while (cursor.moveToNext()); // kurzor na dalsi zaznam
            }
            cursor.close();
            return isOK;
        }



    class DownloadFileFromURL extends AsyncTask<String, Integer, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         */

      //  private Context mContext;

        //  final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBarMain);

        /*
        public DownloadFileFromURL(Context context) {
            mContext = context;
        } */

       // TovarDataModel dm = new TovarDataModel(mContext);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... f_url) {

            try {

                DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
                // Gets the data repository in write mode
                SQLiteDatabase db = dbHelper.getReadableDatabase();


                URL url = new URL(f_url[0]);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();

                int lenghtOfFile = urlConnection.getContentLength();
                BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String csvLine;
                int aa = 0;
                int intProgress = 0;
                while ((csvLine = r.readLine()) != null) {
                    if (aa > 0) {

                        String[] row = csvLine.split(";");
                        Tovar myTovar = new Tovar();

                        myTovar.setNazov(row[0].toString());
                        myTovar.setKodTovaru(row[1].toString());
                        myTovar.setPoznamka(row[2].toString());
                        myTovar.setMinimalneMnozstvo(Double.parseDouble(row[3].toString()));

                      //  long xx = dm.ulozTovar(myTovar);
                        long xx = ulozTovar(myTovar, db);


                        ++intProgress;
                        if (intProgress > 98) intProgress = 1;
                        publishProgress(intProgress);
                    }
                    ++aa;
                }

                db.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            Log.i("Status", "XML loaded into table Tovar");

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(Integer... values) {
            // setting progress percentage
            //  pDialog.setProgress(Integer.parseInt(progress[0]));
            progressBar.setProgress(values[0]);  // update progressbaru
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            //   dismissDialog(progress_bar_type);
            Toast.makeText(getApplicationContext(),"Import je ukonceny",Toast.LENGTH_LONG).show();

        }


        public long ulozTovar(Tovar myTovar, SQLiteDatabase db) {

            boolean existuje = false;
            long vysledok = 0;
            String commandSql = "INSERT INTO Tovar ( NAZOV, KodTovaru, Poznamka, MinimalneMnozstvo) VALUES ( ?,?,?,?  )";

            try
            {
                // https://stackoverflow.com/questions/7331310/how-to-store-image-as-blob-in-sqlite-how-to-retrieve-it
                SQLiteStatement insertStmt      =   db.compileStatement(commandSql);
                insertStmt.clearBindings();

                insertStmt.bindString(1, myTovar.getNazov());
                insertStmt.bindString(2, myTovar.getKodTovaru());
                insertStmt.bindString(3, myTovar.getPoznamka());
                insertStmt.bindDouble(4, myTovar.getMinimalneMnozstvo());

                Log.d("update","Query: "+insertStmt.toString());
                insertStmt.executeInsert();

             //   db.close();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return vysledok;
        }

    }
}





