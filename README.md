# Command executor project
# CSV serialization/deserialization annotation-driven library, created for educational purpose.
### But in any case, feel free to contribute to this project

### Code example is listed below:

``` java

public class Task {

  @CsvField(name = "task_name", position = 1)
  private String name;

  @CsvField(name = "created_at", position = 2)
  @Serialize(value = LocalDateSerializer.class)
  @Deserialize(value = LocalDateDeserializer.class)
  private LocalDate createdAt;

  @CsvField(name = "created_by", position = 3)
  @Escape
  private String createdBy;

  @Transient // Fields annotated with @Transient are skipped
  private Integer priority;

  /**
   * Default constructor is mandatory for deserialization
   */
  public Task() {
  }

  public Task(String name, LocalDate createdAt, String createdBy, Integer priority) {
    this.name = name;
    this.createdAt = createdAt;
    this.createdBy = createdBy;
    this.priority = priority;
  }
  
  @Override
  public String toString() {
    return "Task{" +
      "name='" + name + '\'' +
      ", createdAt=" + createdAt +
      ", createdBy='" + createdBy + '\'' +
      ", priority=" + priority +
      '}';
  }

}

```

``` java

public class SerializeDemo {

  private static final CsvSerializer CSV_SERIALIZER = CsvSerializer.create();

  public static void main(String[] args) {
    var task = new Task("Task 1", LocalDate.now(), "Michael", 10);
    var lines = new ArrayList<String>();

    CSV_SERIALIZER.serializeSingle(task, Task.class, (line, isHeader) -> lines.add(line));
    System.out.println(String.join("\n", lines));
  }
}
```

#### Output:
_task_name;created_at;created_by_
_Task 1;2021-10-23;'Michael'_


``` java
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
```
#### Output:
_Task{name='Task 1', createdAt=2021-10-23, createdBy='Michael', priority=null}_
_Task{name='Task 2', createdAt=2021-10-23, createdBy='Antonio', priority=null}_
