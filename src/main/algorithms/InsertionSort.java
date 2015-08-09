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
public class InsertionSort extends AbstractSort {

    public InsertionSort(CyclicBarrier barrier, Map<Integer, VisualElement> rectangleMap, double screenHeight) {
        super(barrier, rectangleMap, screenHeight);
    }

    @Override
    public void sort(int[] array, UICallback callback) {
        mCallback = callback;
        mMainArray = array;

        for (int i = 1; i < mMainArray.length; i++) {
            int next = mMainArray[i];
            // find the insertion location while moving all larger element up
            int j = i;
            while (j > 0 && mMainArray[j - 1] > next) {
                mMainArray[j] = mMainArray[j - 1];
                refreshUI(j, new int[]{MOVE});
                j--;
            }
            // insert the element
            mMainArray[j] = next;
            refreshUI(j, new int[]{MOVE});
        }
    }

    @Override
    public void redrawElement(int position, int[] params) {
        if (isStopped) return;

        double elementHeight = ((mScreenHeight / mMainArray.length) * mMainArray[position]) + 10;
        final Rectangle r = mRectangles.get(position).getRectangle();
        r.setLayoutY(500 - elementHeight);
        r.setHeight(elementHeight);
        r.setFill(Color.BLACK);

        final Polygon polygon = mRectangles.get(position).getPolygon();
        mRectangles.get(position).refreshCoordinates();
        polygon.setVisible(true);
    }
}
