package pohybytovaru.innovativeproposals.com.pohybytovaru.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Robert on 14.01.2018.
 */

@DatabaseTable(tableName = "Tovar")
public class Tovar {
    @DatabaseField(columnName = "Id", generatedId = true)
    private int Id;
    private String Nazov;
    private byte[] Fotografia;
    private double MinimalneMnozstvo;
}
