package pl.sda.hibernate;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import pl.sda.hibernate.model.*;

public class SchoolClassRepository {

  private EntityManager entityManager;

  public SchoolClassRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public List<Test> getAllTestsBySchoolClassId(long id) {
    return entityManager.find(SchoolClass.class, id).getTests();
  }

  public List<VerbalTest> getAllVerbalTestsBySchoolClassId(long id) {
    return entityManager
        .createQuery(
            "select vt from VerbalTest vt join vt.schoolClass sc where sc.id = :id",
            VerbalTest.class)
        .setParameter("id", id)
        .getResultStream()
        .collect(Collectors.toList());
  }

  public <T extends Test> List<T> getTestsByType(Class<T> type) {
    return entityManager
        .createQuery("from Test t where type(t) = :type", Test.class)
        .setParameter("type", type)
        .getResultStream()
        .map(t -> (T) t)
        .collect(Collectors.toList());
  }
}
