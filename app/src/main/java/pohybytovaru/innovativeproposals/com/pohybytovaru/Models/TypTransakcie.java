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

    @DatabaseField
    private int AssignedResourceId_White;

    @DatabaseField
    private int AssignedResourceId_Black;

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

    public int getAssignedResourceId_White() {
        return AssignedResourceId_White;
    }

    public void setAssignedResourceId_White(int assignedResourceId_White) {
        AssignedResourceId_White = assignedResourceId_White;
    }

    public int getAssignedResourceId_Black() {
        return AssignedResourceId_Black;
    }

    public void setAssignedResourceId_Black(int assignedResourceId_Black) {
        AssignedResourceId_Black = assignedResourceId_Black;
    }
}
