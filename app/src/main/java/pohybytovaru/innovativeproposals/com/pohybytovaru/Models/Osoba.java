package pohybytovaru.innovativeproposals.com.pohybytovaru.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Osoba")
public class Osoba {
    @DatabaseField(columnName = "Id", generatedId = true)
    private int Id;
    @DatabaseField
    private String FullName;
    @DatabaseField
    private String Password;

    @DatabaseField
    private int idMiestnosti;


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

    public int getIdMiestnosti() {
        return idMiestnosti;
    }

    public void setIdMiestnosti(int idMiestnosti) {
        this.idMiestnosti = idMiestnosti;
    }


}
