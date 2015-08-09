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
public class SelectionSort extends AbstractSort {

    public SelectionSort(CyclicBarrier barrier, Map<Integer, VisualElement> rectangleMap, double screenHeight) {
        super(barrier, rectangleMap, screenHeight);
    }

    public void sort(int[] array, UICallback callback) {
        mCallback = callback;
        mMainArray = array;

        for (int i = 0; i < mMainArray.length - 1; i++) {
            int index = i;
            for (int j = i + 1; j < mMainArray.length; j++) {
                refreshUI(j, new int[]{MOVE_ARROW});

                if (mMainArray[j] < mMainArray[index]) {
                    index = j;
                }
            }

            int smallerNumber = mMainArray[index];
            mMainArray[index] = mMainArray[i];
            mMainArray[i] = smallerNumber;

            refreshUI(i, new int[]{FINAL_MOVE});
            refreshUI(index, new int[]{MOVE});
        }
        refreshUI(mMainArray.length - 1, new int[]{FINAL_MOVE});
    }

    @Override
    public void redrawElement(int position, int[] params) {
        if (isStopped) return;

        double elementHeight = ((mScreenHeight / mMainArray.length) * mMainArray[position]) + 10;
        final Rectangle r = mRectangles.get(position).getRectangle();

        if (params[0] == FINAL_MOVE){
            r.setFill(Color.BLACK);
        }
        if (params[0] == MOVE_ARROW){
            final Polygon polygon = mRectangles.get(position).getPolygon();
            mRectangles.get(position).refreshCoordinates();
            polygon.setVisible(true);
        }
        r.setLayoutY(500 - elementHeight);
        r.setHeight(elementHeight);
    }
}
