package pl.rest.kantor.repozytoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.rest.kantor.DTO.kontaDTO;
import pl.rest.kantor.tabele.konta;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface kontaRepo extends JpaRepository<konta, Integer> {

    @Query("FROM konta u WHERE u.id_klienta = :id_klienta AND u.waluta='PLN'")
    List<konta> kontaPLNByIdKlienta(@Param("id_klienta") int id_klienta);

    @Query("SELECT new pl.rest.kantor.DTO.kontaDTO(u.id, u.ilość) FROM konta u WHERE u.id_klienta = :id_klienta AND u.waluta='PLN'")
    List<kontaDTO> iDkontaPLNByIdKlienta(@Param("id_klienta") int id_klienta);

    @Query("SELECT new pl.rest.kantor.DTO.kontaDTO(u.ilość) FROM konta u WHERE u.id = :id")
    List<kontaDTO> saldoKontaByIdKonta(@Param("id") int id);

    @Query("FROM konta u WHERE u.id_klienta = :id AND u.waluta = :waluta")
    List<konta> kontoByIdKlientaIWalucie(@Param("id") int id_klienta, @Param("waluta") String waluta);

    @Query("SELECT new pl.rest.kantor.DTO.kontaDTO(u.id, u.ilość) FROM konta u WHERE u.id_klienta = :id AND u.waluta = :waluta")
    List<kontaDTO> idKontaByIdKlientaIWalucie(@Param("id") int id_klienta, @Param("waluta") String waluta);

    @Transactional
    @Modifying
    @Query("UPDATE konta u SET u.ilość = :ilosc, u.data_modyfikacji = :data WHERE u.id = :id")
    void dodajWplateNaKonto(@Param("ilosc") double ilosc, @Param("data")LocalDateTime data, @Param("id") int id);

    @Query("FROM konta u WHERE u.waluta = :waluta AND u.ilość < :ilosc ORDER BY u.ilość ASC")
    List<konta> kontoByWalucieIIlosciASC(@Param("waluta") String waluta, @Param("ilosc") double ilosc);

    @Query("FROM konta u WHERE u.waluta = :waluta AND u.ilość > :ilosc ORDER BY u.ilość DESC")
    List<konta> kontoByWalucieIIlosciDESC(@Param("waluta") String waluta, @Param("ilosc") double ilosc);

    @Query("FROM konta u WHERE u.waluta = :waluta AND u.ilość = :ilosc")
    List<konta> kontoByWalucieIIlosci(@Param("waluta") String waluta, @Param("ilosc") double ilosc);

    @Query("FROM konta u WHERE u.waluta = :waluta")
    List<konta> kontoByWalucie(@Param("waluta") String waluta);


}
