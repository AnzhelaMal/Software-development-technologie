package ua.kpi.ia33.shellweb.api;


public class SearchIdResponse {

    private Long queryId;

    public SearchIdResponse() {
    }

    public SearchIdResponse(Long queryId) {
        this.queryId = queryId;
    }

    public Long getQueryId() {
        return queryId;
    }

    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }
}
