package su.ias.components.selimg;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

import su.ias.components.selimg.callbacks.PhotoFileCallback;
import su.ias.components.selimg.callbacks.PhotoUriCallback;

public class Selimg {

    public static final int IMAGE_QUALITY = 70;

    private static Selimg INSTANCE;

    private ArrayList<Integer> selectedTypes;
    private boolean useFrontCamera = false;
    private boolean rotateImage = false;

    private PhotoUriCallback photoUriCallback;
    private PhotoFileCallback photoFileCallback;
    private boolean showIcons = true;

    public static Selimg getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Selimg();
        }
        INSTANCE.selectedTypes = new ArrayList<>();
        return INSTANCE;
    }

    public Selimg type(Integer type) {
        selectedTypes.add(type);
        return this;
    }

    public void showWith(FragmentManager fragmentManager) {
        SelimgBottomSheet.newInstance(selectedTypes)
                .show(fragmentManager, this.getClass().getName() + ".selectImageBottomSheet");
    }

    public Selimg useFrontCamera() {
        this.useFrontCamera = true;
        return this;
    }

    public Selimg showIcons(boolean showIcons) {
        this.showIcons = showIcons;
        return this;
    }

    boolean getUseFrontCamera() {
        return useFrontCamera;
    }

    public boolean getShowIcons() {
        return showIcons;
    }

    @Nullable
    PhotoUriCallback getPhotoUriCallback() {
        return photoUriCallback;
    }

    public void setPhotoUriCallback(PhotoUriCallback photoUriCallback) {
        this.photoUriCallback = photoUriCallback;
    }

    @Nullable
    PhotoFileCallback getPhotoFileCallback() {
        return photoFileCallback;
    }

    public void setPhotoFileCallback(PhotoFileCallback photoFileCallback) {
        this.photoFileCallback = photoFileCallback;
    }

    public boolean isRotateImage() {
        return rotateImage;
    }

    /**
     * Set rotation of photo on Samsung-like devices
     * @param rotateImage rotate image or not (boolean)
     */
    public Selimg rotateImage(boolean rotateImage) {
        this.rotateImage = rotateImage;
        return this;
    }
}
