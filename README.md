# FingerPaintView
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)]

A simple view to allow drawing on top of any bitmap, which generates a final bitmap with the correct scaling and transformations.   

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

Add this to your module's `build.gradle` file (make sure the version matches the JitPack badge above):

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
via the view's `getDrawable()` metho. This bitmap will be the result of the original bitmap plus your custom drawing) 
```java
val bitmap = fingerPaintView.bitmap
```

## Features
* `WYSIWYG`: transformation and scaling is applied to ensure the final bitmap is the same as what you see on screen
* Define different settings for each path
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

## Contributing
To contribute code you can do so through directly via GitHub by forking this repository and sending a pull request.

When submitting code, please make every effort to follow existing conventions and style in order to keep the code as readable as possible.

## License

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.