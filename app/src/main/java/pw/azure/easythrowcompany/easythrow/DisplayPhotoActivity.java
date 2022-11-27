package pw.azure.easythrowcompany.easythrow;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

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

//                Call call = client.newCall(request).;
//                Response response = null;
//                try {
//                    response = call.execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                System.out.println(response);
//                if(response != null)
//                {
//                    System.out.println(response.code());
//                    System.out.println(response.body());
//                }

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
                            DisplayPhotoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO
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
