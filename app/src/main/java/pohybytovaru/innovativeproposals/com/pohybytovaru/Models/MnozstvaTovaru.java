package pohybytovaru.innovativeproposals.com.pohybytovaru.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class MnozstvaTovaru implements Parcelable {
    private int Id;
    private String tovar;
    private String miestnost;
    private double mnozstvo;
    private double limitne_mnozstvo; // sem dotahujeme hodnoty z tovaru
    private byte[] fotografia;

    public int getId() {
        return Id;
    }
    public void setId(int id) {
        Id = id;
    }

    public String getTovar() {
        return tovar;
    }
    public void setTovar(String tovar) {
        this.tovar = tovar;
    }

    public String getMiestnost() {
        return miestnost;
    }
    public void setMiestnost(String miestnost) {
        this.miestnost = miestnost;
    }

    public double getMnozstvo() {
        return mnozstvo;
    }
    public void setMnozstvo(double mnozstvo) {
        this.mnozstvo = mnozstvo;
    }

    public double getLimitne_mnozstvo() {
        return limitne_mnozstvo;
    }

    public void setLimitne_mnozstvo(double limitne_mnozstvo) {
        this.limitne_mnozstvo = limitne_mnozstvo;
    }

    public byte[] getFotografia() {
        return fotografia;
    }
    public void setFotografia(byte[] fotografia) {
        this.fotografia = fotografia;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(tovar);
        dest.writeString(miestnost);
        dest.writeDouble(mnozstvo) ;
        dest.writeDouble(limitne_mnozstvo) ;
        dest.writeByteArray(fotografia);
    }

    public static final Parcelable.Creator<MnozstvaTovaru> CREATOR = new Parcelable.Creator<MnozstvaTovaru>() {

        @Override
        public MnozstvaTovaru createFromParcel(Parcel source) {
            return new MnozstvaTovaru(source);
        }
        @Override
        public MnozstvaTovaru[] newArray(int size) {
            return new MnozstvaTovaru[size];
        }
    };

    public MnozstvaTovaru() {
        return ;
    }

    public MnozstvaTovaru(Parcel in) {
        this.Id = in.readInt();
        this.tovar = in.readString();
        this.miestnost = in.readString();
        this.mnozstvo = in.readDouble();
        this.limitne_mnozstvo = in.readDouble();
        this.fotografia = in.createByteArray();
    }

    //Use this for cloning values
    public void Copy(MnozstvaTovaru clone) {
        this.Id = clone.Id;
        this.tovar = clone.tovar;
        this.miestnost = clone.miestnost;
        this.mnozstvo = clone.mnozstvo;
        this.limitne_mnozstvo = clone.limitne_mnozstvo;
        this.fotografia  = clone.fotografia;
    }

    public String getTovarNazov() {
        return tovar;
    }

    public byte[] getImage() {
        return fotografia;
    }

}
