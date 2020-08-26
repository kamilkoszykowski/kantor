package pl.rest.kantor.repozytoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.rest.kantor.DTO.kursyDTO;
import pl.rest.kantor.tabele.kursy;

import java.util.List;

@Repository
public interface kursyRepo extends JpaRepository<kursy, Integer> {

    @Query("SELECT new pl.rest.kantor.DTO.kursyDTO(u.data) FROM kursy u WHERE u.id = :id")
    List<kursyDTO> dataKursuPoId(@Param("id") int id);

    @Query("SELECT new pl.rest.kantor.DTO.kursyDTO(u.id, u.GBP, u.HKD, u.IDR, u.ILS, u.DKK, u.INR, u.CHF, u.MXN, u.CZK, u.SGD, u.THB, u.HRK, u.EUR, u.MYR, u.NOK, u.CNY, u.BGN, u.PHP, u.PLN, u.ZAR, u.CAD, u.ISK, u.BRL, u.RON, u.NZD, u.TRY, u.JPY, u.RUB, u.KRW, u.USD, u.AUD, u.HUF, u.SEK, u.data) FROM kursy u ORDER BY u.data DESC")
    List<kursyDTO> kursyByDataDesc();

    @Query("FROM kursy u ORDER BY u.data DESC")
    List<kursy> kursyDesc();
}
