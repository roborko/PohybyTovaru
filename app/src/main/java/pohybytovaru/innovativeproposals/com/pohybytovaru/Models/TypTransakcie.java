package pohybytovaru.innovativeproposals.com.pohybytovaru.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Robert on 14.01.2018.
 */

@DatabaseTable(tableName = "TypTransakcie")
public class TypTransakcie {
    @DatabaseField(columnName = "Id", generatedId = true)
    private int Id;
    @DatabaseField
    private String Nazov;

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
}
