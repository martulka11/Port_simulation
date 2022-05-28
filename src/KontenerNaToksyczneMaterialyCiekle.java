public class KontenerNaToksyczneMaterialyCiekle extends KontenerCiezki{

    private boolean warstwowy;
    private int objetosc;

    public KontenerNaToksyczneMaterialyCiekle(Nadawca nadawca, double tara, String zabezpieczenia, double wagaNetto, String certyfikaty, String tworzywo, boolean warstwowy, int objetosc) {
        super(nadawca, tara, zabezpieczenia, wagaNetto, certyfikaty, tworzywo);
        this.warstwowy = warstwowy;
        this.objetosc = objetosc;
    }
}
