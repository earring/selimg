package su.ias.components.selimg.providers;

import su.ias.components.selimg.R;

class CameraImageProvider extends ImageProvider {

    public CameraImageProvider(ImageSelector imageSelector) {
        super(imageSelector);
    }

    @Override
    public int getType() {
        return ImageProvider.TYPE_FROM_CAMERA;
    }

    @Override
    public int getTitle() {
        return R.string.selimg_select_from_camera;
    }

    @Override
    public int getImg() {
        return R.drawable.ic_camera;
    }

    @Override
    public void selectImage() {
        imageSelector.openImageFromCamera();
    }

    @Override
    public int getColor() {
        return R.color.selimg_black;
    }
}
