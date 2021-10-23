import com.csv.processor.exception.InvalidTokensException;
import com.csv.processor.exception.ParseException;
import com.csv.processor.impl.CsvDeserializerImpl;
import com.csv.processor.parse.CsvTokenizer;
import com.csv.processor.parse.Token;
import com.csv.processor.parse.TokenType;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class TestCsvDeserializerImpl {

  private static final List<Token> PEOPLE_TOKEN_LIST =
    List.of(
      new Token("George", TokenType.VALUE, -1),
      new Token("22", TokenType.VALUE, -1),
      new Token("Green", TokenType.VALUE, -1),
      new Token(null, TokenType.NEW_LINE, -1),
      new Token("William", TokenType.VALUE, -1),
      new Token("21", TokenType.VALUE, -1),
      new Token("Blue", TokenType.VALUE, -1),
      new Token(null, TokenType.NEW_LINE, -1),
      new Token("Rebecca", TokenType.VALUE, -1),
      new Token("20", TokenType.VALUE, -1),
      new Token("Red", TokenType.VALUE, -1),
      new Token(null, TokenType.NEW_LINE, -1)
    );

  private static final List<Token> PEOPLE_TOKEN_LIST_WITH_HEADER =
    List.of(
      new Token("Favourite color", TokenType.VALUE, -1),
      new Token("Age", TokenType.VALUE, -1),
      new Token("name", TokenType.VALUE, -1),
      new Token(null, TokenType.NEW_LINE, -1),
      new Token("Green", TokenType.VALUE, -1),
      new Token("22", TokenType.VALUE, -1),
      new Token("George", TokenType.VALUE, -1),
      new Token(null, TokenType.NEW_LINE, -1),
      new Token("Blue", TokenType.VALUE, -1),
      new Token("21", TokenType.VALUE, -1),
      new Token("William", TokenType.VALUE, -1),
      new Token(null, TokenType.NEW_LINE, -1),
      new Token("Red", TokenType.VALUE, -1),
      new Token("20", TokenType.VALUE, -1),
      new Token("Rebecca", TokenType.VALUE, -1),
      new Token(null, TokenType.NEW_LINE, -1)
    );

  private static final List<Person> EXPECTED_PEOPLE =
    List.of(
      new Person("George", 22, null, "Green"),
      new Person("William", 21, null, "Blue"),
      new Person("Rebecca", 20, null, "Red")
    );

  private CsvTokenizer csvTokenizer;
  private boolean isHeaderPresent = false;
  private List<Person> people;

  @Test
  void test_deserialize_given_input_produces_null_tokens() {
    givenTokenizerProducesNull();
    Assertions.assertThrows(InvalidTokensException.class, this::whenDeserialize);
  }

  @Test
  void test_deserialize_given_header_is_present_and_token_list_empty() {
    givenTokenizerProducesEmptyTokenList();
    givenHeaderIsPresent();
    Assertions.assertThrows(ParseException.class, this::whenDeserialize);
  }

  @Test
  void test_deserialize_given_header_is_not_present_and_token_list_empty() {
    givenTokenizerProducesEmptyTokenList();
    givenHeaderIsNotPresent();
    whenDeserialize();
    MatcherAssert.assertThat(people, CoreMatchers.equalTo(Collections.emptyList()));
  }

  @Test
  void test_deserialize_given_header_is_not_present_and_token_list_not_empty() {
    givenTokenizerProducesTokenListWithoutHeader();
    givenHeaderIsNotPresent();
    whenDeserialize();
    MatcherAssert.assertThat(people, CoreMatchers.equalTo(EXPECTED_PEOPLE));
  }

  @Test
  void test_deserialize_given_header_is_present_and_token_list_not_empty() {
    givenTokenizerProducesTokenListWithHeader();
    givenHeaderIsPresent();
    whenDeserialize();
    MatcherAssert.assertThat(people, CoreMatchers.equalTo(EXPECTED_PEOPLE));
  }

  private void givenTokenizerProducesTokenListWithoutHeader() {
    csvTokenizer = input -> PEOPLE_TOKEN_LIST;
  }

  private void givenTokenizerProducesTokenListWithHeader() {
    csvTokenizer = input -> PEOPLE_TOKEN_LIST_WITH_HEADER;
  }

  private void whenDeserialize() {
    people = new CsvDeserializerImpl<>(csvTokenizer, Person.class).deserialize(null, isHeaderPresent);
  }

  private void givenHeaderIsPresent() {
    isHeaderPresent = true;
  }

  private void givenHeaderIsNotPresent() {
    isHeaderPresent = false;
  }

  private void givenTokenizerProducesEmptyTokenList() {
    csvTokenizer = input -> Collections.emptyList();
  }

  private void givenTokenizerProducesNull() {
    csvTokenizer = input -> null;
  }

}
