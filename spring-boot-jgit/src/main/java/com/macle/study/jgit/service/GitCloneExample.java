package com.macle.study.jgit.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;

public class GitCloneExample {
    public static void main(String[] args) {
        try {
            Git.cloneRepository().setURI("https://github.com/username/repo.git").setDirectory(new File("./local-repo")).call();
            System.out.println("仓库克隆完成！");
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }
}