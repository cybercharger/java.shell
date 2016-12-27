package com.ea.eadp;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AppTest {
    private static Logger logger = Logger.getLogger(AppTest.class);

    @Test
    public void test() throws InterruptedException, ExecutionException, IOException {
        try {
            List<String> cmdRes = CommandRunner.runCommand(new String[]{"ping", "127.0.0.1"}, null, null, (c, res) ->
                    logger.error(String.format("Failed on %1$s, error:\n%2$s", StringUtils.join(c, " "), StringUtils.join(res, "\n"))));
            cmdRes.forEach(logger::info);
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

//    @Test
    public void testCreateP4ChangeList() throws InterruptedException, ExecutionException, IOException {
        String input = "Change: new\n" +
                "\n" +
                "Client: EASAP_chriskang_EASHDPDESK075_70\n" +
                "\n" +
                "User:   EASAP\\chriskang\n" +
                "\n" +
                "Status: new\n" +
                "\n" +
                "Description: something comments with whitespaces\n" +
                "\tsome more new lines #1\n" +
                "\tsome more new lines #2\n";

        try {
            List<String> p4 = CommandRunner.runCommand(new String[] {"p4", "change", "-i"}, input, null, (c, res) ->
                    logger.error(String.format("Failed on %1$s %2$s, error:\n%3$s", StringUtils.join(c, " "), input, StringUtils.join(res, "\n"))));
            p4.forEach(logger::info);
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw e;
        }
    }

}
