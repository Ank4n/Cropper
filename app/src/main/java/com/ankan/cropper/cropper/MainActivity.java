package com.ankan.cropper.cropper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private RecyclerView mRecyclerView;
    private Uri mCurrentImageUri;
    private String mCurrentPhotoPath;
    private ImageAdapter mAdapter;
    private File storageDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check permissions
        if (!Utilities.checkWritePermissions(this))
            Toast.makeText(this, "Please accept the permission for this app to work correctly", Toast.LENGTH_LONG).show();

        //load existing images
        setupRecyclerView();

        Button buttonCamera = (Button) findViewById(R.id.shoot);
        buttonCamera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                takePicture();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // get uri of image on successful capture
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
            Crop.of(mCurrentImageUri, mCurrentImageUri).asSquare().start(this);

        // add the cropped image to the grid view
        else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK)
            mAdapter.add(mCurrentPhotoPath);

    }

    private void setupRecyclerView() {
        storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists())
            storageDir.mkdirs();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ImageAdapter(this, storageDir);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File imageFile = null;

            try {
                imageFile = Utilities.createImageFile(storageDir);
                mCurrentPhotoPath = imageFile.getAbsolutePath();
                mCurrentImageUri = Uri.fromFile(imageFile);

            } catch (IOException ex) {
                Log.w("ExternalStorage", "Error creating file " + "in " + storageDir, ex);
            }

            // if file is created successfully, only then progress
            if (imageFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentImageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


}
