package com.ea.eadp.common;

import com.ea.eadp.CommandRunner;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chriskang on 12/30/2016.
 */
public class P4SCmdRunner {
    private static Logger logger = Logger.getLogger(P4SCmdRunner.class);

    private static final String infoId = "info";
    private static final String infPatternString = String.format("info\\d*\\:\\s+(?<%s>.+)", infoId);
    private static final Pattern infoPattern = Pattern.compile(infPatternString);

    private static final String errorId = "error";
    private static final String errorPatternString = String.format("error\\d*\\:\\s+(?<%s>.+)", errorId);
    private static final Pattern errorPattern = Pattern.compile(errorPatternString);


    private static final String exitId = "exit";
    private static final String exitPatternString = String.format("exit\\d*\\:\\s+(?<%s>\\d+)", exitId);
    private static final Pattern exitPattern = Pattern.compile(exitPatternString);
    private static final String[] P4S = new String[]{"p4", "-s"};

    public static boolean run(String[] args,
                              String input,
                              String dir,
                              Consumer<List<String>> onSucceeded,
                              Consumer<List<String>> onFailed)
            throws InterruptedException, ExecutionException, IOException {
        if (args == null || args.length <= 0) throw new NullPointerException("args");
        if (onSucceeded == null) throw new NullPointerException("onSucceeded");
        String[] cmd = new String[args.length + P4S.length];
        System.arraycopy(P4S, 0, cmd, 0, P4S.length);
        System.arraycopy(args, 0, cmd, P4S.length, args.length);
        List<String> result = CommandRunner.runCommand(cmd, input, dir, P4SCmdRunner::throwUnexpectedError);
        if (result == null || result.isEmpty()) {
            throw new VersionControlException(String.format("Failed to run:\n%1$s\n No Result ", StringUtils.join(cmd, "\n")));
        }

        String exitString = result.remove(result.size() - 1);
        int exitCode = Integer.parseInt(parseResult(exitPattern, exitString, exitId, true));
        boolean succeeded = (0 == exitCode);

        List<String> output = new ArrayList<>(result.size());
        result.forEach(line -> {
            String l = succeeded ? parseResult(infoPattern, line, infoId, false) : parseResult(errorPattern, line, errorId, false);
            if (!StringUtils.isBlank(l)) {
                output.add(l);
            }
        });
        if (succeeded) {
            onSucceeded.accept(output);
        } else if (onFailed != null) {
            onFailed.accept(output);
        }
        return succeeded;
    }

    private static void throwUnexpectedError(String[] cmd, List<String> res) {
        String msg = "Failed to run:\n" + StringUtils.join(cmd, "\n") + "\nOutput:\n" + StringUtils.join(res, "\n");
        logger.error(msg);
        throw new VersionControlException(msg);
    }

    private static String parseResult(Pattern pattern, String output, String groupId, boolean throwOnUnmatched) {
        Matcher matcher = pattern.matcher(output);
        if (!matcher.matches()) {
            if (throwOnUnmatched) throw new VersionControlException("Cannot parse command result: " + output);
            else return "";
        }
        return matcher.group(groupId);
    }
}
