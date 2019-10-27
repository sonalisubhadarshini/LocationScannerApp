package com.project.locationscannerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ScannedBarcodeActivity extends AppCompatActivity {
    private SurfaceView surfaceviewId;

    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private BarcodeDetector barcodeDetector;
    private TextView textviewScanner;
    private String CurrentLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_barcode);

        surfaceviewId = findViewById(R.id.surfaceviewId);
        textviewScanner = findViewById(R.id.textviewScanner);

        Intent intent = getIntent();

        CurrentLocation = intent.getStringExtra("Current Location");


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            grantRuntimePermission();
//        }



//        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//            if (ActivityCompat.shouldShowRequestPermissionRationale(ScannedBarcodeActivity.this,
//                    Manifest.permission.CAMERA)) {
//                Toast.makeText(getApplicationContext(),"scanner",Toast.LENGTH_SHORT).show();
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
//                // No explanation needed; request the permission
//                ActivityCompat.requestPermissions(ScannedBarcodeActivity.this,
//                        new String[]{Manifest.permission.CAMERA},
//                        REQUEST_CAMERA_PERMISSION);
//            }

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this,barcodeDetector).setRequestedPreviewSize(640,480).setAutoFocusEnabled(true).build();

        if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            initializeBarcode();

        } else {
            ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                    String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);

            if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                initializeBarcode();

            }


        }





            }


            public void initializeBarcode(){


                surfaceviewId.getHolder().addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {


                        try {
                            cameraSource.start(holder);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }


                    @Override
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {
                        cameraSource.stop();

                    }
                });


                barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                    @Override
                    public void release() {

                    }

                    @Override
                    public void receiveDetections(Detector.Detections<Barcode> detections) {

                        final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                        if(qrCodes.size() != 0){

                            textviewScanner.post(new Runnable() {
                                @Override
                                public void run() {

//                                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
//                                    vibrator.vibrate(1000);

                                    if (qrCodes.valueAt(0).displayValue != null) {

                                      //  vibrator.cancel();
                                        textviewScanner.removeCallbacks(null);
                                        String text = qrCodes.valueAt(0).displayValue+"\n"+CurrentLocation;
                                        String url = "https://twitter.com/intent/tweet?text="+urlEncode(text)+"";

                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(url));

                                        startActivity(i);
                                        finish();

                                    }





                                }
                            });

                        }

                    }
                });

            }



    //    @Override
//    protected void onPause() {
//        super.onPause();
//        cameraSource.release();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        initialiseDetectorsAndSources();
//    }




//    @Override
//    protected void onResume() {
//        super.onResume();
//
//
//
//
//
//
//
//        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
//            @Override
//            public void release() {
//
//            }
//
//            @Override
//            public void receiveDetections(Detector.Detections<Barcode> detections) {
//
//                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
//
//                if(qrCodes.size() != 0){
//
//                    textviewScanner.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
//                            vibrator.vibrate(1000);
//
//                            String url = "https://twitter.com/intent/tweet?text="+qrCodes.valueAt(0).displayValue+"";
//                            Intent i = new Intent(Intent.ACTION_VIEW);
//                            i.setData(Uri.parse(url));
//                            startActivity(i);
////                            Toast.makeText(getApplicationContext(),qrCodes.valueAt(0).displayValue,Toast.LENGTH_SHORT).show();
////                            Intent intent = new Intent(ScannedBarcodeActivity.this,MainActivity.class);
////
////                            intent.putExtra("qrCodeValues",qrCodes.valueAt(0).displayValue);
////                            startActivity(intent);
//
//                        }
//                    });
//
//                }
//
//            }
//        });
//
//
//    }


    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    private void grantRuntimePermission() {
//
      ActivityCompat.requestPermissions(ScannedBarcodeActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA_PERMISSION);

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getApplicationContext(),"Permission Accepted", Toast.LENGTH_LONG);



                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
