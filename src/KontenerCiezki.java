public class KontenerCiezki extends KontenerPodstawowy {
    private String tworzywo;

    public KontenerCiezki(Nadawca nadawca, double tara, String zabezpieczenia, double wagaNetto, String certyfikaty, String tworzywo){
        super(nadawca, tara, zabezpieczenia, wagaNetto, certyfikaty);
        this.tworzywo = tworzywo;
    }

}
