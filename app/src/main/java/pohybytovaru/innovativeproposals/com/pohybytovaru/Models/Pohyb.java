package pohybytovaru.innovativeproposals.com.pohybytovaru.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by Robert on 14.01.2018.
 */

@DatabaseTable(tableName = "Pohyb")
public class Pohyb {
    @DatabaseField(columnName = "Id", generatedId = true)
    private int Id;
    private TypTransakcie TypPohybu;
    private Miestnost Miestnost;
    private double PocetKusov;
    private String Poznamka;
    private Date Datum;
}
