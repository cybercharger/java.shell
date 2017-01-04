package com.ea.eadp.p4;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ChrisKang on 1/1/2017.
 */
public class P4ChangeListData {
    private static final String clId = "cl";
    private static final String dateId = "date";
    private static final String clientId = "client";
    private static final String userId = "user";
    private static final String statusId = "status";
    private static final String desId = "des";
    private static final String regExFmt = String.format(
            "[\\S\\s]+Change:\\s+(?<%1$s>\\d+)\n" +
                    "Date:\\s+(?<%2$s>.+)\n" +
                    "Client:\\s+(?<%3$s>.+)\n" +
                    "User:\\s+(?<%4$s>.+)\n" +
                    "Status:\\s+(?<%5$s>.+)\n" +
                    "Description:\\s*\n(?<%6$s>[\\S\\s]+)$",
            clId, dateId, clientId, userId, statusId, desId);
    private static final Pattern pattern = Pattern.compile(regExFmt);

    private static final String bugId = "bug";
    private static Pattern jiraPattern = Pattern.compile(String.format("\\[(?<%s>(?:EADPCOMMERCEBUGS|GOPFR)-\\d+)\\]", bugId));

    private final int changelist;
    private final String date;
    private final String client;
    private final String user;
    private final String status;
    private final String description;
    private final List<String> bugs;

    public int getChangelist() {
        return changelist;
    }

    public String getDate() {
        return date;
    }

    public String getClient() {
        return client;
    }

    public String getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getBugs() {
        return bugs;
    }

    public P4ChangeListData(List<String> rawData) {
        if (rawData == null || rawData.isEmpty()) throw new NullPointerException("rawData");
        String raw = StringUtils.join(rawData, "\n");
        Matcher matcher = pattern.matcher(raw);
        if (!matcher.matches()) throw new IllegalArgumentException(raw);
        changelist = Integer.parseInt(matcher.group(clId));
        date = matcher.group(dateId);
        client = matcher.group(clientId);
        user = matcher.group(userId);
        status = matcher.group(statusId);
        description = matcher.group(desId);

        Matcher jiraMatcher = jiraPattern.matcher(description.toUpperCase());
        List<String> bugList = new LinkedList<>();
        while (jiraMatcher.find()) bugList.add(jiraMatcher.group(bugId));
        bugs = Collections.unmodifiableList(bugList);
    }
}
