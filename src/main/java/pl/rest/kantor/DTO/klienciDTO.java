package pl.rest.kantor.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class klienciDTO {

    public klienciDTO(Integer id) {
        this.id = id;
    }

    public klienciDTO(String PESEL, String numer_rachunku) {
        this.PESEL = PESEL;
        this.numer_rachunku = numer_rachunku;
    }

    public klienciDTO(Integer id, String imię, String nazwisko, LocalDate data_urodzenia, String PESEL, String numer_rachunku) {
        this.id = id;
        this.imię = imię;
        this.nazwisko = nazwisko;
        this.data_urodzenia = data_urodzenia;
        this.PESEL = PESEL;
        this.numer_rachunku = numer_rachunku;
    }

    private Integer id;
    private String imię;
    private String nazwisko;
    private LocalDate data_urodzenia;
    private String PESEL;
    private String numer_rachunku;
}
