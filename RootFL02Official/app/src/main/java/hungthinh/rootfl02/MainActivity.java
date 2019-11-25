package hungthinh.rootfl02;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private Button btnHack;
    private TextView txtDeviceName, txtHintForButtonHack, txtIsDeviceSupported;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        init();
        checkPer();
        loadDeviceInformation();
        rootYourPhone();
    }



    /*
    * Init all components  from layout
    * */

    private void init() {
        txtDeviceName = findViewById(R.id.txt_device_name_label);
        txtIsDeviceSupported = findViewById(R.id.txt_device_is_support);
        txtHintForButtonHack = findViewById(R.id.hint_for_hack_button);
        btnHack = findViewById(R.id.btn_hack_mtk);
    }


    private void checkPer() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    private void loadDeviceInformation(){
        String deviceName = Build.MODEL;
        String ci= Build.BOARD;
        txtDeviceName.setText(txtDeviceName.getText().toString()+deviceName);
        if(deviceName.equals("FL02")){
            txtIsDeviceSupported.setText("Thiết bị này được hỗ trợ");
            txtIsDeviceSupported.setTextColor(Color.parseColor("#007F39"));
        }
        else{
            txtIsDeviceSupported.setText("Thiết bị này không được hỗ trợ");
            txtIsDeviceSupported.setTextColor(Color.parseColor("#ff0000"));
            btnHack.setVisibility(View.GONE);
            txtHintForButtonHack.setVisibility(View.GONE);
        }
    }

    /*
    * Method root your mtk phone
    * */
    private void rootYourPhone() {
        btnHack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPer();
//                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Đang khai thác lỗ hổng");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                Context context = getApplicationContext();
                String path = Environment.getExternalStorageDirectory()+"/init.d";
                File dirInit = new File(path);
                File dirInitBin = new File(path+"bin");
                if (!dirInit.exists()) {
                    dirInit.mkdirs();
                    if(!dirInitBin.exists()){
                        dirInitBin.mkdirs();
                    }
                }

                copyFile(context);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Khai thác thành công.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, InstallPlugin.class);
                        startActivity(intent);
                    }
                }, 3000);


            }
        });
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void copyFile(Context context) {
        AssetManager assetManager = context.getAssets();
        String pathInit = Environment.getExternalStorageDirectory()+"/init.d/";
        try {
            InputStream in = assetManager.open("suboot.sh");
            OutputStream out = new FileOutputStream(pathInit+"/suboot.sh");
            InputStream in2 = assetManager.open("magiskinit");
            OutputStream out2 = new FileOutputStream(pathInit+"/bin/magiskinit");
            InputStream in3 = assetManager.open("mtk-su");
            OutputStream out3 = new FileOutputStream(pathInit+"/bin/mtk-su");
            byte[] buffer = new byte[1024];
            int read = in.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
        } catch (Exception e) {
            e.getMessage();
            Log.e("err",e.toString());
        }
    }
}
