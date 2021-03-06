package pohybytovaru.innovativeproposals.com.pohybytovaru.Models;

import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.android.databinding.library.baseAdapters.BR;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.ImageHelpers;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.IEditableRecyclerItem;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.IFilterableItem;


@DatabaseTable(tableName = "Tovar")
public class Tovar extends BaseObservable implements Parcelable, IEditableRecyclerItem, IFilterableItem {
    @DatabaseField(columnName = "Id", generatedId = true)
    private int Id;
    @DatabaseField
    private String Nazov;
    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] Fotografia;
    @DatabaseField
    private double MinimalneMnozstvo;
    @DatabaseField
    private String Poznamka;
    @DatabaseField
    private String KodTovaru;

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

    public String getNazov() {
        return Nazov;
    }

    public void setNazov(String nazov) {
        Nazov = nazov;
    }

    public String getPoznamka() {
        return Poznamka;
    }

    public void setPoznamka(String poznamka) {
        Poznamka = poznamka;
    }

    public byte[] getFotografia() {
        return Fotografia;
    }

    public void setFotografia(byte[] fotografia) {
        Fotografia = fotografia;
    }

    public double getMinimalneMnozstvo() {
        return MinimalneMnozstvo;
    }
    public void setMinimalneMnozstvo(double minimalneMnozstvo) {
        MinimalneMnozstvo = minimalneMnozstvo;
    }
    public String getKodTovaru() {
        return KodTovaru;
    }
    public void setKodTovaru(String kodTovaru) {
        KodTovaru = kodTovaru;
    }


    @Override
    public String toString() {
        return Nazov.toString();
    }

    @Override
    public boolean filterFunctionResult(String searchString) {
        return this.Nazov.toLowerCase().contains(searchString.toLowerCase().trim());
    }

    public Tovar() {
    }

    public Tovar(Parcel in) {
        this.Id = in.readInt();
        this.Nazov = in.readString();
        this.MinimalneMnozstvo = in.readDouble();
        this.Selected = in.readByte() != 0;
        this.Poznamka = in.readString();
        this.Fotografia = in.createByteArray();
        this.KodTovaru = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeString(Nazov);
        parcel.writeDouble(MinimalneMnozstvo);
        parcel.writeByte((byte) (Selected ? 1 : 0));
        parcel.writeString(Poznamka);
        parcel.writeByteArray(Fotografia);
        parcel.writeString(KodTovaru);
    }

    public static final Parcelable.Creator<Tovar> CREATOR = new Parcelable.Creator<Tovar>() {

        @Override
        public Tovar createFromParcel(Parcel parcel) {
            return new Tovar(parcel);
        }

        @Override
        public Tovar[] newArray(int i) {
            return new Tovar[i];
        }
    };


}
