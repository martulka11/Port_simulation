import java.util.ArrayList;
import java.util.List;

public class Statek {

    private String nazwa;
    private String portMacierzysty;
    private String lokalizacjaDocelowa;
    private String lokalizacjaZródlowa;
    private int liczbaKNiebezpiecznych;
    private int maxCiezkichK;
    private int maxKZasilanychPradem;
    private int maxLiczbaKontenerow;
    private int ladownoscStatku;
    private int id;
    private List<KontenerPodstawowy> listaKontenerow = new ArrayList<>();

    public static int IdDoWziecia = 1;

    public Statek(String nazwa, String portMacierzysty, String lokalizacjaDocelowa, String lokalizacjaZródlowa, int liczbaKNiebezpiecznych, int maxCiezkichKontenerow, int maxKZasilanychPradem, int maxLiczbaKontenerow, int ladownoscStatku) {
        this.nazwa = nazwa;
        this.portMacierzysty = portMacierzysty;
        this.lokalizacjaDocelowa = lokalizacjaDocelowa;
        this.lokalizacjaZródlowa = lokalizacjaZródlowa;
        this.liczbaKNiebezpiecznych = liczbaKNiebezpiecznych;
        this.maxCiezkichK = maxCiezkichKontenerow;
        this.maxKZasilanychPradem = maxKZasilanychPradem;
        this.maxLiczbaKontenerow = maxLiczbaKontenerow;
        this.ladownoscStatku = ladownoscStatku;
        this.id = IdDoWziecia++;
        Port.listaStatkow.add(this);

        System.out.println("Utworzono statek");

    }

    public int getId(){
        return id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public int getMaxLiczbaKontenerow() {
        return maxLiczbaKontenerow;
    }

    public int getLiczbaKNiebezpiecznych() {
        return liczbaKNiebezpiecznych;
    }

    public int getMaxKZasilanychPradem() {
        return maxKZasilanychPradem;
    }

    public List<KontenerPodstawowy> getListaKontenerow(){
        return listaKontenerow;
    }

    public boolean equals(Object obj) {
       return obj instanceof KontenerNaMaterialyWybuchowe || obj instanceof KontenerNaToksyczneMaterialyCiekle || obj instanceof KontenerNaToksyczneMaterialySypkie;
    }

    public boolean czyJestMiejsce(){
        if(listaKontenerow.size() > maxLiczbaKontenerow){
            System.out.println("Brak miejsca");
            return false;
        }else{
            return true;
        }
    };

    public void rozladujKontenerStatek(KontenerPodstawowy kontener){
        listaKontenerow.remove(kontener);
    }

    public void zaladujKontener(KontenerPodstawowy kontener) {
        if(czyJestMiejsce()){
            if(czyStatekNieJestPrzeciazony()){
                if(czyMoznaKonteneryNaPrad()){
                if(equals(kontener)){
                    if(czyMoznaNiebezpieczny()){
                        if(kontener.getNadawca().getLiczbaOstrzezen() < 2){
                            kontener.setDataZaladunku();
                            listaKontenerow.add(kontener);
                            System.out.println("Zaladowano kontener");

                        }else{
                            System.out.println("Otrzymano dwa ostrzezenia dlatego towar zostanie odeslany do nadawcy");
                        }
                    }
                }if(kontener.getNadawca().getLiczbaOstrzezen() < 2){
                        kontener.setDataZaladunku();
                        listaKontenerow.add(kontener);
                        System.out.println("Zaladowano kontener");
                    }else{
                        System.out.println("Otrzymano dwa ostrzezenia dlatego towar zostanie odeslany do nadawcy");
                }}
            }
        }
    }

    public boolean czyMoznaNiebezpieczny() {
        int liczbaK = 0;

        for (int i = 0; i < listaKontenerow.size(); i++) {
            if (equals(listaKontenerow.get(i))) {
                liczbaK++;
            }
        }
        if (liczbaK > getLiczbaKNiebezpiecznych()) {
            System.out.println("Brak miejsca na kontenery niebezpieczne");
            return false;
        }
        return true;
    };


    public boolean czyMoznaKonteneryNaPrad(){
        int liczbaP = 0;

        for (int i = 0; i < listaKontenerow.size(); i++) {
            if (listaKontenerow.get(i) instanceof KontenerChlodniczy){
                if(((KontenerChlodniczy) listaKontenerow.get(i)).getWymaganeZasilanie()){
                    liczbaP++;
                }
            }
        }
        if (liczbaP > (getMaxKZasilanychPradem())) {
            System.out.println("Brak miejsca na kontenery zasilane pradem");
            return false;
        }
        return true;

    };


    public boolean czyStatekNieJestPrzeciazony() {
        float wagaWszystkichKontenerow = 0;

        for (int i = 0; i < listaKontenerow.size(); i++) {
            KontenerPodstawowy kontenerZListy = listaKontenerow.get(i);
            wagaWszystkichKontenerow = (float) (kontenerZListy.getWagaBrutto() + wagaWszystkichKontenerow);
        }
        if (wagaWszystkichKontenerow > ladownoscStatku) {
            System.out.println("Statek jest przeciazony");
            return false;
        }
        return true;
    }

    public void sortujKonteneryStatek(){
        for(int j =0 ; j <listaKontenerow.size(); j++){
        for(int i = 1; i< listaKontenerow.size(); i++){
            if(listaKontenerow.get(i-1).getWagaBrutto() > listaKontenerow.get(i).getWagaBrutto() ){
                KontenerPodstawowy kontenerPodstawowy = listaKontenerow.get(i-1);
                listaKontenerow.set(i-1, listaKontenerow.get(i));
                listaKontenerow.set(i, kontenerPodstawowy);
            }
        }
    }
    }

    public String toString() {
        return  "Id: " + id + '\n' +
                "Nazwa: " + nazwa + '\n' +
                "Port macierzysty: " + portMacierzysty + '\n' +
                "Lokalizacja docelowa: " + lokalizacjaDocelowa + '\n' +
                "Lokalizacja zródlowa: " + lokalizacjaZródlowa + '\n' +
                "Maksymalna liczba kontenerów niebezpiecznych: " + liczbaKNiebezpiecznych + '\n' +
                "Maksymalna liczba kontenerów ciezkich: " + maxCiezkichK + '\n' +
                "Makysymalna liczba kontenerów zasilanych pradem: " + maxKZasilanychPradem + '\n' +
                "Maksymalna liczba wszystkich kontenrów: " + maxLiczbaKontenerow + '\n' +
                "Ladownosc statku: " + ladownoscStatku +  '\n' + " { " +
                 listaKontenerow.toString() + '\n' + " } ";

    }

}
