package ua.kpi.ia33.shellweb.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "search_query")
public class SearchQuery {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameMask;
    private String ext;
    private Instant dateFrom;
    private Instant dateTo;
    private Long sizeMin;
    private Long sizeMax;
    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNameMask() { return nameMask; }
    public void setNameMask(String nameMask) { this.nameMask = nameMask; }

    public String getExt() { return ext; }
    public void setExt(String ext) { this.ext = ext; }

    public Instant getDateFrom() { return dateFrom; }
    public void setDateFrom(Instant dateFrom) { this.dateFrom = dateFrom; }

    public Instant getDateTo() { return dateTo; }
    public void setDateTo(Instant dateTo) { this.dateTo = dateTo; }

    public Long getSizeMin() { return sizeMin; }
    public void setSizeMin(Long sizeMin) { this.sizeMin = sizeMin; }

    public Long getSizeMax() { return sizeMax; }
    public void setSizeMax(Long sizeMax) { this.sizeMax = sizeMax; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
