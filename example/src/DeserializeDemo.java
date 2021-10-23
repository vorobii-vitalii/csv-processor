import com.csv.processor.CsvDeserializer;
import com.csv.processor.Deserializer;

import java.util.List;

public class DeserializeDemo {

  private static final String INPUT = """
    task_name;created_at;created_by
    Task 1;2021-10-23;'Michael'
    Task 2;2021-10-23;'Antonio'
    """;

  private static final CsvDeserializer<Task> CSV_DESERIALIZER = CsvDeserializer.createDefault(Task.class);

  public static void main(String[] args) {
    var tasks = CSV_DESERIALIZER.deserialize(INPUT, true);

    tasks.forEach(System.out::println);
  }

}
