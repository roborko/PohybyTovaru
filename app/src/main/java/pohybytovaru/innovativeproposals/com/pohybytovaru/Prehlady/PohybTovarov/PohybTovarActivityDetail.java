package pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.PohybTovarov;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.j256.ormlite.dao.Dao;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Database.DatabaseHelper;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.ImageHelpers;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.OrmLiteAppCompatActivity;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Miestnost;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Pohyb;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Tovar;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.TypTransakcie;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;

@EActivity(R.layout.activity_pohyb_tovar_detail)
@OptionsMenu(R.menu.menu_save)
public class PohybTovarActivityDetail extends OrmLiteAppCompatActivity<DatabaseHelper> {

    @OptionsMenuItem(R.id.menu_item_save)
    MenuItem menuSave;

    @ViewById
    Toolbar toolbar;

    @ViewById(R.id.activity_pohyb_tovar_layout_miestnostFrom)
    LinearLayout layout_miestnostFrom;

    @ViewById(R.id.activity_pohyb_tovar_layout_miestnostTo)
    LinearLayout layout_miestnostTo;

    @ViewById(R.id.activity_pohyb_tovar_selectedTovar)
    SearchableSpinner selectedTovarSpinner;

    @ViewById(R.id.activity_pohyb_tovar_transactionType_spinner)
    Spinner transactionTypeSpinner;

    @ViewById(R.id.activity_pohyb_tovar_miestnostFromSpinner)
    SearchableSpinner miestnostFromSpinner;

    @ViewById(R.id.activity_pohyb_tovar_miestnostToSpinner)
    SearchableSpinner miestnostToSpinner;

    @ViewById(R.id.activity_pohyb_tovar_inputLayout_pocetKusov)
    TextInputLayout inputLayout_pocetKusov;

    @ViewById(R.id.activity_pohyb_tovar_detail_pocetKusov)
    EditText pocetKusov;

    @ViewById(R.id.detailView_Image)
    ImageView tovarImage;

    List<Tovar> list_tovary = new ArrayList();
    List<TypTransakcie> list_typTransakcie = new ArrayList();
    List<Miestnost> list_miestnost = new ArrayList();

    //user selections
    TypTransakcie transactionType;
    Tovar selectedTovar;
    Miestnost miestnostFrom;
    Miestnost miestnostTo;


//    @Extra("EXTRA_POHYB")
    Pohyb passedInPohyb;

    @AfterViews
    void AfterView() {
        Dao<Tovar, Integer> tovarDAO = getHelper().TovarDAO();
        Dao<TypTransakcie, Integer> typTransakcieDAO = getHelper().TypTransakcieDAO();
        Dao<Miestnost, Integer> miestnostDAO = getHelper().MiestnostDAO();

        try {
            list_tovary = tovarDAO.queryForAll();
            list_typTransakcie = typTransakcieDAO.queryForAll();
            list_miestnost = miestnostDAO.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //setup toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupAdapters();
    }

    private void setupAdapters() {
        ArrayAdapter<Tovar> adapter = new ArrayAdapter<Tovar>(this, android.R.layout.simple_dropdown_item_1line, list_tovary);
        selectedTovarSpinner.setAdapter(adapter);

        ArrayAdapter<TypTransakcie> adapter_spinner = new ArrayAdapter<TypTransakcie>(this, android.R.layout.simple_dropdown_item_1line, list_typTransakcie);
        transactionTypeSpinner.setAdapter(adapter_spinner);
        transactionTypeSpinner.setSelection(0);

        ArrayAdapter<Miestnost> miestnostAdapter = new ArrayAdapter<Miestnost>(this, android.R.layout.simple_dropdown_item_1line, list_miestnost);
        miestnostFromSpinner.setAdapter(miestnostAdapter);
        miestnostToSpinner.setAdapter(miestnostAdapter);
    }


    private boolean validateUserEntries() {
        if (pocetKusov.getText().toString().equals("")) {
            inputLayout_pocetKusov.setError("Musite zadat hodnotu vacsiu ako 0");
            return false;
        }

        return true;
    }

    @OptionsItem(R.id.menu_item_save)
    void saveButtonClicked() {
        //validations
        if (!validateUserEntries()) return;

        if (passedInPohyb == null)
            passedInPohyb = new Pohyb();

        if (transactionType.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Delete))) {
            //remove item from designated room
            passedInPohyb.setMiestnostFrom(null);
        }

        if (transactionType.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Add))) {
            //add item to designated room
            passedInPohyb.setMiestnostFrom(null);
        }


        passedInPohyb.setPocetKusov(Integer.valueOf(pocetKusov.getText().toString()));
        passedInPohyb.setTypPohybu(transactionType);
        passedInPohyb.setTovar(selectedTovar);
        passedInPohyb.setDatum(new Date());
        passedInPohyb.setMiestnostTo(miestnostTo);

        Dao<Pohyb, Integer> pohybDAO = getHelper().PohybDAO();

        try {
            if (passedInPohyb.getId() == 0) {
                //new entry
                pohybDAO.create(passedInPohyb);
            } else {
                //already existing entry
                pohybDAO.update(passedInPohyb);
            }

            this.finish();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @ItemSelect(R.id.activity_pohyb_tovar_transactionType_spinner)
    public void transactionTypeChanged(boolean selected, TypTransakcie typTransakcie) {
        transactionType = typTransakcie;

        if (typTransakcie.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Delete))) {
            //remove item from designated room
            layout_miestnostFrom.setVisibility(View.GONE);
        }

        if (typTransakcie.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Add))) {
            //add item to designated room
            layout_miestnostFrom.setVisibility(View.GONE);
        }

        if (typTransakcie.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Move))) {
            //move item from one room to another
            layout_miestnostFrom.setVisibility(View.VISIBLE);
        }

    }

    @ItemSelect(R.id.activity_pohyb_tovar_selectedTovar)
    public void tovarSelectionChanged(boolean selected, Tovar tovar) {
        selectedTovar = tovar;
        tovarImage.setImageBitmap(ImageHelpers.convertBytesToBitmap(tovar.getFotografia()));
    }

    @ItemSelect(R.id.activity_pohyb_tovar_miestnostFromSpinner)
    public void miestnostFromChanged(boolean selected, Miestnost miestnost) {
        miestnostFrom = miestnost;
    }

    @ItemSelect(R.id.activity_pohyb_tovar_miestnostToSpinner)
    public void miestnostToChanged(boolean selected, Miestnost miestnost) {
        miestnostTo = miestnost;
    }
}
