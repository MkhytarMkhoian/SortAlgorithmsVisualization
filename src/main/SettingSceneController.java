package main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Created by Mkhitar on 15.03.2015.
 */
public class SettingSceneController implements Initializable, EventHandler<ActionEvent> {

    public static final String OK_BTN_ID = "mOkBtn";
    public static final String CANCEL_BTN_ID = "mCancelBtn";
    public static final String TYPE_OF_ARRAY_BOX_ID = "mTypeOfArrayBox";
    public static final String SIZE_BOX_ID = "mSizeBox";
    public static final String TIME_BOX_ID = "mTimeBox";

    public static final String RANDOM = "Random";
    public static final String NEARLY_SORTED = "Nearly Sorted";
    public static final String REVERSED = "Reversed";
    public static final String FEW_UNIQUE = "Few Unique";

    @FXML
    private Button mOkBtn;
    @FXML
    private Button mCancelBtn;
    @FXML
    private ChoiceBox mTypeOfArrayBox;
    @FXML
    private ChoiceBox mSizeBox;
    @FXML
    private ChoiceBox mTimeBox;
    private Preferences mPreferences;
    private Stage mStage;

    public SettingSceneController() {
        mPreferences = Preferences.userRoot().node(this.getClass().getName());
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Parent o = (Parent) actionEvent.getSource();

        switch (o.getId()) {
            case OK_BTN_ID:

                if (mStage.getUserData() == null) {
                    setFlag(false);
                }
                mStage.hide();
                break;
            case CANCEL_BTN_ID:

                setFlag(false);
                mStage.hide();
                break;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        mOkBtn.setOnAction(this);
        mCancelBtn.setOnAction(this);
        mSizeBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer value, Integer newValue) {
                mPreferences.putInt(SIZE_BOX_ID, newValue);
                setFlag(true);
            }
        });
        mTypeOfArrayBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observableValue, String value, String newValue) {
                mPreferences.put(TYPE_OF_ARRAY_BOX_ID, newValue);
                setFlag(true);
            }
        });

        mTimeBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer value, Integer newValue) {
                mPreferences.putInt(TIME_BOX_ID, newValue);
                setFlag(true);
            }
        });

        mTimeBox.setItems(FXCollections.observableArrayList(20, 30, 40, 50, 100, 150, 200, 300, 400, 500));
        mTypeOfArrayBox.setItems(FXCollections.observableArrayList(RANDOM, NEARLY_SORTED, REVERSED, FEW_UNIQUE));
        mSizeBox.setItems(FXCollections.observableArrayList(50, 100, 150, 200));

        mSizeBox.setValue(mPreferences.getInt(SIZE_BOX_ID, 50));
        mTypeOfArrayBox.setValue(mPreferences.get(TYPE_OF_ARRAY_BOX_ID, RANDOM));
        mTimeBox.setValue(mPreferences.getInt(TIME_BOX_ID, 50));

    }

    private void setFlag(boolean flag) {
        if (mStage != null) {
            mStage.setUserData(flag);
        }
    }

    public Stage getStage() {
        return mStage;
    }

    public void setStage(Stage stage) {
        this.mStage = stage;
    }
}
