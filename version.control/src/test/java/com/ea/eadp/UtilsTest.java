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
}
