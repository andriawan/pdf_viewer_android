package irwan.andriawan.pdfviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pdf = findViewById(R.id.button);
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter Name");
        final EditText input = new EditText(MainActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Generate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendDataToServer();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void sendDataToServer() {
        AndroidNetworking.
                get("http://www.africau.edu/images/default/sample.pdf")
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {

                Log.d("dds", "Adad");

                try {
                    File file = new File(getApplication().getFilesDir(), "test.pdf");
                    byte[] bytes = response.getBytes();
                    file.createNewFile();
                    Log.d("dds",String.valueOf(file.exists()));
                    if(file.exists()){
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                        bos.write(response.getBytes());
                        bos.flush();
                        bos.close();
                        Log.d("dds",file.getAbsolutePath());
                        Log.d("dds",String.valueOf(file.length()));
                        Intent intent = new Intent(MainActivity.this, PdfViewer.class);
                        intent.putExtra("name", file.getAbsolutePath());
                        startActivity(intent);

                        Toast.makeText(MainActivity.this, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {

                }

            }

            @Override
            public void onError(ANError anError) {

            }
        });

    }
}
