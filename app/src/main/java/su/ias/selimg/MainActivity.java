package su.ias.selimg;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import su.ias.components.selimg.Selimg;
import su.ias.components.selimg.callbacks.PhotoFileCallback;
import su.ias.components.selimg.callbacks.PhotoUriCallback;
import su.ias.components.selimg.providers.ImageProvider;

public class MainActivity extends AppCompatActivity implements PhotoUriCallback, PhotoFileCallback {

    @BindView(R.id.img_uri)
    ImageView imgUri;

    @BindView(R.id.img_file)
    ImageView imgFile;

    @BindView(R.id.txt_uri)
    TextView txtUri;

    @BindView(R.id.txt_file)
    TextView txtFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_select_image)
    public void onViewClicked() {
        requestImage();
    }

    private void requestImage() {
        Selimg.getInstance()
                .type(ImageProvider.TYPE_FROM_CAMERA)
                .type(ImageProvider.TYPE_FROM_GALLERY)
                .type(ImageProvider.TYPE_CANCEL)
                .showIcons(true)
                .uri(this)
                .file(this)
                .showWith(getSupportFragmentManager());
    }

    @Override
    public void onUriSelected(Uri uri) {
        Glide.with(this).load(uri).fitCenter().into(imgUri);
        txtUri.setText(uri.toString());
    }

    @Override
    public void onUriNotSelected() {
        imgUri.setImageDrawable(null);
        txtUri.setText("URI");
    }

    @Override
    public void onFileSelected(File file) {
        Glide.with(this).load(file).fitCenter().into(imgFile);
        txtFile.setText(file.getPath());
    }

    @Override
    public void onFileNotSelected() {
        imgFile.setImageDrawable(null);
        txtFile.setText("File");
    }
}
