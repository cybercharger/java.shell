package com.ea.eadp.p4.cmd;

import com.ea.eadp.common.CmdRunner;
import com.ea.eadp.common.Utils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by chriskang on 9/7/2016.
 */
public class P4Where {
    private static String CMD_FMT = Utils.getArgFormat("p4 where %s");
    private static String ZTAG_CMD_FMT = Utils.getArgFormat("p4 -Ztag where %s");

    public static List<String> run(String input) {
        if (StringUtils.isBlank(input)) throw new NullPointerException("input");
        return CmdRunner.getP4CmdRunner().run(() -> Utils.convertToArgArray(String.format(CMD_FMT, input)), cmdRes -> cmdRes);
    }

    public static List<String> runZtag(String input) {
        if (StringUtils.isBlank(input)) throw new NullPointerException("input");
        return CmdRunner.getP4CmdRunner().run(() -> Utils.convertToArgArray(String.format(ZTAG_CMD_FMT, input)), cmdRes -> cmdRes);
    }
}
