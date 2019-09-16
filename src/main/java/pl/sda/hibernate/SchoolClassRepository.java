package pl.sda.hibernate;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

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
        .getResultList();
  }

  @SuppressWarnings("unchecked")
  public <T extends Test> List<T> getTestsByType(Class<T> type) {
    return (List<T>)
        entityManager
            .createQuery("from Test t where type(t) = :type", Test.class)
            .setParameter("type", type)
            .getResultList();
  }

  public Optional<SchoolClass> getSchoolClassByName(String name) {
    try {
      return Optional.of(
              entityManager
              .createNamedQuery("getSchoolClassByName", SchoolClass.class)
              .setParameter("name", name)
              .getSingleResult()
      );
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  public List<String> getTopics(List<Long> ids) {
      return entityManager
                      .createQuery("select st from SchoolClass sc join sc.lessonTopics st where sc.id IN(:ids)", String.class)
                      .setParameter("ids", ids)
                      .getResultList();

  }
}
