public class KontenerChlodniczy extends KontenerCiezki{
    private boolean wymaganeZasilanie;
    private int temperaturaChlodzenia;

    public KontenerChlodniczy(Nadawca nadawca, double tara, String zabezpieczenia, double wagaNetto, String certyfikaty, String tworzywo, int temperaturaChlodzenia){
        super(nadawca, tara, zabezpieczenia, wagaNetto, certyfikaty, tworzywo);
        this.temperaturaChlodzenia = temperaturaChlodzenia;
        this.wymaganeZasilanie = true;
    }

    public boolean getWymaganeZasilanie() {
        return wymaganeZasilanie;
    }
}

