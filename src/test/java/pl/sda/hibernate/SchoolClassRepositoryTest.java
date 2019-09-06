package pl.sda.hibernate;

import static org.assertj.core.api.Java6Assertions.assertThat;

import java.time.LocalDate;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.sda.hibernate.model.SchoolClass;
import pl.sda.hibernate.model.VerbalTest;
import pl.sda.hibernate.model.WrittenTest;
import pl.sda.hibernate.utils.HibernateBootstraper;
import pl.sda.test.base.DatabaseSetup;

class SchoolClassRepositoryTest {

  @RegisterExtension static DatabaseSetup db = new DatabaseSetup();

  private EntityManagerFactory factory = HibernateBootstraper.createEntityManagerFactory();
  private EntityManager entityManager = factory.createEntityManager();

  private SchoolClassRepository schoolClassRepository = new SchoolClassRepository(entityManager);

  @Test
  void testGetAllTestsBySchoolClassId() {
    assertThat(schoolClassRepository.getAllTestsBySchoolClassId(1L))
        .containsExactlyInAnyOrder(
            new WrittenTest(6L, "Funkcje kwadratowe", LocalDate.parse("2019-02-03"), 60L),
            new VerbalTest(1L, "Ułamki", LocalDate.parse("2019-02-03"), false),
            new VerbalTest(2L, "Funkcje liniowe", LocalDate.parse("2019-02-03"), false));
  }

  @Test
  void testGetAllVerbalTests() {
    assertThat(schoolClassRepository.getAllVerbalTestsBySchoolClassId(1L))
        .containsExactlyInAnyOrder(
            new VerbalTest(1L, "Ułamki", LocalDate.parse("2019-02-03"), false),
            new VerbalTest(2L, "Funkcje liniowe", LocalDate.parse("2019-02-03"), false));
  }

  @Test
  void testGetTestsByType() {
    assertThat(schoolClassRepository.getTestsByType(WrittenTest.class))
        .containsExactlyInAnyOrder(
            new WrittenTest(6L, "Funkcje kwadratowe", LocalDate.parse("2019-02-03"), 60L),
            new WrittenTest(7L, "Mechanika", LocalDate.parse("2019-03-11"), 60L),
            new WrittenTest(8L, "Chemia organiczna", LocalDate.parse("2019-12-03"), 120L));

    assertThat(schoolClassRepository.getTestsByType(VerbalTest.class))
        .containsExactlyInAnyOrder(
            new VerbalTest(1L, "Ułamki", LocalDate.parse("2019-02-03"), false),
            new VerbalTest(2L, "Funkcje liniowe", LocalDate.parse("2019-02-03"), false),
            new VerbalTest(3L, "Prezentacja o polimerach", LocalDate.parse("2019-03-11"), true),
            new VerbalTest(4L, "Ruch liniowy", LocalDate.parse("2019-12-03"), false),
            new VerbalTest(5L, "Prezentacja z optyki", LocalDate.parse("2019-02-03"), true));
  }

  @Test
  void testAddTopic() {

    entityManager.getTransaction().begin();

    entityManager.find(SchoolClass.class, 1L).addLessonTopic("Dzielenie");

    entityManager.getTransaction().commit();

    assertThat(
            new JdbcTemplate(db.getDatasource())
                .queryForObject(
                    "SELECT lessonTopics FROM SchoolClass_lessonTopics WHERE lessonTopics = ?",
                    String.class,
                    "Dzielenie"))
        .isEqualTo("Dzielenie");
  }
}
