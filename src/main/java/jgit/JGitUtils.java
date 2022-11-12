package jgit;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.FS;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/*
* JGit 的 API 有两种基本的层次：底层命令和高层命令。
* 这个两个术语都来自 Git ，并且 JGit 也被按照相同的方式粗略地划分：
* 高层 API 是一个面向普通用户级别功能的友好的前端（一系列普通用户使用 Git 命令行工具时可能用到的东西），
* 底层 API 则直接作用于低级的仓库对象。
*
* Low-level API
    从名称中获取SHA-1引用，例如refs/heads/master
    从名称或SHA-1中获取commit-object
    获取commit-message
    从commit-object，name或SHA-1中获取tree-object
    读取文件/blob的内容
    从名称或SHA-1中获取tag-object
    解决复杂的引用，例如SHA-1的头^
    迭代分支上的提交
    迭代一系列提交
    从特定提交中读取特定文件的内容
    列出为当前存储库配置的远程设备
    从Git打印用户信息
    读取文件属性，例如可执行状态、文件或目录、大小。
    使用BranchTrackingStatus类检索与远程分支相比的前/后提交数
    检查其他分支上的提交是否合并到给定分支中
    列出目录中特定提交或标记的文件
    递归地迭代提交的文件
    迭代提交non-recursively的文件
    查找可通过标记、分支、远程、头等访问的所有提交。
* 大多数 JGit 会话会以 Repository 类作为起点，你首先要做的事就是创建一个它的实例。
* 对于一个基于文件系统的仓库来说（JGit 允许其它的存储模型），用 FileRepositoryBuilder 完成它

* 底层 API 更加完善，但是有时将它们串起来以实现普通的目的非常困难，例如将一个文件添加到索引，或者创建一个新的提交。
* 为了解决这个问题， JGit 提供了一系列高层 API ，使用这些 API 的入口点就是 Git 类
* 高层API通过创建Git对象进行调用
*
*   Repository 包括所有的对象和引用，用来管理源码


    AnyObjectId 表示SHA1对象，可以获得SHA1的值，进而可以获得git对象


    Ref 引用对象，表示.git/refs下面的文件引用 Ref HEAD = repository.getRef("refs/heads/master");


    RevWalk 可以遍历提交对象，并按照顺序返回提交对象


    RevCommit 代表一个提交对象


    RevTag 代表标签对象


    RevTree 代表树对象
* */
public class JGitUtils {
    public static Git openRpo(String dir) {
        Git git = null;
        try {
            Repository repo = new FileRepositoryBuilder().setGitDir(new File(dir, ".git")).build();
            git = new Git(repo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return git;
    }

    public static Git openNowRpo() {
        Git git = null;
        try {
            Repository repo = new FileRepositoryBuilder().readEnvironment().findGitDir().build();
            git = new Git(repo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return git;
    }

    public static Git initRpo(String dir) {
        Git git = null;
        try {
            git = Git.init().setDirectory(new File(dir)).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return git;
    }

    public static void add(Git git, String filepattern) {
        try {
            git.add().addFilepattern(filepattern).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public static void commit(Git git, String commitMessage) {
        try {
            git.commit().setMessage(commitMessage).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public static void printLog(Git git) {
        Iterable<RevCommit> iterable = null;
        try {
            iterable = git.log().call();
            Iterator<RevCommit> iter = iterable.iterator();
            while (iter.hasNext()) {
                RevCommit commit = iter.next();
                String email = commit.getAuthorIdent().getEmailAddress();
                String name = commit.getAuthorIdent().getName();  //作者

                String commitEmail = commit.getCommitterIdent().getEmailAddress();//提交者
                String commitName = commit.getCommitterIdent().getName();

                int time = commit.getCommitTime();

                String fullMessage = commit.getFullMessage();
                String shortMessage = commit.getShortMessage();  //返回message的firstLine

                String commitID = commit.getName();  //这个应该就是提交的版本号

                System.out.println("authorEmail:" + email);
                System.out.println("authorName:" + name);
                System.out.println("commitEmail:" + commitEmail);
                System.out.println("commitName:" + commitName);
                System.out.println("time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time * 1000L));
                System.out.println("fullMessage:" + fullMessage);
                System.out.println("shortMessage:" + shortMessage);
                System.out.println("commitID:" + commitID);
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        }

    }


    public static void push(Git git, String username, String password, String remoteName, String branchName, String privateToken) {
        CredentialsProvider provider = new UsernamePasswordCredentialsProvider(username, privateToken);
        try {
            git.push()
                    .setRemote(remoteName)
                    .setRefSpecs(new RefSpec(branchName))
                    .setCredentialsProvider(provider)
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public static void pull(Git git, String username, String password, String branchName, String privateToken) {
        CredentialsProvider provider = new UsernamePasswordCredentialsProvider(username, privateToken);
        try {
            git.pull()
                    .setRemoteBranchName(branchName)
                    .setCredentialsProvider(provider)
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public static void branchCreate(Git git, String branchName) {
        try {
            Ref ref = git.branchCreate().setName(branchName).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public static void branchDelete(Git git, String branchName) {
        try {
            git.branchDelete().setBranchNames(branchName).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public static void branchCheckout(Git git, String branchName) {
        try {
            Ref ref = git.checkout().setName(branchName).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public static void branchList(Git git) {
        try {
            List<Ref> call = git.branchList().call();     //得到所有分支信息
            for (Ref ref : call)
                System.out.println(ref);
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

}

