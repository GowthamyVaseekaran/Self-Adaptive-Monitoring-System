//package monitoring.core.Entities.DBConfiguration;
//
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//import java.util.List;
//
//@Transactional
//@Repository
//public class TestRepositoryImplementation implements TestRepository {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Override
//    public List<Metrics> findAllByCpuAndMemory() {
//        Query query = entityManager.createQuery("SELECT cpu FROM Metrics cpu",
//                Metrics.class);
//        return query.getResultList();
//    }
//
//
//}
