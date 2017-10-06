package su.ias.components.selimg.providers;

import su.ias.components.selimg.R;

class CancelProvider extends ImageProvider {

    public CancelProvider(ImageSelector imageSelector) {
        super(imageSelector);
    }

    @Override
    public int getType() {
        return ImageProvider.TYPE_CANCEL;
    }

    @Override
    public int getTitle() {
        return R.string.selimg_cancel;
    }

    @Override
    public int getImg() {
        return R.drawable.ic_cancel;
    }

    @Override
    public void selectImage() {
        imageSelector.cancel();
    }
}
