import java.time.LocalDate;

public class KontenerPodstawowy {
    private Nadawca nadawca;
    private double tara;
    private String zabezpieczenia;
    private double wagaNetto;
    private double wagaBrutto;
    private String certyfikaty;
    private LocalDate dataZaladunku;
    private LocalDate dataUtylizacji;
    private int id;

    public static int IdDoWziecia = 1;


    public KontenerPodstawowy(Nadawca nadawca, double tara, String zabezpieczenia, double wagaNetto, String certyfikaty) {
        this.nadawca = nadawca;
        this.tara = tara;
        this.zabezpieczenia = zabezpieczenia;
        this.wagaNetto = wagaNetto;
        this.wagaBrutto = wagaNetto + tara;
        this.certyfikaty = certyfikaty;
        this.dataZaladunku = null;
        this.id = IdDoWziecia++;
        System.out.println("Utworzono kontener");
    }

    public Nadawca getNadawca() {
        return nadawca;
    }

    public int getId() {
        return id;
    }

    public double getWagaBrutto() {
        return wagaBrutto;
    }

    public void setDataZaladunku() {
        this.dataZaladunku = Port.data;
    }

    ;

    public LocalDate getDataZaladunku() {
        return dataZaladunku;
    }

    public void setDataUtylizacji() {
        this.dataUtylizacji = Port.data;
    }

    ;

    public LocalDate getDataUtylizacji() {
        return dataUtylizacji;
    }


    @Override
    public String toString() {
        return "Nadawca: " + nadawca + " { " +
                "Tara: " + tara + " / " +
                "Zabezpieczenia: " + zabezpieczenia + " / " +
                "Waga netto: " + wagaNetto + " / " +
                "Waga brutto: " + wagaBrutto + " / " +
                "Certyfikaty: " + certyfikaty + " / " +
                "Data zaladunku: " + dataZaladunku + " / " +
                "Id: " + id;

    }


}
