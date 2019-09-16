package pl.sda.hibernate;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import pl.sda.hibernate.model.SchoolClass;
import pl.sda.hibernate.model.Student;

public class StudentsRepository {

  private EntityManager entityManager;

  public StudentsRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public Optional<Student> findStudentById(Long id) {
    return Optional.ofNullable(entityManager.find(Student.class, id));
  }

  public Student findStudentByIdLazily(Long id) {
    return entityManager.getReference(Student.class, id);
  }

  public Student createStudent(Student student) {
    entityManager.getTransaction().begin();
    entityManager.persist(student);
    entityManager.getTransaction().commit();
    return student;
  }

  public void updateStudent(Student student) {
    entityManager.getTransaction().begin();
    entityManager.merge(student);
    entityManager.getTransaction().commit();
  }

  public void deleteStudent(Student student) {
    entityManager.getTransaction().begin();
    entityManager.remove(student);
    entityManager.getTransaction().commit();
  }

  public void refreshStudent(Student student) {
    entityManager.getTransaction().begin();
    entityManager.refresh(student);
    entityManager.getTransaction().commit();
  }

  public List<Student> getStudents() {
    return entityManager.createQuery("from Student", Student.class).getResultList();
  }

  public long getStudentsCount() {
    return entityManager
        .createQuery("select count(s) from Student s", Long.class)
        .getSingleResult();
  }

  public List<Student> findStudentsByName(String name) {
    TypedQuery<Student> query =
        entityManager.createQuery(
            "from Student s where lower(s.firstName) like lower(:name) or lower(s.lastName) like lower(:name)",
            Student.class);
    query.setParameter("name", name + "%");
    return query.getResultList();
  }

  public Optional<Student> getStudentByName(String name) {
    TypedQuery<Student> query =
        entityManager.createQuery(
            "from Student s where lower(s.firstName) like lower(:name) or lower(s.lastName) like lower(:name)",
            Student.class);
    query.setParameter("name", name);
    try {
      return Optional.of(query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  public List<SchoolClass> getClassesByStudentId(long studentId) {
    TypedQuery<SchoolClass> query =
        entityManager.createQuery(
            "select sc from SchoolClass sc join sc.students s where s.id = :id", SchoolClass.class);
    query.setParameter("id", studentId);
    return query.getResultList();
  }

  public List<Student> getAllFriendStudentsByStudentId(long studentId) {
    return entityManager
        .createQuery(
            "select distinct st from Student s "
                + "join s.schoolClasses sc join sc.students st "
                + "join fetch st.schoolClasses where s.id = :id and st.id != :id",
            Student.class)
        .setParameter("id", studentId)
        .getResultList();
  }
}
