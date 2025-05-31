package khiem.nhg.Project_64131000.model;

import java.time.LocalDateTime;

public class UserActivityDTO {
    private String description;
    private LocalDateTime timestamp;

    public UserActivityDTO(String description, LocalDateTime timestamp) {
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

