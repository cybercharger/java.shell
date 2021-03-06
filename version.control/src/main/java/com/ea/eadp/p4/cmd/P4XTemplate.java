package com.ea.eadp.p4.cmd;

import com.ea.eadp.common.CmdRunner;
import com.ea.eadp.common.TempFileManager;
import com.ea.eadp.common.Utils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * Created by chriskang on 9/27/2016.
 */
class P4XTemplate {
    private static final String CMD_FMT = Utils.getArgFormat("p4 -x %1$s %2$s");
    static <T> T run(String cmd,
                     String tmpFilePrefix,
                     Iterable<? extends CharSequence> lines,
                     Function<List<String>, T> resultHandler) throws Exception {
        if (StringUtils.isBlank(cmd)) throw new NullPointerException("cmd");
        if (StringUtils.isBlank(tmpFilePrefix)) throw new NullPointerException("tmpFilePrefix");
        if (lines == null) throw new NullPointerException("lines");
        if (resultHandler == null) throw new NullPointerException("resultHandler");
        UUID id = UUID.randomUUID();
        Path tmpFilePath = null;
        try {
            tmpFilePath = TempFileManager.getInstance().writeTempFile(String.format("%1$s_%2$s", tmpFilePrefix, id), lines, StandardCharsets.UTF_8);
            final String arg = tmpFilePath.toString();
            return CmdRunner.getP4CmdRunner().run(() -> Utils.convertToArgArray(String.format(CMD_FMT, arg, cmd)), resultHandler);
        } finally {
            TempFileManager.getInstance().deleteTempFile(tmpFilePath);
        }
    }
}
