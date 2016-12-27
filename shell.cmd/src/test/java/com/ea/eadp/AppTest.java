package com.ea.eadp;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AppTest {
//    @Test
    public void test() {
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
                    System.err.println(String.format("Failed on %1$s %2$s, error:\n%3$s", StringUtils.join(c, " "), input, StringUtils.join(res, "\n"))));
            p4.forEach(System.out::println);
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
