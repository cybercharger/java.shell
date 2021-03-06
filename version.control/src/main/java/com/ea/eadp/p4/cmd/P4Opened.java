package com.ea.eadp.p4.cmd;

import com.ea.eadp.common.CmdRunner;
import com.ea.eadp.common.Utils;
import com.ea.eadp.p4.P4FileOpenedInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chriskang on 8/29/2016.
 */
public class P4Opened {
    private static final String P4_CMD_STR = Utils.getArgFormat("p4 opened %s");

    public static List<P4FileOpenedInfo> run(String p4Repo) {
        if (StringUtils.isBlank(p4Repo)) throw new NullPointerException("p4Repo");
        return CmdRunner.getP4CmdRunner().run(() -> Utils.convertToArgArray(String.format(P4_CMD_STR, p4Repo)),
                cmdRes -> cmdRes.stream().
                        map(P4FileOpenedInfo::create).
                        filter(cur -> cur != null).
                        collect(Collectors.toCollection(LinkedList::new))
        );
    }
}
