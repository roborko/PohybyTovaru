package pohybytovaru.innovativeproposals.com.pohybytovaru.Models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Robert on 14.01.2018.
 */

@DatabaseTable(tableName = "Tovar")
public class Tovar {
    @DatabaseField(columnName = "Id", generatedId = true)
    private int Id;
    @DatabaseField
    private String Nazov;
    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] Fotografia;
    @DatabaseField
    private double MinimalneMnozstvo;

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
}
