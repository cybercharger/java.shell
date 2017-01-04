package com.ea.eadp;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by chriskang on 1/3/2017.
 */
public class JiraService {
    private final JiraRestClient client;
    private static final String INT_CM_FIELD = "INT CM No";
    private static final String PROD_CM_FIELD = "Prod CM No";

    public JiraService(String url, String user, String password) throws URISyntaxException {
        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        client = factory.createWithBasicHttpAuthentication(new URI(url), user, password);
    }

    public JiraIssueInfo getJiraIssue(String key) {
        if (StringUtils.isBlank(key)) throw new NullPointerException("key");
        SearchResult result = client.getSearchClient().searchJql("key = " + key, 1, 0, null).claim();
        if (result.getTotal() > 1) throw new IllegalStateException("More than 1 found: " + result.getTotal());
        Issue issue = result.getIssues().iterator().next();
        return new JiraIssueInfo(key, getFieldValue(issue, INT_CM_FIELD), getFieldValue(issue, PROD_CM_FIELD));
    }

    private static String getFieldValue(Issue issue, String fieldName) {
        IssueField field = issue.getFieldByName(fieldName);
        if (field == null) {
            throw new IllegalArgumentException(String.format("Cannot get field %1$s from issue %2$s", fieldName, issue.getKey()));
        }
        Object value = field.getValue();
        return value == null ? "" : value.toString();
    }
}
