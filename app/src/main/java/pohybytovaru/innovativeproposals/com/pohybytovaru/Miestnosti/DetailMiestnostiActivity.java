package pohybytovaru.innovativeproposals.com.pohybytovaru.Miestnosti;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Switch;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Budovy.ListBudovaDataModel;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Miestnost;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;

@EActivity(R.layout.activity_list_miestnosti_detail)
public class DetailMiestnostiActivity extends AppCompatActivity {

    ListBudovaDataModel dm = new ListBudovaDataModel(this);

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

    int myBudovaId = 0;
    int myPoschodieId = 0;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            myBudovaId = getIntent().getIntExtra("budovaID",0);
            myPoschodieId = getIntent().getIntExtra("poschodieID",0);
        }

    }

    @AfterViews
    void ProcessAfterViews() {
        if(miestnost != null && miestnost.getId() > 0){
            txt_Miestnost.setText(miestnost.getNazov());
            jeSklad.setChecked(miestnost.isJeSklad());
        }
    }

    @TextChange(R.id.txt_MiestnostName)
    void onMiestnostTextChange(){
        textInputLayout.setError(null);
    }

    @Click(R.id.btn_Save)
    void SaveChanges() {
        String itemName = txt_Miestnost.getText().toString().trim();

        //validuj miestnost, tj ci sa uz rovnaka nenachadza v databaze
        if(duplicateExists(itemName)){
            textInputLayout.setError(getString(R.string.lbl_duplicate_miestnost));
            return;
        }

        //no duplicate, create new miestnost
        Miestnost resultMiestnost;
        if(miestnost == null) {
            resultMiestnost = new Miestnost();
            resultMiestnost.setIdBudova(myBudovaId);
            resultMiestnost.setIdPoschodie(myPoschodieId);
        }
        else
            resultMiestnost = miestnost;

        resultMiestnost.setNazov(itemName);
        resultMiestnost.setJeSklad(jeSklad.isChecked());

        // ? long ako =  dm.ulozMiestnost(resultMiestnost);

        Intent intent = new Intent();
        intent.putExtra("EXTRA_MIESTNOST", resultMiestnost);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }

    @Click(R.id.btn_Cancel)
    void CloseWindow() {
        this.finish();
    }

    private boolean duplicateExists(String itemName){
        if(miestnost != null && miestnost.getId() > 0){
            //skontroluj ci sa nazov zhoduje
            if(miestnost.getNazov().equals(itemName)){
                //uzivatel nezmenil nazov
                return false;
            }
        }

        for (int i = 0; i < list_miestnosti.size(); i++) {
            if(list_miestnosti.get(i).getNazov().equals(itemName)) {
                //name is the same; check if it is is sklad
//                if(list_miestnosti.get(i).isJeSklad() == jeSklad.isChecked())
                    return true;
            }
        }

        //no match found
        return  false;
    }
}
