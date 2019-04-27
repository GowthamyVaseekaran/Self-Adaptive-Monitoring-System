package monitoring.core.entities.dbConfiguration;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

/**
 * CRUD Repo.
 */
public interface TestRepository extends CrudRepository<Metrics,Integer>{
    List<Metrics> findAll();
}
