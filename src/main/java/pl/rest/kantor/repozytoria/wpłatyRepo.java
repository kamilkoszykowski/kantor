package pl.rest.kantor.repozytoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.rest.kantor.tabele.wpłaty;

@Repository
public interface wpłatyRepo extends JpaRepository<wpłaty, Integer> {

}
