package com.jfm.modules.paint;

import java.io.File;

import com.jfm.core.FileModule;

public class MiniPaintModule implements FileModule {

    @Override
    public boolean supports(File file) {
        String n = file.getName().toLowerCase();
        return n.endsWith(".png") || n.endsWith(".jpg") || n.endsWith(".jpeg");
    }

    @Override
    public void open(File file) {
        new MiniPaint(file); // MiniPaint öffnet Bild direkt
    }
}
