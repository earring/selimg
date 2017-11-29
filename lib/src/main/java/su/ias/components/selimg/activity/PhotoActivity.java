package su.ias.components.selimg.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.FotoapparatBuilder;
import io.fotoapparat.parameter.selector.FocusModeSelectors;
import io.fotoapparat.parameter.selector.LensPositionSelectors;
import io.fotoapparat.parameter.selector.Selectors;
import io.fotoapparat.result.PendingResult;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.view.CameraView;
import su.ias.components.selimg.R;
import su.ias.components.selimg.Selimg;
import su.ias.utils.BitmapUtils;

public class PhotoActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_FILE = "extraImageFile";
    public static final String EXTRA_USE_FRONT_CAMERA = "useFrontCamera";

    private Fotoapparat fotoapparat;
    private File file;
    private boolean useFrontCamera;
    private CameraView cameraView;
    private FloatingActionButton btnShot;
    private ProgressBar progress;
    private BitmapRotationAsyncTask bitmapRotationAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        file = (File) getIntent().getSerializableExtra(EXTRA_IMAGE_FILE);
        useFrontCamera = getIntent().getBooleanExtra(EXTRA_USE_FRONT_CAMERA, false);

        cameraView = findViewById(R.id.camera_view);
        btnShot = findViewById(R.id.btn_shot);
        progress = findViewById(R.id.progress);

        FotoapparatBuilder builder = Fotoapparat.with(this);

        // setup the type of camera
        if (useFrontCamera) {
            builder.lensPosition(LensPositionSelectors.front());
        } else {
            builder.lensPosition(LensPositionSelectors.back());
        }

        // setup the type of focus
        // we use the first focus mode which is supported by device
        builder.focusMode(Selectors.firstAvailable(FocusModeSelectors.continuousFocus(),
                                                   FocusModeSelectors.autoFocus(),
                                                   FocusModeSelectors.fixed()));

        fotoapparat = builder.into(cameraView).build();

        if (!fotoapparat.isAvailable()) {
            setResult(RESULT_CANCELED);
            finish();
        }

        btnShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnShot.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                PhotoResult photoResult = fotoapparat.takePicture();
                try {
                    photoResult.saveToFile(file).whenAvailable(new PendingResult.Callback<Void>() {
                        @Override
                        public void onResult(Void aVoid) {
                            bitmapRotationAsyncTask = new BitmapRotationAsyncTask(file);
                            bitmapRotationAsyncTask.execute();
                        }
                    });
                } catch (RuntimeException e) {
                    progress.setVisibility(View.GONE);
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fotoapparat.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        fotoapparat.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmapRotationAsyncTask != null) {
            bitmapRotationAsyncTask.cancel(true);
        }
    }

    private class BitmapRotationAsyncTask extends AsyncTask<Void, Void, Void> {

        private File bitmapFile;

        public BitmapRotationAsyncTask(File bitmapFile) {
            this.bitmapFile = bitmapFile;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // rotate image if necessary
                if (Selimg.getInstance().isRotateImage()) {
                    Bitmap bitmap = BitmapUtils.rotateImage(bitmapFile.getAbsolutePath());
                    FileOutputStream fileOutputStream = new FileOutputStream(bitmapFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,
                                    Selimg.IMAGE_QUALITY,
                                    fileOutputStream);
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!isCancelled()) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

}
