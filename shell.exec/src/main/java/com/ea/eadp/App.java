package com.ea.eadp;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class App {
    private static Logger logger = Logger.getLogger(App.class);
    public static void main(String[] args) {
        if (args == null || args.length <= 0) {
            logger.warn("Nothing to run");
            return;
        }

        try {
            List<String> p4 = CommandRunner.runCommand(args, null, null, (c, res) ->
                    System.err.println(String.format("Failed on %1$s, error:\n%2$s", StringUtils.join(c, " "), StringUtils.join(res, "\n"))));
            p4.forEach(System.out::println);
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
