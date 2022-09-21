package com.example.qrcode3;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button scan;
    TextView scannedText;
    ViewPager2 viewPager2;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference reference = firebaseDatabase.getReference("Views");
    ArrayList<String> list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        scan = findViewById(R.id.scan);
        viewPager2 = findViewById(R.id.viewPager);
        scan.setOnClickListener(this);


       // startActivity(new Intent(MainActivity.this,PDFviewerActivity.class));


        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET

                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot data : snapshot.getChildren())
                {
                    Model_ImagesID model = data.getValue(Model_ImagesID.class);
                    assert model != null;
                    list.add(model.imageLink);

                }

                AdapterForImages adapter = new AdapterForImages(list,getApplicationContext());
                viewPager2.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    // Register the launcher and result handler
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(MainActivity.this, "Failed to Scan", Toast.LENGTH_LONG).show();
                } else {
                    //use library...here

                    String urlofpdf = result.getContents();
                    Intent intent = new Intent(MainActivity.this, PDFviewerActivity.class);
                    intent.putExtra("url", urlofpdf);
                    startActivity(intent);


                   /* try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlofpdf));
                        intent.setType("application/pdf");
                        startActivity(intent);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(MainActivity.this, "No application found to handle this request...", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }*/


                    //Toast.makeText(MainActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                }
            });



    @Override
    public void onClick(View view) {
        barcodeLauncher.launch(new ScanOptions());
    }




}