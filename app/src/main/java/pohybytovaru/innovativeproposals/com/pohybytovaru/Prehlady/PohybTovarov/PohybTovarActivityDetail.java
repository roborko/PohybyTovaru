package pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.PohybTovarov;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.j256.ormlite.dao.Dao;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Database.DatabaseHelper;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.OrmLiteAppCompatActivity;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Miestnost;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Tovar;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.TypTransakcie;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;

@EActivity(R.layout.activity_pohyb_tovar_detail)
public class PohybTovarActivityDetail extends OrmLiteAppCompatActivity<DatabaseHelper> {

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

    @ViewById(R.id.detailView_Image)
    ImageView tovarImage;

    List<Tovar> list_tovary = new ArrayList();
    List<TypTransakcie> list_typTransakcie = new ArrayList();
    List<Miestnost> list_miestnost = new ArrayList();

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

    @ItemSelect(R.id.activity_pohyb_tovar_transactionType_spinner)
    public void transactionTypeChanged(boolean selected, TypTransakcie typTransakcie) {
        Log.i("", "transactionTypeChanged: ");
    }

    @ItemSelect(R.id.activity_pohyb_tovar_selectedTovar)
    public void tovarSelectionChanged(boolean selected, Tovar tovar) {

        if (tovar.getFotografia() == null || tovar.getFotografia().length == 0) {
            tovarImage.setImageBitmap(null);
            return;
        }

        ByteArrayInputStream imageStream = new ByteArrayInputStream(tovar.getFotografia());
        Bitmap resultImage = BitmapFactory.decodeStream(imageStream);

        tovarImage.setImageBitmap(resultImage);

    }
}
