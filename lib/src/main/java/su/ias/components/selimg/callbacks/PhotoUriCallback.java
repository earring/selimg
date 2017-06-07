package su.ias.components.selimg.callbacks;

import android.net.Uri;

public interface PhotoUriCallback {
    void onUriSelected(Uri uri);

    void onUriNotSelected();
}
