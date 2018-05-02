package pohybytovaru.innovativeproposals.com.pohybytovaru.Models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.widget.ImageView;

import com.android.databinding.library.baseAdapters.BR;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.SimpleDateFormat;
import java.util.Date;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.ImageHelpers;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.IEditableRecyclerItem;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.IFilterableItem;

/**
 * Created by Robert on 14.01.2018.
 */

@DatabaseTable(tableName = "Pohyb")
public class Pohyb extends BaseObservable implements IEditableRecyclerItem, IFilterableItem {
    @DatabaseField(columnName = "Id", generatedId = true)
    private int Id;

    @DatabaseField(columnName = "TypPohybu", foreign = true, foreignAutoRefresh = true)
    private TypTransakcie TypPohybu;

    @DatabaseField(columnName = "MiestnostFrom", foreign = true, foreignAutoRefresh = true)
    private Miestnost MiestnostFrom;

    @DatabaseField(columnName = "MiestnostTo", foreign = true, foreignAutoRefresh = true)
    private Miestnost MiestnostTo;

    @DatabaseField(columnName = "Osoba", foreign = true, foreignAutoRefresh = true)
    private Osoba Osoba;

    @DatabaseField(columnName = "Tovar", foreign = true, foreignAutoRefresh = true)
    private Tovar Tovar;

    @DatabaseField
    private double PocetKusov;
    @DatabaseField
    private String Poznamka;
    @DatabaseField
    private Date Datum;


    private boolean Selected;

    @Bindable
    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean value) {
        Selected = value;
        notifyPropertyChanged(BR.selected);
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public TypTransakcie getTypPohybu() {
        return TypPohybu;
    }

    public void setTypPohybu(TypTransakcie typPohybu) {
        TypPohybu = typPohybu;
    }

    public Miestnost getMiestnostFrom() {
        return MiestnostFrom;
    }

    public void setMiestnostFrom(Miestnost miestnost) {
        MiestnostFrom = miestnost;
    }

    public Miestnost getMiestnostTo() {
        return MiestnostTo;
    }

    public void setMiestnostTo(Miestnost miestnost) {
        MiestnostTo = miestnost;
    }


    public Osoba getOsoba() {
        return Osoba;
    }

    public void setOsoba(Osoba osoba) {
        Osoba = osoba;
    }

    public Tovar getTovar() {
        return Tovar;
    }

    public void setTovar(Tovar tovar) {
        Tovar = tovar;
    }

    public double getPocetKusov() {
        return PocetKusov;
    }

    public void setPocetKusov(double pocetKusov) {
        PocetKusov = pocetKusov;
    }

    public String getPoznamka() {
        return Poznamka;
    }

    public void setPoznamka(String poznamka) {
        Poznamka = poznamka;
    }

    public Date getDatum() {
        return Datum;
    }

    public void setDatum(Date datum) {
        Datum = datum;
    }

    @Override
    public boolean filterFunctionResult(String searchString) {
        if(this.MiestnostFrom == null || this.MiestnostTo == null || this.Tovar == null)
            return false;

        return this.MiestnostFrom.getNazov().toLowerCase().contains(searchString.toLowerCase().trim()) ||
                this.MiestnostTo.getNazov().toLowerCase().contains(searchString.toLowerCase().trim()) ||
                this.Tovar.getNazov().toLowerCase().contains(searchString.toLowerCase().trim());
    }



//    //adapter used to deserialize byte array of images to imageview
//    @BindingAdapter({"bind:imageSource"})
//    public static void loadImage(ImageView view, int imageId){
//        view.setImageResource(imageId);
//    }
}
