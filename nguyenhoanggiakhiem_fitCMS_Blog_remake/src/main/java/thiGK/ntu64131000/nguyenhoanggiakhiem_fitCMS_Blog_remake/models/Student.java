package thiGK.ntu64131000.nguyenhoanggiakhiem_fitCMS_Blog_remake.models;

public class Student {
    private Long id;
    private String name;
    private Long groupId;

    // Constructors
    public Student() {}

    public Student(String name, Long groupId) {
        this.name = name;
        this.groupId = groupId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}