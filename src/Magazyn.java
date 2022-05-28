import java.util.LinkedList;
import java.util.List;
import java.time.Period;

public class Magazyn {
    private int maxLiczbaKontenerow;
    private int id;
    private List<KontenerPodstawowy> listaKontenerow = new LinkedList<>();

    public static int IdDoWziecia = 1;


    public Magazyn(int maxLiczbaKontenerow) {
        this.maxLiczbaKontenerow = maxLiczbaKontenerow;
        this.id = IdDoWziecia++;
        Port.listaMagazynow.add(this);
        System.out.println("Utworzono magazyn");
    };

    public int getId() {
        return id;
    }

    public List<KontenerPodstawowy> getListaKontenerow() {
        return listaKontenerow;
    }


    public boolean czyJestMiejsce(){
        if(listaKontenerow.size() > maxLiczbaKontenerow){
            System.out.println("Brak miejsca");
            return false;
        }else{
            return true;
        }
    };


    public void rozladujKontenerMagazyn(KontenerPodstawowy kontener){
        listaKontenerow.remove(kontener);
    }

    public void zaladujKontener(KontenerPodstawowy kontener) {
        if(czyJestMiejsce()){
            if (kontener.getNadawca().getLiczbaOstrzezen() < 2) {
                kontener.setDataZaladunku();
                listaKontenerow.add(kontener);
            } else {
                System.out.println("Otrzymano dwa ostrzezenia dlatego towar zostanie odeslany do nadawcy");
            }
        }
    }



    public int iloscDni(KontenerPodstawowy kontener){
        Period period = Period.between(kontener.getDataZaladunku(), Port.data);
        System.out.println(period);
        int dni = period.getDays();
        return dni;
    }


    public void sortujKonteneryMagazyn() {
        for (int j = 0; j < listaKontenerow.size(); j++) {
            for (int i = 1; i < listaKontenerow.size(); i++) {
                if (listaKontenerow.get(i - 1).getDataZaladunku().equals(listaKontenerow.get(i).getDataZaladunku())) {
                    if (listaKontenerow.get(i - 1).getNadawca().getNazwisko().toLowerCase().compareTo(listaKontenerow.get(i).getNadawca().getNazwisko().toLowerCase()) < 0) {
                        KontenerPodstawowy kontenerPodstawowy = listaKontenerow.get(i - 1);
                        listaKontenerow.set(i - 1, listaKontenerow.get(i));
                        listaKontenerow.set(i, kontenerPodstawowy);
                    }
                } else {
                    if (listaKontenerow.get(i - 1).getDataZaladunku().isAfter(listaKontenerow.get(i).getDataZaladunku())) {
                        KontenerPodstawowy kontenerPodstawowy = listaKontenerow.get(i - 1);
                        listaKontenerow.set(i - 1, listaKontenerow.get(i));
                        listaKontenerow.set(i, kontenerPodstawowy);
                    }
                }
            }
        }
    }



    @Override
    public String toString() {
        return "Makysmalna liczba kontenerów: " + maxLiczbaKontenerow + '\n' +
                        "Id: " + id + '\n' +
                        '\n' + " { " + listaKontenerow.toString()
                        + '\n' + "}";

    }

    }

