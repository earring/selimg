package su.ias.components.selimg.providers;

import su.ias.components.selimg.R;

class GalleryImageProvider extends ImageProvider {

    public GalleryImageProvider(ImageSelector imageSelector) {
        super(imageSelector);
    }

    @Override
    public int getType() {
        return ImageProvider.TYPE_FROM_GALLERY;
    }

    @Override
    public int getTitle() {
        return R.string.selimg_select_from_gallery;
    }

    @Override
    public int getImg() {
        return R.drawable.ic_image_multiple;
    }

    @Override
    public void selectImage() {
        imageSelector.openImageFromGallery();
    }
}
