package main.algorithms;

import main.SettingSceneController;
import main.UICallback;
import main.VisualElement;

import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.prefs.Preferences;

/**
 * Created by Mkhitar on 26.03.2015.
 */
public abstract class AbstractSort {

    public static final int MOVE_ARROW = 1;
    public static final int FINAL_MOVE = 2;
    public static final int MOVE = 3;
    public static final int SET_COLOR = 4;

    protected CyclicBarrier mBarrier;
    protected UICallback mCallback;
    protected Map<Integer, VisualElement> mRectangles;
    protected Preferences mPreferences;

    protected int[] mMainArray;
    protected double mScreenHeight;

    protected boolean isStopped = false;

    public AbstractSort(CyclicBarrier barrier, Map<Integer, VisualElement> rectangleMap, double screenHeight) {
        mBarrier = barrier;
        mRectangles = rectangleMap;
        mPreferences = Preferences.userRoot().node(SettingSceneController.class.getName());
        mScreenHeight = screenHeight;
    }

    public abstract void sort(int[] array, UICallback callback);

    public abstract void redrawElement(final int position, int[] params);

    public void refreshUI(int position, int[] params) {
        mCallback.refresh(position, params);
        try {
            mBarrier.await();
        } catch (InterruptedException | BrokenBarrierException ignored) {

        }
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void stop() {
        this.isStopped = true;
    }
}
