package ua.kpi.ia33.shellweb.api;


public class SearchRequest {

    private String nameMask;
    private String ext;

    public SearchRequest() {
    }

    public String getNameMask() {
        return nameMask;
    }

    public void setNameMask(String nameMask) {
        this.nameMask = nameMask;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
