package bos.whu.familytree.support.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import bos.whu.familytree.MyApplication;
import bos.whu.familytree.R;
import bos.whu.familytree.model.PersonBean;

public class TreeNode {

    private boolean selected = false;
    private PersonBean person;
    private static final int WIDTH_DP = 56;
    private static final int HEIGHT_DP = 88;

    private static final int CORNER_RADIUS = 8;
    private static final int TEXT_LEFT = 130;
    private static final int PIC_MARGIN_DP = 8;
    private static final int PIC_SIZE_DP = 40;
    private static final int LINE1_VERT_OFFSET_DP = 66;
    private static final int LINE2_VERT_OFFSET_DP = 80;
    private static final int NAME_TEXT_SIZE = 16;
    private static final int OTHER_TEXT_SIZE = 12;

    public static int WIDTH;
    public static int HEIGHT;
    private static int PIC_MARGIN;
    private static int PIC_SIZE;
    private static int LINE1_VERT_OFFSET;
    private static int LINE2_VERT_OFFSET;

    private TreeNode father = null;
    private TreeNode mother = null;

    private static Paint rectPaint;
    private static Paint selectedRectPaint;
    private static Paint nameTextPaint;
    private static Paint otherTextPaint;

    private static Bitmap circleOverlay;
    private static Bitmap circleOverlaySelected;
    private static Bitmap femaleImg;
    private static Bitmap maleImg;
    private static Bitmap unknownImg;

    // rect is the bounding rectangle for this tree node "widget"
    private RectF rect;

    private Context context;

    static {
        WIDTH = MyApplication.dip2px(WIDTH_DP);
        HEIGHT = MyApplication.dip2px(HEIGHT_DP);
        PIC_MARGIN = MyApplication.dip2px(PIC_MARGIN_DP);
        PIC_SIZE = MyApplication.dip2px(PIC_SIZE_DP);
        LINE1_VERT_OFFSET = MyApplication.dip2px(LINE1_VERT_OFFSET_DP);
        LINE2_VERT_OFFSET = MyApplication.dip2px(LINE2_VERT_OFFSET_DP);

        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setStyle(Style.FILL);
        rectPaint.setColor(Color.parseColor("#343331"));

        selectedRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedRectPaint.setStyle(Style.FILL);
        selectedRectPaint.setColor(Color.LTGRAY);

        nameTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nameTextPaint.setColor(Color.WHITE);
        nameTextPaint.setStyle(Style.FILL);
        nameTextPaint.setTextSize(NAME_TEXT_SIZE);
        nameTextPaint.setTextAlign(Align.CENTER);

        otherTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        otherTextPaint.setColor(Color.WHITE);
        otherTextPaint.setStyle(Style.FILL);
        otherTextPaint.setTextSize(OTHER_TEXT_SIZE);
        otherTextPaint.setTextAlign(Align.CENTER);

        makeCircleOverlays();
    }

    private static void makeCircleOverlays() {
        // Fill the canvas with the background color then paint a transparent
        // circle in the middle
        Paint clearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        clearPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        Rect rect = new Rect(0, 0, PIC_SIZE, PIC_SIZE);

        Bitmap bitmap = Bitmap.createBitmap(PIC_SIZE, PIC_SIZE,
                Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(rect, rectPaint);
        canvas.drawCircle(PIC_SIZE / 2, PIC_SIZE / 2, PIC_SIZE / 2, clearPaint);
        circleOverlay = bitmap;

        bitmap = Bitmap.createBitmap(PIC_SIZE, PIC_SIZE, Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawRect(rect, selectedRectPaint);
        canvas.drawCircle(PIC_SIZE / 2, PIC_SIZE / 2, PIC_SIZE / 2, clearPaint);
        circleOverlaySelected = bitmap;
    }

    public TreeNode(Context context) {
        this.context = context;
        init();
    }

    public TreeNode(Context context,PersonBean person) {
        this.context = context;
        this.person = person;
        init();
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

//    public void setPhoto(Bitmap photo) {
//        this.photo = photo;
//        // images need to be PIC_SIZE x PIC_SIZE
//        if (photo != null
//                && (photo.getWidth() != PIC_SIZE || photo.getHeight() != PIC_SIZE)) {
//            this.photo = Bitmap.createScaledBitmap(photo, PIC_SIZE, PIC_SIZE,
//                    true);
//        }
//    }

    private void init() {
        rect = new RectF(0, 0, WIDTH, HEIGHT);

        femaleImg = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.female_sill);
        femaleImg = Bitmap.createScaledBitmap(femaleImg, PIC_SIZE, PIC_SIZE,
                true);
        maleImg = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.male_sill);
        maleImg = Bitmap.createScaledBitmap(maleImg, PIC_SIZE, PIC_SIZE, true);
        unknownImg = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.unknown_sill);
        unknownImg = Bitmap.createScaledBitmap(unknownImg, PIC_SIZE, PIC_SIZE,
                true);

    }

    public void draw(Canvas canvas, float x, float y) {
        rect.offsetTo(x, y); // rect now defines the bounds of this node, and
                             // where it is on the canvas
        if (selected) {
            canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS,
                    selectedRectPaint);
        } else {
            canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, rectPaint);
        }

        canvas.drawText(person.getFullname(), WIDTH / 2 + x, LINE1_VERT_OFFSET + y,
                nameTextPaint);
        canvas.drawText(person.getBirthday(), WIDTH / 2 + x, LINE2_VERT_OFFSET + y,
                otherTextPaint);
        Bitmap bmp = null;
        if (bmp == null) {
            if (person.getSex() == 1) {
                bmp = maleImg;
            } else if (person.getSex() == 2) {
                bmp = femaleImg;
            } else {
                bmp = unknownImg;
            }
        }
        canvas.drawBitmap(bmp, PIC_MARGIN + x, PIC_MARGIN + y, null);
        if (selected) {
            canvas.drawBitmap(circleOverlaySelected, PIC_MARGIN + x, PIC_MARGIN
                    + y, null);
        } else {
            canvas.drawBitmap(circleOverlay, PIC_MARGIN + x, PIC_MARGIN + y,
                    null);
        }
        drawBorder(canvas, x + PIC_MARGIN, y + PIC_MARGIN, PIC_SIZE, PIC_SIZE);
    }

    /**
     * Is point p in the bounds of this TreeNode?
     * 
     * @param p
     * @return
     */
    public boolean isPointInBounds(PointF p) {
        return rect.contains(p.x, p.y);
    }

    private void drawBorder(Canvas canvas, float x, float y, final int width,
            final int height) {
        int mBorderWidth = 2;
        if (mBorderWidth == 0) {
            return;
        }
        final Paint mBorderPaint = new Paint();
        mBorderPaint.setStyle(Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(Color.GRAY);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        canvas.drawCircle(x + (width >> 1), y + (height >> 1), (width >> 1)
                + mBorderWidth, mBorderPaint);
        canvas = null;
    }
    
    public Bitmap getRoundedCornerBitmap(Bitmap bitmap){
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        final int color =0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPX = bitmap.getWidth()/2;
        paint.setAntiAlias(true);
        canvas.drawARGB(0,0,0,0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return outBitmap;
    }

    public PersonBean getPerson() {
        return person;
    }

    public void setPerson(PersonBean person) {
        this.person = person;
    }
}
