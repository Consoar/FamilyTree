package bos.whu.familytree.support.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import bos.whu.familytree.model.PersonBean;
import bos.whu.familytree.ui.fragments.HomeFragment;

/**
 * Created by Consoar on 2015/3/21.
 */

public class TreeView extends ZoomView {

    private Context context;
    private Paint bracketLinePaint;
    private Path bracketLinePath = new Path();

    private TreeNode rootNode = null;
    private TreeNode wife = null;
    private TreeNode father = null;
    private TreeNode mother = null;
    private TreeNode mothersFather = null;
    private TreeNode mothersMother = null;
    private TreeNode fathersFather = null;
    private TreeNode fathersMother = null;
    private List<TreeNode> childs = null;
    private TreeNode selectedNode = null;

    private List<TreeNode> allTreeNode;
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
    private List<PersonBean> mChildsList;
    public TreeView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public TreeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        // paint object for lines
        bracketLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bracketLinePaint.setColor(Color.BLACK);
        bracketLinePaint.setStrokeWidth(2f);
        bracketLinePaint.setStyle(Style.STROKE);

        H_GAP = TreeNode.WIDTH / 3;
        // how much unscaled room the tree takes:
        treeWidth = MARGIN * 2 + TreeNode.WIDTH * 5 + H_GAP * 3;
        treeHeight = TreeNode.HEIGHT * 4 + MIN_V_GAP * 7 + MARGIN * 4;
        allTreeNode = new ArrayList<TreeNode>();
        childs = new ArrayList<TreeNode>();
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
        if (rootNode.getPerson().getFather() != null) {
            father = new TreeNode(context, rootNode.getPerson().getFather());
        }
        if (rootNode.getPerson().getWife() != null) {
            wife = new TreeNode(context, rootNode.getPerson().getWife());
        }
        if (rootNode.getPerson().getMother() != null) {
            mother = new TreeNode(context, rootNode.getPerson().getMother());
        }
        if (mother != null && mother.getPerson().getFather() != null) {
            mothersFather = new TreeNode(context, mother.getPerson().getFather());
        }
        if (mother != null && mother.getPerson().getMother() != null) {
            mothersMother = new TreeNode(context, mother.getPerson().getMother());
        }
        if (father != null && father.getPerson().getFather() != null) {
            fathersFather = new TreeNode(context, father.getPerson().getFather());
        }
        if (father != null && father.getPerson().getMother() != null) {
            fathersMother = new TreeNode(context, father.getPerson().getMother());
        }
         mChildsList = HomeFragment.mChildsMap.get(String.valueOf(rootNode.getPerson().getPid()));
        if (mChildsList != null) {
            int childNum = mChildsList.size();
            for (int i = 0; i < childNum; ++i) {
                TreeNode child = new TreeNode(context, mChildsList.get(i));
                childs.add(child);
            }

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
        int startY = treeHeight - (nodeHeight * 2 + MIN_V_GAP * 3);
        rootNode.draw(canvas, hCenter - nodeWidth / 2, startY);
        allTreeNode.add(rootNode);

        if (father != null || mother != null) {
            drawBracketLines(canvas, hCenter - nodeWidth - H_GAP, startY
                    - MIN_V_GAP * 2, nodeWidth * 2 + H_GAP * 2, MIN_V_GAP * 2,true);
        }
        if (father != null) {
            father.draw(canvas, hCenter - nodeWidth - H_GAP
                    - (int) (nodeWidth * 0.5), startY - MIN_V_GAP * 2
                    - nodeHeight);
            allTreeNode.add(father);

            if (fathersFather != null || fathersMother != null) {
                drawBracketLines(canvas, hCenter - nodeWidth - H_GAP
                        - (nodeWidth + H_GAP) / 2, startY - MIN_V_GAP * 4
                        - nodeHeight, nodeWidth + H_GAP, MIN_V_GAP * 2,true);
                if (fathersFather != null) {
                    fathersFather.draw(canvas, hCenter - (int) (H_GAP * 1.5)
                            - (nodeWidth * 2), startY - MIN_V_GAP * 4
                            - nodeHeight * 2);
                    allTreeNode.add(fathersFather);
                }
                if (fathersMother != null) {
                    fathersMother.draw(canvas, hCenter - nodeWidth
                            - (int) (H_GAP * 0.5), startY - MIN_V_GAP * 4
                            - nodeHeight * 2);
                    allTreeNode.add(fathersMother);
                }
            }
        }
        if (mother != null) {
            mother.draw(canvas, hCenter + H_GAP + (int) (nodeWidth * 0.5),
                    startY - MIN_V_GAP * 2 - nodeHeight);
            allTreeNode.add(mother);

            if (mothersFather != null || mothersMother != null) {
                drawBracketLines(canvas, hCenter + (int) (H_GAP * 0.5)
                        + (int) (nodeWidth * 0.5), startY - MIN_V_GAP * 4
                        - nodeHeight, nodeWidth + H_GAP, MIN_V_GAP * 2, true);
                if (mothersFather != null) {
                    mothersFather.draw(canvas, hCenter + (int) (H_GAP * 0.5),
                            startY - MIN_V_GAP * 4 - nodeHeight * 2);
                    allTreeNode.add(mothersFather);
                }
                if (mothersMother != null) {
                    mothersMother.draw(canvas, hCenter + (int) (H_GAP * 1.5)
                            + nodeWidth, startY - MIN_V_GAP * 4 - nodeHeight * 2);
                    allTreeNode.add(mothersMother);
                }
            }
        }
        if (wife != null) {
            if (mChildsList != null) {
                drawBracketLines(canvas, hCenter, startY + nodeHeight, nodeWidth
                        + H_GAP, MIN_V_GAP * 2, true);
            } else {
                drawBracketLines(canvas, hCenter, startY + nodeHeight, nodeWidth
                        + H_GAP, MIN_V_GAP * 2, false);
            }
            wife.draw(canvas, hCenter + H_GAP + (int) (nodeWidth * 0.5), startY);
            allTreeNode.add(wife);
            if (mChildsList != null) {
                drawChilds(canvas);
            }
        }
    }

    /**
     * draw the lines that connect a node to its parent nodes
     *
     * @param canvas - the canvas to draw on
     * @param x      - the upper left corner x-coord of the box bounding the lines
     * @param y      - the upper left corner y-coord of the box bounding the lines
     * @param width  - the width of the box bounding the lines
     * @param height
     */

    private void drawBracketLines(Canvas canvas, float x, float y, float width,
                                  float height ,boolean down) {
        float middleY = y + (height / 2);
        float middleX = x + (width / 2);
        float rightX = x + width;
        float bottomY = y + height;
        if (down)
            canvas.drawLine(middleX, bottomY, middleX, middleY, bracketLinePaint);
        bracketLinePath.reset();
        bracketLinePath.moveTo(x, y);
        bracketLinePath.lineTo(x, middleY);
        bracketLinePath.lineTo(rightX, middleY);
        bracketLinePath.lineTo(rightX, y);
        canvas.drawPath(bracketLinePath, bracketLinePaint);
    }

    /**
     * draw the lines that connect a node to its parent nodes
     *
     * @param canvas - the canvas to draw on
     * @param x      - the down right corner x-coord of the box bounding the lines
     * @param y      - the down right corner y-coord of the box bounding the lines
     * @param width  - the width of the box bounding the lines
     * @param height - the height of the box bounding the lines
     */
    private void drawChildBracketLines(Canvas canvas, float x, float y,
                                       float width, float height) {
        int childNum =mChildsList.size();
        // 平移N-3个间隔，因为3个的时候正好中间可以放个子孙
        x = x - (int) ((childNum - 3) * (H_GAP + TreeNode.WIDTH) / 2.0);
        width = width + (int) ((childNum - 3) * (H_GAP + TreeNode.WIDTH));

        float middleY = y - (height / 2);
        float middleX = x + (width / 2);
        float rightX = x + width;
        float topY = y - height;
        bracketLinePath.reset();
        bracketLinePath.moveTo(x, y);
        bracketLinePath.lineTo(x, topY);
        bracketLinePath.lineTo(rightX, topY);
        bracketLinePath.lineTo(rightX, y);
        canvas.drawPath(bracketLinePath, bracketLinePaint);

        // // 在中间画上超过两个的线
        for (int i = 0; i < childNum - 2; ++i) {
            canvas.drawLine(x + (i + 1) * (H_GAP + TreeNode.WIDTH), y, x
                            + (i + 1) * (H_GAP + TreeNode.WIDTH), topY,
                    bracketLinePaint);
        }
    }

//    private void drawSiblings(Canvas canvas) {
//        int siblingNum = 0;
//        int nodeHeight = TreeNode.HEIGHT;
//        int vCenter = (int) (treeHeight / 2);
//        int nodeWidth = TreeNode.WIDTH;
//
//        siblingNum = rootNode.getSiblings().size();
//        if (siblingNum > 0) {
//            List<TreeNode> list = new ArrayList<TreeNode>();
//            list = rootNode.getSiblings();
//            int startY = vCenter;
//            int startX = MARGIN + nodeWidth / 2;
//            int height = 120;
//            for (int i = 0; i < list.size(); ++i) {
//                canvas.drawLine(startX, startY - nodeHeight / 2 - height * i
//                        - nodeHeight * i, startX, startY - nodeHeight / 2
//                        - height * (i + 1) - nodeHeight * i, bracketLinePaint);
//                list.get(i).draw(
//                        canvas,
//                        MARGIN,
//                        startY - nodeHeight / 2 - height * (i + 2) - nodeHeight
//                                * i);
//                allTreeNode.add(list.get(i));
//            }
//
//        }
//    }

    private void drawChilds(Canvas canvas) {
        int vCenter = (int) (treeHeight / 2);
        int hCenter = (int) (treeWidth / 2);
        int nodeWidth = TreeNode.WIDTH;
        int nodeHeight = TreeNode.HEIGHT;
        int startY = treeHeight - (nodeHeight * 2 + MIN_V_GAP * 3);

        drawChildBracketLines(canvas, hCenter - (H_GAP + nodeWidth) / 2, startY
                        + nodeHeight + MIN_V_GAP * 3, (nodeWidth + H_GAP) * 2,
                MIN_V_GAP);
        int childNum =mChildsList.size();
        int x = hCenter - (H_GAP + nodeWidth) / 2
                - (int) ((childNum - 3) * (H_GAP + TreeNode.WIDTH) / 2.0);
        int y = startY + nodeHeight + MIN_V_GAP * 3;
        int width = (nodeWidth + H_GAP) * 2
                + (int) ((childNum - 3) * (H_GAP + TreeNode.WIDTH));
        for (int i = 0; i < childNum; ++i) {
            childs.get(i).draw(canvas,
                    x - (int) (0.5 * nodeWidth) + (H_GAP + nodeWidth)
                            * i, y);

            allTreeNode.add(childs.get(i));
        }
    }

}
