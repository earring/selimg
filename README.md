# selimg
[ ![Download](https://api.bintray.com/packages/interactiveservices/maven/selimg/images/download.svg) ](https://bintray.com/interactiveservices/maven/selimg/_latestVersion)
[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/MIT)
[![API](https://img.shields.io/badge/API-14%2B-green.svg)](https://developer.android.com/about/versions/android-4.0.html)

This is simple library for getting URI or file path of image selected by user.
Simple one-call interface and two callbacks let you get neccessary objects.

## UI
![bottomsheet](https://raw.githubusercontent.com/interactiveservices/selimg/master/screenshots/bottomsheet.png)

## Usage
Get `Selimg` builder via `getInstance()` method.
### Builder configuration
- `type(int)` is for possible types of selected image
- `useFrontCamera()` is front camera start as main
- `showIcons()` show icons in list of options or not?
- `rotateImage()` rotate image on Samsung-like devices or not?
- `setPhotoUriCallback()` is for setting callback when you want an URI of selected image
- `setPhotoFileCallback()` is for setting callback when you want a file of selected image
### Warning
In order to using library you must specify additional components in build.gradle dependencies.
### Example
```java
public class MainActivity extends AppCompatActivity implements PhotoFileCallback {

    ...
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Selimg.getInstance().setPhotoFileCallback(this);
        Selimg.getInstance().setPhotoUriCallback(this);
    }

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
    
    @Override
    protected void onDestroy() {
        Selimg.getInstance().setPhotoFileCallback(null);
        Selimg.getInstance().setPhotoUriCallback(null);
        super.onDestroy();
    }
}
```
### Adding library to your project
in root build.gradle
```groovy
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
```
in app build.gradle
```groovy
dependencies {
    ...
    
    implementation 'su.ias.components:adapters-android:1.0.4'
    implementation 'su.ias.components:utils-android:1.0.17'
    implementation 'su.ias.components:selimg:1.2.2'
    implementation 'io.fotoapparat.fotoapparat:library:1.4.1'
}
```
