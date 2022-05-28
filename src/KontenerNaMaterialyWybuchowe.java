public class KontenerNaMaterialyWybuchowe extends KontenerCiezki{

    private int szerokosc;

    public KontenerNaMaterialyWybuchowe(Nadawca nadawca, double tara, String zabezpieczenia, double wagaNetto, String certyfikaty, String tworzywo, int szerokosc) {
        super(nadawca, tara, zabezpieczenia, wagaNetto, certyfikaty, tworzywo);
        this.szerokosc = szerokosc;

    }
}
