package pohybytovaru.innovativeproposals.com.pohybytovaru;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


// ukrytie klavesnice
// http://karimvarela.com/2012/07/24/android-how-to-hide-keyboard-by-touching-screen-outside-keyboard/

public class SettingsActivity extends AppCompatActivity  {

    private EditText etMailAddress,etServerPort, etDayesOfInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etMailAddress = findViewById(R.id.etMailAddress);
      //  etServerPort = findViewById(R.id.etServerPort);
      //  etDayesOfInventory = findViewById(R.id.etDaysOfInventory);

        // ukrytie klavesnice

        /*
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_settings);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                hideKeyboard(view);
                return false;
            }
        });
*/
        loadAccountData(findViewById(android.R.id.content)); // Get root view from current activity
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                saveAccountData();

                /*
                // Globalne SharedPreferences - obsahuje meno balika
                SharedPreferences sharedPreferences = getSharedPreferences(getPackageName()+Constants.PREF_FILE_NAME, Context.MODE_PRIVATE);

                // ziskam odkaz na editor
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // zapis hodnot
                editor.putString(Constants.MAIL_ADDRESS,etMailAddress.getText().toString());
                // editor.putString(Constants.KEY_PORT,etServerPort.getText().toString());
                // editor.putInt(Constants.KEY_DAYS_OF_INVENTORY,Integer.parseInt(etDayesOfInventory.getText().toString()));
                // uloz Asynchronne
                editor.apply(); // moze sa pouzit editor.commit(), ale ta vracia boolean hodnotu a uklada Synchronne
                */

                Intent intent = new Intent(this, MainActivity_.class);
                startActivity(intent);

                this.finish();
                return true;

            case R.id.menu_item_save:

                saveAccountData();
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveAccountData() { // View view

        // Globalne SharedPreferences - obsahuje meno balika
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName()+Constants.PREF_FILE_NAME, Context.MODE_PRIVATE);

        // ziskam odkaz na editor
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // zapis hodnot
        editor.putString(Constants.MAIL_ADDRESS,etMailAddress.getText().toString());
       // editor.putString(Constants.KEY_PORT,etServerPort.getText().toString());
       // editor.putInt(Constants.KEY_DAYS_OF_INVENTORY,Integer.parseInt(etDayesOfInventory.getText().toString()));
        // uloz Asynchronne
        editor.apply(); // moze sa pouzit editor.commit(), ale ta vracia boolean hodnotu a uklada Synchronne
    }

    public void loadAccountData(View view) { // View view

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName()+Constants.PREF_FILE_NAME,MODE_PRIVATE);

        String address = sharedPreferences.getString(Constants.MAIL_ADDRESS,"jozko.mrkvicka@gmail.com"); // druhy parameter je defaultna hodnota
     //   String port = sharedPreferences.getString(Constants.KEY_PORT,"11235");
      //  int daysOfInventory = sharedPreferences.getInt(Constants.KEY_DAYS_OF_INVENTORY,5);

        etMailAddress.setText(address);
     //   etServerPort.setText(port);
     //   etDayesOfInventory.setText(Integer.toString(daysOfInventory));
    }

    /*
    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
*/

    // odchytenie hw klavesy na odchod z Activity
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) && event.getRepeatCount() == 0) {

            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setTitle("Údaje nebudú uložené");
            alertbox.setMessage("Aj tak ukončiť ? ");

            alertbox.setPositiveButton("Áno",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            doExit();
                        }
                    });

            alertbox.setNeutralButton("Nie",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });

            alertbox.show();

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    private void doExit() {
        this.finish();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}

