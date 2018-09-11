package pohybytovaru.innovativeproposals.com.pohybytovaru.Voziky;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Vozik;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;


@EActivity(R.layout.vozik_activity_detail)
public class VozikDetailActivity extends AppCompatActivity {

    @ViewById
    EditText txt_VozikName, txt_VozikKod;

    @ViewById
    TextInputLayout inputLayout_VozikNazov;

    @Extra("EXTRA_VOZIK")
    Vozik vozik;

    @ViewById
    Toolbar toolbar;

    @AfterViews
    void ProcessAfterViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(vozik != null){
            txt_VozikName.setText(vozik.getName());
            txt_VozikKod.setText(vozik.getKod());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //toto potrebujeme, inac nebudeme mat save button
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;

            case R.id.menu_item_save:
                SaveItem();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

     //Method which serializes object and sends it back to activity
    private void SaveItem() {
        if(validateControls() == false){
            return;
        }
        if(vozik == null)
            vozik = new Vozik();

        vozik.setName(txt_VozikName.getText().toString());
        vozik.setKod(txt_VozikKod.getText().toString());

        Intent returnIntent = new Intent();
        returnIntent.putExtra(VozikListActivity.CODE_INTENT_VOZIK, vozik);
        setResult(Activity.RESULT_OK, returnIntent);
        this.finish();
    }

    @TextChange(R.id.txt_VozikName)
    void onNazovTextChange(){
        inputLayout_VozikNazov.setError(null);
    }

    //validates control values
    private boolean validateControls(){
        if(txt_VozikName.length() == 0){
            inputLayout_VozikNazov.setError(getString(R.string.lbl_error_hint_vozik_nazov));
            return false;
        }
        return true;
    }
}
