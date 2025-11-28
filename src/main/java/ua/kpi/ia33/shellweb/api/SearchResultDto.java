package ua.kpi.ia33.shellweb.api;

public class SearchResultDto {

    private String path;
    private String name;
    private String type;

    public SearchResultDto() {
    }

    public SearchResultDto(String path, String name, String type) {
        this.path = path;
        this.name = name;
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }
}
