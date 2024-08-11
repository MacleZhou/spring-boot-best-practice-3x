package com.macle.study.deployment;

import java.io.File;
import java.io.FilenameFilter;

public class JarFileFilter implements FilenameFilter {
    public JarFileFilter(){}

    public boolean accept(File dir, String name) {
        if(dir.isDirectory()){
            return false;
        } else {
            return name.endsWith(".jar");
        }
    }
}
