package pohybytovaru.innovativeproposals.com.pohybytovaru.Models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.databinding.library.baseAdapters.BR;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.IEditableRecyclerItem;

/**
 * Created by Robert on 14.01.2018.
 */
@DatabaseTable(tableName = "Miestnost")
public class Miestnost extends BaseObservable implements Parcelable, IEditableRecyclerItem {
    @DatabaseField(columnName = "Id", generatedId = true)
    private int Id;
    @DatabaseField
    private String Nazov;
    @DatabaseField
    private boolean JeSklad;

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

    public boolean isJeSklad() {
        return JeSklad;
    }

    public void setJeSklad(boolean jeSklad) {
        JeSklad = jeSklad;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public boolean filterFunctionResult(String searchString) {
        return this.Nazov.toLowerCase().contains(searchString.toLowerCase().trim());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeString(Nazov);
        parcel.writeByte((byte) (JeSklad ? 1 : 0));
        parcel.writeByte((byte) (Selected ? 1 : 0));
    }

    public Miestnost() {
    }

    public Miestnost(Parcel in) {
        this.Id = in.readInt();
        this.Nazov = in.readString();
        this.JeSklad = in.readByte() != 0;
        this.Selected= in.readByte() != 0;
    }

    public static final Parcelable.Creator<Miestnost> CREATOR = new Parcelable.Creator<Miestnost>() {

        @Override
        public Miestnost createFromParcel(Parcel parcel) {
            return new Miestnost(parcel);
        }

        @Override
        public Miestnost[] newArray(int i) {
            return new Miestnost[i];
        }
    };

    public void CopyData(Miestnost otherItem){
        this.Nazov = otherItem.Nazov;
        this.JeSklad = otherItem.JeSklad;
        this.Selected = otherItem.Selected;
    }
}
