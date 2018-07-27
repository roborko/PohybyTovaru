package pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.PohybTovarov;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Budovy.ThreeLevelExpandableListView;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Database.DatabaseHelper;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.ImageHelpers;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.OrmLiteAppCompatActivity;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.AktualneMnozstvo;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Miestnost;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Pohyb;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Tovar;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.TypTransakcie;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.MnozstvaTovarov.MnozstvaTovarovDataModel;


@EActivity(R.layout.activity_pohyb_tovar_detail)
@OptionsMenu(R.menu.menu_save)
public class PohybTovarActivityDetail extends OrmLiteAppCompatActivity<DatabaseHelper>  { // implements View.OnClickListener

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

    /*
    @ViewById(R.id.activity_pohyb_tovar_miestnostFromSpinner)
    SearchableSpinner miestnostFromSpinner;

    @ViewById(R.id.activity_pohyb_tovar_miestnostToSpinner)
    SearchableSpinner miestnostToSpinner; */


    @ViewById(R.id.activity_pohyb_tovar_inputLayout_pocetKusov)
    TextInputLayout inputLayout_pocetKusov;

    @ViewById(R.id.activity_pohyb_tovar_detail_pocetKusov)
    EditText pocetKusov;

    @ViewById(R.id.lbl_miestnostFrom)
    TextView lbMiestnostFrom;

    @ViewById(R.id.lbl_MiestnostTo)
    TextView lbMiestnostTo;

    @ViewById(R.id.lbl_miestnostFrom_pocetKusov)
    TextView miestnostFrom_povodnyPocetKusovTovaru;

    @ViewById(R.id.lbl_miestnostTo_pocetKusov)
    TextView miestnostTo_povodnyPocetKusovTovaru;

    @ViewById(R.id.productImage)
    ImageView tovarImage;

    @ViewById(R.id.btPohyb_tovar_miestnostFrom)
    Button btMiestnostFrom;

    @ViewById(R.id.btPohyb_tovar_miestnostTo)
    Button btMiestnostTo;

    List<Tovar> list_tovary = new ArrayList();
    List<TypTransakcie> list_typTransakcie = new ArrayList();
    List<Miestnost> list_miestnostFROM = new ArrayList();
    List<Miestnost> list_miestnostTO = new ArrayList();

    MnozstvaTovarovDataModel dm = new MnozstvaTovarovDataModel(this); // pri kopirovani do inej triedy zmen

    //user selections
    TypTransakcie transactionType;
    Tovar selectedTovar;
    Miestnost miestnostFrom = new Miestnost();
    Miestnost miestnostTo = new Miestnost();

    AktualneMnozstvo miestnostFromMnozstvo;
    AktualneMnozstvo miestnostToMnozstvo;

    ArrayAdapter<Tovar> tovarAdapter;
    Pohyb passedInPohyb;

    private ArrayAdapter<Miestnost> miestnostAdapterFROM;
    private ArrayAdapter<Miestnost> miestnostAdapterTO;
    private String header4Budovy = "";

    @AfterViews
    void AfterView() {
        Dao<Tovar, Integer> tovarDAO = getHelper().TovarDAO();
        Dao<TypTransakcie, Integer> typTransakcieDAO = getHelper().TypTransakcieDAO();
        Dao<Miestnost, Integer> miestnostDAO = getHelper().MiestnostDAO();

        try {
            list_tovary = tovarDAO.queryForAll();

            Collections.sort(list_tovary, new Comparator<Tovar>() {
                @Override
                public int compare(Tovar lhs, Tovar rhs) {
                    return lhs.getNazov().compareTo(rhs.getNazov());
                }
            });

            list_typTransakcie = typTransakcieDAO.queryForAll();

            list_miestnostFROM = dm.getMiestnostSInventarom(0); // bolo miestnostDAO.queryForAll();
            list_miestnostTO = dm.getMiestnostSInventarom(0); // bolo miestnostDAO.queryForAll();
            Collections.sort(list_miestnostFROM, new Comparator<Miestnost>() {
                @Override
                public int compare(Miestnost lhs, Miestnost rhs) {
                    return lhs.getNazov().compareTo(rhs.getNazov());
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //setup toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupAdapters();

    }

    public void onButtonMiestnostFromClick(View target) {
        Intent intent = new Intent(this, ThreeLevelExpandableListView.class);
        intent.putExtra("header", header4Budovy);
        startActivityForResult(intent, 1);
    }

    public void onButtonMiestnostToClick(View target) {
        Intent intent = new Intent(this, ThreeLevelExpandableListView.class);
        intent.putExtra("header", header4Budovy);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) { // requestCode pre miestnostFrom

            if (resultCode == Activity.RESULT_OK) {

                String[] umiestnenie = data.getStringArrayExtra("umiestnenie");
                lbMiestnostFrom.setText(umiestnenie[0] + "-" + umiestnenie[1] + "-" + umiestnenie[2]);

                // zisti id miestnostiFrom
                String[] myUmiestnenieId = dm.getIdMiestnosti(umiestnenie[0], umiestnenie[1], umiestnenie[2]);

                miestnostFrom.setIdBudova(Integer.parseInt( myUmiestnenieId[0]));
                miestnostFrom.setIdPoschodie(Integer.parseInt( myUmiestnenieId[1]));
                miestnostFrom.setId(Integer.parseInt( myUmiestnenieId[2]));
                miestnostFrom.setNazov(umiestnenie[0] + "-" + umiestnenie[1] + "-" + umiestnenie[2]);
                miestnostFromChanged(true, miestnostFrom);
            }
        }

        if (requestCode == 2) { // requestCode pre miestnostTo

            if (resultCode == Activity.RESULT_OK) {

                String[] umiestnenie = data.getStringArrayExtra("umiestnenie");
                lbMiestnostTo.setText(umiestnenie[0] + "-" + umiestnenie[1] + "-" + umiestnenie[2]);

                // zisti id miestnostiFrom
                String[] myUmiestnenieId = dm.getIdMiestnosti(umiestnenie[0], umiestnenie[1], umiestnenie[2]);

                miestnostTo.setIdBudova(Integer.parseInt( myUmiestnenieId[0])); // tu to padlo ked som mal rozkliknute obe budovyu aj s poschodiami a klikol som na nejaku miestnost v druhej budove
                miestnostTo.setIdPoschodie(Integer.parseInt( myUmiestnenieId[1]));
                miestnostTo.setId(Integer.parseInt( myUmiestnenieId[2]));
                miestnostTo.setNazov(umiestnenie[0] + "-" + umiestnenie[1] + "-" + umiestnenie[2]);

                miestnostToChanged(true, miestnostTo);
            }
        }
    }

    private void setupAdapters() {
        //ArrayAdapter<Tovar> adapter = new ArrayAdapter<Tovar>(this, android.R.layout.simple_dropdown_item_1line, list_tovary);
        tovarAdapter = new ArrayAdapter<Tovar>(this, android.R.layout.simple_dropdown_item_1line, list_tovary);
        selectedTovarSpinner.setAdapter(tovarAdapter);

        ArrayAdapter<TypTransakcie> adapter_spinner = new ArrayAdapter<TypTransakcie>(this, android.R.layout.simple_dropdown_item_1line, list_typTransakcie);
        transactionTypeSpinner.setAdapter(adapter_spinner);
        transactionTypeSpinner.setSelection(0);

        // TODO toto zamen za vlastny layout

        miestnostAdapterFROM = new ArrayAdapter<Miestnost>(this, android.R.layout.simple_dropdown_item_1line, list_miestnostFROM);
        miestnostAdapterTO = new ArrayAdapter<Miestnost>(this, android.R.layout.simple_dropdown_item_1line, list_miestnostTO);
       // miestnostFromSpinner.setAdapter(miestnostAdapterFROM);  PRVA ZAMENA !! a asi netreba ani adapter
       // miestnostToSpinner.setAdapter(miestnostAdapterTO); DRUHA ZAMENA !! a asi netreba ani adapter
    }

    private boolean validateUserEntries() {
        if (pocetKusov.getText().toString().equals("")) {
            inputLayout_pocetKusov.setError(getString(R.string.MusiteZadatHodnotuViacNezNula));
            return false;
        } else {
            inputLayout_pocetKusov.setError(null);
        }

        //validacia miestnosti do nwmoze byt povinne pre skartovanie
        if ((miestnostTo.getId() == 0) && (!transactionType.toString().equalsIgnoreCase(getString(R.string.TransactionType_Delete)))) {
            Toast.makeText(this, R.string.NajprvMusiteVybratMiestnostDo, Toast.LENGTH_SHORT).show();
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
            //try to retrieve aktualne mnozstvo v miestnosti TransactionType_Delete

            AktualneMnozstvo aktualneMnozstvo = dm.getAktualneMnozstvoTovaruZMiestnosti(selectedTovar.getId(),miestnostFrom.getId());

            if (aktualneMnozstvo == null || !canDeleteNumberOfItemsFromRoom(aktualneMnozstvo.getMnozstvo(), Double.valueOf(pocetKusov.getText().toString()))) {
                inputLayout_pocetKusov.setError("V miestnosti sa nachadza menej kusov !");
                return;
            }

            //remove item from designated room
            passedInPohyb.setMiestnostTo(null);
        }

        if (transactionType.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Add))) {
            //add item to designated room
            passedInPohyb.setMiestnostFrom(null);
        }

        if (transactionType.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Move))) {
            //validacia miestnosti z -> toto je povinne pre tento krok
            if (miestnostFrom.getId() == 0) { // bolo miestnostFrom == null)
                Toast.makeText(this, "Musite najprv vybrat miestnost 'Z'. ", Toast.LENGTH_SHORT).show();
                return;
            }

            //try to retrieve aktualne mnozstvo v miestnosti
            AktualneMnozstvo aktualneMnozstvo = dm.getAktualneMnozstvoTovaruZMiestnosti(selectedTovar.getId(),miestnostFrom.getId());

            if (aktualneMnozstvo == null || !canDeleteNumberOfItemsFromRoom(aktualneMnozstvo.getMnozstvo(), Double.valueOf(pocetKusov.getText().toString()))) {
                inputLayout_pocetKusov.setError("V miestnosti sa nachadza menej kusov !");
                return;
            }

            //add item to designated room
            passedInPohyb.setMiestnostFrom(miestnostFrom);
        }

        if (transactionType.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Inventory))) {
            // pri inventure prepis mnozstvo
            passedInPohyb.setMiestnostFrom(null);
        }

        passedInPohyb.setPocetKusov(Integer.valueOf(pocetKusov.getText().toString()));
        passedInPohyb.setTypPohybu(transactionType);
        passedInPohyb.setTovar(selectedTovar);
        passedInPohyb.setDatum(new Date());

        if (transactionType.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Delete)))
            passedInPohyb.setMiestnostFrom(miestnostFrom);
        else
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
            if ((miestnostToMnozstvo == null) && (!transactionType.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Delete)))) {
                //new entry, can only be +
                AktualneMnozstvo mnozstvoTo = new AktualneMnozstvo();
                mnozstvoTo.setTovar(selectedTovar);
                mnozstvoTo.setMiestnost(miestnostTo);
                mnozstvoTo.setMnozstvo(passedInPohyb.getPocetKusov());
                aktualneMnozstvoDAO.create(mnozstvoTo);
            } else {
                //entry already exists, check if delete or add

                if (transactionType.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Add))) {
                    //substract from original
                    miestnostToMnozstvo.setMnozstvo(miestnostToMnozstvo.getMnozstvo() + passedInPohyb.getPocetKusov());
                }

                if (transactionType.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Inventory))) {
                    //nahrad
                    miestnostToMnozstvo.setMnozstvo( passedInPohyb.getPocetKusov());
                }

                //update
                if (transactionType.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Delete)))
                    //aktualneMnozstvoDAO.update(miestnostFromMnozstvo);
                    dm.setMnozstvoTovaruVMiestnosti(selectedTovar.getId(), miestnostFrom.getId(), miestnostFromMnozstvo.getMnozstvo());
                else
                    //aktualneMnozstvoDAO.update(miestnostToMnozstvo);
                    dm.setMnozstvoTovaruVMiestnosti(selectedTovar.getId(), miestnostTo.getId(), miestnostToMnozstvo.getMnozstvo());
            }

            //check for MOVE
            if (passedInPohyb.getMiestnostFrom() != null) {
                if (miestnostFromMnozstvo != null) {
                    //aktualne mnozstvo musi byt zadefinovane, inac nemoze povolit odobrat z danej miestnosti!
                    miestnostFromMnozstvo.setMnozstvo(miestnostFromMnozstvo.getMnozstvo() - passedInPohyb.getPocetKusov());
                    //aktualneMnozstvoDAO.update(miestnostFromMnozstvo);
                    dm.setMnozstvoTovaruVMiestnosti(selectedTovar.getId(), miestnostFrom.getId(), miestnostFromMnozstvo.getMnozstvo());

                    if(miestnostToMnozstvo != null) // pri skartacii je to null
                    {
                        miestnostToMnozstvo.setMnozstvo(miestnostToMnozstvo.getMnozstvo() + passedInPohyb.getPocetKusov());
                       // aktualneMnozstvoDAO.update(miestnostToMnozstvo);
                        dm.setMnozstvoTovaruVMiestnosti(selectedTovar.getId(), miestnostTo.getId(), miestnostToMnozstvo.getMnozstvo());
                    }
                }
            }

            this.finish();

            // TODO sem daj kontrolu na pminimalne mnozstvo alebo nula (celkove)


        } catch (SQLException e) {
            e.printStackTrace();
        }
        // aktualizuj list v PohybTovarActivity

        //ukonci aktualnu aktivitu

        // x Pohyb result = data.getParcelableExtra("EXTRA_MIESTNOST");
        //Intent returnIntent = new Intent();
        // returnIntent.putExtra("EXTRA_MIESTNOST", inventar);

    }

    @ItemSelect(R.id.activity_pohyb_tovar_transactionType_spinner)
    public void transactionTypeChanged(boolean selected, TypTransakcie typTransakcie) {

        transactionType = typTransakcie;

        if (typTransakcie.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Delete))) {
            //remove item from designated room
            layout_miestnostTo.setVisibility(View.GONE);
            layout_miestnostFrom.setVisibility(View.VISIBLE);
            btMiestnostFrom.setText(R.string.v_miestnosti);
            header4Budovy = "Miestnosť pre skartáciu";
        }

        if (typTransakcie.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Add)))  {
            //add item to designated room
            layout_miestnostTo.setVisibility(View.VISIBLE); // pridal som
            layout_miestnostFrom.setVisibility(View.GONE);
            btMiestnostTo.setText(R.string.do_miestnosti);
            header4Budovy = "Miestnosť pre príjem";

        }

        if ( typTransakcie.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Inventory))) {
            //set quantity of item to designated room
            layout_miestnostTo.setVisibility(View.VISIBLE); // pridal som
            layout_miestnostFrom.setVisibility(View.GONE);
            btMiestnostTo.setText(R.string.v_miestnosti);
            header4Budovy = "Miestnosť pre inventúru";
        }

        if (typTransakcie.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Move))) {
            //move item from one room to another
            layout_miestnostFrom.setVisibility(View.VISIBLE);
            btMiestnostFrom.setText(R.string.z_miestnosti);
            btMiestnostTo.setText(R.string.do_miestnosti);
            header4Budovy = "Miestnosť pre výdaj";
        }
    }

    @ItemSelect(R.id.activity_pohyb_tovar_selectedTovar)
    public void tovarSelectionChanged(boolean selected, Tovar tovar) {
        selectedTovar = tovar;

        // TODO list_miestnost podla transakcie
        //        - pri presune, inventure a skartovani ponukni len tie miestnosti, kde sa nachadza mnozstvo > 0
        //                - miestnost do nemoze byt taka ista ako miestnost z
        //   list_miestnost = getMiestnostSInventarom();

        String myTransakcia = transactionTypeSpinner.getSelectedItem().toString();

        if (myTransakcia.equals(getString(R.string.TransactionType_Move)) || myTransakcia.equals(getString(R.string.TransactionType_Inventory ))
                || myTransakcia.equals(getString(R.string.TransactionType_Remove))) {

            list_miestnostFROM = dm.getMiestnostSInventarom(selectedTovar.getId());

        } else

            list_miestnostFROM = dm.getMiestnostSInventarom(0);

        miestnostAdapterFROM.clear();
        miestnostAdapterFROM.addAll(list_miestnostFROM);


        Bitmap resultImage = ImageHelpers.convertBytesToBitmap(tovar.getFotografia());
        if (resultImage == null) {
            tovarImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_do_not_disturb_alt_black_18dp));
        } else {
            tovarImage.setImageBitmap(resultImage);
        }
        //trigger events to recalculate aktualne mnozstvo

        // todo zamen toto: miestnostFromChanged(true, miestnostFrom);

        miestnostFromChanged(true, miestnostFrom);
        miestnostToChanged(true, miestnostTo);
    }

   // @ItemSelect(R.id.activity_pohyb_tovar_miestnostFromSpinner)
    //@ItemSelect(R.id.lbl_miestnostFrom)
    public void miestnostFromChanged(boolean selected, Miestnost miestnost) {

        miestnostFrom = miestnost;
        //Tovar myTovar = (Tovar) selectedTovarSpinner.getSelectedItem();

        // Tovar kde = tovarAdapter.getItem(selectedTovarSpinner.getSelectedItem());

        if (miestnostFrom.getId() == 0 || selectedTovar == null) return;
        // if (miestnostFrom == null || myTovar == null) return;

        // getAktualneMnozstvoToavruZMiestnosti
        miestnostFromMnozstvo = dm.getAktualneMnozstvoTovaruZMiestnosti(selectedTovar.getId(),miestnostFrom.getId());

        if (miestnostFromMnozstvo == null) {
            miestnostFrom_povodnyPocetKusovTovaru.setText("0");
        } else {
            miestnostFrom_povodnyPocetKusovTovaru.setText(String.valueOf(miestnostFromMnozstvo.getMnozstvo()));
        }

        // TODO v zozname miestnostTo vyhod zo zoznamu miestnostFrom
        list_miestnostTO = dm.getMiestnostSInventarom(0);
        // vyhod miestnostFROM

        for (int i = list_miestnostTO.size() - 1; i >= 0; i--) {
            //if (list_miestnostTO.get(i).contains("foo")) {
            if (list_miestnostTO.get(i).getNazov().equalsIgnoreCase(miestnostFrom.getNazov())) {
                list_miestnostTO.remove(i);
            }
        }

        miestnostAdapterTO.clear();
        miestnostAdapterTO.addAll(list_miestnostTO);

        // pokial prijem, intentura alebo skartovanie tak focusuj
        if ((transactionType.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Add))) ||
                (transactionType.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Inventory))) ||
                        (transactionType.getINTERNAL_NAME().equals(getString(R.string.TransactionType_Delete)))) {

            pocetKusov.requestFocus();

            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//Hide:
         //   imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//Show
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
    }

  //  @ItemSelect(R.id.activity_pohyb_tovar_miestnostToSpinner)
 // @ItemSelect(R.id.lbl_MiestnostTo)
    public void miestnostToChanged(boolean selected, Miestnost miestnost) {
        miestnostTo = miestnost;

        if (miestnostTo.getId() == 0 || selectedTovar == null) return;

        miestnostToMnozstvo = dm.getAktualneMnozstvoTovaruZMiestnosti(selectedTovar.getId(),miestnostTo.getId());

        if (miestnostToMnozstvo == null) {
            miestnostTo_povodnyPocetKusovTovaru.setText("0");
        } else {
            miestnostTo_povodnyPocetKusovTovaru.setText(String.valueOf(miestnostToMnozstvo.getMnozstvo()));
        }

        // tu vzdy focusuj
        pocetKusov.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//Hide:
      //  imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//Show
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

    }

    // TODO prepisat
    private AktualneMnozstvo tryGetAktualneMnozstvoVYMAZ(ForeignCollection<AktualneMnozstvo> mnozstva, int tovarId) {

        for (AktualneMnozstvo aktualneMnozstvo : mnozstva) {
            if (aktualneMnozstvo.getTovar().getId() == tovarId) {
                return aktualneMnozstvo;
            }
        }

        return null;
    }



}
