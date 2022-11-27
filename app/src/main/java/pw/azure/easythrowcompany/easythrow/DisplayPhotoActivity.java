package pw.azure.easythrowcompany.easythrow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.nio.ByteBuffer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DisplayPhotoActivity extends AppCompatActivity {

    private final String url = "http://20.67.122.191/api/v1/service/testend/score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.captured_image);

        ImageView photoIv = findViewById(R.id.capturedImageView);
        Bundle extras = getIntent().getExtras();
        Uri imageUri = Uri.parse(extras.getString("imageUri"));
        //photoIv.setImageURI(imageUri);

        ProgressBar progressBar = findViewById(R.id.uploadPb);

        String json = "{\"image_uri\": \"abcccccccccc\"}";



        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        bitmap = RotateBitmap(bitmap, 90);
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.RGB_565);


        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(Color.RED);
        canvas.drawBitmap(bitmap,0,0, null);
        canvas.drawRect(0,0,100,100, paint);
        photoIv.setImageBitmap(bmp);


        int size = bitmap.getRowBytes() * bitmap.getHeight();
        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        bitmap.copyPixelsToBuffer(byteBuffer);
        byte[] byteArray = byteBuffer.array();

        Button uploadBtn = findViewById(R.id.confirmBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = MultipartBody.create(MediaType.parse("application/octet-stream"),byteArray);
                RequestBody testbody = RequestBody.create(
                        MediaType.parse("application/text"), "");
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
                            System.out.println(imageUri);
                            DisplayPhotoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO
                                }
                            });
                        }
                        else{
                            System.out.println("NIE PRZESZLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
                            String myResponse = response.body().string();
                            System.out.println(myResponse);
                            System.out.println(byteArray.toString());
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

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
