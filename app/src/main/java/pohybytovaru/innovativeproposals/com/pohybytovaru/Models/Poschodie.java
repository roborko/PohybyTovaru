package pohybytovaru.innovativeproposals.com.pohybytovaru.Models;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.databinding.library.baseAdapters.BR;


import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.IEditableRecyclerItem;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.IFilterableItem;

@DatabaseTable(tableName = "Poschodie")
public class Poschodie extends BaseObservable implements Parcelable, IEditableRecyclerItem, IFilterableItem {
    @DatabaseField(columnName = "Id", generatedId = true)
    private int Id;
    @DatabaseField(columnName = "IdBudova")
    private int IdBudova;
    @DatabaseField
    private String Nazov;


    //POSCHODIE  :     ID, IDBUDOVA, NAZOV

    private boolean Selected;

    @Bindable
    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean selected) {
        Selected = selected;
        notifyPropertyChanged(BR.selected);
    }

    public int getId() {
        return Id;
    }

    public void setIdBudova(int idbudova) {
        IdBudova = idbudova;
    }

    public int getIdBudova() {
        return IdBudova;
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

    @Override
    public int describeContents() {
        return 0;
    }


    public boolean filterFunctionResult(String searchString) {
        return this.Nazov.toLowerCase().contains(searchString.toLowerCase().trim());
    }

    @Override
    public String toString() {
        return Nazov.toString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeString(Nazov);
        parcel.writeByte((byte) (Selected ? 1 : 0));
    }

    public Poschodie() {
    }

    public Poschodie(Parcel in) {
        this.Id = in.readInt();
        this.Nazov = in.readString();
        this.Selected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Poschodie> CREATOR = new Parcelable.Creator<Poschodie>() {

        @Override
        public Poschodie createFromParcel(Parcel parcel) {
            return new Poschodie(parcel);
        }

        @Override
        public Poschodie[] newArray(int i) {
            return new Poschodie[i];
        }
    };

    public void CopyData(Poschodie otherItem) {
        this.Nazov = otherItem.Nazov;
        this.Selected = otherItem.Selected;
    }
}

