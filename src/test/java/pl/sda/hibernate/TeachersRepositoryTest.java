package pl.sda.hibernate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import pl.sda.hibernate.model.SchoolClass;
import pl.sda.hibernate.model.Teacher;
import pl.sda.hibernate.utils.HibernateBootstraper;
import pl.sda.test.base.DatabaseSetupTest;

public class TeachersRepositoryTest {

  @RegisterExtension static DatabaseSetupTest db = new DatabaseSetupTest();

  private EntityManagerFactory factory = HibernateBootstraper.createEntityManagerFactory();

  private EntityManager entityManager = factory.createEntityManager();

  private TeachersRepository teachersRepository = new TeachersRepository(entityManager);

  @Test
  @DisplayName("All name details should be returned")
  void testGetTeachersNameDetails() {
    assertThat(teachersRepository.getTeachersNames())
        .containsExactlyInAnyOrder("Damian Lewandowski", "Beata Woźniak", "Artur Wójcik");
  }

  @Test
  @DisplayName("When pagination is provided, correct results should be returned")
  void testTeachersPaging() {
    System.out.println(teachersRepository.getTeachersPaging(0, 2));
    assertThat(teachersRepository.getTeachersPaging(0, 2))
        .containsExactlyInAnyOrder(
            new Teacher(1L, "Damian", "Lewandowski", Collections.emptySet()),
            new Teacher(3L, "Artur", "Wójcik", Collections.emptySet()));

    assertThat(teachersRepository.getTeachersPaging(1, 2))
        .containsExactlyInAnyOrder(new Teacher(2L, "Beata", "Woźniak", Collections.emptySet()));
  }

  @Test
  @DisplayName("Teacher should be able to be assigned to class.")
  void testAssignToSchoolClass() {
    entityManager.getTransaction().begin();
    Teacher teacher = entityManager.find(Teacher.class, 1L);
    SchoolClass schoolClass = entityManager.find(SchoolClass.class, 2L);

    teacher.assignToSchoolClass(schoolClass);
    entityManager.getTransaction().commit();

    assertThat(entityManager.find(Teacher.class, 1L).getSchoolClasses()).contains(schoolClass);
    assertThat(entityManager.find(SchoolClass.class, 2L).getTeacher()).isEqualTo(teacher);
  }

  @Test
  @DisplayName("Should prevent removing teacher.")
  void testPreRemove() {
    entityManager.getTransaction().begin();
    Teacher teacher = entityManager.find(Teacher.class, 1L);

    assertThrows(IllegalStateException.class, () -> entityManager.remove(teacher));

    teacher.getSchoolClasses().clear();
    entityManager.remove(teacher);
    entityManager.getTransaction().commit();
  }

  @Test
  @DisplayName("Should prevent removing teacher.")
  void testGetTeacherInitials() {
    assertThat(teachersRepository.getTeacherInitials()).isEqualTo("D.L.,B.W.,A.W.");
  }
}
