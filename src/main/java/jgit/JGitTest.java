package jgit;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.util.Set;

public class JGitTest {
    static String localPath = "D:\\data\\magic-api2";
    static String localPath2 = "D:\\coding\\jgitTest";
    static String username = "PRIVATE-TOKEN";
    static String password = "";
    static String privateToken = "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";
    static String remoteName = "origin";
    static String branchName = "master";
    public static void main(String[] args) {
//        System.out.println(JGitUtils.openRpo(localPath));
//        System.out.println(JGitUtils.openNowRpo());
//        System.out.println(JGitUtils.initRpo(localPath));
//        System.out.println(JGitUtils.openRpo(localPath));
//        Git git = JGitUtils.openRpo(localPath);
        Git git = JGitUtils.openNowRpo();
        String filepattern = ".";
        JGitUtils.add(git,filepattern);
        JGitUtils.commit(git, "first commit");
        JGitUtils.printLog(git);
        JGitUtils.push(git, username, password, remoteName, branchName, privateToken);
    }
}
