package su.ias.components.selimg.callbacks;

import java.io.File;

public interface PhotoFileCallback {
    void onFileSelected(File file);

    void onFileNotSelected();
}
