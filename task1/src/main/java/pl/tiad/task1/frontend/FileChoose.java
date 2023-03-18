package pl.tiad.task1.frontend;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class FileChoose {
    private FileChoose() {
    }

    private static String lastUsedDir = "";

    /**
     * Method opens given type of dialog.
     *
     * @param windowTitle String
     * @param actionEvent ActionEvent
     * @return String
     */
    public static List<String> choose(
            String windowTitle,
            ActionEvent actionEvent
    ) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(windowTitle);
        if (!lastUsedDir.isEmpty()) {
            chooser.setInitialDirectory(new File(lastUsedDir));
        }
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Articles data (*.csv)", "*.csv"));
        List<File> chosenFiles = chooser.showOpenMultipleDialog(((Button) actionEvent.getSource())
                .getScene()
                .getWindow());
        if (chosenFiles == null) {
            return Collections.emptyList();
        }
        lastUsedDir = chosenFiles.get(0).getAbsoluteFile().getParentFile().getAbsolutePath();
        return chosenFiles.stream().map(File::getAbsolutePath).toList();
    }
}
