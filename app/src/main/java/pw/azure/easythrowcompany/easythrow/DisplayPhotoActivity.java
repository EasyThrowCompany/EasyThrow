package pw.azure.easythrowcompany.easythrow;

import android.annotation.SuppressLint;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DisplayPhotoActivity extends AppCompatActivity {

    private final String url = "http://20.67.122.191/api/v1/service/testend/score";

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.captured_image);

        ImageView photoIv = findViewById(R.id.capturedImageView);
        Bundle extras = getIntent().getExtras();
        Uri imageUri = Uri.parse(extras.getString("imageUri"));
        photoIv.setImageURI(imageUri);

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


//        Canvas canvas = new Canvas(bmp);
//        Paint paint = new Paint();
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(10);
//        paint.setColor(Color.RED);
//        canvas.drawBitmap(bitmap,0,0, null);
//        canvas.drawRect(0,0,100,100, paint);
//        photoIv.setImageBitmap(bmp);


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Button uploadBtn = findViewById(R.id.confirmBtn);
        Bitmap finalBitmap = bitmap;
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();

                RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);


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
                            try {
                                DrawRectanglesFromJSONResponse(myResponse, finalBitmap, photoIv);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            DisplayPhotoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO
                                }
                            });
                        }
                        else{
                            System.out.println("NIE PRZESZLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
                            String myResponse2 = response.request().body().toString();
                            String myResponse = response.body().string();
                            System.out.println(myResponse);
                            System.out.println(myResponse2);
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

    public static void DrawRectanglesFromJSONResponse(String response, Bitmap bitmap, ImageView imageView) throws IOException, ParseException, JSONException {

        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();

        canvas.drawBitmap(bitmap,0,0, null);

        JSONObject jo = new JSONObject(response);

        String filename = (String) jo.get("filename");

        JSONArray boxes = jo.getJSONArray("boxes");

        // iterating phoneNumbers

        for (int i = 0; i < boxes.length(); i++) {
            JSONObject box = boxes.getJSONObject(i).getJSONObject("box");
            double topX = box.getDouble("topX");
            double topY = box.getDouble("topY");
            double bottomX = box.getDouble("bottomX");
            double bottomY = box.getDouble("bottomY");
            String label = boxes.getJSONObject(i).getString("label");
            double score = boxes.getJSONObject(i).getDouble("score");

            if(score >= 0.5){
                int topXPixel = (int) (topX * bitmap.getWidth());
                int topYPixel = (int) (topY * bitmap.getHeight());
                int bottomXPixel = (int) (bottomX * bitmap.getWidth());
                int bottomYPixel = (int) (bottomY * bitmap.getHeight());

                int color;

                if(label.equals("PlasticAndMetal")){
                    color = Color.YELLOW;
                } else if( label.equals("Mixed")){
                    color = Color.BLACK;
                } else if(label.equals("Glass")){
                    color = Color.GREEN;
                } else if(label.equals("Paper")){
                    color = Color.BLUE;
                } else if(label.equals("WasteCollectionPoint")){
                    color = Color.RED;
                } else {
                    color = Color.MAGENTA;
                }

                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(10);
                paint.setColor(color);

                canvas.drawRect(topXPixel,topYPixel,bottomXPixel,bottomYPixel, paint);


            }
        }

        imageView.setImageBitmap(bmp);

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
