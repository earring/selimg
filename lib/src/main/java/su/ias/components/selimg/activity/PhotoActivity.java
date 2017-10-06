package su.ias.components.selimg.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.FotoapparatBuilder;
import io.fotoapparat.parameter.selector.LensPositionSelectors;
import io.fotoapparat.result.PendingResult;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.view.CameraView;
import su.ias.components.selimg.R;

public class PhotoActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_FILE = "extraImageFile";
    public static final String EXTRA_USE_FRONT_CAMERA = "useFrontCamera";

    CameraView cameraView;

    private Fotoapparat fotoapparat;
    private File file;
    private boolean useFrontCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        file = (File) getIntent().getSerializableExtra(EXTRA_IMAGE_FILE);
        useFrontCamera = getIntent().getBooleanExtra(EXTRA_USE_FRONT_CAMERA, false);

        cameraView = (CameraView) findViewById(R.id.camera_view);

        FotoapparatBuilder builder = Fotoapparat.with(this);
        if (useFrontCamera) {
            builder.lensPosition(LensPositionSelectors.front());
        } else {
            builder.lensPosition(LensPositionSelectors.back());
        }
        fotoapparat = builder.into(cameraView).build();

        cameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoResult photoResult = fotoapparat.takePicture();
                try {
                    photoResult.saveToFile(file).whenAvailable(new PendingResult.Callback<Void>() {
                        @Override
                        public void onResult(Void aVoid) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                } catch (RuntimeException e) {
                    setResult(RESULT_CANCELED);
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
}
