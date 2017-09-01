package armored.g12matrickapp.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Falcon on 7/18/2017 :: 20:45 inside G12MatrickApp .
 * ALL RIGHTS RECEIVED!
 */

public class Functions {

    public static Bitmap getHexagonShape(Bitmap scaleBitmapImage) {
        // TODO Auto-generated method stub
        int targetWidth = 200 ;
        int targetHeight = 200 ;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap. Config.ARGB_8888);
        Canvas canvas = new Canvas (targetBitmap);
        Path path = new Path();
        float stdW = 200;
        float stdH = 200 ;
        float w3 =stdW / 2 ;
        float h2 = stdH / 2 ;
        float radius=stdH/ 2 -10;
        float triangleHeight = ( float) ( Math.sqrt( 3 ) * radius / 2 );
        float centerX = stdW/ 2 ;
        float centerY = stdH/ 2 ;
        path.moveTo(centerX, centerY + radius);
        path.lineTo(centerX - triangleHeight, centerY + radius/ 2 );
        path.lineTo(centerX - triangleHeight, centerY - radius/ 2 );
        path.lineTo(centerX, centerY - radius);
        path.lineTo(centerX + triangleHeight, centerY - radius/ 2 );
        path.lineTo(centerX + triangleHeight, centerY + radius/ 2);
        path.moveTo(centerX, centerY + radius);
        canvas.clipPath(path);
        canvas.drawBitmap(scaleBitmapImage,
                new Rect( 0 , 0, scaleBitmapImage.getWidth(),
                        scaleBitmapImage.getHeight()),
                new Rect( 0 , 0, targetWidth,
                        targetHeight), null);
        return targetBitmap;
    }
    public static Bitmap drawableToBitmap ( Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap
                (drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                        Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas (bitmap);
        drawable.setBounds( 0, 0 , canvas.getWidth(), canvas.getHeight()
        );
        drawable.draw(canvas);
        return bitmap;
    }

}
