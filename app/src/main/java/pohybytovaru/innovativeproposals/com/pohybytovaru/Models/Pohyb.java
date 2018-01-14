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

    @DatabaseField(columnName = "TypPohybu", foreign = true, foreignAutoRefresh = true)
    private TypTransakcie TypPohybu;

    @DatabaseField(columnName = "Miestnost", foreign = true, foreignAutoRefresh = true)
    private Miestnost Miestnost;

    @DatabaseField(columnName = "Osoba", foreign = true, foreignAutoRefresh = true)
    private Osoba Osoba;

    @DatabaseField
    private double PocetKusov;
    @DatabaseField
    private String Poznamka;
    @DatabaseField
    private Date Datum;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public TypTransakcie getTypPohybu() {
        return TypPohybu;
    }

    public void setTypPohybu(TypTransakcie typPohybu) {
        TypPohybu = typPohybu;
    }

    public Miestnost getMiestnost() {
        return Miestnost;
    }

    public void setMiestnost(Miestnost miestnost) {
        Miestnost = miestnost;
    }

    public Osoba getOsoba() {
        return Osoba;
    }

    public void setOsoba(Osoba osoba) {
        Osoba = osoba;
    }

    public double getPocetKusov() {
        return PocetKusov;
    }

    public void setPocetKusov(double pocetKusov) {
        PocetKusov = pocetKusov;
    }

    public String getPoznamka() {
        return Poznamka;
    }

    public void setPoznamka(String poznamka) {
        Poznamka = poznamka;
    }

    public Date getDatum() {
        return Datum;
    }

    public void setDatum(Date datum) {
        Datum = datum;
    }

}
