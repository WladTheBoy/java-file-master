# Java File Master (JFM) BETA

Java File Master (JFM) is a modular desktop application written in Java that acts as a **universal file opener**. Depending on the selected file type, JFM automatically launches the appropriate internal module (application) to handle and edit the file.

The goal of JFM is to provide a **single, extensible platform** for working with many different file types instead of using separate programs.

<img width="512" height="512" alt="JFM_Logo" src="https://github.com/user-attachments/assets/18615ac3-c1de-4b85-80c1-a8843b42cb75" />


---

##  Features

*  **Central File Chooser** – Open any file from one main application
*  **Modular Architecture** – Each file type is handled by its own module
*  **MiniPaint Module** – Open, edit, and save image files (PNG, JPG, etc.)
*  **Audio Player Module** – Play audio files (WAV, MP3) with timeline control
*  **Text Editor Module** – Edit text and source code with syntax highlighting
*  **Easy to Extend** – New modules can be added without changing the core

---

##  Architecture Overview

JFM is built around a simple but powerful interface-based architecture.

### Core Concept

* The main application (`JFMMain`) does **not** know details about file types
* Each module implements the `FileModule` interface
* When a file is selected, JFM finds the first module that supports it

```
JFMMain
   |
   v
FileModule (interface)
   |
   +-- MiniPaintModule
   +-- AudioPlayerModule
   +-- TextEditorModule
```

This design follows solid software engineering principles (Open/Closed Principle, loose coupling).

---

##  Modules

###  MiniPaint Module

* Opens image files (PNG, JPG, JPEG)
* Drawing tools: pen, eraser, shapes
* Color picker and brush size
* Undo / Redo
* Save & Save As

###  Audio Player Module

* Supports WAV and MP3 files
* Play / Pause / Stop
* Timeline with progress tracking
* JavaFX-based audio backend for MP3 support

###  Text Editor Module

* Open and edit text-based files
* Syntax highlighting (Java, XML, etc.)
* Save and Save As functionality

---

##  Technology Stack

* **Java 17+**
* **Swing** – Main UI framework
* **JavaFX** – Audio playback (MP3 support)
* **Maven** – Dependency management
* **RSyntaxTextArea** – Syntax highlighting

---

##  Project Structure

```
com.jfm
 ├─ core
 │   └─ JFMMain.java
 ├─ modules
 │   ├─ paint
 │   ├─ audio
 │   └─ text
 └─ FileModule.java
```

---

##  How to Run

1. Make sure **Java 17 or newer** is installed
2. Clone the repository
3. Open the project in Eclipse or IntelliJ
4. Run `JFMMain`

---

##  Current Status

*  Core system implemented
*  Image, audio, and text modules working
*  More file types planned (PDF, video, archives, etc.)
*  Plugin system planned

---

##  Future Plans

* PDF viewer/editor
* Video player module
* Code editor improvements
* Plugin marketplace
* File preview mode
* Custom themes

---

##  Author

Developed as a learning and experimental project focused on:

* modular architecture
* clean design
* extensibility

---

##  License

This project is currently for educational and personal use.
A license may be added later.

---

> **Java File Master** – One application. Many file types.
