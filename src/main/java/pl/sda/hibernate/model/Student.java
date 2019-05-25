package pl.sda.hibernate.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import javax.persistence.*;

@Entity
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String firstName;

  private String lastName;

  private LocalDate birthdate;

  @ManyToMany
  @JoinTable(
      name = "SchoolClassStudent",
      joinColumns = {@JoinColumn(name = "student_id")},
      inverseJoinColumns = {@JoinColumn(name = "schoolClass_id")})
  private List<SchoolClass> schoolClasses;

  public Student() {}

  public Student(Long id, String firstName, String lastName, LocalDate birthdate) {
    this.id = id;
    this.firstName = Objects.requireNonNull(firstName);
    this.lastName = Objects.requireNonNull(lastName);
    this.birthdate = Objects.requireNonNull(birthdate);
  }

  public Long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public LocalDate getBirthdate() {
    return birthdate;
  }

  public List<SchoolClass> getSchoolClasses() {
    return schoolClasses;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setBirthdate(LocalDate birthdate) {
    this.birthdate = birthdate;
  }

  public void setSchoolClasses(List<SchoolClass> schoolClasses) {
    this.schoolClasses = schoolClasses;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Student.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("firstName='" + firstName + "'")
        .add("lastName='" + lastName + "'")
        .add("birthdate=" + birthdate)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Student student = (Student) o;
    return Objects.equals(id, student.id)
        && firstName.equals(student.firstName)
        && lastName.equals(student.lastName)
        && birthdate.equals(student.birthdate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, birthdate);
  }
}
