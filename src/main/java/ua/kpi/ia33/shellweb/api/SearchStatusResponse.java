package ua.kpi.ia33.shellweb.api;

import java.time.Instant;

public class SearchStatusResponse {

    private Long id;
    private String nameMask;
    private String ext;
    private Instant createdAt;
    private long resultsCount;
    private String state;

    public SearchStatusResponse() {
    }

    public SearchStatusResponse(Long id,
                                String nameMask,
                                String ext,
                                Instant createdAt,
                                long resultsCount,
                                String state) {
        this.id = id;
        this.nameMask = nameMask;
        this.ext = ext;
        this.createdAt = createdAt;
        this.resultsCount = resultsCount;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public String getNameMask() {
        return nameMask;
    }

    public String getExt() {
        return ext;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public long getResultsCount() {
        return resultsCount;
    }

    public String getState() {
        return state;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNameMask(String nameMask) {
        this.nameMask = nameMask;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setResultsCount(long resultsCount) {
        this.resultsCount = resultsCount;
    }

    public void setState(String state) {
        this.state = state;
    }
}
