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

@DatabaseTable(tableName = "Budova")
public class Budova extends BaseObservable implements Parcelable, IEditableRecyclerItem, IFilterableItem {
    @DatabaseField(columnName = "Id", generatedId = true)
    private int Id;
    @DatabaseField
    private String Nazov;

    //BUDOVA  ID, NAZOV

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

    public Budova() {
    }

    public Budova(Parcel in) {
        this.Id = in.readInt();
        this.Nazov = in.readString();
        this.Selected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Budova> CREATOR = new Parcelable.Creator<Budova>() {

        @Override
        public Budova createFromParcel(Parcel parcel) {
            return new Budova(parcel);
        }

        @Override
        public Budova[] newArray(int i) {
            return new Budova[i];
        }
    };

    public void CopyData(Budova otherItem) {
        this.Nazov = otherItem.Nazov;
        this.Selected = otherItem.Selected;
    }
}
