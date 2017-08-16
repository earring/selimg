package su.ias.components.selimg;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

import su.ias.components.selimg.callbacks.PhotoFileCallback;
import su.ias.components.selimg.callbacks.PhotoUriCallback;

public class Selimg {

    private static Selimg INSTANCE;

    private ArrayList<Integer> selectedTypes;
    private boolean useFrontCamera = false;
    private PhotoUriCallback photoUriCallback;
    private PhotoFileCallback photoFileCallback;

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

    public Selimg uri(PhotoUriCallback photoUriCallback) {
        this.photoUriCallback = photoUriCallback;
        return this;
    }

    public Selimg file(PhotoFileCallback photoFileCallback) {
        this.photoFileCallback = photoFileCallback;
        return this;
    }

    public void showWith(FragmentManager fragmentManager) {
        SelimgBottomSheet.newInstance(selectedTypes)
                .show(fragmentManager, this.getClass().getName() + ".selectImageBottomSheet");
    }

    public Selimg useFrontCamera(){
        this.useFrontCamera = true;
        return this;
    }

    boolean getUseFrontCamera(){
        return useFrontCamera;
    }

    @Nullable
    PhotoUriCallback getPhotoUriCallback() {
        return photoUriCallback;
    }

    @Nullable
    PhotoFileCallback getPhotoFileCallback() {
        return photoFileCallback;
    }
}
