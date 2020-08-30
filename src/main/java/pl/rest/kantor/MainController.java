package pl.rest.kantor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.rest.kantor.repozytoria.historiaRepo;
import pl.rest.kantor.repozytoria.klienciRepo;
import pl.rest.kantor.repozytoria.kontaRepo;
import pl.rest.kantor.repozytoria.kursyRepo;
import pl.rest.kantor.repozytoria.wpłatyRepo;
import pl.rest.kantor.DTO.klienciDTO;
import pl.rest.kantor.DTO.kontaDTO;
import pl.rest.kantor.DTO.kursyDTO;
import pl.rest.kantor.tabele.historia;
import pl.rest.kantor.tabele.klienci;
import pl.rest.kantor.tabele.konta;
import pl.rest.kantor.tabele.kursy;
import pl.rest.kantor.tabele.wpłaty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;


@RestController
@Controller
public class MainController {

    @Autowired
    private historiaRepo historiaRepo;

    @Autowired
    private klienciRepo klienciRepo;

    @Autowired
    private kontaRepo kontaRepo;

    @Autowired
    private kursyRepo kursyRepo;

    @Autowired
    private wpłatyRepo wpłatyRepo;

    //[HELP]
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping(value = "/pomoc")
    public String pomoc() {
        return  "AKTUALIZACJA KURSÓW\n" + "/kursy/aktualizuj\n\n" +
                "DODAWANIE KLIENTÓW\n" + "/klienci/dodaj/imie:nazwisko:data_urodzenia(rrrr-mm-dd):PESEL:numer_rachunku\n\n" +
                "DODAWANIE WPŁAT\n" + "/wplaty/dodaj/numer_rachunku:kwota\n\n" +
                "WYMIANA WALUTY (NA DANĄ ILOŚĆ)\n" + "przykład wymiany x PLN na 1000USD: /wymiana/klient:1->PLN:1000->USD\n" +
                "WYMIANA WALUTY (NA DANĄ ILOŚĆ)\n" + "przykład wymiany x PLN na 1000USD: /wymiana/klient:1->PLN->USD:1000\n" +
                "WYŚWIETLANIE WSZYSTKICH REKORDÓW\n" + "/klienci /kursy /wplaty /konta /historia\n+" +
                "WYŚWIETLANIE WSZYSTKICH REKORDÓW PO id\n" + "/klienci/{id} ...";
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //[KURSY]
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping(value = "/kursy")
    public List<kursy> kursy() {
        return kursyRepo.kursyDesc();
    }

    @GetMapping(value = "/kursy/aktualizuj")
    public String aktualizujKursy() {
        if (czyPotrzebnaAktualizacja()) {
            kursyRepo.save(zapisywanieKursow());
            return "Zaktualizowano pomyślnie.";
        } else {
            return "Kursy walut są aktualne." ;
        }
    }

    @GetMapping(value = "/testPolaczeniaZApi")
    public String testApiConnection() {
        LinkedHashMap<String, Double> lhm = GsonReader.danePobraneZApi();
        if (lhm.size()>0) {
            return "Pomyślnie połączono z API i poprano dane! (" + lhm.size() + " linii danych)" ;
        } else {
            return "Błąd! Połączenie z API i pobranie danych nie powiodło się.";
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //[KLIENCI]
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping(value = "/klienci")
    public List<klienci> klienci() {
        return klienciRepo.findAll();
    }

    @PostMapping(value = "/klienci/dodaj/{klient}")
    public String dodajKlienta(@PathVariable String klient) {
        String[] dane = klient.split(":");
        if (czyDaneKlientaSaPoprawne(dane)) {
            if (czyPESELiNumerRachunkuSaUnikalne(dane)) {
                klienciRepo.save(zapisywanieKlientow(dane));
                return "Pomyślnie dodano klienta do bazy danych.";
            } else {
                return "Błąd! W bazie występuje klient o danym numerze konta lub PESEL.";
            }
        } else {
            return "Dodawanie nie powiodło się! Wprowadź poprawne dane i spróbuj ponownie!";
        }
    }

    @GetMapping(value = "/klienci/id/{id}")
    public List<klienci> klienciPoId(@PathVariable int id) {
        return klienciRepo.findAllById(id);
    }

    @GetMapping(value = "/klienci/imie/{imie}")
    public List<klienci> klienciPoImieniu(@PathVariable String imie) {
        return klienciRepo.findAllByImię(imie);
    }

    @GetMapping(value = "/klienci/nazwisko/{nazwisko}")
    public List<klienci> klienciPoNazwisku(@PathVariable String nazwisko) {
        return klienciRepo.findAllByNazwisko(nazwisko);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //[WPŁATY]
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping(value = "/wplaty")
    public List<wpłaty> wplaty() {
        return wpłatyRepo.findAll();
    }

    @PostMapping(value = "wplaty/dodaj/{wplata}")
    public String dodajWplate(@PathVariable String wplata) {
        String[] dane = wplata.split(":");
        if (czyDaneWplatySaPoprawne(dane)) {
            if (czyIstniejeKlientZRachunkiemZWplaty(dane)) {
                List<klienciDTO> klient = klienciRepo.idKlientaPoNumerzeRachunku(dane[0]);
                int id = klient.get(0).getId();
                if (czyIstniejeKontoPLNDlaDanegoKlienta(id)) {
                    List<kontaDTO> konto = kontaRepo.iDkontaPLNByIdKlienta(id);
                    int id_konta = konto.get(0).getId();
                    double ilosc = konto.get(0).getIlość();
                    double kwota = Double.parseDouble(dane[1]);
                    wpłatyRepo.save(zapisywanieWplat(dane));
                    kontaRepo.dodajWplateNaKonto(ilosc + kwota, LocalDateTime.now(), id_konta);
                    return "Pomyślnie dodano wpłatę do bazy danych oraz zaktualizowano stan konta klienta.";
                } else {
                    wpłatyRepo.save(zapisywanieWplat(dane));
                    kontaRepo.save(zapisywanieKontPLN(dane, id));
                    return "Pomyślnie utworzono konto z saldem równym kwocie wpłaty";
                }
            } else {
                return "Błąd! Brak klienta powiązanego z danym numerem rachunku";
            }

        } else {
            return "Błąd! Dane wpłaty są niepoprawne.";
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //[KONTA]
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping(value = "/konta")
    public List<konta> konta() {
        return kontaRepo.findAll();
    }

    @GetMapping(value = "/konta/{id_klienta}/{waluta}")
    public List<konta> kontaPoIdIWalucie(@PathVariable int id_klienta, @PathVariable String waluta) {
        return kontaRepo.kontoByIdKlientaIWalucie(id_klienta, waluta);
    }

    @GetMapping(value = "/konta/{waluta}")
    public List<konta> kontaPoIdWalucieIIlosci(@PathVariable String waluta) {
        return kontaRepo.kontoByWalucie(waluta);
    }

    @GetMapping(value = "/konta/{waluta}/>{ilosc}")
    public List<konta> kontaPoIdWalucieIIlosciDESC(@PathVariable String waluta, @PathVariable String ilosc) {
        double ilosc2 = Double.parseDouble(ilosc);
        return kontaRepo.kontoByWalucieIIlosciDESC(waluta, ilosc2);
    }

    @GetMapping(value = "/konta/{waluta}/<{ilosc}")
    public List<konta> kontaPoIdWalucieIIlosciASC(@PathVariable String waluta, @PathVariable String ilosc) {
        double ilosc2 = Double.parseDouble(ilosc);
        return kontaRepo.kontoByWalucieIIlosciASC(waluta, ilosc2);
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //[HISTORIA]
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping(value = "/historia")
    public List<historia> historia() {
        return historiaRepo.findAll();
    }

    @RequestMapping(value = "/wymiana/{wymiana}")
    public String wymiana(@PathVariable String wymiana) {
        String oldWymiana = wymiana;
        String[] dane = wymiana.replaceAll("klient:", "").replaceAll("->", ":").split(":");
        boolean format2 = false;

        if (oldWymiana.matches("klient:\\d+->\\w{3}->\\w{3}:\\d+")) {
            String temp = dane[2];
            dane[2] = dane[3];
            dane[3] = temp;
            format2 = true;
        }

        if (czyDaneKontaSaPoprawne(dane)) {
            List<kursyDTO> kursy = kursyRepo.kursyByDataDesc();
            int id_klienta = Integer.parseInt(dane[0]);
            String waluta_1 = dane[1];
            double ilosc_1 = Double.parseDouble(dane[2]);
            String waluta_2 = dane[3];
            double kurs_1;
            double kurs_2;
            switch (waluta_1) {
                case "GBP": kurs_1 = kursy.get(0).getGBP(); break;
                case "HKD": kurs_1 = kursy.get(0).getHKD(); break;
                case "IDR": kurs_1 = kursy.get(0).getIDR(); break;
                case "ILS": kurs_1 = kursy.get(0).getILS(); break;
                case "DKK": kurs_1 = kursy.get(0).getDKK(); break;
                case "INR": kurs_1 = kursy.get(0).getINR(); break;
                case "CHF": kurs_1 = kursy.get(0).getCHF(); break;
                case "MXN": kurs_1 = kursy.get(0).getMXN(); break;
                case "CZK": kurs_1 = kursy.get(0).getCZK(); break;
                case "SGD": kurs_1 = kursy.get(0).getSGD(); break;
                case "THB": kurs_1 = kursy.get(0).getTHB(); break;
                case "HRK": kurs_1 = kursy.get(0).getHRK(); break;
                case "EUR": kurs_1 = kursy.get(0).getEUR(); break;
                case "MYR": kurs_1 = kursy.get(0).getMYR(); break;
                case "NOK": kurs_1 = kursy.get(0).getNOK(); break;
                case "CNY": kurs_1 = kursy.get(0).getCNY(); break;
                case "BGN": kurs_1 = kursy.get(0).getBGN(); break;
                case "PHP": kurs_1 = kursy.get(0).getPHP(); break;
                case "PLN": kurs_1 = kursy.get(0).getPLN(); break;
                case "ZAR": kurs_1 = kursy.get(0).getZAR(); break;
                case "CAD": kurs_1 = kursy.get(0).getCAD(); break;
                case "ISK": kurs_1 = kursy.get(0).getISK(); break;
                case "BRL": kurs_1 = kursy.get(0).getBRL(); break;
                case "RON": kurs_1 = kursy.get(0).getRON(); break;
                case "NZD": kurs_1 = kursy.get(0).getNZD(); break;
                case "TRY": kurs_1 = kursy.get(0).getTRY(); break;
                case "JPY": kurs_1 = kursy.get(0).getJPY(); break;
                case "RUB": kurs_1 = kursy.get(0).getRUB(); break;
                case "KRW": kurs_1 = kursy.get(0).getKRW(); break;
                case "USD": kurs_1 = kursy.get(0).getUSD(); break;
                case "AUD": kurs_1 = kursy.get(0).getAUD(); break;
                case "HUF": kurs_1 = kursy.get(0).getHUF(); break;
                case "SEK": kurs_1 = kursy.get(0).getSEK(); break;
                default:
                    kurs_1 = 0.0;
            }
            switch (waluta_2) {
                case "GBP": kurs_2 = kursy.get(0).getGBP(); break;
                case "HKD": kurs_2 = kursy.get(0).getHKD(); break;
                case "IDR": kurs_2 = kursy.get(0).getIDR(); break;
                case "ILS": kurs_2 = kursy.get(0).getILS(); break;
                case "DKK": kurs_2 = kursy.get(0).getDKK(); break;
                case "INR": kurs_2 = kursy.get(0).getINR(); break;
                case "CHF": kurs_2 = kursy.get(0).getCHF(); break;
                case "MXN": kurs_2 = kursy.get(0).getMXN(); break;
                case "CZK": kurs_2 = kursy.get(0).getCZK(); break;
                case "SGD": kurs_2 = kursy.get(0).getSGD(); break;
                case "THB": kurs_2 = kursy.get(0).getTHB(); break;
                case "HRK": kurs_2 = kursy.get(0).getHRK(); break;
                case "EUR": kurs_2 = kursy.get(0).getEUR(); break;
                case "MYR": kurs_2 = kursy.get(0).getMYR(); break;
                case "NOK": kurs_2 = kursy.get(0).getNOK(); break;
                case "CNY": kurs_2 = kursy.get(0).getCNY(); break;
                case "BGN": kurs_2 = kursy.get(0).getBGN(); break;
                case "PHP": kurs_2 = kursy.get(0).getPHP(); break;
                case "PLN": kurs_2 = kursy.get(0).getPLN(); break;
                case "ZAR": kurs_2 = kursy.get(0).getZAR(); break;
                case "CAD": kurs_2 = kursy.get(0).getCAD(); break;
                case "ISK": kurs_2 = kursy.get(0).getISK(); break;
                case "BRL": kurs_2 = kursy.get(0).getBRL(); break;
                case "RON": kurs_2 = kursy.get(0).getRON(); break;
                case "NZD": kurs_2 = kursy.get(0).getNZD(); break;
                case "TRY": kurs_2 = kursy.get(0).getTRY(); break;
                case "JPY": kurs_2 = kursy.get(0).getJPY(); break;
                case "RUB": kurs_2 = kursy.get(0).getRUB(); break;
                case "KRW": kurs_2 = kursy.get(0).getKRW(); break;
                case "USD": kurs_2 = kursy.get(0).getUSD(); break;
                case "AUD": kurs_2 = kursy.get(0).getAUD(); break;
                case "HUF": kurs_2 = kursy.get(0).getHUF(); break;
                case "SEK": kurs_2 = kursy.get(0).getSEK(); break;
                default:
                    kurs_2 = 0.0;
            }
            double ilosc_2;

            if (format2) {
                ilosc_2 = (ilosc_1 / kurs_2);
                double temp = ilosc_1;
                ilosc_1 = ilosc_2;
                ilosc_2 = temp;
            } else {
                ilosc_2 = (kurs_2 / kurs_1) * ilosc_1;
            }

            if (czyIstniejeKontoDanegoKlientaZDanaWaluta(dane)) {
                List<kontaDTO> sprawdzenieSalda = kontaRepo.idKontaByIdKlientaIWalucie(id_klienta, waluta_1);
                double saldoIlosc = sprawdzenieSalda.get(0).getIlość();

                if (saldoIlosc < ilosc_1) {
                    return "Błąd. Brak wystarczającej ilości środków na koncie.";
                } else {
                    List<kontaDTO> konta = kontaRepo.idKontaByIdKlientaIWalucie(id_klienta, waluta_1);
                    int id_1 = konta.get(0).getId();
                    double saldo = konta.get(0).getIlość();
                    kontaRepo.dodajWplateNaKonto(saldo - ilosc_1, LocalDateTime.now(), id_1);

                    if (czyIstniejeKonto2DanegoKlientaZDanaWaluta(dane)) {
                        List<kontaDTO> konta_2 = kontaRepo.idKontaByIdKlientaIWalucie(id_klienta, waluta_2);
                        int id_2 = konta_2.get(0).getId();
                        double saldo_2 = konta_2.get(0).getIlość();
                        kontaRepo.dodajWplateNaKonto(saldo_2 + ilosc_2, LocalDateTime.now(), id_2);
                        historiaRepo.save(zapisywanieHistorii(dane, ilosc_2));

                        return "Wymiana walut zakończona sukcesem.";
                    } else {
                        kontaRepo.save(zapisywanieKont(dane, ilosc_2));
                        historiaRepo.save(zapisywanieHistorii(dane, ilosc_2));

                        return "Utworzono nowe konto walutowe. Wymiana walut zakończona sukcesem.";
                    }
                }
            } else {
                return "Błąd! Klient nie posiada konta z pierwszą walutą";
            }
        } else {
            return "Błąd! Niepoprawne dane.";
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //KURSY METODY
    public kursy zapisywanieKursow() {
        LinkedHashMap<String, Double> dane = GsonReader.danePobraneZApi();
        kursy k = new kursy();
        int ilosc = (int) kursyRepo.count();
        k.setId(ilosc + 1);
        k.setGBP(dane.get("GBP"));
        k.setHKD(dane.get("HKD"));
        k.setIDR(dane.get("IDR"));
        k.setILS(dane.get("ILS"));
        k.setDKK(dane.get("DKK"));
        k.setINR(dane.get("INR"));
        k.setCHF(dane.get("CHF"));
        k.setMXN(dane.get("MXN"));
        k.setCZK(dane.get("CZK"));
        k.setSGD(dane.get("SGD"));
        k.setTHB(dane.get("THB"));
        k.setHRK(dane.get("HRK"));
        k.setEUR(dane.get("EUR"));
        k.setMYR(dane.get("MYR"));
        k.setNOK(dane.get("NOK"));
        k.setCNY(dane.get("CNY"));
        k.setBGN(dane.get("BGN"));
        k.setPHP(dane.get("PHP"));
        k.setPLN(dane.get("PLN"));
        k.setZAR(dane.get("ZAR"));
        k.setCAD(dane.get("CAD"));
        k.setISK(dane.get("ISK"));
        k.setBRL(dane.get("BRL"));
        k.setRON(dane.get("RON"));
        k.setNZD(dane.get("NZD"));
        k.setTRY(dane.get("TRY"));
        k.setJPY(dane.get("JPY"));
        k.setRUB(dane.get("RUB"));
        k.setKRW(dane.get("KRW"));
        k.setUSD(dane.get("USD"));
        k.setAUD(dane.get("AUD"));
        k.setHUF(dane.get("HUF"));
        k.setSEK(dane.get("SEK"));
        k.setData(LocalDateTime.now());
        return k;
    }

    public LocalDateTime dataOstatniejAktualizacjiKursow() {
        int id = (int) kursyRepo.count();
        if (id > 0) {
            List<kursyDTO> list = kursyRepo.dataKursuPoId(id);
            LocalDateTime data = list.get(0).getData();
            return data;
        } else {
            return null;
        }

    }

    public boolean czyPotrzebnaAktualizacja() {
        boolean bool = false;
        LocalDateTime now = LocalDateTime.now();
        try {
            if (!(dataOstatniejAktualizacjiKursow().toLocalTime().isAfter(LocalTime.of(16, 30)) && dataOstatniejAktualizacjiKursow().plusDays(1).isAfter(now)) && !(now.getDayOfWeek().name().equals("SATURDAY") || now.getDayOfWeek().name().equals("SUNDAY"))) {
                bool = true;
            }
        } catch (NullPointerException e) {
            bool = true;
        }
        return bool;
    }


    //KLIENCI METODY
    public klienci zapisywanieKlientow(String[] dane) {
        klienci k = new klienci();
        int ilosc = (int) klienciRepo.count();
        k.setId(ilosc + 1);
        k.setImię(dane[0]);
        k.setNazwisko(dane[1]);
        k.setData_urodzenia(LocalDate.parse(dane[2]));
        k.setPESEL(dane[3]);
        k.setNumer_rachunku(dane[4]);

        return k;
    }

    public boolean czyDaneKlientaSaPoprawne(String[] dane) {
        return dane[0].length() > 0 && dane[1].length() > 0 && dane[2].matches("\\d{4}-\\d{2}-\\d{2}") && dane[3].length() == 11 && dane[4].length() == 26;
    }

    public boolean czyPESELiNumerRachunkuSaUnikalne(String[] dane) {
        boolean flag = true;
        String PESEL = dane[3];
        String numer_rachunku = dane[4];
        List<klienciDTO> klienci = klienciRepo.numeryRachunkowIPESELe();
        for (klienciDTO k : klienci) {
            if (k.getPESEL().equals(PESEL) || k.getNumer_rachunku().equals(numer_rachunku)) {
                flag = false;
                break;
            }
        }
    return flag;
    }

    public boolean czyIstniejeKlientZDanymNumeremRachunku(String[] dane) {
        List<klienci> klienci = klienciRepo.klienciPoNumerzeRachunku(dane[4]);
        boolean flag = true;
        try {
            flag = klienci.size() > 0;
        } catch (NullPointerException e) {
            flag = false;
        }
        return flag;
    }


    //WPŁATY METODY
    public wpłaty zapisywanieWplat(String[] dane) {
        wpłaty w = new wpłaty();
        int ilosc = (int) wpłatyRepo.count();
        w.setId(ilosc + 1);
        w.setNumer_rachunku(dane[0]);
        w.setKwota(Double.parseDouble(dane[1]));
        w.setData(LocalDateTime.now());

        return w;
    }

    public boolean czyDaneWplatySaPoprawne(String[] dane) {
        boolean flag = true;
        try {
            flag = dane[0].length() == 26 && Double.parseDouble(dane[1]) > 0;
        } catch (NumberFormatException e) {
            flag = false;
        }
        return flag;
    }

    public boolean czyIstniejeKlientZRachunkiemZWplaty(String[] dane) {
        List<klienci> klienci = klienciRepo.klienciPoNumerzeRachunku(dane[0]);
        boolean flag = true;
        try {
            flag = klienci.size() > 0;
        } catch (NullPointerException e) {
            flag = false;
        }
        return flag;
    }


    //KONTA METODY
    public konta zapisywanieKont(String[] dane, double ilosc_2) {
        konta k = new konta();
        int ilosc = (int) kontaRepo.count();
        k.setId(ilosc + 1);
        k.setId_klienta(Integer.parseInt(dane[0]));
        k.setWaluta(dane[3]);
        k.setIlość(ilosc_2);
        k.setData_modyfikacji(LocalDateTime.now());

        return k;
    }

    public konta zapisywanieKontPLN(String[] dane, int id) {
        konta k = new konta();
        int ilosc = (int) kontaRepo.count();
        k.setId(ilosc + 1);
        k.setId_klienta(id);
        k.setWaluta("PLN");
        k.setIlość(Double.parseDouble(dane[1]));
        k.setData_modyfikacji(LocalDateTime.now());

        return k;
    }

    public boolean czyDaneKontaSaPoprawne(String[] dane) {
        boolean flag = true;
        try {
            flag = Integer.parseInt(dane[0]) > 0 && dane[1].length() == 3 && Double.parseDouble(dane[2]) > 0;
        } catch (NumberFormatException e) {
            flag = false;
        }
        return flag;
    }

    public boolean czyIstniejeKontoPLNDlaDanegoKlienta(int id_klienta) {
        List<konta> konta = kontaRepo.kontaPLNByIdKlienta(id_klienta);
        boolean flag = true;
        try {
            flag = konta.size() > 0;
        } catch (NullPointerException e) {
            flag = false;
        }
        return flag;
    }

    public boolean czyIstniejeKontoDanegoKlientaZDanaWaluta(String[] dane) {
        List<konta> konta = kontaRepo.kontoByIdKlientaIWalucie(Integer.parseInt(dane[0]), dane[1]);
        boolean flag = true;
        try {
            flag = konta.size() > 0;
        } catch (NullPointerException e) {
            flag = false;
        }
        return flag;
    }

    public boolean czyIstniejeKonto2DanegoKlientaZDanaWaluta(String[] dane) {
        List<konta> konta = kontaRepo.kontoByIdKlientaIWalucie(Integer.parseInt(dane[0]), dane[3]);
        boolean flag = true;
        try {
            flag = konta.size() > 0;
        } catch (NullPointerException e) {
            flag = false;
        }
        return flag;
    }


    //HISTORIA METODY
    public historia zapisywanieHistorii(String[] dane, double ilosc_2) {
        historia h = new historia();
        int ilosc = (int) historiaRepo.count();
        h.setId(ilosc + 1);
        h.setId_klienta(Integer.parseInt(dane[0]));
        h.setWaluta_1(dane[1]);
        h.setIlość_1(Double.parseDouble(dane[2]));
        h.setWaluta_2(dane[3]);
        h.setIlość_2(ilosc_2);
        h.setData(LocalDateTime.now());

        return h;
    }
}
