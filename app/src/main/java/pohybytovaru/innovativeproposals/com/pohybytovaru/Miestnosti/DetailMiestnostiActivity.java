package pohybytovaru.innovativeproposals.com.pohybytovaru.Miestnosti;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Database.DatabaseHelper;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.OrmLiteAppCompatActivity;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Miestnost;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;

@EActivity(R.layout.activity_list_miestnosti_detail)
public class DetailMiestnostiActivity extends AppCompatActivity {
    @Extra("EXTRA_MIESTNOST")
    Miestnost miestnost;

    @Extra("EXTRA_LIST_MIESTNOST")
    ArrayList<Miestnost> list_miestnosti = new ArrayList<>();

    @ViewById(R.id.inputLayout_MiestnostName)
    TextInputLayout textInputLayout;

    @ViewById(R.id.txt_MiestnostName)
    EditText txt_Miestnost;

    @ViewById(R.id.activity_list_miestnost_jeSklad)
    Switch jeSklad;

    @AfterViews
    void ProcessAfterViews() {
    }


    @TextChange(R.id.txt_MiestnostName)
    void onMiestnostTextChange(){
        textInputLayout.setError(null);
    }

    @Click(R.id.btn_Save)
    void SaveChanges() {
        String itemName = txt_Miestnost.getText().toString().trim().toLowerCase();

        //validuj miestnost, tj ci sa uz rovnaka nenachadza v databaze
        if(duplicateNameExists(itemName)){
            textInputLayout.setError(getString(R.string.lbl_duplicate_miestnost));
            return;
        }

        //no duplicate, create new miestnost
        Miestnost resultMiestnost = new Miestnost();
        resultMiestnost.setNazov(itemName);
        resultMiestnost.setJeSklad(jeSklad.isChecked());

        Intent intent = new Intent();
        intent.putExtra("EXTRA_MIESTNOST", resultMiestnost);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }

    @Click(R.id.btn_Cancel)
    void CloseWindow() {
        this.finish();
    }

    private boolean duplicateNameExists(String itemName){
        for (int i = 0; i < list_miestnosti.size(); i++) {
            if(list_miestnosti.get(i).getNazov().toLowerCase().equals(itemName))
                return true;
        }

        //no match found
        return  false;
    }
}
