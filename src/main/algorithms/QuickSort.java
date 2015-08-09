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
public class QuickSort extends AbstractSort {

    public QuickSort(CyclicBarrier barrier, Map<Integer, VisualElement> rectangleMap, double screenHeight) {
        super(barrier, rectangleMap, screenHeight);
    }

    public void quickSort(int start, int end) {
        int i = start;                          // index of left-to-right scan
        int k = end;                            // index of right-to-left scan

        if (end - start >= 1) {                 // check that there are at least two elements to sort
            int pivot = mMainArray[start];       // set the pivot as the first element in the partition

            while (k > i) {                 // while the scan indices from left and right have not met,

                while (mMainArray[i] <= pivot && i <= end && k > i) {     // from the left, look for the first
                    i++;
                }
                // element greater than the pivot
                while (mMainArray[k] > pivot && k >= start && k >= i) {   // from the right, look for the first
                    k--;
                }                                                   // element not greater than the pivot
                if (k > i) {                                        // if the left seekindex is still smaller than
                    swap(i, k, false);                              // the right index, swap the corresponding elements
                } else {
                    refresh(i, true);
                }
            }
            swap(start, k, true);          // after the indices have crossed, swap the last element in
            // the left partition with the pivot
            quickSort(start, k - 1); // quicksort the left partition
            quickSort(k + 1, end);   // quicksort the right partition
        }
    }

    public void swap(int index1, int index2, boolean isFinalSwap) {
        int temp = mMainArray[index1];
        mMainArray[index1] = mMainArray[index2];
        mMainArray[index2] = temp;

        refresh(index1, isFinalSwap);
        refresh(index2, isFinalSwap);
    }

    @Override
    public void sort(int[] array, UICallback callback) {
        mCallback = callback;
        mMainArray = array;

        quickSort(0, mMainArray.length - 1);
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

    private void refresh(int position, boolean isFinalSwap) {
        if (isFinalSwap) {
            refreshUI(position, new int[]{FINAL_MOVE});
        } else {
            refreshUI(position, new int[]{MOVE});
        }
    }

//    private int[] numbers;
//    private int number;
//
//    public void sort(int[] values) {
//        // check for empty or null array
//        if (values ==null || values.length==0){
//            return;
//        }
//        this.numbers = values;
//        number = values.length;
//        quicksort(0, number - 1);
//    }
//
//    private void quicksort(int low, int high) {
//        int i = low, j = high;
//        // Get the pivot element from the middle of the list
//        int pivot = numbers[low + (high-low)/2];
//
//        // Divide into two lists
//        while (i <= j) {
//            // If the current value from the left list is smaller then the pivot
//            // element then get the next element from the left list
//            while (numbers[i] < pivot) {
//                i++;
//            }
//            // If the current value from the right list is larger then the pivot
//            // element then get the next element from the right list
//            while (numbers[j] > pivot) {
//                j--;
//            }
//
//            // If we have found a values in the left list which is larger then
//            // the pivot element and if we have found a value in the right list
//            // which is smaller then the pivot element then we exchange the
//            // values.
//            // As we are done we can increase i and j
//            if (i <= j) {
//                exchange(i, j);
//                i++;
//                j--;
//            }
//        }
//        // Recursion
//        if (low < j)
//            quicksort(low, j);
//        if (i < high)
//            quicksort(i, high);
//    }
//
//    private void exchange(int i, int j) {
//        int temp = numbers[i];
//        numbers[i] = numbers[j];
//        numbers[j] = temp;
//    }
}
