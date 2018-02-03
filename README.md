# FingerPaintView
![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat) [![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-FingerPaintView-green.svg?style=flat )]( https://android-arsenal.com/details/1/6715 )

A simple view to allow drawing on top of any bitmap, which generates a final bitmap with the correct scaling and transformations applied to it, ensuring the output respects what is shown on the screen.

## How to install

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

Add this to your module's `build.gradle` file:

```gradle
dependencies {
	...
	compile 'com.github.picnic:FingerPaintView:1.0.0'
}
```

## How to use

1. Include the `FingerPaintImageView` into your layout `xml` file. Make sure you set the base `src` image:

```xml
<nl.picnic.fingerpaintview.FingerPaintImageView
        android:id="@+id/finger"
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:layout_alignParentTop="true"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="8dp"
        android:src="@drawable/ic_launcher_background"
        app:inEditMode="true" />
```

2. Make sure the view is in edit mode (`app:inEditMode="true"`)

3. Go crazy drawing on the view. When you're done with the drawing, you can access the final bitmap
via the view's `getDrawable()` method. This bitmap will be the result of the original bitmap combined with your custom drawing.
```java
val bitmap = fingerPaintView.bitmap
```

## Features
* `WYSIWYG`: transformation and scaling is applied to ensure the final bitmap is the same as what you see on screen
* Define different settings for each individual path
* Smooth bézier paths
* Undo and clear all support

## Customization
The following attributes can be set either via xml or programmatically at runtime to generate different drawing effects:

```xml
<attr name="strokeColor" format="color" />          <!-- color of the path -->
<attr name="strokeWidth" format="dimension" />      <!-- width of the path -->
<attr name="inEditMode" format="boolean" />         <!-- enable/disable drawing on the view -->
<attr name="touchTolerance" format="float" />       <!-- smoothness between paths -->
```

## Sample
Refer to the sample project to see a live example of this library in action.

![sample](https://raw.githubusercontent.com/PicnicSupermarket/FingerPaintView/master/sample.gif)

## Contributing
To contribute code you can do so through directly via GitHub by forking this repository and sending a pull request.

When submitting code, please make every effort to follow existing conventions and style in order to keep the code as readable as possible.

## License

Copyright 2018 Picnic Technologies

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
