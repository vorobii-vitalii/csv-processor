import com.csv.processor.Deserializer;

import java.time.LocalDate;

public class LocalDateDeserializer implements Deserializer {
  @Override
  public Object deserialize(String fromText, Class<?> type) {
    return LocalDate.parse(fromText);
  }
}
