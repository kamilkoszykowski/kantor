package pl.rest.kantor.repozytoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.rest.kantor.tabele.historia;

import java.util.List;

@Repository
public interface historiaRepo extends JpaRepository<historia, Integer> {

}
