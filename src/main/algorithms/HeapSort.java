package main.algorithms;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import main.UICallback;
import main.VisualElement;

import java.util.Map;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Mkhitar on 26.03.2015.
 */
public class HeapSort extends AbstractSort {

    private int total;

    public HeapSort(CyclicBarrier barrier, Map<Integer, VisualElement> rectangleMap, double screenHeight) {
        super(barrier, rectangleMap, screenHeight);
    }

    private void refresh(int position, boolean isFinalSwap) {
        if (isFinalSwap) {
            refreshUI(position, new int[]{FINAL_MOVE});
        } else {
            refreshUI(position, new int[]{MOVE});
        }
    }

    private void swap(int a, int b, boolean isFinalSwap) {
        int tmp = mMainArray[a];
        mMainArray[a] = mMainArray[b];
        mMainArray[b] = tmp;
        if (b == 1) {
            refresh(a, isFinalSwap);
        } else {
            refresh(a, false);
        }
        refresh(b, isFinalSwap);
    }

    private void heapify(int i) {
        int lft = i * 2;
        int rgt = lft + 1;
        int grt = i;

        if (lft <= total && mMainArray[lft] > mMainArray[grt]) {
            grt = lft;
        }
        if (rgt <= total && mMainArray[rgt] > mMainArray[grt]) {
            grt = rgt;
        }
        if (grt != i) {
            swap(i, grt, false);
            heapify(grt);
        }
    }

    @Override
    public void sort(int[] array, UICallback callback) {
        mCallback = callback;
        mMainArray = array;

        total = mMainArray.length - 1;

        for (int i = total / 2; i >= 0; i--)
            heapify(i);

        for (int i = total; i > 0; i--) {
            swap(0, i, true);
            total--;
            heapify(0);
        }
    }

    @Override
    public void redrawElement(int position, int[] params) {
        if (isStopped) return;

        double elementHeight = ((mScreenHeight / mMainArray.length) * mMainArray[position]) + 10;
        final Rectangle r = mRectangles.get(position).getRectangle();
        r.setLayoutY(500 - elementHeight);
        r.setHeight(elementHeight);

        if (params[0] == FINAL_MOVE) {
            r.setFill(Color.BLACK);
        }

        final Polygon polygon = mRectangles.get(position).getPolygon();
        mRectangles.get(position).refreshCoordinates();
        polygon.setVisible(true);
    }
}
