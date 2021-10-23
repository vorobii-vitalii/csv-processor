import com.csv.processor.annotations.*;

import java.time.LocalDate;

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
