package com.macle.study.jgit.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

public class JgitService {
    public static void main(String[] args) {
        try {
            // 初始化仓库, 我们使用Git.init()创建了一个新的Git仓库
            Git git = Git.init()
                    .setDirectory(new File("./my-repo"))
                    .call();

            // 创建文件并提交,在仓库中创建了一个测试文件
            File myFile = new File("./my-repo/test.txt");
            myFile.createNewFile();

            //通过git.add()将文件添加到暂存区
            git.add().addFilepattern("test.txt").call();

            //最后用git.commit()提交更改
            git.commit().setMessage("我的第一次提交！").call();
            System.out.println("仓库创建成功！");
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
    }
}