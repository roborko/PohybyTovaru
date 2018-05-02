package pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.PohybTovarov;

import android.graphics.Bitmap;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
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
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.AktualneMnozstvo;
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

    @ViewById(R.id.lbl_miestnostFrom_pocetKusov)
    TextView miestnostFrom_povodnyPocetKusovTovaru;

    @ViewById(R.id.lbl_miestnostTo_pocetKusov)
    TextView miestnostTo_povodnyPocetKusovTovaru;

    @ViewById(R.id.productImage)
    ImageView tovarImage;

    List<Tovar> list_tovary = new ArrayList();
    List<TypTransakcie> list_typTransakcie = new ArrayList();
    List<Miestnost> list_miestnost = new ArrayList();

    //user selections
    TypTransakcie transactionType;
    Tovar selectedTovar;
    Miestnost miestnostFrom;
    Miestnost miestnostTo;

    AktualneMnozstvo miestnostFromMnozstvo;
    AktualneMnozstvo miestnostToMnozstvo;

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
            inputLayout_pocetKusov.setError(getString(R.string.MusiteZadatHodnotuViacNezNula));
            return false;
        } else {
            inputLayout_pocetKusov.setError(null);
        }

        //validacia miestnosti do -> toto je povinne
        if (miestnostTo == null) {
            Toast.makeText(this, R.string.NajprvMusiteVybratMiestnost, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //boolean to determine if we can remove items from room
    private boolean canDeleteNumberOfItemsFromRoom(double original, double newValue) {
        return original - newValue >= 0;
    }

    @OptionsItem(R.id.menu_item_save)
    void saveButtonClicked() {
        //validations
        if (!validateUserEntries()) return;

        if (passedInPohyb == null)
            passedInPohyb = new Pohyb();

        if (transactionType.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Delete))) {
            //try to retrieve aktualne mnozstvo v miestnosti
            AktualneMnozstvo aktualneMnozstvo = tryGetAktualneMnozstvo(miestnostTo.AktualneMnozstvo(), selectedTovar.getId());

            if (aktualneMnozstvo == null || !canDeleteNumberOfItemsFromRoom(aktualneMnozstvo.getMnozstvo(), Double.valueOf(pocetKusov.getText().toString()))) {
                inputLayout_pocetKusov.setError("Nemozete odstranit viac poloziek, ako sa ma v miestnosti povodne nachadzat.");
                return;
            }

            //remove item from designated room
            passedInPohyb.setMiestnostFrom(null);
        }

        if (transactionType.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Add))) {
            //add item to designated room
            passedInPohyb.setMiestnostFrom(null);
        }

        if (transactionType.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Move))) {
            //validacia miestnosti z -> toto je povinne pre tento krok
            if (miestnostFrom == null) {
                Toast.makeText(this, "Musite najprv vybrat miestnost 'Z'. ", Toast.LENGTH_SHORT).show();
                return;
            }

            //try to retrieve aktualne mnozstvo v miestnosti
            AktualneMnozstvo aktualneMnozstvo = tryGetAktualneMnozstvo(miestnostFrom.AktualneMnozstvo(), selectedTovar.getId());

            if (aktualneMnozstvo == null || !canDeleteNumberOfItemsFromRoom(aktualneMnozstvo.getMnozstvo(), Double.valueOf(pocetKusov.getText().toString()))) {
                inputLayout_pocetKusov.setError("Nemozete presunut viac poloziek, ako sa povodne v miestnosti nachadza.");
                return;
            }


            //add item to designated room
            passedInPohyb.setMiestnostFrom(miestnostFrom);
        }

        passedInPohyb.setPocetKusov(Integer.valueOf(pocetKusov.getText().toString()));
        passedInPohyb.setTypPohybu(transactionType);
        passedInPohyb.setTovar(selectedTovar);
        passedInPohyb.setDatum(new Date());
        passedInPohyb.setMiestnostTo(miestnostTo);

        Dao<Pohyb, Integer> pohybDAO = getHelper().PohybDAO();
        Dao<AktualneMnozstvo, Integer> aktualneMnozstvoDAO = getHelper().AktualneMnozstvoDAO();

        try {
            if (passedInPohyb.getId() == 0) {
                //new entry
                pohybDAO.create(passedInPohyb);

            } else {
                //already existing entry
                pohybDAO.update(passedInPohyb);
            }

            //calculate aktualne mnozstvo To
            if (miestnostToMnozstvo == null) {
                //new entry, can only be +
                AktualneMnozstvo mnozstvoTo = new AktualneMnozstvo();
                mnozstvoTo.setTovar(selectedTovar);
                mnozstvoTo.setMiestnost(miestnostTo);
                mnozstvoTo.setMnozstvo(passedInPohyb.getPocetKusov());
                aktualneMnozstvoDAO.create(mnozstvoTo);
            } else {
                //entry already exists, check if delete or add
                if (transactionType.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Delete))) {
                    //substract from original
                    miestnostToMnozstvo.setMnozstvo(miestnostToMnozstvo.getMnozstvo() - passedInPohyb.getPocetKusov());
                } else {
                    miestnostToMnozstvo.setMnozstvo(miestnostToMnozstvo.getMnozstvo() + passedInPohyb.getPocetKusov());
                }

                //update
                aktualneMnozstvoDAO.update(miestnostToMnozstvo);
            }

            //check for MOVE
            if (passedInPohyb.getMiestnostFrom() != null) {
                if (miestnostFromMnozstvo != null) {
                    //aktualne mnozstvo musi byt zadefinovane, inac nemoze povolit odobrat z danej miestnosti!
                    miestnostFromMnozstvo.setMnozstvo(miestnostFromMnozstvo.getMnozstvo() - passedInPohyb.getPocetKusov());

                    //update
                    aktualneMnozstvoDAO.update(miestnostFromMnozstvo);
                }
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
            //activity_pohyb_tovar_layout_miestnostFrom.set
        } else {
            layout_miestnostFrom.setVisibility(View.VISIBLE);
        }

        if (typTransakcie.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Move))) {
            //move item from one room to another
            layout_miestnostFrom.setVisibility(View.VISIBLE);
        }
    }

    @ItemSelect(R.id.activity_pohyb_tovar_selectedTovar)
    public void tovarSelectionChanged(boolean selected, Tovar tovar) {
        selectedTovar = tovar;

        Bitmap resultImage = ImageHelpers.convertBytesToBitmap(tovar.getFotografia());
        if (resultImage == null) {
            tovarImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_do_not_disturb_alt_black_18dp));
        } else {
            tovarImage.setImageBitmap(resultImage);
        }
        //trigger events to recalculate aktualne mnozstvo
        miestnostFromChanged(true, miestnostFrom);
        miestnostToChanged(true, miestnostTo);
    }

    @ItemSelect(R.id.activity_pohyb_tovar_miestnostFromSpinner)
    public void miestnostFromChanged(boolean selected, Miestnost miestnost) {
        miestnostFrom = miestnost;

        if (miestnostFrom == null || selectedTovar == null) return;
        miestnostFromMnozstvo = tryGetAktualneMnozstvo(miestnostFrom.AktualneMnozstvo(), selectedTovar.getId());

        if (miestnostFromMnozstvo == null) {
            miestnostFrom_povodnyPocetKusovTovaru.setText("0");
        } else {
            miestnostFrom_povodnyPocetKusovTovaru.setText(String.valueOf(miestnostFromMnozstvo.getMnozstvo()));
        }
    }

    @ItemSelect(R.id.activity_pohyb_tovar_miestnostToSpinner)
    public void miestnostToChanged(boolean selected, Miestnost miestnost) {
        miestnostTo = miestnost;
        if (miestnostTo == null || selectedTovar == null) return;
        miestnostToMnozstvo = tryGetAktualneMnozstvo(miestnostTo.AktualneMnozstvo(), selectedTovar.getId());

        if (miestnostToMnozstvo == null) {
            miestnostTo_povodnyPocetKusovTovaru.setText("0");
        } else {
            miestnostTo_povodnyPocetKusovTovaru.setText(String.valueOf(miestnostToMnozstvo.getMnozstvo()));
        }
    }

    private AktualneMnozstvo tryGetAktualneMnozstvo(ForeignCollection<AktualneMnozstvo> mnozstva, int tovarId) {
        for (AktualneMnozstvo aktualneMnozstvo : mnozstva) {
            if (aktualneMnozstvo.getTovar().getId() == tovarId) {
                return aktualneMnozstvo;
            }
        }

        return null;
    }
}
