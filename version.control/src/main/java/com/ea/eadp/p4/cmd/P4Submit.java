package com.ea.eadp.p4.cmd;

import com.ea.eadp.common.P4SCmdRunner;
import com.ea.eadp.common.Utils;
import com.ea.eadp.common.VersionControlException;
import com.ea.eadp.p4.P4ChangeListData;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by chriskang on 1/3/2017.
 */
public class P4Submit {
    private static Logger logger = Logger.getLogger(P4Submit.class);
    private static String SUBMIT_CMD_FMT = Utils.getArgFormat("submit -c %s");

    public static int submit(String changelist) throws InterruptedException, ExecutionException, IOException {
        String[] cmd = Utils.convertToArgArray(String.format(SUBMIT_CMD_FMT, changelist));
        P4SCmdRunner.run(cmd, null, null,
                r -> {
                    logger.info(StringUtils.join(r, "\n"));
                    return "";
                },
                P4Submit::throwError);

        P4ChangeListData data = P4Change.getChangeListData(changelist);
        return data.getChangelist();
    }

    private static void throwError(List<String> result) {
        throw new VersionControlException(StringUtils.join(result, "\n"));
    }
}
