import java.time.LocalDate;
import java.util.ArrayList;

public class Nadawca{
    private String nazwisko;
    private String imie;
    private String adres;
    private long pesel;
    private LocalDate dataUrodzenia;
    private int liczbaOstrzezen;
    private int id;

    public static int IdDoWziecia = 1;

    public Nadawca(String nazwisko, String imie, String adres, long pesel){
        this.nazwisko = nazwisko;
        this.imie = imie;
        this.adres = adres;
        this.pesel = pesel;
        this.dataUrodzenia = dataUrodzenia(this.pesel);
        this.liczbaOstrzezen = 0;
        this.id = IdDoWziecia++;
        Port.listaNadawcow.add(this);
        System.out.println("Utworzono nadawce");
    }

    public int getId() {
        return id;
    }

    public int getLiczbaOstrzezen() {
        return liczbaOstrzezen;
    }

    public String getNazwisko(){
        return nazwisko;
    }

    public int setLiczbaOstrzezen() {
        return this.liczbaOstrzezen++;
    }


    public static LocalDate dataUrodzenia(long pesel) {
        String PESEL = (pesel / 100000) + "";

        char[] cyfry = PESEL.toCharArray();

        String rok = String.valueOf(19) + String.valueOf(cyfry[0]) + String.valueOf(cyfry[1]);
        String miesiac1 = String.valueOf(cyfry[2]) + cyfry[3];
        String dzien1 = String.valueOf(cyfry[4]) + cyfry[5];

        String pelnadata = rok + "-" + miesiac1 + "-" + dzien1;
        return LocalDate.parse(pelnadata);

    }

    @Override
    public String toString() {
        return "Nazwisko: " + nazwisko + '\n' +
                "Imie: " + imie + '\n' +
                "Adres: " + adres + '\n' +
                "Data Urodzenia: " + dataUrodzenia + '\n' +
                "Pesel: " + pesel;
    }

}
