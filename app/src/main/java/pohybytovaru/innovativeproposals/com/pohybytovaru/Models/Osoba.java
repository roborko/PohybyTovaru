package pohybytovaru.innovativeproposals.com.pohybytovaru.Models;

import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Robert on 14.01.2018.
 */

@DatabaseTable(tableName = "Osoba")
public class Osoba {
    private int Id;
    private String FullName;
    private String Password;
}
