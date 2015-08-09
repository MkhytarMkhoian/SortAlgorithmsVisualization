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
 * Created by Mkhitar on 21.03.2015.
 */
public class MergeSort extends AbstractSort {

    private int[] helper;

    public MergeSort(CyclicBarrier barrier, Map<Integer, VisualElement> rectangleMap, double screenHeight) {
        super(barrier, rectangleMap, screenHeight);
    }

    private void mergesort(int low, int high) {
        // check if low is smaller then high, if not then the array is sorted
        if (low < high) {
            // Get the index of the element which is in the middle
            int middle = low + (high - low) / 2;
            // Sort the left side of the array
            mergesort(low, middle);
            // Sort the right side of the array
            mergesort(middle + 1, high);
            // Combine them both
            merge(low, middle, high);
        }
    }

    private void merge(int low, int middle, int high) {

        // Copy both parts into the helper array
        for (int i = low; i <= high; i++) {
            helper[i] = mMainArray[i];
        }

        int i = low;
        int j = middle + 1;
        int k = low;
        // Copy the smallest values from either the left or the right side back
        // to the original array
        while (i <= middle && j <= high) {
            if (helper[i] <= helper[j]) {

                mMainArray[k] = helper[i];
                refreshUI(k, null);

                i++;
            } else {

                mMainArray[k] = helper[j];
                refreshUI(k, null);

                j++;
            }
            k++;
        }
        // Copy the rest of the left side of the array into the target array
        while (i <= middle) {

            mMainArray[k] = helper[i];
            refreshUI(k, null);

            k++;
            i++;
        }

        clearColor(low, high);
    }

    @Override
    public void sort(int[] values, UICallback callback) {
        mCallback = callback;
        mMainArray = values;

        int number = values.length;
        this.helper = new int[number];
        mergesort(0, number - 1);

        for (int i = 0; i < mMainArray.length; i++) {
            VisualElement element = mRectangles.get(i);
            element.getRectangle().setFill(Color.BLACK);
        }
    }

    private void clearColor(int start, int finish) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                for (int i = start; i <= finish; i++) {
                    final Rectangle r = mRectangles.get(i).getRectangle();
                    r.setFill(Color.GRAY);
                }
            }
        });
    }

    @Override
    public void redrawElement(int position, int[] params) {
        if (isStopped) return;

        double elementHeight = ((mScreenHeight / mMainArray.length) * mMainArray[position]) + 10;

        final Polygon polygon = mRectangles.get(position).getPolygon();
        mRectangles.get(position).refreshCoordinates();
        polygon.setVisible(true);

        final Rectangle r = mRectangles.get(position).getRectangle();
        r.setLayoutY(500 - elementHeight);
        r.setHeight(elementHeight);
        r.setFill(Color.BLACK);
    }
}
