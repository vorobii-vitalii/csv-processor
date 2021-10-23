import com.csv.processor.CsvSerializer;

import java.time.LocalDate;
import java.util.ArrayList;

public class SerializeDemo {

  private static final CsvSerializer CSV_SERIALIZER = CsvSerializer.create();

  public static void main(String[] args) {
    var task = new Task("Task 1", LocalDate.now(), "Michael", 10);

    var lines = new ArrayList<String>();

    CSV_SERIALIZER.serializeSingle(task, Task.class, (line, isHeader) -> lines.add(line));

    System.out.println(String.join("\n", lines));
  }
}
