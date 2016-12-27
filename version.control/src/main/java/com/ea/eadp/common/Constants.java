package com.ea.eadp.common;

import java.util.regex.Pattern;

/**
 * Created by chriskang on 12/27/2016.
 */
public class Constants {
    public static final String FULL_COMMIT_ID_PTRN = "[a-fA-F0-9]{40}";
    public static final String ABBR_COMMIT_ID_PTRN = "[a-fA-F0-9]{7}";
    public static final Pattern fullCommitIdPattern = Pattern.compile(FULL_COMMIT_ID_PTRN);
    public static final Pattern abbrCommitIdPattern = Pattern.compile(ABBR_COMMIT_ID_PTRN);
}
