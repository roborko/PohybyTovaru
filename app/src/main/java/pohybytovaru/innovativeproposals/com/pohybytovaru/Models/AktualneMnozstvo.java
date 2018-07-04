package pohybytovaru.innovativeproposals.com.pohybytovaru.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "AktualneMnozstvo")
public class AktualneMnozstvo {
    @DatabaseField(columnName = "Id", generatedId = true)
    private int Id;

    @DatabaseField(columnName = "Miestnost", foreign = true, foreignAutoRefresh = true)
    private Miestnost Miestnost;

    @DatabaseField(columnName = "Tovar", foreign = true, foreignAutoRefresh = true)
    private Tovar Tovar;

    @DatabaseField
    private double Mnozstvo;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Miestnost getMiestnost() {
        return Miestnost;
    }

    public void setMiestnost(Miestnost miestnost) {
        Miestnost = miestnost;
    }

    public Tovar getTovar() {
        return Tovar;
    }

    public void setTovar(Tovar tovar) {
        Tovar = tovar;
    }

    public double getMnozstvo() {
        return Mnozstvo;
    }

    public void setMnozstvo(double mnozstvo) {
        Mnozstvo = mnozstvo;
    }
}
