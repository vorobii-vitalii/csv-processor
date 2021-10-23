import com.csv.processor.Serializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer implements Serializer {
  @Override
  public CharSequence serialize(Object object) {
    LocalDate localDate = (LocalDate) object;

    return localDate.format(DateTimeFormatter.ISO_DATE);
  }
}
