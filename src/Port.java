
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Port extends Thread {

    public static LocalDate data = LocalDate.now();
    public static List<Statek> listaStatkow = new ArrayList<>();
    public static List<Magazyn> listaMagazynow = new ArrayList<>();
    public static List<Nadawca> listaNadawcow = new ArrayList<>();

    public LocalDate getData() {
        return data;
    }

    public static void zapisPliku(String sciezkapliku) {
        try {
            FileWriter fileWriter = new FileWriter(sciezkapliku);
            fileWriter.write(listaStatkow.toString() + listaMagazynow.toString() + listaNadawcow.toString());

            fileWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void odpywStatku(Statek statek) {
        listaStatkow.remove(statek);
        System.out.println(statek.getNazwa() + " - odplyw Statku z portu");
    }


    public void utylizacjaKontenera(Magazyn magazyn, KontenerPodstawowy kontener) throws IrresponsibleSenderWithDangerousGoods {
        LocalDate dataUtylizacji = Port.data;
        magazyn.getListaKontenerow().remove(kontener);
        kontener.getNadawca().setLiczbaOstrzezen();
        throw new IrresponsibleSenderWithDangerousGoods(
                "Id: " + kontener.getId() + ", " + "Data zaladunku: " + kontener.getDataZaladunku() + ", " + "Data utylizacji: " + dataUtylizacji);
    }


    public void RozladujNaPociag(Statek statek, SkladKolejowy skladKolejowy) {
        new Thread(() -> {
            while (statek.getListaKontenerow().size() > 0) {
                if (skladKolejowy.wagony.size() < 10) {
                    KontenerPodstawowy kontenerDoRozladowania = statek.getListaKontenerow().get(0);
                    skladKolejowy.wagony.add(kontenerDoRozladowania);
                    statek.rozladujKontenerStatek(kontenerDoRozladowania);
                } else {
                    System.out.println("aktualna liczba kontenerow: " + statek.getListaKontenerow().size());
                    skladKolejowy.wagony = new LinkedList<KontenerPodstawowy>();
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

            if (skladKolejowy.wagony.size() != 0) {
                skladKolejowy.wagony = new LinkedList<KontenerPodstawowy>();
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void zapominanieKontenera() {
        new Thread(() -> {
            int dni = 0;
            LocalDate nowaData = LocalDate.now();
            System.out.println(nowaData);
            DateTimeFormatter nowyFormat = DateTimeFormatter.ofPattern("yyyy MM dd");
            while (!Thread.currentThread().isInterrupted()) {
                for (int k = 0; k < listaMagazynow.size(); k++) {
                    for (int i = 0; i < listaMagazynow.get(k).getListaKontenerow().size(); i++) {
                        if (listaMagazynow.get(k).getListaKontenerow().get(i) instanceof KontenerNaMaterialyWybuchowe) {
                            if (iloscDni(listaMagazynow.get(k).getListaKontenerow().get(i)) == 5) {
                                try {
                                    utylizacjaKontenera(listaMagazynow.get(k), listaMagazynow.get(k).getListaKontenerow().get(i));
                                    System.out.println("Zapomniano kontener");
                                } catch (IrresponsibleSenderWithDangerousGoods e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (listaMagazynow.get(k).getListaKontenerow().get(i) instanceof KontenerNaToksyczneMaterialyCiekle) {
                            if (iloscDni(listaMagazynow.get(k).getListaKontenerow().get(i)) == 10) {
                                try {
                                    utylizacjaKontenera(listaMagazynow.get(k), listaMagazynow.get(k).getListaKontenerow().get(i));
                                    System.out.println("Zapomniano kontener");
                                } catch (IrresponsibleSenderWithDangerousGoods e) {
                                    e.printStackTrace();
                                }
                            }
                            if (listaMagazynow.get(k).getListaKontenerow().get(i) instanceof KontenerNaToksyczneMaterialySypkie) {
                                if (iloscDni(listaMagazynow.get(k).getListaKontenerow().get(i)) == 14) {
                                    try {
                                        utylizacjaKontenera(listaMagazynow.get(k), listaMagazynow.get(k).getListaKontenerow().get(i));
                                        System.out.println("Zapomniano kontener");
                                    } catch (IrresponsibleSenderWithDangerousGoods e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(5000);
                    LocalDate dataPowiekszona = nowaData.plusDays(dni);
                    dni++;
                    data = dataPowiekszona;
                   // System.out.println(nowyFormat.format(data));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public int iloscDni(KontenerPodstawowy kontener) {
        Period period = Period.between(kontener.getDataZaladunku(), this.getData());
        System.out.println(period);
        int dni = period.getDays();
        return dni;
    }

    public void sortujStatki() {
        for (int j = 0; j < listaStatkow.size(); j++) {
            for (int i = 1; i < listaStatkow.size(); i++) {
                if (listaStatkow.get(i - 1).getNazwa().toLowerCase().compareTo(listaStatkow.get(i).getNazwa().toLowerCase()) < 0) {
                    Statek statek = listaStatkow.get(i - 1);
                    listaStatkow.set(i - 1, listaStatkow.get(i));
                    listaStatkow.set(i, statek);
                }
            }
        }
    }


    public static void main(String[] args) {

        System.out.println("Wpisz nazwe metody która chcesz wywolac...");

        SkladKolejowy skladKolejowy = new SkladKolejowy();
        Port port = new Port();

        port.zapominanieKontenera();


        Magazyn magazyn1 = new Magazyn(11);
        Magazyn magazyn2 = new Magazyn(8);

        Statek statek1 = new Statek("Czarna Perla", "Gdansk", "Oslo", "", 5, 4, 2, 10, 800);
        Statek statek2 = new Statek("Morski Wilk", "Oslo", "Brighton", "Gdansk", 4, 4, 2, 9, 900);
        Statek statek3 = new Statek("Morski Pirat", "Kopenhaga", "Oslo", "Ryga", 3, 2, 0, 10, 1000);
        Statek statek4 = new Statek("Fala", "Gdansk", "Amsterdam", "Ryga", 6, 5, 1, 6, 1200);
        Statek statek5 = new Statek("Dar Morza", "Portsmouth", "Gdansk", "Amsterdam", 1, 6, 8, 11, 1000);


        Nadawca nadawca1 = new Nadawca("Kowalski", "Jan", "Wroclaw 01-909", 88010166701L);
        Nadawca nadawca2 = new Nadawca("Sosna", "Piotr", "Warszawa 00-021", 99012345611L);
        Nadawca nadawca3 = new Nadawca("Byk", "Piotr", "Warszawa 00-021", 99012845622L);

        KontenerCiezki kontenerCiezki1 = new KontenerCiezki(nadawca1, 40, "brak", 220, "SCI", "plastki");
        KontenerCiezki kontenerCiezki2 = new KontenerCiezki(nadawca2, 50, "brak", 250, "MON", "metal");
        KontenerPodstawowy kontenerPodstawowy1 = new KontenerPodstawowy(nadawca3, 60, "COS", 100, "MON, SCI");
        KontenerChlodniczy kontenerChlodniczy1 = new KontenerChlodniczy(nadawca1, 40, "PKL", 150, "ceryki", "plastik", -40);
        KontenerNaMaterialyWybuchowe kontenerNaMaterialyWybuchowe = new KontenerNaMaterialyWybuchowe(nadawca1, 40, "klu", 200, "COl", "metal", 100);
        KontenerNaToksyczneMaterialyCiekle kontenerNaToksyczneMaterialyCiekle1 = new KontenerNaToksyczneMaterialyCiekle(nadawca1, 70, "PKL", 140, "KLA", "metal", true, 500);
        KontenerNaMaterialyWybuchowe kontenerNaMaterialyWybuchowe1 = new KontenerNaMaterialyWybuchowe(nadawca2, 50, "PKL", 170, "KLA MON", "plastik", 100);
        KontenerChlodniczy kontenerChlodniczy2 = new KontenerChlodniczy(nadawca2, 30, "PKL", 180, "SCI", "plastik", -200);
        KontenerNaToksyczneMaterialyCiekle kontenerNaToksyczneMaterialyCiekle2 = new KontenerNaToksyczneMaterialyCiekle(nadawca3, 40, "PKL", 100, "SCI", "metal", false, 200);


        statek1.zaladujKontener(kontenerCiezki1);
        statek1.zaladujKontener(kontenerCiezki2);
        statek1.zaladujKontener(kontenerPodstawowy1);
        statek1.zaladujKontener(kontenerChlodniczy1);


        magazyn1.zaladujKontener(kontenerNaMaterialyWybuchowe);
        magazyn1.zaladujKontener(kontenerCiezki1);
        magazyn1.zaladujKontener(kontenerPodstawowy1);
        magazyn1.zaladujKontener(kontenerCiezki2);


        for (int i = 0; i < 45; i++) {
            statek1.getListaKontenerow().add(new KontenerPodstawowy(nadawca1, i * 10, "COS", 100, "MON, SCI"));
        }


        Scanner sc = new Scanner(System.in);

        while (!Thread.currentThread().isInterrupted()) {
            String sentence = sc.nextLine().toLowerCase();

            //METODA
            if (sentence.startsWith("rozladuj caly statek")) {
                boolean isInputRequired = true;
                int idStatku = 0;

                while (isInputRequired) {
                    System.out.println("podaj numer statku:");
                    String numerStatku = sc.nextLine();
                    try {
                        idStatku = Integer.parseInt(numerStatku);
                        isInputRequired = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Zly format - wymagany int");
                    }
                }

                Statek podanyStatek = null;
                for (Statek statek : listaStatkow) {
                    if (statek.getId() == idStatku) {
                        podanyStatek = statek;
                        port.RozladujNaPociag(podanyStatek, skladKolejowy);
                    }
                }

                if (podanyStatek == null) {
                    System.out.println("Nie znaleziono statku");
                }

            }

            //METODA
            if (sentence.startsWith("zaladuj kontener na statek")) {
                boolean isInputRequired80 = true;
                int idStatku = 0;
                while (isInputRequired80) {
                    System.out.println("podaj numer statku:");
                    String numerStatku = sc.nextLine();
                    try {
                        idStatku = Integer.parseInt(numerStatku);
                        isInputRequired80 = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Zly format - wymagany int");
                    }
                }
                Statek podanyStatek = null;
                for (int i = 0; i < listaStatkow.size(); i++) {
                    if (listaStatkow.get(i).getId() == idStatku) {
                        podanyStatek = listaStatkow.get(i);
                        System.out.println("podaj numer kontenera:");
                        String numerkontenera = sc.nextLine();
                        int idKontenera = Integer.parseInt(numerkontenera);
                        for (int k = 0; k < listaMagazynow.size(); k++) {
                            for (int j = 0; j < listaMagazynow.get(k).getListaKontenerow().size(); j++) {
                                if (listaMagazynow.get(k).getListaKontenerow().get(j).getId() == idKontenera) {
                                    KontenerPodstawowy naszKontener = listaMagazynow.get(k).getListaKontenerow().get(j);
                                    podanyStatek.zaladujKontener(listaMagazynow.get(k).getListaKontenerow().get(j));
                                    listaMagazynow.get(i).rozladujKontenerMagazyn(naszKontener);
                                }
                            }
                        }
                    }
                } if (podanyStatek == null) {
                        System.out.println("Nie znaleziono magazynu");
                }
            }


            if (sentence.startsWith("zaladuj kontener do magazynu")) {
                boolean isInputRequired89 = true;
                int idMagazynu = 0;
                while (isInputRequired89) {
                    System.out.println("podaj numer magazynu:");
                    String numerMagazynu = sc.nextLine();
                    try {
                        idMagazynu = Integer.parseInt(numerMagazynu);
                        isInputRequired89 = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Zly format - wymagany int");
                    }
                }
                Magazyn podanyMagazyn = null;
                for (int i = 0; i < listaMagazynow.size(); i++) {
                    if (listaMagazynow.get(i).getId() == idMagazynu) {
                        podanyMagazyn = listaMagazynow.get(i);
                        System.out.println("podaj numer kontenera:");
                        String numerkontenera = sc.nextLine();
                        int idKontenera = Integer.parseInt(numerkontenera);
                        for (int k = 0; k < listaMagazynow.size(); k++) {
                            for (int j = 0; j < listaMagazynow.get(k).getListaKontenerow().size(); j++) {
                                if (listaMagazynow.get(k).getListaKontenerow().get(j).getId() == idKontenera) {
                                    KontenerPodstawowy naszKontener = listaMagazynow.get(k).getListaKontenerow().get(j);
                                    podanyMagazyn.zaladujKontener(listaMagazynow.get(k).getListaKontenerow().get(j));
                                    podanyMagazyn.rozladujKontenerMagazyn(naszKontener);
                                    System.out.println("Zaladowano kontener");
                                }
                            }
                        }
                    }
                }if (podanyMagazyn == null) {
                    System.out.println("Nie znaleziono magazynu");
                }
            }

            //METODA
            if (sentence.startsWith("odplyw statku")) {
                boolean isInputRequired3 = true;
                int idStatku = 0;
                while (isInputRequired3) {
                    System.out.println("podaj numer statku:");
                    String numerStatku = sc.nextLine();
                    try {
                        idStatku = Integer.parseInt(numerStatku);
                        isInputRequired3 = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Zly format - wymagany int");
                    }
                }
                Statek podanyStatek = null;
                for (Statek statek : listaStatkow) {
                    if (statek.getId() == idStatku) {
                        podanyStatek = statek;
                        odpywStatku(podanyStatek);
                    }
                }
                if (podanyStatek == null) {
                    System.out.println("Nie znaleziono statku");
                }
            }


            //METODA
            if (sentence.startsWith("wypisz informacje o statku")) {
                boolean isInputRequired4 = true;
                int idStatku = 0;
                while (isInputRequired4) {
                    System.out.println("podaj numer statku:");
                    String numerStatku = sc.nextLine();
                    try {
                        idStatku = Integer.parseInt(numerStatku);
                        isInputRequired4 = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Zly format - wymagany int");
                    }
                }
                Statek podanyStatek = null;
                for (Statek statek : listaStatkow) {
                    if (statek.getId() == idStatku) {
                        podanyStatek = statek;
                        System.out.println(podanyStatek.toString());
                    }
                }
                if (podanyStatek == null) {
                    System.out.println("Nie znaleziono statku");
                }
            }


            //METODA
            if (sentence.startsWith("sortuj statki")) {
                port.sortujStatki();
            }


            //METODA
            if (sentence.startsWith("sortuj kontenery na statku")) {
                boolean isInputRequired5 = true;
                int idStatku = 0;
                while (isInputRequired5) {
                    System.out.println("podaj numer statku:");
                    String numerStatku = sc.nextLine();
                    try {
                        idStatku = Integer.parseInt(numerStatku);
                        isInputRequired5 = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Zly format - wymagany int");
                    }
                }
                Statek podanyStatek = null;
                for (Statek statek : listaStatkow) {
                    if (statek.getId() == idStatku) {
                        podanyStatek = statek;
                        podanyStatek.sortujKonteneryStatek();

                    }
                }
                if (podanyStatek == null) {
                    System.out.println("Nie znaleziono statku");
                }
            }


            //METODA
            if (sentence.startsWith("sortuj kontenery w magazynie")) {
                boolean isInputRequired6 = true;
                int idMagazynu = 0;
                while (isInputRequired6) {
                    System.out.println("podaj numer magazynu:");
                    String numerMagazynu = sc.nextLine();
                    try {
                        idMagazynu = Integer.parseInt(numerMagazynu);
                        isInputRequired6 = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Zly format - wymagany int");
                    }
                }
                Magazyn podanyMagazyn = null;
                for (Magazyn magazyn : listaMagazynow) {
                    if (magazyn.getId() == idMagazynu) {
                        podanyMagazyn = magazyn;
                        podanyMagazyn.sortujKonteneryMagazyn();

                    }
                }
                if (podanyMagazyn == null) {
                    System.out.println("Nie znaleziono magazynu");
                }
            }


            //METODA
            if (sentence.startsWith("utylizacja kontenera")) {
                boolean isInputRequired7 = true;
                int idKontenera = 0;
                while (isInputRequired7) {
                    System.out.println("podaj numer kontenera:");
                    String numerKontenera = sc.nextLine();
                    try {
                        idKontenera = Integer.parseInt(numerKontenera);
                        isInputRequired7 = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Zly format - wymagany int");
                    }
                }
                boolean isInputRequired8 = true;
                int idMagazynu = 0;
                while (isInputRequired8) {
                    System.out.println("podaj numer magazynu:");
                    String numerMagazynu = sc.nextLine();
                    try {
                        idMagazynu = Integer.parseInt(numerMagazynu);
                        isInputRequired8 = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Zly format - wymagany int");
                    }
                }

                KontenerPodstawowy podanyKontener = null;
                Magazyn podanyMagazyn = null;
                for (Magazyn magazyn : listaMagazynow) {
                    if (magazyn.getId() == idMagazynu) {
                        podanyMagazyn = magazyn;
                    }
                }
                for (KontenerPodstawowy kontener : podanyMagazyn.getListaKontenerow()) {
                    if (kontener.getId() == idKontenera) {
                        podanyKontener = kontener;
                        try {
                            port.utylizacjaKontenera(podanyMagazyn, podanyKontener);
                        } catch (IrresponsibleSenderWithDangerousGoods e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (podanyMagazyn == null || podanyKontener == null) {
                    System.out.println("Nie znaleziono magazynu lub kontenera");
                }
            }


            //METODA
            if (sentence.startsWith("utworz statek")) {
                System.out.println("podaj nazwe statku:");
                String nazwa = sc.nextLine();
                System.out.println("podaj port macierzysty:");
                String portMacierzysty = sc.nextLine();
                System.out.println("podaj lokalizacje docelowa:");
                String lokalizacjaDocelowa = sc.nextLine();
                System.out.println("podaj lokalizacje zródlowa:");
                String lokalizacjaZródlowa = sc.nextLine();
                boolean isInputRequired9 = true;
                int liczbaKontenerowN = 0;
                while (isInputRequired9) {
                    System.out.println("podaj liczbe kontenerow niebezpiecznych:");
                    String liczbaKNiebezpiecznych = sc.nextLine();
                    try {
                        liczbaKontenerowN = Integer.parseInt(liczbaKNiebezpiecznych);
                        isInputRequired9 = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Zly format - wymagany int");
                    }
                }
                String liczbaKNiebezpiecznych = sc.nextLine();
                int KonteneryNiebezpieczne = Integer.parseInt(liczbaKNiebezpiecznych);
                System.out.println("podaj maksymalna liczbe kontenerow ciezkich:");
                String maxCiezkichK = sc.nextLine();
                int maxCiezkichKontenerow = Integer.parseInt(maxCiezkichK);
                System.out.println("podaj maksymalna liczbe kontenerow zasilanych pradem:");
                String maxKZasilanychPradem = sc.nextLine();
                int maxKZasilanych = Integer.parseInt(maxKZasilanychPradem);
                System.out.println("podaj maksymalna liczbe kontenerow:");
                String maxLiczbaKontenerow = sc.nextLine();
                int maksymalnaIloscK = Integer.parseInt(maxLiczbaKontenerow);
                System.out.println("podaj ładownosc statku:");
                String ladownoscStatku = sc.nextLine();
                int ladownosc = Integer.parseInt(ladownoscStatku);
                System.out.println("podaj nazwe tworzonego obiektu:");

                Statek statek = new Statek(nazwa, portMacierzysty, lokalizacjaDocelowa, lokalizacjaZródlowa, KonteneryNiebezpieczne, maxCiezkichKontenerow, maxKZasilanych, maksymalnaIloscK, ladownosc);
            }


            //METODA:
            if (sentence.startsWith("utworz nadawce")) {
                System.out.println("podaj nazwisko:");
                String nazwisko = sc.nextLine();
                System.out.println("podaj imie:");
                String imie = sc.nextLine();
                System.out.println("podaj adres:");
                String adres = sc.nextLine();
                boolean isInputRequired18 = true;
                long pesel = 0;
                while (isInputRequired18) {
                    System.out.println("podaj pesel:");
                    String peselwpisany = sc.nextLine();
                    try {
                        pesel = Long.parseLong(peselwpisany);
                        isInputRequired18 = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Zly format - wymagany int");
                    }
                }

                Nadawca nadawca = new Nadawca(nazwisko, imie, adres, pesel);
            }


            //METODA
            if (sentence.startsWith("zakoncz program")) {
                Thread.currentThread().isInterrupted();
                return;

            }

            //METODA
            if (sentence.startsWith("utworz magazyn")) {
                boolean isInputRequired19 = true;
                int maxLiczbaKontenerow = 0;
                while (isInputRequired19) {
                    System.out.println("podaj maksymalna liczbe kontenerow:");
                    String maxLiczbaK = sc.nextLine();
                    try {
                        maxLiczbaKontenerow = Integer.parseInt(maxLiczbaK);
                        isInputRequired19 = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Zly format - wymagany int");
                    }
                }

                Magazyn magazyn = new Magazyn(maxLiczbaKontenerow);
            }


            //METODA:
            if (sentence.startsWith("utworz kontener")) {
                KontenerPodstawowy nowyKontener = null;
                int rodzajKontenera = 0;

                boolean isInputRequired0 = true;
                while (isInputRequired0) {
                    System.out.println("Podaj numer oznaczajacy rodzaj kontenera:");
                    System.out.println("Kontener podstawowy - 1");
                    System.out.println("Kontener chlodniczy- 2");
                    System.out.println("Kontener na materialy wybuchowe - 3");
                    System.out.println("Kontener ciezki- 4");
                    System.out.println("Kontener na toksyczne materialy sypkie - 5");
                    System.out.println("Kontener na toksyczne materialy ciekle - 6");
                    System.out.println("Kontener na materialy ciekle - 7");
                    String wybranyRodzaj = sc.nextLine();
                    try {
                        rodzajKontenera = Integer.parseInt(wybranyRodzaj);
                        if (rodzajKontenera > 7 || rodzajKontenera <= 0) {
                            System.out.println("Nie ma takiego rodzaju, zakres 1-7");
                        } else {
                            isInputRequired0 = false;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Zly format - wymagany int");
                    }
                }


                int idNadawcy = 0;
                isInputRequired0 = true;
                while (isInputRequired0) {
                    System.out.println("podaj numer magazynu:");
                    String numerNadawcy = sc.nextLine();
                    try {
                        idNadawcy = Integer.parseInt(numerNadawcy);
                        isInputRequired0 = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Zly format - wymagany int");
                    }
                }
                Nadawca podanyNadawca = null;
                for (Nadawca nadawca : listaNadawcow) {
                    if (nadawca.getId() == idNadawcy) {
                        podanyNadawca = nadawca;
                    }
                }
                if (podanyNadawca == null) {
                    System.out.println("Nie znaleziono nadawcy");
                }


                double tara = 0;
                isInputRequired0 = true;
                while (isInputRequired0) {
                    System.out.println("Podaj tare:");
                    String podanaTara = sc.nextLine();
                    try {
                        tara = Double.parseDouble(podanaTara);
                        isInputRequired0 = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Zly format - wymagany double");
                    }
                }


                String zabezpieczenia;
                System.out.println("Podaj zabezpieczenia:");
                zabezpieczenia = sc.nextLine();


                double wagaNetto = 0;
                isInputRequired0 = true;
                while (isInputRequired0) {
                    System.out.println("Podaj wageNetto:");
                    String podanawagaNetto = sc.nextLine();
                    try {
                        wagaNetto = Double.parseDouble(podanawagaNetto);
                        isInputRequired0 = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Zly format - wymagany double");
                    }
                }

                String certyfikaty;
                System.out.println("Podaj certyfikaty:");
                certyfikaty = sc.nextLine();


                switch (rodzajKontenera) {
                    case 1:
                        nowyKontener = new KontenerPodstawowy(podanyNadawca, tara, zabezpieczenia, wagaNetto, certyfikaty);

                        break;
                    case 2:
                        String material;
                        System.out.println("Podaj material:");
                        material = sc.nextLine();

                        int temperaturaChlodzenia = 0;
                        isInputRequired0 = true;
                        while (isInputRequired0) {
                            System.out.println("Podaj temperature chlodzenia:");
                            String podanaWartosc = sc.nextLine();
                            try {
                                int wartosc = Integer.parseInt(podanaWartosc);
                                if (wartosc < 0) {
                                    temperaturaChlodzenia = wartosc;
                                    isInputRequired0 = false;
                                } else {
                                    System.out.println("Podaj wartosc ujemna");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Zly format - wymagany int");
                            }
                        }
                        nowyKontener = new KontenerChlodniczy(podanyNadawca, tara, zabezpieczenia, wagaNetto, certyfikaty, material, temperaturaChlodzenia);
                        break;
                    case 3:

                        int szerokosc = 0;
                        isInputRequired0 = true;
                        while (isInputRequired0) {
                            System.out.println("Podaj tare:");
                            String podanaSzerokosc = sc.nextLine();
                            try {
                                szerokosc = Integer.parseInt(podanaSzerokosc);
                                isInputRequired0 = false;
                            } catch (NumberFormatException e) {
                                System.out.println("Zly format - wymagany int");
                            }
                        }

                        String material2;
                        System.out.println("Podaj material:");
                        material2 = sc.nextLine();

                        nowyKontener = new KontenerNaMaterialyWybuchowe(podanyNadawca, tara, zabezpieczenia, wagaNetto, certyfikaty, material2, szerokosc);
                        break;
                    case 4:
                        String material1;
                        System.out.println("Podaj material:");
                        material1 = sc.nextLine();

                        nowyKontener = new KontenerCiezki(podanyNadawca, tara, zabezpieczenia, wagaNetto, certyfikaty, material1);
                        break;

                    case 5:
                        String material4;
                        System.out.println("Podaj material:");
                        material4 = sc.nextLine();

                        boolean wodoszczelny = false;
                        isInputRequired0 = true;
                        while (isInputRequired0) {
                            System.out.println("Czy jest wodoszczelny (0 - Nie / 1 - Tak:");
                            String podanaWartosc = sc.nextLine();
                            try {
                                int wartosc = Integer.parseInt(podanaWartosc);
                                if (wartosc == 0) {
                                    wodoszczelny = false;
                                    isInputRequired0 = false;
                                }
                                if (wartosc == 1) {
                                    wodoszczelny = true;
                                    isInputRequired0 = false;
                                } else {
                                    System.out.println("Podaj wartosc 0 albo 1");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Zly format - wymagany int");
                            }
                        }
                        nowyKontener = new KontenerNaToksyczneMaterialySypkie(podanyNadawca, tara, zabezpieczenia, wagaNetto, certyfikaty, material4, wodoszczelny);

                        break;
                    case 6:

                        String material5;
                        System.out.println("Podaj material:");
                        material5 = sc.nextLine();


                        int objetosc1 = 0;
                        isInputRequired0 = true;
                        while (isInputRequired0) {
                            System.out.println("Podaj tare:");
                            String podanaObjetosc = sc.nextLine();
                            try {
                                objetosc1 = Integer.parseInt(podanaObjetosc);
                                isInputRequired0 = false;
                            } catch (NumberFormatException e) {
                                System.out.println("Zly format - wymagany int");
                            }
                        }

                        boolean warstwowy = false;
                        isInputRequired0 = true;
                        while (isInputRequired0) {
                            System.out.println("Czy potrzebuje zasilania (0 - Nie / 1 - Tak:");
                            String podanaWartosc1 = sc.nextLine();
                            try {
                                int wartosc = Integer.parseInt(podanaWartosc1);
                                if (wartosc == 0) {
                                    warstwowy = false;
                                    isInputRequired0 = false;
                                }
                                if (wartosc == 1) {
                                    warstwowy = true;
                                    isInputRequired0 = false;
                                } else {
                                    System.out.println("Podaj wartosc 0 albo 1");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Zly format - wymagany int");
                            }
                        }

                        nowyKontener = new KontenerNaToksyczneMaterialyCiekle(podanyNadawca, tara, zabezpieczenia, wagaNetto, certyfikaty, material5, warstwowy, objetosc1);
                        break;
                    case 7:
                        int objetosc = 0;
                        isInputRequired0 = true;
                        while (isInputRequired0) {
                            System.out.println("Podaj tare:");
                            String podanaObjetosc = sc.nextLine();
                            try {
                                objetosc = Integer.parseInt(podanaObjetosc);
                                isInputRequired0 = false;
                            } catch (NumberFormatException e) {
                                System.out.println("Zly format - wymagany int");
                            }
                        }
                        nowyKontener = new KontenerNaMaterialyCiekle(podanyNadawca, tara, zabezpieczenia, wagaNetto, certyfikaty, objetosc);
                        break;
                    default:
                        break;
                }

                int statekCzyMagazyn = 0;
                isInputRequired0 = true;
                while (isInputRequired0) {
                    System.out.println("Gdzie dodac nowy kontener(0 - statek / 1 - magazyn:");
                    String innaWartosc = sc.nextLine();
                    try {
                        int wartosc = Integer.parseInt(innaWartosc);
                        if (wartosc == 0) {
                            statekCzyMagazyn = 0;
                            isInputRequired0 = false;
                        }
                        if (wartosc == 1) {
                             statekCzyMagazyn = 1;
                            isInputRequired0 = false;
                        } else {
                            System.out.println("Podaj wartosc 0 albo 1");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Zly format - wymagany int");
                    }

                }
                if(statekCzyMagazyn == 0){
                    for(int i =0; i <listaStatkow.size(); i++){
                        if(listaStatkow.get(i).czyJestMiejsce()){
                            listaStatkow.get(i).zaladujKontener(nowyKontener);
                        }
                    }
                } else {
                    for(int i =0; i <listaMagazynow.size(); i++){
                    if(listaMagazynow.get(i).czyJestMiejsce()){
                        listaMagazynow.get(i).zaladujKontener(nowyKontener);
                    }
                }

                }

            }

                //METODA
                if (sentence.startsWith("wypisz informacje o magazynie")) {
                    boolean isInputRequired41 = true;
                    int idMagazynu = 0;
                    while (isInputRequired41) {
                        System.out.println("podaj numer magazynu:");
                        String numerMagazynu = sc.nextLine();
                        try {
                            idMagazynu = Integer.parseInt(numerMagazynu);
                            isInputRequired41 = false;
                        } catch (NumberFormatException e) {
                            System.out.println("Zly format - wymagany int");
                        }
                    }
                    Magazyn podanyMagazyn = null;
                    for (Magazyn magazyn : listaMagazynow) {
                        if (magazyn.getId() == idMagazynu) {
                            podanyMagazyn = magazyn;
                            System.out.println(podanyMagazyn.toString());
                        }
                    }
                    if (podanyMagazyn == null) {
                        System.out.println("Nie znaleziono magazynu");
                    }
                }


            //METODA
            if (sentence.startsWith("wypisz informacje o nadawcy")) {
                boolean isInputRequired40 = true;
                int idNadawcy = 0;
                while (isInputRequired40) {
                    System.out.println("podaj numer nadawcy:");
                    String numerNadawcy = sc.nextLine();
                    try {
                        idNadawcy = Integer.parseInt(numerNadawcy);
                        isInputRequired40 = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Zly format - wymagany int");
                    }
                }
                Nadawca podanyNadawca = null;
                for (Nadawca nadawca : listaNadawcow) {
                    if (nadawca.getId() == idNadawcy) {
                        podanyNadawca = nadawca;
                        System.out.println(podanyNadawca.toString());
                    }
                }
                if (podanyNadawca == null) {
                    System.out.println("Nie znaleziono nadawcy");
                }
            }

        //METODA
            if (sentence.startsWith("zapisz stan portu")) {
                System.out.println("podaj sciezke pliku:");
                String sciezka = sc.nextLine();
                zapisPliku(sciezka);
                System.out.println("Port zostal pomyslnie zapisany");
            }


        }
    }
}



