package com.macle.study.jgit.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;

public class BranchExample {
    public static void main(String[] args) {
        try (Git git = Git.open(new File("./my-repo"))) {
            // 创建新分支
            git.branchCreate().setName("feature-branch").call();

            // 切换分支
            git.checkout().setName("feature-branch").call();
            System.out.println("分支操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}