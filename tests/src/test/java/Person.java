import com.csv.processor.annotations.CsvField;
import com.csv.processor.annotations.Transient;

import java.time.LocalDate;
import java.util.Objects;

public class Person {

  private String name;

  @CsvField(name = "Age", position = 2)
  private Integer age;

  @Transient
  private LocalDate birthDate;

  @CsvField(name = "Favourite color", position = 3)
  private String favouriteColor;

  // Used by csv-processor to create instance!
  public Person() {
  }

  public Person(String name, Integer age, LocalDate birthDate, String favouriteColor) {
    this.name = name;
    this.age = age;
    this.birthDate = birthDate;
    this.favouriteColor = favouriteColor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return Objects.equals(name, person.name) && Objects.equals(age, person.age) && Objects.equals(birthDate, person.birthDate) && Objects.equals(favouriteColor, person.favouriteColor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, age, birthDate, favouriteColor);
  }
}
