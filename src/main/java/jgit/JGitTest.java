package jgit;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.util.Set;

public class JGitTest {
    static String localPath = "D:\\data\\magic-api2";
    static String localPath2 = "D:\\coding\\jgitTest";
//    static String username = "PRIVATE-TOKEN";
    static String username = "16638838362";
    static String password = "74ad1b20c9c7415e6249e2f32f582967";
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
        JGitUtils.commit(git, "second commit");
//        JGitUtils.printLog(git);
//        JGitUtils.push(git, username, password, remoteName, branchName, privateToken);
//        JGitUtils.pull(git, username, password, branchName, privateToken);
//        JGitUtils.branchCreate(git, "dev");
//        JGitUtils.branchCheckout(git, "dev");
//        JGitUtils.branchDelete(git, "dev");
//        JGitUtils.branchList(git);

    }
}
