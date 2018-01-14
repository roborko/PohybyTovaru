package pohybytovaru.innovativeproposals.com.pohybytovaru.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Robert on 14.01.2018.
 */
@DatabaseTable(tableName = "Miestnost")
public class Miestnost {
    @DatabaseField(columnName = "Id", generatedId = true)
    private int Id;
    private String Nazov;
    private boolean JeSklad;
}