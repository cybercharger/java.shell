package com.ea.eadp.p4.cmd;

import com.ea.eadp.common.CmdRunner;
import com.ea.eadp.common.P4SCmdRunner;
import com.ea.eadp.common.Utils;
import com.ea.eadp.common.VersionControlException;
import com.ea.eadp.p4.P4ChangeListData;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chriskang on 8/29/2016.
 */
public class P4Change {
    private static final String[] CREATE_EMPTY_CL_CMD = new String[]{"p4 change -i"};
    private static final String[] GET_CL_FMT_CMD = new String[]{"p4 change -o"};
    private static final String CHANGE_o_O_FMT = Utils.getArgFormat("change -o -O %s");
    private static final String changelistGroupId = "changelist";
    private static final Pattern pattern = Pattern.compile(String.format("Change (?<%s>\\d+) created\\.", changelistGroupId));

    public static String createEmptyChangeList(String description) {
        if (StringUtils.isBlank(description)) throw new NullPointerException("des");

        List<String> spec = CmdRunner.getP4CmdRunner().run(() -> GET_CL_FMT_CMD, cmdRes -> cmdRes);
        List<String> newSpec = new LinkedList<>();
        for (String line : spec) {
            if (line.contains("<enter description here>")) break;
            newSpec.add(line);
        }
        newSpec.add("\t" + description);

        final String[] cmd = CREATE_EMPTY_CL_CMD;
        return CmdRunner.getP4CmdRunner().run(() -> CREATE_EMPTY_CL_CMD,
                cmdRes -> {
                    if (cmdRes == null || cmdRes.size() != 1) {
                        throw new IllegalStateException(String.format("Invalid return of running '%s'", StringUtils.join(cmd, " ")));
                    }
                    Matcher matcher = pattern.matcher(cmdRes.get(0));
                    if (!matcher.matches()) {
                        throw new IllegalStateException(String.format("Invalid return of running '%1$s'\n%2$s", StringUtils.join(cmd, " "), cmdRes.get(0)));
                    }
                    return matcher.group(changelistGroupId);
                }, StringUtils.join(newSpec, "\n"));
    }

    public static P4ChangeListData getChangeListData(String changelist) throws InterruptedException, ExecutionException, IOException {
        if (StringUtils.isBlank(changelist)) throw new NullPointerException("changelist");
        String[] cmd = Utils.convertToArgArray(String.format(CHANGE_o_O_FMT, changelist));
        return P4SCmdRunner.run(cmd, null, null, P4ChangeListData::new, P4Change::throwError);
    }

    private static void throwError(List<String> result) {
        throw new VersionControlException(StringUtils.join(result, "\n"));
    }
}
