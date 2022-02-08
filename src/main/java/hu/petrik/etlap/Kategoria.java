package hu.petrik.etlap;

public class Kategoria {
    private int id;
    private String nev;

    public Kategoria(int id, String nev) {
        this.id = id;
        this.nev = nev;
    }

    public int getId() {
        return id;
    }

    public String getNev() {
        return nev;
    }

}
