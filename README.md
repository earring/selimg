# selimg
[ ![Download](https://api.bintray.com/packages/interactiveservices/maven/selimg/images/download.svg) ](https://bintray.com/interactiveservices/maven/selimg/_latestVersion)
[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/MIT)
[![API](https://img.shields.io/badge/API-14%2B-green.svg)](https://developer.android.com/about/versions/android-4.0.html)

This is simple library for getting URI or file path of image selected by user.
Simple one-call interface and two callbacks let you get neccessary objects.

## Usage
Get `Selimg` builder via `getInstance()` method.
### Configuration
- `type(int)` is for possible types of selected image
- `uri()` is for setting callback when you want an URI of selected image
- `file()` is for setting callback when you want a file of selected image
### Warning
In order to using camera provider (type ImageProvider.TYPE_FROM_CAMERA) you must specify permission in your AndroidManifest.xml.
```java
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```
### Example
```java
public class MainActivity extends AppCompatActivity implements PhotoFileCallback {

    ...

    private void requestImage() {
        // call image selector
        Selimg.getInstance()
                .type(ImageProvider.TYPE_FROM_CAMERA)
                .type(ImageProvider.TYPE_FROM_GALLERY)
                .file(this)
                .showWith(getSupportFragmentManager());
    }

    @Override
    public void onFileSelected(File file) {
        // when user selected file and Selimg successfully got it
    }

    @Override
    public void onFileNotSelected() {
        // when user didn't select file
    }
}
```
### Adding library to your project
```groovy
dependencies {
    ...
    compile 'su.ias.components:selimg:1.0.0'
}
```
