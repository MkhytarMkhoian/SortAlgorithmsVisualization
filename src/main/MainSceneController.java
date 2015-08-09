package main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import main.algorithms.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.prefs.Preferences;

/**
 * Created by Mkhitar on 15.03.2015.
 */
public class MainSceneController implements Initializable, EventHandler<ActionEvent> {

    public static final String START_BTN_ID = "mStartBtn";
    public static final String STOP_BTN_ID = "mStopBtn";
    public static final String SETTING_BTN_ID = "mSettingBtn";
    public static final String CHOICE_ALGORITHMS_ID = "mChoiceAlgorithms";

    public static final String MERGE_SORT_ALGORITHM = "Merge sort";
    public static final String BUBBLE_SORT_ALGORITHM = "Bubble sort";
    public static final String HEAP_SORT_ALGORITHM = "Heap sort";
    public static final String INSERTION_SORT_ALGORITHM = "Insertion sort";
    public static final String QUICK_SORT_ALGORITHM = "Quick sort";
    public static final String SELECTION_SORT_ALGORITHM = "Selection sort";
    public static final String SHELL_SORT_ALGORITHM = "Shell sort";


    @FXML
    private Button mStartBtn;
    @FXML
    private Button mStopBtn;
    @FXML
    private Button mSettingBtn;
    @FXML
    private AnchorPane mDrawView;
    @FXML
    private ChoiceBox mChoiceAlgorithms;

    private ExecutorService mThreadPoolExecutor = Executors.newSingleThreadExecutor();
    private Future<?> mFuture;
    private Preferences mPreferences;

    private Map<Integer, VisualElement> mRectangles;
    private int[] array;
    private double elementWidth;
    private double height;
    private double width;

    private boolean isSorted = false;

    private AbstractSort mSort;

    public MainSceneController() {
        mPreferences = Preferences.userRoot().node(SettingSceneController.class.getName());
        mRectangles = new HashMap<>();
    }

    private void draw() {
        array = getArrayForSort();
        draw(array);
    }

    private void draw(int[] array) {

        width = mDrawView.prefWidth(-1);
        height = mDrawView.prefHeight(-1);

        elementWidth = (width - 3 * array.length) / array.length;

        mDrawView.getChildren().clear();
        mRectangles.clear();

        Polygon polygon = new Polygon();
        polygon.setFill(Color.RED);
        polygon.setVisible(false);

        for (int i = 0; i < array.length; i++) {

            double elementHeight = ((height / array.length) * array[i]) + 10;

            Rectangle r = new Rectangle();
            if (i == 0) {
                r.setLayoutX(3);
            } else {
                r.setLayoutX((elementWidth * i) + (3 * i) + 3);
            }
            r.setLayoutY(500 - elementHeight);
            r.setWidth(elementWidth);
            r.setHeight(elementHeight);
            r.setFill(Color.GRAY);

            double arg = (elementWidth / 2);
            Double[] coordinates = new Double[]{r.getLayoutX() + arg, 500d, r.getLayoutX() + 4 + arg, 510d, r.getLayoutX() - 4 + arg, 510d};

            mRectangles.put(i, new VisualElement(r, polygon, coordinates));
            mDrawView.getChildren().add(r);
        }
        mDrawView.getChildren().add(polygon);
    }

    private int[] getArrayForSort() {

        int arraySize = mPreferences.getInt(SettingSceneController.SIZE_BOX_ID, 50);

        int[] array = new int[arraySize];

        switch (mPreferences.get(SettingSceneController.TYPE_OF_ARRAY_BOX_ID, SettingSceneController.RANDOM)) {
            case SettingSceneController.RANDOM:
                for (int i = 0; i < arraySize; i++) {
                    array[i] = i + 1;
                }
                Utils.shuffleArray(array);
                break;
            case SettingSceneController.NEARLY_SORTED:
                for (int i = 0; i < arraySize; i++) {
                    array[i] = i + 1;
                }
                array = Utils.nearlySorted(array, 3);
                break;
            case SettingSceneController.REVERSED:
                for (int i = 0; i < arraySize; i++) {
                    array[i] = arraySize - i;
                }
                break;
            case SettingSceneController.FEW_UNIQUE:
                Random generator = new Random();
                List<Integer> unique = new ArrayList<>(4);
                while (unique.size() != 4) {
                    int u = generator.nextInt(arraySize);
                    if (!unique.contains(u)) {
                        unique.add(u);
                    }
                }
                int size = array.length / 4;
                int h = array.length % 4;

                Arrays.fill(array, 0, size, unique.get(0));
                Arrays.fill(array, size, (size * 2), unique.get(1));
                Arrays.fill(array, size * 2, (size * 3), unique.get(2));
                Arrays.fill(array, size * 3, (size * 4) + h, unique.get(3));

                Utils.shuffleArray(array);
                break;
        }
        return array;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mStartBtn.setOnAction(this);
        mStopBtn.setOnAction(this);
        mSettingBtn.setOnAction(this);

        mChoiceAlgorithms.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observableValue, String value, String newValue) {
                mPreferences.put(CHOICE_ALGORITHMS_ID, newValue);
            }
        });
        mChoiceAlgorithms.setItems(FXCollections.observableArrayList(MERGE_SORT_ALGORITHM, BUBBLE_SORT_ALGORITHM,
                SELECTION_SORT_ALGORITHM, INSERTION_SORT_ALGORITHM,
                SHELL_SORT_ALGORITHM, HEAP_SORT_ALGORITHM, QUICK_SORT_ALGORITHM));
        mChoiceAlgorithms.setValue(mPreferences.get(CHOICE_ALGORITHMS_ID, MERGE_SORT_ALGORITHM));

        draw();
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Parent o = (Parent) actionEvent.getSource();

        switch (o.getId()) {
            case START_BTN_ID:

                if (mFuture != null) {
                    return;
                } else if (isSorted) {
                    draw();
                }

                mSettingBtn.setDisable(true);
                mStartBtn.setDisable(true);

                final CyclicBarrier barrier = new CyclicBarrier(2);

                mFuture = mThreadPoolExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Arrays.toString(array));
                        mSort = getSortAlgorithms(barrier);
                  //      mSort = new TimSort(barrier, mRectangles, height);
                        mSort.sort(array, new UICallback() {
                            @Override
                            public void refresh(final int position, final int[] params) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mFuture == null) {
                                            return;
                                        }

                                        mSort.redrawElement(position, params);

                                        Timeline timeline = new Timeline(new KeyFrame(
                                                Duration.millis(mPreferences.getInt(SettingSceneController.TIME_BOX_ID, 50)), //mPreferences.getInt(SettingSceneController.TIME_BOX_ID, 50)
                                                new EventHandler<ActionEvent>() {
                                                    @Override
                                                    public void handle(ActionEvent actionEvent) {
                                                        try {
                                                            barrier.await();
                                                        } catch (InterruptedException | BrokenBarrierException ignored) {

                                                        }
                                                    }
                                                }));
                                        timeline.play();
                                    }
                                });
                            }
                        });
                        mRectangles.get(0).getPolygon().setVisible(false);

                        System.out.println(Arrays.toString(array));
                        if (mFuture != null) {
                            mFuture = null;
                            isSorted = true;
                        }
                        mSettingBtn.setDisable(false);
                        mStartBtn.setDisable(false);
                    }
                });
                break;
            case STOP_BTN_ID:

                if (mFuture != null) {
                    mSort.stop();
                    mFuture.cancel(true);
                    mFuture = null;
                    isSorted = false;
                    mSettingBtn.setDisable(false);
                    mStartBtn.setDisable(false);
                    draw();
                }

                break;
            case SETTING_BTN_ID:
                try {
                    Platform.setImplicitExit(false);

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout/setting.fxml"));

                    Stage stage = new Stage();
                    stage.setScene(new Scene(fxmlLoader.load()));
                    SettingSceneController controller = fxmlLoader.getController();
                    controller.setStage(stage);

                    stage.setTitle("Setting");
                    stage.setOnHidden(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent windowEvent) {
                            Stage source = (Stage) windowEvent.getSource();
                      //      boolean needUpdate = (boolean) source.getUserData();
                            if (source.getUserData() != null && (boolean) source.getUserData()) {
                                draw();
                                isSorted = false;
                            }
                        }
                    });

                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private AbstractSort getSortAlgorithms(CyclicBarrier barrier) {
        AbstractSort sort = null;
        switch (mPreferences.get(CHOICE_ALGORITHMS_ID, MERGE_SORT_ALGORITHM)) {
            case BUBBLE_SORT_ALGORITHM:
                sort = new BubbleSort(barrier, mRectangles, height);
                break;
            case HEAP_SORT_ALGORITHM:
                sort = new HeapSort(barrier, mRectangles, height);
                break;
            case INSERTION_SORT_ALGORITHM:
                sort = new InsertionSort(barrier, mRectangles, height);
                break;
            case QUICK_SORT_ALGORITHM:
                sort = new QuickSort(barrier, mRectangles, height);
                break;
            case SELECTION_SORT_ALGORITHM:
                sort = new SelectionSort(barrier, mRectangles, height);
                break;
            case SHELL_SORT_ALGORITHM:
                sort = new ShellSort(barrier, mRectangles, height);
                break;
            default:
                sort = new MergeSort(barrier, mRectangles, height);
                break;
        }
        return sort;
    }
}
