package fi.ptm.readandwriteexternalstorageexample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private static int WRITE_PERMISSION = 1;
    private static int READ_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        if (isWriteStoragePermissionGranted()) {
            write();
        } else {
            textView.setText("Write permission is not granted!");
        }
        if (isReadStoragePermissionGranted()) {
            read();
        } else {
            textView.setText("Read permission is not granted!");
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_PERMISSION);
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            write();
        } else if (requestCode == READ_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            read();
        }
    }

    public void write() {
        try {
            File root = Environment.getExternalStorageDirectory();
            File file = new File(root, "test.txt");
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fileWriter);
            out.write("Write text to External Storage.");
            out.close();
            textView.setText("Write text to External Storage.");
        } catch (IOException e) {
            textView.setText("Cannot write text to External Storage!");
        }catch (Exception e) {
            Toast.makeText(getBaseContext(), "Cannot write text to External Storage!", Toast.LENGTH_SHORT).show();
        }
    }

    public void read() {
        try {
            File root = Environment.getExternalStorageDirectory();
            File file = new File(root, "test.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader in = new BufferedReader(fileReader);
            String sLine = in.readLine();
            textView.setText(sLine);
        } catch (IOException e) {
            textView.setText("Cannot read text from External Storage!");
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Cannot read text from External Storage!", Toast.LENGTH_SHORT).show();
        }
    }
}
