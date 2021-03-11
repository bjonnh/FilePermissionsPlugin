# FilePermissionsPlugin

![Build](https://github.com/bjonnh/FilePermissionsPlugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/16238.svg)](https://plugins.jetbrains.com/plugin/16238-filepermissionsplugin)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/16238.svg)](https://plugins.jetbrains.com/plugin/16238-filepermissionsplugin)

<!-- Plugin description -->
**File Permissions Plugin** allows the change of file permissions directly from IntelliJ.

It adds two actions:

- **Change Permissions** to change the permissions by showing you a dialog. 
- **Make File Executable** to make the file executable by its owner.

These actions are accessible from the IntelliJ Actions: using shift-shift or control-shift-A (⇧⌘A on MacOS)

They are also accessible in the File menu and inside File Properties:

![A screenshot of the file menu showing the two actions](https://raw.githubusercontent.com/bjonnh/FilePermissionsPlugin/main/images/filepermission.jpg)

If you choose **Change Permissions** you will see the following dialog:

![Screenshot of the dialog when changing permissions](https://raw.githubusercontent.com/bjonnh/FilePermissionsPlugin/main/images/screenshot.png)
<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "FilePermissionsPlugin"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/bjonnh/FilePermissionsPlugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template

## Authors and participants

- Thanks to Natalia Melnikova (JB) for the menu screenshot and the proposition to add it
