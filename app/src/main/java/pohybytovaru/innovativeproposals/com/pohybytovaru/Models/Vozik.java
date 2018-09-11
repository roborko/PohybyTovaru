package pohybytovaru.innovativeproposals.com.pohybytovaru.Models;

import android.databinding.Bindable;
import com.android.databinding.library.baseAdapters.BR;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;
import com.j256.ormlite.dao.ForeignCollection;

import com.j256.ormlite.field.ForeignCollectionField;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.IEditableRecyclerItem;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.IFilterableItem;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.IEditableRecyclerItem;



@DatabaseTable(tableName = "Vozik")
public class Vozik extends BaseObservable implements Parcelable, IEditableRecyclerItem, IFilterableItem  {

    @DatabaseField(columnName = "Id", generatedId = true)
    private int Id;
    @DatabaseField
    private String Name;
    @DatabaseField
    private String Kod;

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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getKod() {
        return Kod;
    }

    public void setKod(String kod) {
        Kod = kod;
    }

    // a este toto musi byt pre Observe

    @Override
    public boolean filterFunctionResult(String searchString) {
        return this.Name.toLowerCase().contains(searchString.toLowerCase().trim());
    }

    public Vozik() {
    }

    public Vozik(Parcel in) {
        this.Id = in.readInt();
        this.Name = in.readString();
        this.Selected = in.readByte() != 0;
        this.Kod = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeString(Name);
        parcel.writeByte((byte) (Selected ? 1 : 0));
        parcel.writeString(Kod);
    }

    public static final Parcelable.Creator<Vozik> CREATOR = new Parcelable.Creator<Vozik>() {

        @Override
        public Vozik createFromParcel(Parcel parcel) {
            return new Vozik(parcel);
        }

        @Override
        public Vozik[] newArray(int i) {
            return new Vozik[i];
        }
    };



}