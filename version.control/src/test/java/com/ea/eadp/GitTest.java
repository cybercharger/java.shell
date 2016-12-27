package com.ea.eadp;

import org.junit.Test;

/**
 * Created by chriskang on 12/27/2016.
 */
public class GitTest {
    @Test
    public void testGitLogInfo() {
//        final String cmdRes = "251adbef66f2db998f88c4833ad521877b521955  change on p4: revision 6 [git-p4 depot-paths = //nucleus/SANDBOX/testgitp4/: change = 313596]";
//        GitLogInfo info = new GitLogInfo(cmdRes);
//        Assert.assertEquals("251adbef66f2db998f88c4833ad521877b521955", info.getCommit());
//        Assert.assertEquals("change on p4: revision 6 [git-p4 depot-paths = //nucleus/SANDBOX/testgitp4/: change = 313596]", info.getComment());
    }

    @Test
    public void testGitFileInfo() {
//
//        String add = "A       src/main/java/gitp4/GitP4Operation.java";
//        GitFileInfo info = new GitFileInfo(add);
//        Assert.assertEquals(GitChangeType.Add, info.getChangeType());
//        Assert.assertEquals("src/main/java/gitp4/GitP4Operation.java", info.getFile());
//
//        add = "A\tcatalog/testData/smoke/lookup/GameEditionTypeFacetKey/Anniversary Edition.xml";
//        info = new GitFileInfo(add);
//        Assert.assertEquals(GitChangeType.Add, info.getChangeType());
//        Assert.assertEquals("catalog/testData/smoke/lookup/GameEditionTypeFacetKey/Anniversary Edition.xml", info.getFile());
//
//        final String modify = "M       src/main/java/gitp4/p4/P4RepositoryInfo.java";
//        info = new GitFileInfo(modify);
//        Assert.assertEquals(GitChangeType.Modify, info.getChangeType());
//        Assert.assertEquals("src/main/java/gitp4/p4/P4RepositoryInfo.java", info.getFile());
//
//        final String delete = "D\t src/main/java/gitp4/p4/P4RepositoryInfo.java";
//        info = new GitFileInfo(delete);
//        Assert.assertEquals(GitChangeType.Delete, info.getChangeType());
//        Assert.assertEquals("src/main/java/gitp4/p4/P4RepositoryInfo.java", info.getFile());
//
//        Exception exp = null;
//        try {
//            final String move = "R087\tsrc/main/java/gitp4/p4/P4Change.java\tsrc/main/java/gitp4/p4/P4ChangeInfo.java";
//            info = new GitFileInfo(move);
//            Assert.assertEquals(GitChangeType.Rename, info.getChangeType());
//            Assert.assertEquals("src/main/java/gitp4/p4/P4Change.java", info.getFile());
//
//
//        } catch (Exception e) {
//            if (e instanceof IllegalStateException) {
//                exp = e;
//            } else {
//                exp = null;
//            }
//        }
//        Assert.assertNotNull(exp);
    }
}
