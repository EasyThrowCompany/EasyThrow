package drawing;


import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class DrawingApply {
//    ImageView file = new File();


    public void drawRectangle(){
        Canvas canvas = new Canvas();
        String path = "test";

        canvas.drawBitmap(BitmapFactory.decodeFile(path), 0, 0,null);

    }
}
