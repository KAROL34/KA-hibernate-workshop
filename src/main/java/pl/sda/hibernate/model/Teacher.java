package pl.sda.hibernate.model;

import pl.sda.hibernate.model.dto.FullName;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import javax.persistence.*;

@Entity
public class Teacher {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private FullName fullName;

  @OneToMany(mappedBy = "teacher")
  private Set<SchoolClass> schoolClasses;

  public Teacher() {}

  public Teacher(Long id, String firstName, String lastName, Set<SchoolClass> schoolClasses) {
    this.schoolClasses = schoolClasses;
    Objects.requireNonNull(firstName);
    Objects.requireNonNull(lastName);

    this.id = id;
    this.fullName = new FullName(firstName, lastName);
  }

  public Long getId() {
    return id;
  }

  public String getFirstName() {
    return fullName.getFirstName();
  }

  public void setFirstName(String firstName) {
    this.fullName.setFirstName(firstName);
  }

  public String getLastName() {
    return fullName.getLastName();
  }

  public void setLastName(String lastName) {
    this.fullName.setLastName(lastName);
  }

  public Set<SchoolClass> getSchoolClasses() {
    return schoolClasses;
  }

  public void assignToSchoolClass(SchoolClass schoolClass) {
    schoolClasses.add(schoolClass);
    schoolClass.setTeacher(this);
  }

  @PreRemove
  void preRemove() {
    if (!schoolClasses.isEmpty()) {
      throw new IllegalStateException("Teacher is teaching classes");
    }
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Teacher.class.getSimpleName() + "[", "]")
        .add("id=" + getId())
        .add("firstName='" + getFirstName() + "'")
        .add("lastName='" + getLastName() + "'")
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Teacher student = (Teacher) o;
    return Objects.equals(id, student.getId())
        && getFirstName().equals(student.getFirstName())
        && getLastName().equals(student.getLastName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getFirstName(), getLastName());
  }
}
