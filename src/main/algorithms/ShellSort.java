package main.algorithms;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import main.SettingSceneController;
import main.UICallback;
import main.VisualElement;

import java.util.Map;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Mkhitar on 26.03.2015.
 */
public class ShellSort extends AbstractSort {

    public ShellSort(CyclicBarrier barrier, Map<Integer, VisualElement> rectangleMap, double screenHeight) {
        super(barrier, rectangleMap, screenHeight);
    }


    @Override
    public void sort(int[] array, UICallback callback) {
        mCallback = callback;
        mMainArray = array;

        int increment = mMainArray.length / 3;
        while (increment > 0) {
            for (int i = increment; i < mMainArray.length; i++) {
                int j = i;
                int temp = mMainArray[i];
                while (j >= increment && mMainArray[j - increment] > temp) {
                    mMainArray[j] = mMainArray[j - increment];
                    refresh(j, increment);
                    j = j - increment;
                }
                mMainArray[j] = temp;
                refresh(j, increment);
            }
            if (increment == 2) {
                increment = 1;
            } else {
                increment *= (5.0 / 11);
            }
        }
    }

    private void refresh(int position, int increment) {
        if (increment == 1) {
            refreshUI(position, new int[]{FINAL_MOVE});
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    redrawElement(0, new int[]{FINAL_MOVE});
                }
            });

        } else {
            refreshUI(position, new int[]{MOVE});
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

        if (params[0] != FINAL_MOVE) {
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(mPreferences.getInt(SettingSceneController.TIME_BOX_ID, 50)), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    r.setFill(Color.GRAY);
                }
            }));
            timeline.play();
        }
    }
}
