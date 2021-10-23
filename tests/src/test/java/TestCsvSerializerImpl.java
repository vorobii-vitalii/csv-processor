import com.csv.processor.impl.CsvSerializerImpl;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCsvSerializerImpl {

  private static final char CUSTOM_DELIMITER = ',';
  private CsvSerializerImpl serializer;

  private Person personToSerialize;

  private final List<String> lines = new ArrayList<>();
  private final List<Boolean> isHeaderList = new ArrayList<>();

  @Test
  void testSerializeSingleGivenStandardConfiguration() {
    givenStandardConfiguration();
    givenPerson(new Person("George", 22, LocalDate.now(), "Green"));
    whenSerializeSingle();
    thenExpectHeader("name;Age;Favourite color");
    thenExpectLines("George;22;Green");
  }

  @Test
  void testSerializeSingleGivenCustomDelimiter() {
    givenCustomDelimiter();
    givenPerson(new Person("George", 22, LocalDate.now(), "Green"));
    whenSerializeSingle();
    thenExpectHeader("name,Age,Favourite color");
    thenExpectLines("George,22,Green");
  }

  private void givenStandardConfiguration() {
    serializer = new CsvSerializerImpl();
  }

  private void givenCustomDelimiter() {
    serializer = new CsvSerializerImpl(CUSTOM_DELIMITER);
  }

  private void thenExpectLines(String ... expectedLines) {
    assertThat(lines.stream().skip(1).collect(Collectors.toList()), equalTo(List.of(expectedLines)));
    assertTrue(isHeaderList.stream().skip(1).noneMatch(a -> a));
  }

  private void thenExpectHeader(String header) {
    assertThat(lines.get(0), equalTo(header));
    assertThat(isHeaderList.get(0), equalTo(true));
  }

  private void givenPerson(Person person) {
    personToSerialize = person;
  }

  private void whenSerializeSingle() {
    serializer.serializeSingle(personToSerialize, Person.class, (a, b) -> {
      lines.add(a);
      isHeaderList.add(b);
    });
  }

}
