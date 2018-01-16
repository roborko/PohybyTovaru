package pohybytovaru.innovativeproposals.com.pohybytovaru.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Robert on 14.01.2018.
 */
@DatabaseTable(tableName = "Miestnost")
public class Miestnost implements Parcelable {
    @DatabaseField(columnName = "Id", generatedId = true)
    private int Id;
    @DatabaseField
    private String Nazov;
    @DatabaseField
    private boolean JeSklad;

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

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeString(Nazov);
        parcel.writeByte((byte) (JeSklad ? 1 : 0));
    }

    public Miestnost() {
    }

    public Miestnost(Parcel in) {
        this.Id = in.readInt();
        this.Nazov = in.readString();
        this.JeSklad = in.readByte() != 0;
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
}
