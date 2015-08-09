package main.algorithms;

import javafx.application.Platform;
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
public class BubbleSort extends AbstractSort {

    public BubbleSort(CyclicBarrier barrier, Map<Integer, VisualElement> rectangleMap, double screenHeight) {
        super(barrier, rectangleMap, screenHeight);
    }

    @Override
    public void sort(int[] array, UICallback callback) {
        mCallback = callback;
        mMainArray = array;

        int temp;
        int t = -1;
        int left = 0;
        int right = mMainArray.length - 1;

        for (int i = left; i < right; i++) {
            if (isStopped) return;
            for (int j = right; j > i; j--) {
                if (isStopped) return;

                if (mMainArray[j] < mMainArray[j - 1]) {
               //     swap(j, j - 1);
                    temp = mMainArray[j];
                    mMainArray[j] = mMainArray[j - 1];
                    mMainArray[j - 1] = temp;

                    refreshWithoutPause(j);
                    refreshUI(j - 1, null);
                    t = j -1;
                }
            }
            if (t != -1) {
                refreshUI(t, new int[]{});
            }
        }
        for (VisualElement element : mRectangles.values()) {
            element.getRectangle().setFill(Color.BLACK);
        }
    }

    public void swap(int left, int right) {
        int temp = mMainArray[left];
        mMainArray[left] = mMainArray[right];
        mMainArray[right] = temp;

        refreshWithoutPause(left);
        refreshUI(right, null);
    }

    private void refreshWithoutPause(int position) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                redrawElement(position, null);
            }
        });
    }

    @Override
    public void redrawElement(int position, int[] params) {
        if (isStopped) return;

        double elementHeight = ((mScreenHeight / mMainArray.length) * mMainArray[position]) + 10;

        final Rectangle r = mRectangles.get(position).getRectangle();
        r.setLayoutY(500 - elementHeight);
        r.setHeight(elementHeight);

        if (params == null) {
            final Polygon polygon = mRectangles.get(position).getPolygon();
            mRectangles.get(position).refreshCoordinates();
            polygon.setVisible(true);
        } else {
            r.setFill(Color.BLACK);

            for (int i = position - 1; i >= 0; i--) {
                VisualElement element = mRectangles.get(i);
                element.getRectangle().setFill(Color.BLACK);
            }
        }
    }
}
