package com.ea.eadp.p4.cmd;

import com.ea.eadp.common.CmdRunner;
import com.ea.eadp.common.Utils;
import com.ea.eadp.p4.P4FileStatInfo;
import com.ea.eadp.p4.P4RepositoryInfo;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by chriskang on 9/1/2016.
 */
public class P4Fstat {
    private static final String FSTAT_CL_CMD = Utils.getArgFormat("p4 fstat -e %1$s %2$s");
    private static final String FSTAT_FILE_CMD = Utils.getArgFormat("p4 fstat %s");
    private static final String FSTAT_FILE_X_CMD = Utils.getArgFormat("p4 -x %s fstat");
    private static final String FSTAT_CMD = "fstat";

    public static P4FileStatInfo getChangelistStats(String changlist, P4RepositoryInfo repoInfo) {
        if (StringUtils.isBlank(changlist)) throw new NullPointerException("changelist");
        if (repoInfo == null) throw new NullPointerException("repoInfo");
        return CmdRunner.getP4CmdRunner().run(() -> Utils.convertToArgArray(String.format(FSTAT_CL_CMD, changlist, repoInfo.getPathWithSubContents())),
                P4FileStatInfo::create);
    }

    public static P4FileStatInfo getFileStats(String files) {
        if (StringUtils.isBlank(files)) throw new NullPointerException("changelist");
        return CmdRunner.getP4CmdRunner().run(() -> Utils.convertToArgArray(String.format(FSTAT_FILE_CMD, files)), P4FileStatInfo::create);
    }

    public static P4FileStatInfo batchGetFileStats(Iterable<? extends CharSequence> files) throws Exception {
        return P4XTemplate.run(FSTAT_CMD, FSTAT_CMD, files, P4FileStatInfo::create);
    }

    public static P4FileStatInfo batchGetFileStatsAfter(Iterable<? extends CharSequence> files, String changelist) throws Exception {
        return P4XTemplate.run(FSTAT_CMD, FSTAT_CMD, files, P4FileStatInfo::create);
    }
}
