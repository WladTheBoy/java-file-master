package com.jfm.modules.audio;

import java.io.File;

import com.jfm.core.FileModule;

public class AudioPlayerModule implements FileModule {

    @Override
    public boolean supports(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".wav") || name.endsWith(".mp3");
    }

    @Override
    public void open(File file) {
        new AudioPlayer(file);
    }
}

