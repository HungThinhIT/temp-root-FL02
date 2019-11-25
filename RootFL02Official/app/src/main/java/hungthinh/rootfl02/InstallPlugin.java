package hungthinh.rootfl02;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class InstallPlugin extends AppCompatActivity {
    private Button btnCopyUrl, btnFinalStep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_plugin);


        String path = Environment.getExternalStorageDirectory()+"/apk_111965_src";
        File dirInit = new File(path);
        if (!dirInit.exists()) {
            dirInit.mkdirs();
        }
        installApplications();

        init();
    }
    private void init(){
        btnCopyUrl = findViewById(R.id.btnCopyURL);
        btnCopyUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Label", "https://raw.githubusercontent.com/topjohnwu/magisk_files/841e978604f989d04549013cd4dcc7e34aea5288/stable.json");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(InstallPlugin.this, "Đã copy", Toast.LENGTH_SHORT).show();
            }
        });

        btnFinalStep = findViewById(R.id.btnFinalStep);
        btnFinalStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstallPlugin.this, FinalStep.class);
                startActivity(intent);
            }
        });
    }
    private void installApplications(){
        String path = Environment.getExternalStorageDirectory()+"/apk_111965_src";


        AssetManager assetManager = getAssets();

        InputStream mmv711, initd_script = null;
        OutputStream out = null;

        try {
            mmv711 = assetManager.open("mm.apk");
//            initd_script = assetManager.open("init-script.apk");
            out = new FileOutputStream(path+"/mm.apk");

            byte[] buffer = new byte[1024];
            int read;
            while((read = mmv711.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            mmv711.close();
            mmv711 = null;

            out.flush();
            out.close();
            out = null;

        } catch(Exception e) {
            Log.e("er_r",e.toString());

        }
        try {
            initd_script = assetManager.open("initd.apk");
            out = new FileOutputStream(path+"/initd.apk");

            byte[] buffer = new byte[1024];
            int read;
            while((read = initd_script.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            initd_script.close();
            initd_script = null;

            out.flush();
            out.close();
            out = null;

        } catch(Exception e) {
            Log.e("er_r",e.toString());

        }
    }
}
