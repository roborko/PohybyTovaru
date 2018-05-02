package pohybytovaru.innovativeproposals.com.pohybytovaru.Tovary;

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

import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.ImageHelpers;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Miestnost;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Tovar;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;

@EActivity(R.layout.activity_list_tovar_detail)
public class DetailTovarActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 199;

    @ViewById(R.id.detailView_Image)
    ImageView tovarImage;

    @ViewById
    EditText txt_TovarName, txt_MinMnozstvo,txt_Poznamka, txt_KodTovaru;

    @ViewById
    TextInputLayout inputLayout_TovarNazov;

    @Extra("EXTRA_TOVAR")
    Tovar tovar;

    @ViewById
    Toolbar toolbar;

    @AfterViews
    void ProcessAfterViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(tovar != null){
            txt_TovarName.setText(tovar.getNazov());
            txt_MinMnozstvo.setText(String.valueOf(tovar.getMinimalneMnozstvo()));
            txt_Poznamka.setText(tovar.getPoznamka());
            txt_KodTovaru.setText(tovar.getKodTovaru());
            tovarImage.setImageBitmap(ImageHelpers.convertBytesToBitmap(tovar.getFotografia()));
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

    @Click(R.id.fab_tovar_detail_take_picture)
    void takePhotoClick() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @OnActivityResult(CAMERA_REQUEST)
    void onResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            tovarImage.setImageBitmap(photo);
        }
    }

    //Method which serializes object and sends it back to activity
    private void SaveItem() {
        if(validateControls() == false){
            return;
        }
        if(tovar == null)
            tovar = new Tovar();

        tovar.setNazov(txt_TovarName.getText().toString());
        tovar.setFotografia(ImageHelpers.getImageBytesFromImageView(tovarImage));
        tovar.setMinimalneMnozstvo(Double.valueOf(txt_MinMnozstvo.getText().toString()));
        tovar.setPoznamka(txt_Poznamka.getText().toString());
        tovar.setKodTovaru(txt_KodTovaru.getText().toString());

        Intent returnIntent = new Intent();
        returnIntent.putExtra(ListTovarActivity.CODE_INTENT_TOVAR, tovar);
        setResult(Activity.RESULT_OK, returnIntent);
        this.finish();
    }

    @TextChange(R.id.txt_TovarName)
    void onNazovTextChange(){
        inputLayout_TovarNazov.setError(null);
    }

    //validates control values
    private boolean validateControls(){
        if(txt_TovarName.length() == 0){
            inputLayout_TovarNazov.setError(getString(R.string.lbl_error_hint_tovar_nazov));
            return false;
        }
        return true;
    }
}
