package pl.rest.kantor.repozytoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.rest.kantor.DTO.klienciDTO;
import pl.rest.kantor.tabele.klienci;

import java.util.List;

@Repository
public interface klienciRepo extends JpaRepository<klienci, Integer> {

    @Query("SELECT new pl.rest.kantor.DTO.klienciDTO(u.PESEL, u.numer_rachunku) FROM klienci u")
    List<klienciDTO> numeryRachunkowIPESELe();

    @Query("FROM klienci u WHERE u.numer_rachunku = :numer")
    List<klienci> klienciPoNumerzeRachunku(@Param("numer") String numer);

    @Query("SELECT new pl.rest.kantor.DTO.klienciDTO(u.id) FROM klienci u WHERE u.numer_rachunku = :numer")
    List<klienciDTO> idKlientaPoNumerzeRachunku(@Param("numer") String numer);

    @Query("FROM klienci u WHERE u.id = ?1")
    List<klienci> findAllById(int id);

    @Query("FROM klienci u WHERE u.imię = ?1")
    List<klienci> findAllByImię(String imie);

    @Query("FROM klienci u WHERE u.nazwisko = ?1")
    List<klienci> findAllByNazwisko(String nazwisko);

    @Query("FROM klienci u WHERE u.numer_rachunku = ?1")
    List<klienci> findAllByNumer_rachunku(String numer_rachunku);

    @Query("FROM klienci u WHERE u.PESEL = ?1")
    List<klienci> findAllByPESEL(String PESEL);
}
