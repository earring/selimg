package su.ias.components.selimg;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import su.ias.components.selimg.activity.PhotoActivity;
import su.ias.components.selimg.callbacks.PhotoFileCallback;
import su.ias.components.selimg.callbacks.PhotoUriCallback;
import su.ias.components.selimg.providers.ImageProvider;
import su.ias.components.selimg.providers.ImageSelector;
import su.ias.components.selimg.utils.FileUtils;
import su.ias.utils.BitmapUtils;

public final class SelimgBottomSheet extends BottomSheetDialogFragment implements ImageSelector {

    private static final String TYPE_ARRAY = "typeArray";
    private static final int REQUEST_GALLERY_OPEN = 1;
    private static final int REQUEST_CAMERA_OPEN = 2;
    private static final int REQUEST_WRITE_EXTERNAL_PERMISSION = 1000;

    private List<Integer> typeList;
    private File cameraOutputFile;

    public static SelimgBottomSheet newInstance(ArrayList<Integer> types) {
        Bundle args = new Bundle();
        SelimgBottomSheet fragment = new SelimgBottomSheet();
        args.putIntegerArrayList(TYPE_ARRAY, types);
        fragment.setArguments(args);
        return fragment;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,  /* prefix */
                                   ".jpg",         /* suffix */
                                   storageDir      /* directory */);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View view = View.inflate(getContext(), R.layout.bottom_sheet_selector, null);
        dialog.setContentView(view);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rcview_selector);
        List<ImageProvider> imageProviderList = new ArrayList<>();
        typeList = getArguments().getIntegerArrayList(TYPE_ARRAY);
        if (typeList != null) {
            for (int type : typeList) {
                imageProviderList.add(ImageProvider.createImageProvider(SelimgBottomSheet.this,
                                                                        type));
            }
        }
        recyclerView.setAdapter(new ImageProviderAdapter(imageProviderList,
                                                         null,
                                                         Selimg.getInstance().getShowIcons()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void openImageFromGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,
                                                    getString(R.string.selimg_chooser_select_picture)),
                               REQUEST_GALLERY_OPEN);
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @Override
    public void openImageFromCamera() {
        if (ContextCompat.checkSelfPermission(getContext(),
                                              Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat
                .checkSelfPermission(getContext(),
                                     Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openImageFromCameraIntent();
        } else {
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_WRITE_EXTERNAL_PERMISSION);
        }
    }

    @Override
    public void cancel() {
        dismiss();
    }

    private void openImageFromCameraIntent() {
        Intent takePictureIntent = new Intent(getContext(), PhotoActivity.class);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            try {
                cameraOutputFile = createImageFile();
                takePictureIntent.putExtra(PhotoActivity.EXTRA_IMAGE_FILE, cameraOutputFile);
                takePictureIntent.putExtra(PhotoActivity.EXTRA_USE_FRONT_CAMERA,
                                           Selimg.getInstance().getUseFrontCamera());
                startActivityForResult(takePictureIntent, REQUEST_CAMERA_OPEN);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Uri uri = null;
        File file = null;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_OPEN) {
                uri = intent.getData();
                String fileName = FileUtils.getImageFileName(getContext(), uri);
                if (!TextUtils.isEmpty(fileName)) {
                    file = new File(fileName);
                }
            } else if (requestCode == REQUEST_CAMERA_OPEN) {
                try {
                    // rotate image if necessary
                    Bitmap bitmap = BitmapUtils.rotateImage(cameraOutputFile.getAbsolutePath());
                    FileOutputStream fileOutputStream = new FileOutputStream(cameraOutputFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream);
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                file = cameraOutputFile;
                uri = Uri.fromFile(file);
            }
        }

        PhotoUriCallback uriCallback = Selimg.getInstance().getPhotoUriCallback();
        if (uriCallback != null) {
            if (uri != null) {
                uriCallback.onUriSelected(uri);
            } else {
                uriCallback.onUriNotSelected();
            }
        }

        PhotoFileCallback fileCallback = Selimg.getInstance().getPhotoFileCallback();
        if (fileCallback != null) {
            if (file != null) {
                fileCallback.onFileSelected(file);
            } else {
                fileCallback.onFileNotSelected();
            }
        }

        dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openImageFromCameraIntent();
                } else {
                    Toast.makeText(getContext(),
                                   R.string.selimg_permission_rationale_write_external_storage,
                                   Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}