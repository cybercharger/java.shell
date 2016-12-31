package com.ea.eadp;

import com.ea.eadp.common.P4SCmdRunner;
import com.ea.eadp.common.VersionControlException;
import com.ea.eadp.p4.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by chriskang on 12/27/2016.
 */
public class P4Test {
    private static Logger logger = Logger.getLogger(P4Test.class);

    @Test
    public void testP4ChangeInfo() {
        final String info = "Change 313596 on 2016/08/22 by EASAP\\chriskang@EASAP_chriskang_ws5 'change on p4: revision 6 '";
        P4ChangeInfo change = P4ChangeInfo.create(info);
        Assert.assertEquals("313596", change.getChangeList());
        Assert.assertEquals("2016/08/22", change.getDate());
        Assert.assertEquals("EASAP\\chriskang@EASAP_chriskang_ws5", change.getP4UserInfo().toString());
        Assert.assertEquals("'change on p4: revision 6 '", change.getComments());
    }

    @Test
    public void testP4FileInfo() {
        String info = "... //nucleus/SANDBOX/testgitp4/createdOnGit.txt#6 edit";

        P4FileInfo fileInfo = P4FileInfo.create(info, "//nucleus/SANDBOX/testgitp4/");

        Assert.assertEquals("//nucleus/SANDBOX/testgitp4/createdOnGit.txt", fileInfo.getFile());
        Assert.assertEquals(6, fileInfo.getRevision());
        Assert.assertEquals(P4Operation.edit, fileInfo.getOperation());

        info = "... //nucleus/SANDBOX/catalog/catalog.ui/src/main/java/com/ea/eadp/catalog/ui/CatalogUIException.java#5 move/delete";
        fileInfo = P4FileInfo.create(info, "//nucleus/SANDBOX/catalog/");
        Assert.assertEquals("//nucleus/SANDBOX/catalog/catalog.ui/src/main/java/com/ea/eadp/catalog/ui/CatalogUIException.java", fileInfo.getFile());
        Assert.assertEquals(5, fileInfo.getRevision());
        Assert.assertEquals(P4Operation.delete, fileInfo.getOperation());

        Assert.assertNull(P4FileInfo.create(info, "//abc"));
    }

    @Test
    public void testP4ChangelistInfo() {
        final String info = "Change 313596 by EASAP\\chriskang@EASAP_chriskang_ws5 on 2016/08/22 02:57:56\n" +
                "\n" +
                "        change on p4: revision 6\nABC" +
                "\n" +
                "Affected files ...\n" +
                "\n" +
                "... //nucleus/SANDBOX/testgitp4/createdOnGit.txt#6 edit\n" +
                "... //nucleus/SANDBOX/testgitp4/abc.txt#6 edit\n" +
                "... //nucleus/SANDBOX/abc/abc.txt#6 edit";
        P4ChangeListInfo clInfo = new P4ChangeListInfo(Arrays.asList(StringUtils.split(info, '\n')), "//nucleus/SANDBOX/testgitp4/");
        Assert.assertEquals("313596", clInfo.getChangelist());
        Assert.assertEquals("2016/08/22 02:57:56", clInfo.getTimestamp());
        Assert.assertEquals("EASAP\\chriskang", clInfo.getP4UserInfo().getUser());
        Assert.assertEquals("EASAP_chriskang_ws5", clInfo.getP4UserInfo().getWorkspace());
        Assert.assertEquals("        change on p4: revision 6\nABC", clInfo.getFullComments());
        Assert.assertEquals(2, clInfo.getFiles().size());
    }

    @Test
    public void testP4FileOpenInfo() {
        String info = "//nucleus/SANDBOX/catalog/sandboxLoader.sh#1 - edit default change (xtext)";
        P4FileOpenedInfo foInfo = P4FileOpenedInfo.create(info);
        Assert.assertEquals("//nucleus/SANDBOX/catalog/sandboxLoader.sh", foInfo.getFile());

        info = "//nucleus/SANDBOX/catalog - file(s) not opened on this client.";
        foInfo = P4FileOpenedInfo.create(info);
        Assert.assertNull(foInfo);
    }


    @Test
    public void testP4FileStatsInfo() {
        String input = "... depotFile //nucleus/SANDBOX/catalog/tomcat.app/src/main/webapp/config/devbox/search.properties\n" +
                "... clientFile D:\\EASAP_chriskang_EASHDPDESK075_70\\nucleus\\SANDBOX\\catalog\\tomcat.app\\src\\main\\webapp\\config\\devbox\\search.properties\n" +
                "... isMapped \n" +
                "... headAction integrate\n" +
                "... headType text\n" +
                "... headTime 1420531837\n" +
                "... headRev 5\n" +
                "... headChange 282987\n" +
                "... headModTime 1400056319\n" +
                "... haveRev 1\n" +
                "\n" +
                "... depotFile //nucleus/SANDBOX/catalog/tomcat.app/src/main/webapp/config/prod/mysql.properties\n" +
                "... clientFile D:\\EASAP_chriskang_EASHDPDESK075_70\\nucleus\\SANDBOX\\catalog\\tomcat.app\\src\\main\\webapp\\config\\prod\\mysql.properties\n" +
                "... isMapped \n" +
                "... headAction move/add\n" +
                "... headType text\n" +
                "... headTime 1420531837\n" +
                "... headRev 6\n" +
                "... headChange 282987\n" +
                "... headModTime 1418196382\n" +
                "... haveRev 1\n" +
                "\n" +
                "... desc Merging\n" +
                "\n" +
                "//nucleus/NNG/catalog/...\n" +
                "\n" +
                "to //nucleus/SANDBOX/catalog/...\n" +
                "\n" +
                "\n";
        P4FileStatInfo info = P4FileStatInfo.create(Arrays.asList(StringUtils.split(input, "\n")));
        final String des = "Merging\n//nucleus/NNG/catalog/...\nto //nucleus/SANDBOX/catalog/...";
        Assert.assertEquals(des, info.getDescription());
        Assert.assertEquals(2, info.getFiles().size());
        P4FileInfoEx file = new P4FileInfoEx(
                "//nucleus/SANDBOX/catalog/tomcat.app/src/main/webapp/config/devbox/search.properties",
                "D:\\EASAP_chriskang_EASHDPDESK075_70\\nucleus\\SANDBOX\\catalog\\tomcat.app\\src\\main\\webapp\\config\\devbox\\search.properties",
                P4Operation.integrate,
                5, 282987);
        Assert.assertEquals(file, info.getFiles().get(0));
        file = new P4FileInfoEx(
                "//nucleus/SANDBOX/catalog/tomcat.app/src/main/webapp/config/prod/mysql.properties",
                "D:\\EASAP_chriskang_EASHDPDESK075_70\\nucleus\\SANDBOX\\catalog\\tomcat.app\\src\\main\\webapp\\config\\prod\\mysql.properties",
                P4Operation.add,
                6, 282987);
        Assert.assertEquals(file, info.getFiles().get(1));

        input = "deploy.sh - no such file(s).\n" +
                "fullcycle.cmd - no such file(s).\n" +
                "fullcycle.sh - no such file(s).\n" +
                "loadalloffers.bat - no such file(s).\n" +
                "loadalloffers.sh - no such file(s).\n";
        info = P4FileStatInfo.create(Arrays.asList(StringUtils.split(input, "\n")));
        Assert.assertEquals(0, info.getFiles().size());

        input = "deploy.sh - no such file(s).\n" +
                "fullcycle.cmd - no such file(s).\n" +
                "fullcycle.sh - no such file(s).\n" +
                "loadalloffers.bat - no such file(s).\n" +
                "loadalloffers.sh - no such file(s).\n" +
                "... depotFile //nucleus/RELEASES/REL430.0/catalog/OneBoxDeploy.sh\n" +
                "... clientFile E:\\EASAP_chriskang_ws5\\nucleus\\RELEASES\\REL430.0\\catalog\\OneBoxDeploy.sh\n" +
                "... isMapped\n" +
                "... headAction integrate\n" +
                "... headType xtext\n" +
                "... headTime 1470197255\n" +
                "... headRev 4\n" +
                "... headChange 312722\n" +
                "... headModTime 1469749522\n" +
                "... haveRev 4\n" +
                "\n" +
                "... depotFile //nucleus/RELEASES/REL430.0/catalog/OneBoxDeploy.sh\n" +
                "... clientFile E:\\EASAP_chriskang_ws5\\nucleus\\RELEASES\\REL430.0\\catalog\\OneBoxDeploy.sh\n" +
                "... isMapped\n" +
                "... headAction integrate\n" +
                "... headType xtext\n" +
                "... headTime 1469644009\n" +
                "... headRev 3\n" +
                "... headChange 312420\n" +
                "... headModTime 1469641501\n" +
                "... haveRev 4\n" +
                "\n" +
                "... depotFile //nucleus/RELEASES/REL430.0/catalog/pom.xml\n" +
                "... clientFile E:\\EASAP_chriskang_ws5\\nucleus\\RELEASES\\REL430.0\\catalog\\pom.xml\n" +
                "... isMapped\n" +
                "... headAction edit\n" +
                "... headType text\n" +
                "... headTime 1471940199\n" +
                "... headRev 5\n" +
                "... headChange 313654\n" +
                "... headModTime 1471939068\n" +
                "... haveRev 5\n" +
                "... ... otherOpen0 EAHQ\\jdai@Work_1666\n" +
                "... ... otherAction0 edit\n" +
                "... ... otherChange0 312971\n" +
                "... ... otherOpen1 EASAP\\ianlu@ianlu_mac_air\n" +
                "... ... otherAction1 edit\n" +
                "... ... otherChange1 313194\n" +
                "... ... otherOpen 2";

        info = P4FileStatInfo.create(Arrays.asList(StringUtils.split(input, "\n")));
        Assert.assertEquals(3, info.getFiles().size());
        file = new P4FileInfoEx(
                "//nucleus/RELEASES/REL430.0/catalog/OneBoxDeploy.sh",
                "E:\\EASAP_chriskang_ws5\\nucleus\\RELEASES\\REL430.0\\catalog\\OneBoxDeploy.sh",
                P4Operation.integrate,
                4, 312722);
        Assert.assertEquals(file, info.getFiles().get(0));

        file = new P4FileInfoEx(
                "//nucleus/RELEASES/REL430.0/catalog/OneBoxDeploy.sh",
                "E:\\EASAP_chriskang_ws5\\nucleus\\RELEASES\\REL430.0\\catalog\\OneBoxDeploy.sh",
                P4Operation.integrate,
                3, 312420);
        Assert.assertEquals(file, info.getFiles().get(1));

        file = new P4FileInfoEx(
                "//nucleus/RELEASES/REL430.0/catalog/pom.xml",
                "E:\\EASAP_chriskang_ws5\\nucleus\\RELEASES\\REL430.0\\catalog\\pom.xml",
                P4Operation.edit,
                5, 313654);
        Assert.assertEquals(file, info.getFiles().get(2));

        input = "... depotFile //nucleus/MAIN/catalog/catalog.db/src/main/sql/REL440/data/locale.sql\n" +
                "... clientFile /home/chris/Perforce/EASAP_chriskang_chris-ubuntu_3031/nucleus/MAIN/catalog/catalog.db/src/main/sql/REL440/data/locale.sql\n" +
                "... isMapped\n" +
                "... action add\n" +
                "... change 315421\n" +
                "... type text\n" +
                "... actionOwner EASAP\\chriskang";
        info = P4FileStatInfo.create(Arrays.asList(StringUtils.split(input, "\n")));
        Assert.assertEquals(1, info.getFiles().size());
    }

    @Test
    public void testP4RepoInfo() throws IOException {
        P4RepositoryInfo info = new P4RepositoryInfo("//nucleus/SANDBOX/catalog/...");
        Assert.assertEquals("//nucleus/SANDBOX/catalog/", info.getPath());
        Assert.assertEquals("//nucleus/SANDBOX/catalog/...", info.getPathWithSubContents());

        info = new P4RepositoryInfo("//nucleus/SANDBOX/catalog/...@265261");
        Assert.assertEquals("//nucleus/SANDBOX/catalog/", info.getPath());
        Assert.assertEquals("//nucleus/SANDBOX/catalog/...", info.getPathWithSubContents());

        info = new P4RepositoryInfo("//nucleus/SANDBOX/catalog/...#head");
        Assert.assertEquals("//nucleus/SANDBOX/catalog/", info.getPath());
        Assert.assertEquals("//nucleus/SANDBOX/catalog/...", info.getPathWithSubContents());

        info = new P4RepositoryInfo("//nucleus/SANDBOX/catalog/...@265261,#head");
        Assert.assertEquals("//nucleus/SANDBOX/catalog/", info.getPath());
        Assert.assertEquals("//nucleus/SANDBOX/catalog/...", info.getPathWithSubContents());

        info = new P4RepositoryInfo("//nucleus/RELEASES/REL440.0/...@265261,#head");
        Assert.assertEquals("//nucleus/RELEASES/REL440.0/", info.getPath());
        Assert.assertEquals("//nucleus/RELEASES/REL440.0/...", info.getPathWithSubContents());

        Exception exp = null;
        try {
            new P4RepositoryInfo("//nucleus/SANDBOX/catalog...");
        } catch (VersionControlException e) {
            exp = e;
        }
        Assert.assertNotNull(exp);


        try {
            new P4RepositoryInfo("//nucleus/SANDBOX/catalog...@265261,@265261");
        } catch (VersionControlException e) {
            exp = e;
        }
        Assert.assertNotNull(exp);


        exp = null;
        try {
            new P4RepositoryInfo("//nucleus/SANDBOX/catalog/");
        } catch (VersionControlException e) {
            exp = e;
        }
        Assert.assertNotNull(exp);

        exp = null;
        try {
            new P4RepositoryInfo("//nucleus/SANDBOX/catalog/...@123, @234");
        } catch (VersionControlException e) {
            exp = e;
        }
        Assert.assertNotNull(exp);

        exp = null;
        try {
            new P4RepositoryInfo("//nucleus/SANDBOX/catalog/...#head,@234");
        } catch (VersionControlException e) {
            exp = e;
        }
        Assert.assertNotNull(exp);
    }


//    @Test
    public void testCreateP4ChangeList() throws InterruptedException, ExecutionException, IOException {
        String input = "Change: new\n" +
                "\n" +
                "Client: EASAP_chriskang_EASHDPDESK075_70\n" +
                "\n" +
                "User:   EASAP\\chriskang\n" +
                "\n" +
                "Status: new\n" +
                "\n" +
                "Description: something comments with whitespaces\n" +
                "\tsome more new lines #1\n" +
                "\tsome more new lines #2\n";

        final String[] cmd = new String[]{"-Ztag", "change", "-i"};
        P4SCmdRunner.run(cmd, input, null,
                (res) -> logger.info("STDOUT:\n" + StringUtils.join(res, "\n")),
                (err) -> logger.error(String.format("Failed on %1$s %2$s, error:\n%3$s", StringUtils.join(cmd, " "), input, StringUtils.join(err, "\n"))));
    }

//    @Test
    public void testP4Login() throws InterruptedException, ExecutionException, IOException {
        String input = "User@123";
        P4SCmdRunner.run(new String[]{"-ZTag", "login"}, input, null,
                res -> logger.info("STDOUT:\n" + StringUtils.join(res, "\n")),
                err -> logger.error("STDERR:\n" + StringUtils.join(err, "\n")));
    }


//    @Test
    public void testP4LoginS() throws InterruptedException, ExecutionException, IOException {
        P4SCmdRunner.run(new String[]{"login", "-s"}, null, null,
                c -> logger.info("S: " + StringUtils.join(c, "\n")),
                e -> logger.error("F: " + StringUtils.join(e, "\n")));
    }

//    @Test
    public void testP4Logout() throws InterruptedException, ExecutionException, IOException {
        P4SCmdRunner.run(new String[]{"logout"}, null, null,
                c -> logger.info("S: " + StringUtils.join(c, "\n")),
                e -> logger.error("F: " + StringUtils.join(e, "\n")));
    }

    @Test
    public void testP4Submit() throws InterruptedException, ExecutionException, IOException {
        boolean res = P4SCmdRunner.run(new String[]{"-ZTag", "submit", "-c", "319189"}, null, null,
                c -> logger.info("S: " + StringUtils.join(c, "\n")),
                e -> logger.error("F: " + StringUtils.join(e, "\n")));
    }
}
