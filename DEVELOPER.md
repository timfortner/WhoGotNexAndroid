# Developer documentation #

This documentation will help you to understand Whogotnex android side code.  

If you want to report a bug or ask a question, [create an issue](https://github.com/tfortne/WhoGotNexAndroid/issues/new).

## Summary ##

- [General](#general)
- [Requirements](#requirements)
- [Installation](#installation)
- [Code structure](#code-structure)
- [Build](#build)
- [Running](#running)

## General ##

- **Author** : Tim Fortner
- **Version** : 1.0

## Requirements ##

1. Android Studio 
2. JDK 8.1.4 or higher

## Installation ##

1. Run `git clone https://github.com/tfortne/WhoGotNexAndroid.git`
2. Open the 'WhoGotNexAndroid' in android studio.

## Code structure ##

```
WhoGotNexAndroid
├── .git
├── app
├── build
├── gradle
```

- **Code** : the base app is configured by a combination of xml, which contains the UI inforamtion and basic functions, and java, which specifies certain functions that attributes perform.

|File/Folder|Description|
|---|---|
|**.git**|Contains file templates for Gitlab|
|**app**|Contains all files for app functionality and structure|
|**build**|Contains android profile JSON files|
|**gradle**|Contains Java wrapper|

### app ###

```
├── app
    ├── build
    │   scr
    │   libs
    ...
...
```
|Folder|Description|
|---|---|
|app|contains all java and xml files|
|app/build|Contains assests from android studio|
|app/src|Contains app source code|
|app/libs|Contains all libraries needed|

## Build ##

### Development environment  

1. To run, simply hit the 'build and debug' button in the android studio window and have an android device ready to run the code or a virtual device.
2. Or you can install the app's APK file onto your device.
