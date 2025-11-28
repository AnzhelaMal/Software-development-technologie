package ua.kpi.ia33.shellweb.api;


public class FsCopyMoveRequest {

    private String src;
    private String dst;

    public FsCopyMoveRequest() {
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }
}
