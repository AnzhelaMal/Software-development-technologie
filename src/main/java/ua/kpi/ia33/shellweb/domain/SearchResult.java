package ua.kpi.ia33.shellweb.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "search_result")
public class SearchResult {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SearchQuery query;

    @Column(nullable = false, length = 2048)
    private String path;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String type;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public SearchQuery getQuery() { return query; }
    public void setQuery(SearchQuery query) { this.query = query; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }




}
