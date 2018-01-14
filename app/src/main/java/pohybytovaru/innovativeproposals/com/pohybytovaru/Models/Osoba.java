package pohybytovaru.innovativeproposals.com.pohybytovaru.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Robert on 14.01.2018.
 */

@DatabaseTable(tableName = "Osoba")
public class Osoba {
    @DatabaseField(columnName = "Id", generatedId = true)
    private int Id;
    @DatabaseField
    private String FullName;
    @DatabaseField
    private String Password;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
