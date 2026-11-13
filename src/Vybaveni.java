public class Vybaveni {
    private String nazev;
    private String kategorie;
    private double hmotnost;
    private boolean poskozene;

    public Vybaveni(String nazev, String kategorie, double hmotnost, boolean poskozene) {
        this.nazev = nazev;
        this.kategorie = kategorie;
        this.hmotnost = hmotnost;
        this.poskozene = poskozene;
    }

    public String getNazev() {
        return nazev;
    }

    public String getKategorie() {
        return kategorie;
    }

    public double getHmotnost() {
        return hmotnost;
    }

    public boolean isPoskozene() {
        return poskozene;
    }

    @Override
    public String toString() {
        String stav = poskozene ? "POŠKOZENÉ" : "v pořádku";
        return String.format("%s [kategorie: %s, hmotnost: %.2f kg, stav: %s]",
                nazev, kategorie, hmotnost, stav);
    }
}