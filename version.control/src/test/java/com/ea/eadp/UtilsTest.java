package com.ea.eadp;

import com.ea.eadp.common.Utils;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by chriskang on 12/27/2016.
 */
public class UtilsTest {

    @Test
    public void testUtilsCollectionContains() {
        Set<String> pattern = new HashSet<String>() {{
            add("a");
            add("e/x");
        }};
        Map<String, Boolean> files = new HashMap<String, Boolean>() {{
            put("a/b/c", true);
            put("a/d", true);
            put("b/x/y", false);
            put("e/x", true);
            put("c/x/y", false);
            put("e/f/y", false);
            put("e/x/y", true);
        }};

        for (Map.Entry<String, Boolean> entry : files.entrySet()) {
            boolean expected = entry.getValue();
            Assert.assertEquals(String.format("file: %1$s", entry.getKey()), expected, Utils.collectionContains(pattern, entry.getKey()::startsWith));
        }
    }

    @Test
    public void testUtilsFormatArgsConvertToArgArray() {
        String cmdFmt = "p4 -zTag %1$s@=%2$s -x  %3$s";
        String cmdFmtExp = "p4${delimiter}-zTag${delimiter}%1$s@=%2$s${delimiter}-x${delimiter}${delimiter}%3$s";
        Assert.assertEquals(cmdFmtExp, Utils.getArgFormat(cmdFmt));
        String depotPath = "//nucleus something/NNG/...";
        String cl = "318568";
        String xFile = "tmp file.tmp";
        String[] argArrayExp = new String[]{"p4", "-zTag", depotPath + "@=" + cl, "-x", xFile};
        Assert.assertArrayEquals(argArrayExp, Utils.convertToArgArray(String.format(cmdFmtExp, depotPath, cl, xFile)));

        cmdFmt = Utils.getArgFormat("p4 -x %1$s %2$s");
        String xCmdFmt = Utils.getArgFormat("%1$s -c %2$s");
        String opt = "edit";
        argArrayExp = new String[]{"p4", "-x", xFile, opt, "-c", cl};
        String cmd = String.format(xCmdFmt, opt, cl);
        cmd = String.format(cmdFmt, xFile, cmd);
        Assert.assertArrayEquals(argArrayExp, Utils.convertToArgArray(cmd));
    }
}
