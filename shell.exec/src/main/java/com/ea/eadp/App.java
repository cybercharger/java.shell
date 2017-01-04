package com.ea.eadp;

import com.ea.eadp.common.VersionControlException;
import com.ea.eadp.p4.P4ChangeListData;
import com.ea.eadp.p4.cmd.P4Change;
import com.ea.eadp.p4.cmd.P4Submit;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

public class App {
    public static final String JIRA_USER = "JIRA_USER";
    public static final String JIRA_PWD = "JIRA_PWD";

    private static Logger logger = Logger.getLogger(App.class);
    private static final String CL_ARG = "cl";
    private static final String CM_ARG = "cm";
    private static final String JIRA_URL_ARG = "jira";
    private static final String DEFAULT_JIRA_URL = "https://eadpjira.ea.com";

    private static final String HELP_INFO = String.format("Environment variables %1$s & %2$s should be set for JIRA access", JIRA_USER, JIRA_PWD);

    public static void main(String[] args) {
        try {
            String jiraUser = System.getenv(JIRA_USER);
            if (StringUtils.isBlank(jiraUser)) throw new IllegalStateException(JIRA_USER + " is not set");
            String jiraPwd = System.getenv(JIRA_PWD);
            if (StringUtils.isBlank(jiraPwd)) throw new IllegalStateException(JIRA_PWD + " is not set");
            CommandLine line = parse(args);
            String cl = line.getOptionValue(CL_ARG);
            String cm = line.getOptionValue(CM_ARG);
            String url = line.hasOption(JIRA_URL_ARG) ? line.getOptionValue(JIRA_URL_ARG) : DEFAULT_JIRA_URL;

            logger.info("Checking changelist...");
            P4ChangeListData clData = P4Change.getChangeListData(cl);
            if (clData.getBugs().isEmpty()) {
                throw new IllegalArgumentException("Failed to find bugs from " + clData.getDescription());
            }
            JiraService service = new JiraService(url, jiraUser, jiraPwd);
            for (String bug : clData.getBugs()) {
                logger.info(String.format("Checking bug %s ...", bug));
                JiraIssueInfo info = service.getJiraIssue(bug);
                if (!info.getIntCM().equals(cm) && !info.getProdCM().equals(cm)) {
                    throw new VersionControlException(String.format("Issue %1$s is not marked for CM %2$s", bug, cm));
                }
            }

            logger.info(String.format("Submitting %s ...", cl));
            int newCl = P4Submit.submit(cl);
            logger.info(String.format("Changelist %1$s submitted as %2$d, for bug(s):\n%3$s", cl, newCl, StringUtils.join(clData.getBugs(), "\n")));
            logger.info("CM fill-up is under construction");

        } catch (ParseException e) {
            System.exit(1);
        } catch (InterruptedException | ExecutionException | IOException | URISyntaxException e) {
            logger.error(e);
            System.exit(1);
        } catch (VersionControlException | IllegalStateException e) {
            logger.error(e.getMessage());
            System.exit(1);
        }
    }

    private static CommandLine parse(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        Options options = buildOptions();
        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(HELP_INFO, options);
            throw e;
        }
    }

    private static Options buildOptions() {
        Options options = new Options();
        options.addOption(Option.builder()
                .argName(CL_ARG)
                .longOpt(CL_ARG)
                .hasArg()
                .required()
                .desc("p4 changelist")
                .build());
        options.addOption(Option.builder()
                .argName(CM_ARG)
                .longOpt(CM_ARG)
                .hasArg()
                .required()
                .desc("target cm number")
                .build());
        options.addOption(Option.builder()
                .argName(JIRA_URL_ARG)
                .longOpt(JIRA_URL_ARG)
                .hasArg()
                .desc("Jira base url, such as https://eadpjira.ea.com")
                .build());
        return options;
    }
}
