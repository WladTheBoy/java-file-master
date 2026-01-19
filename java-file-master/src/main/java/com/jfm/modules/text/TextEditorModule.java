package com.jfm.modules.text;

import java.io.File;

import com.jfm.core.FileModule;

public class TextEditorModule implements FileModule {

	@Override
	public boolean supports(File file) {
		String name = file.getName().toLowerCase();
		return name.endsWith(".txt") || name.endsWith(".java") || name.endsWith(".md");
	}

	@Override
	public void open(File file) {
		new TextEditor(file);

	}

}
