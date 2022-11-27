package pw.azure.easythrowcompany.easythrow;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DisplayPhotoActivity extends AppCompatActivity {

    private final String url = "https://convert-to-labeled.azurewebsites.net/api/HttpTrigger1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.captured_image);

        ImageView photoIv = findViewById(R.id.capturedImageView);
        Bundle extras = getIntent().getExtras();
        Uri imageUri = Uri.parse(extras.getString("imageUri"));
        photoIv.setImageURI(imageUri);

        File file = new File(imageUri.getPath());

        ProgressBar progressBar = findViewById(R.id.uploadPb);

        String json = "{\"image_uri\": \"abcccccccccc\"}";
        Button uploadBtn = findViewById(R.id.confirmBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(
                        MediaType.parse("application/json"),
                        json
                );
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String myResponse = response.body().string();
                            System.out.println(myResponse);
                            System.out.println(imageUri);
                            DisplayPhotoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(DisplayPhotoActivity.this, myResponse, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });

        Button retakeBtn = findViewById(R.id.retakeBtn);
        retakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
