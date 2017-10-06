package su.ias.components.selimg.providers;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

public abstract class ImageProvider {

    public static final int TYPE_FROM_GALLERY = 0;
    public static final int TYPE_FROM_CAMERA = 1;
    public static final int TYPE_CANCEL = 2;

    protected ImageSelector imageSelector;

    public ImageProvider(ImageSelector imageSelector) {
        this.imageSelector = imageSelector;
    }

    abstract public int getType();

    @StringRes
    abstract public int getTitle();

    @DrawableRes
    abstract public int getImg();

    abstract public void selectImage();

    @Nullable
    public static ImageProvider createImageProvider(ImageSelector imageSelector, int type) {
        switch (type) {
            case TYPE_FROM_GALLERY:
                return new GalleryImageProvider(imageSelector);
            case TYPE_FROM_CAMERA:
                return new CameraImageProvider(imageSelector);
            case TYPE_CANCEL:
                return new CancelProvider(imageSelector);
        }
        return null;
    }
}
