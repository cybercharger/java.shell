package com.ea.eadp;

/**
 * Created by chriskang on 1/3/2017.
 */
public class JiraIssueInfo {
    private final String key;
    private final String intCM;
    private final String prodCM;

    public JiraIssueInfo(String key, String intCM, String prodCM) {
        this.key = key;
        this.intCM = intCM;
        this.prodCM = prodCM;
    }

    public String getKey() {
        return key;
    }

    public String getIntCM() {
        return intCM;
    }

    public String getProdCM() {
        return prodCM;
    }
}
