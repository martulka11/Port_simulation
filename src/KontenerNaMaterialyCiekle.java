public class KontenerNaMaterialyCiekle extends KontenerPodstawowy{

    private int objetosc;

    public KontenerNaMaterialyCiekle(Nadawca nadawca, double tara, String zabezpieczenia, double wagaNetto, String certyfikaty, int objetosc) {
        super(nadawca, tara, zabezpieczenia, wagaNetto, certyfikaty);
        this.objetosc = objetosc;
    }
}
