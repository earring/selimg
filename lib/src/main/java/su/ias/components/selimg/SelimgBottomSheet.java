package su.ias.components.selimg;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import su.ias.components.selimg.callbacks.PhotoFileCallback;
import su.ias.components.selimg.callbacks.PhotoUriCallback;
import su.ias.components.selimg.providers.ImageProvider;
import su.ias.components.selimg.providers.ImageSelector;
import su.ias.components.selimg.utils.FileUtils;

public final class SelimgBottomSheet extends BottomSheetDialogFragment implements ImageSelector {

    private static final String TYPE_ARRAY = "typeArray";
    private static final int REQUEST_GALLERY_OPEN = 1;
    private static final int REQUEST_CAMERA_OPEN = 2;
    private static final int REQUEST_WRITE_EXTERNAL_PERMISSION = 1000;

    private List<Integer> typeList;
    private Uri cameraOutputFile;

    public static SelimgBottomSheet newInstance(ArrayList<Integer> types) {
        Bundle args = new Bundle();
        SelimgBottomSheet fragment = new SelimgBottomSheet();
        args.putIntegerArrayList(TYPE_ARRAY, types);
        fragment.setArguments(args);
        return fragment;
    }

    public static String generateImageFilename() {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        return timeStamp + ".jpg";
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
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
        recyclerView.setAdapter(new ImageProviderAdapter(imageProviderList, null));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void openImageFromGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,
                                                    getString(R.string.chooser_select_picture)),
                               REQUEST_GALLERY_OPEN);
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @Override
    public void openImageFromCamera() {
        if (ContextCompat.checkSelfPermission(getContext(),
                                              Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openImageFromCameraIntent();
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                               REQUEST_WRITE_EXTERNAL_PERMISSION);
        }
    }

    private void openImageFromCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, generateImageFilename());
            cameraOutputFile = getContext().getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraOutputFile);
            startActivityForResult(takePictureIntent, REQUEST_CAMERA_OPEN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Uri uri = null;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_OPEN) {
                uri = intent.getData();
            } else if (requestCode == REQUEST_CAMERA_OPEN) {
                uri = cameraOutputFile;
            }
        }

        File file = null;
        String fileName = FileUtils.getImageFileName(getContext(), uri);
        if (!TextUtils.isEmpty(fileName)) {
            file = new File(fileName);
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
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageFromCameraIntent();
                } else {
                    Toast.makeText(getContext(),
                                   R.string.permission_rationale_write_external_storage,
                                   Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}