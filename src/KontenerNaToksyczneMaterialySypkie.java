public class KontenerNaToksyczneMaterialySypkie extends KontenerCiezki{

    private boolean wodoszczelny;

    public KontenerNaToksyczneMaterialySypkie(Nadawca nadawca, double tara, String zabezpieczenia, double wagaNetto, String certyfikaty, String tworzywo, boolean wodoszczelny) {
        super(nadawca, tara, zabezpieczenia, wagaNetto, certyfikaty, tworzywo);
        this.wodoszczelny = wodoszczelny;
    }
}
