import java.util.*;

public class SkladKolejowy {

    private int iloscKontenerow;
    private int id;
    public List<KontenerPodstawowy> wagony = new ArrayList<>();

    public static int IdDoWziecia = 1;

    public SkladKolejowy() {
        this.iloscKontenerow = 10;
        this.id = IdDoWziecia++;
    }


}


