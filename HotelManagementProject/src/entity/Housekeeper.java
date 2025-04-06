package entity;
import java.util.ArrayList;
import java.util.List;

public class Housekeeper {
    private String korisnickoIme;
    private int brojDodeljenihSoba;
    private List<Room> sobeZaCiscenje;

    public Housekeeper(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
        this.brojDodeljenihSoba = 0;
        this.sobeZaCiscenje = new ArrayList<>();

    }

    public void dodeliSobu() {
        brojDodeljenihSoba++;
    }

    public int getBrojDodeljenihSoba() {
        return brojDodeljenihSoba;
    }
    public void dodajSobuZaCiscenje(Room soba) {
        sobeZaCiscenje.add(soba);
    }

}
