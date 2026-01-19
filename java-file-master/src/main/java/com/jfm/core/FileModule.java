package com.jfm.core;

import java.io.File;

public interface FileModule {
    boolean supports(File file);
    void open(File file);
}

