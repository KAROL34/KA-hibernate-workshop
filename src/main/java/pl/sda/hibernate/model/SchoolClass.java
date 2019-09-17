package pl.sda.hibernate.model;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import javax.persistence.*;

@Entity
@NamedQuery(name = "getSchoolClassByName", query = "from SchoolClass t where name = :name")
public class SchoolClass {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ManyToOne private Teacher teacher;

  @ManyToMany(mappedBy = "schoolClasses")
  private List<Student> students;

  @OneToMany(mappedBy = "schoolClass")
  private List<Test> tests;

  @CollectionTable(name = "LessonTopics", joinColumns = @JoinColumn(name = "schoolClass_id"))
  @Column(name = "topic")
  @ElementCollection
  private List<String> lessonTopics;

  public SchoolClass() {}

  public SchoolClass(Long id, String name) {
    Objects.requireNonNull(name);

    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Teacher getTeacher() {
    return teacher;
  }

  public void setTeacher(Teacher teacher) {
    this.teacher = teacher;
    teacher.getSchoolClasses().add(this);
  }

  public List<Student> getStudents() {
    return students;
  }

  public List<Test> getTests() {
    return tests;
  }

  public List<String> getLessonTopics() {
    return lessonTopics;
  }

  public void addLessonTopic(String topic) {
    this.lessonTopics.add(topic);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SchoolClass that = (SchoolClass) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", SchoolClass.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("name='" + name + "'")
        .toString();
  }
}
