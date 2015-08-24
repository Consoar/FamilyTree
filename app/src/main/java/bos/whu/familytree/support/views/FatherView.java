package bos.whu.familytree.support.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import bos.whu.familytree.R;
/**
 * Created by Consoar on 2015/3/21.
 */
public class FatherView extends ZoomView {

    private Paint arrowPaint;

    private TreeNode rootNode = null;
    private TreeNode selectedNode = null;

    private List<TreeNode> allTreeNode;
    private List<TreeNode> fatherTreeNode;
    int fatherNum = 0;
    // margin between sides of view and nodes
    private static final int MARGIN = 10;

    // horizontal gap between nodes
    private static int H_GAP;

    // minimum vertical gap between nodes
    private static int MIN_V_GAP = 20;

    // how much room will the tree take unscaled?
    private int treeWidth = 0;
    private int treeHeight = 0;

    // used in calculating initial scale and translate, and detecting size
    // changes:
    private int calcWidth = 0;
    private int calcHeight = 0;

    private Bitmap arrowImg;
    
    private Context context;
    
    public FatherView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public FatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public FatherView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        H_GAP = TreeNode.WIDTH / 3;
        // how much unscaled room the tree takes:
        treeWidth = MARGIN * 2 + TreeNode.WIDTH *6;
        treeHeight = TreeNode.HEIGHT *6+MARGIN * 4;
        allTreeNode = new ArrayList<TreeNode>();
        
        arrowImg = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.arrow);
        arrowImg = Bitmap.createScaledBitmap(arrowImg, 56, (int) (arrowImg.getHeight() * (56.0 / arrowImg.getWidth())),
                true);

        fatherNum = 0;
        fatherTreeNode = new ArrayList<TreeNode>();
    }

    private void calculateInitialScaleAndTranslate(Canvas canvas) {
        CanvasCamera cc = getCanvasCamera();
        int oldCalcWidth = calcWidth;
        int oldCalcHeight = calcHeight;
        calcWidth = this.getWidth();
        calcHeight = this.getHeight();
        // if our dimensions changed, we need to recenter the tree in the view:
        if (oldCalcHeight != calcHeight || oldCalcWidth != calcWidth) {
            float startScale = ((float) calcWidth) / ((float) treeWidth);
            cc.setScale(startScale);
            float heightDiff = calcHeight - (treeHeight * startScale);
            cc.setTranslation(0, heightDiff / 2 / startScale);
        }
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public TreeNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(TreeNode tn) {
        rootNode = tn;
        TreeNode temp = rootNode;
        while (temp.getPerson().getFather()!=null) {
            TreeNode father = new TreeNode(context,temp.getPerson().getFather());
            temp = father;
            fatherTreeNode.add(father);
            ++fatherNum;
        }
    }

    @Override
    public void onClickEvent(MotionEvent event) {
        PointF transformedPoint = getAsAbsoluteCoordinate(event.getX(),
                event.getY());
        TreeNode clickedNode = null;
        for (int i = 0; i < allTreeNode.size(); ++i) {
            if (allTreeNode.get(i).isPointInBounds(transformedPoint))
                clickedNode = allTreeNode.get(i);
        }
        if (clickedNode != null) {
            if (selectedNode != null) {
                selectedNode.setSelected(false);
            }
            selectedNode = clickedNode;
            clickedNode.setSelected(true);
            invalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // this call needs to be made every time because the
        // height fluctuates as the view settles
        this.calculateInitialScaleAndTranslate(canvas);
        CanvasCamera cc = getCanvasCamera();
        float scaleFactor = cc.getScale();
        float translateX = cc.getTranslation().x;
        float translateY = cc.getTranslation().y;

        canvas.save();

        canvas.scale(scaleFactor, scaleFactor);
        canvas.translate(translateX, translateY);

        /* put the canvas drawing code here: */

        drawTree(canvas);

        /* end of drawing code */

        canvas.restore();

    }

    private void drawTree(Canvas canvas) {
        if (rootNode == null) {
            return;
        }
        int vCenter = (int) (treeHeight / 2);
        int hCenter = (int) (treeWidth / 2);
        int nodeWidth = TreeNode.WIDTH;
        int nodeHeight = TreeNode.HEIGHT;
        int startY = treeHeight - nodeHeight-nodeHeight/2-MARGIN;
        rootNode.draw(canvas, hCenter - nodeWidth / 2, startY);
        int arrowWidth = arrowImg.getWidth();
        int arrowHeight = arrowImg.getHeight();
        for(int i=0;i<fatherNum;i++) {
            startY = startY - arrowHeight;
            canvas.drawBitmap(arrowImg, hCenter - arrowWidth / 2, startY, arrowPaint);
            startY = startY - nodeHeight;
            fatherTreeNode.get(i).draw(canvas, hCenter - nodeWidth / 2, startY);
            
        }
    }
}
