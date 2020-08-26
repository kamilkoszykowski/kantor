package pl.rest.kantor.tabele;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;

@Entity
@Setter
@Getter
public class klienci {

    @Id
    private Integer id;
    private String imię;
    private String nazwisko;
    private LocalDate data_urodzenia;
    private String PESEL;
    private String numer_rachunku;

}
