package pl.tiad.task1.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jfree.chart.ChartUtilities;
import pl.tiad.task1.backend.ParticleSwarmAlgorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class MainFormController {
    public static final String MAIN_FORM_RESOURCE = "MainForm.fxml";
    public static final String MAIN_FORM_TITLE = "Particle Swarm Algorithm";

    @FXML
    TextField amountOfIterations;
    @FXML
    TextField amountOfParticles;
    @FXML
    TextField maxx;
    @FXML
    TextField minx;
    @FXML
    TextField maxy;
    @FXML
    TextField miny;
    @FXML
    TextField function;
    @FXML
    TextField inertion;
    @FXML
    TextField cognition;
    @FXML
    TextField social;
    @FXML
    ImageView chart;
    @FXML
    ImageView chart2;
    @FXML
    TextArea consoleArea;

    public void saveLogs(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        String stringPath = FileChoose.saveChooser("Save logs to file ", actionEvent);
        if (!stringPath.isBlank()) {
            Files.write(Paths.get(stringPath), consoleArea.getText().getBytes());
            consoleArea.setText("");
            consoleArea.appendText("Logs saved saved to: %s\n".formatted(stringPath));
        }
    }

    public void start() {
        ParticleSwarmAlgorithm psa = new ParticleSwarmAlgorithm(Integer.parseInt(amountOfIterations.getText()),
                Integer.parseInt(amountOfParticles.getText()),
                Double.parseDouble(maxx.getText()), Double.parseDouble(minx.getText()),
                Double.parseDouble(maxy.getText()), Double.parseDouble(miny.getText()), function.getText(),
                Double.parseDouble(inertion.getText()), Double.parseDouble(cognition.getText()), Double.parseDouble(social.getText()));
        Map<String, Double> extremum = psa.start();
        consoleArea.appendText("Lowest value in function: " + extremum.get("Adaptation") + "\n");
        consoleArea.appendText("X: " + extremum.get("X") + "\n");
        consoleArea.appendText("Y: " + extremum.get("Y") + "\n");

        try {
            ChartUtilities.saveChartAsPNG(
                    new File("chart.png"),
                    ChartGenerator.generatePlot(psa.getIterations().toArray(new Integer[0]),
                            psa.getAvgPopulationValues().toArray(new Double[0]), "Avg population value"),
                    400, 220);
            FileInputStream input = new FileInputStream("chart.png");
            chart.setImage(new Image(input));

            ChartUtilities.saveChartAsPNG(
                    new File("chart2.png"),
                    ChartGenerator.generatePlot(psa.getIterations().toArray(new Integer[0]),
                            psa.getMinPopulationValues().toArray(new Double[0]), "Min population value"),
                    400, 220);
            input = new FileInputStream("chart2.png");
            chart2.setImage(new Image(input));
        } catch (IOException e) {
            consoleArea.appendText("Wystapił problem przy generowaniu wykresu. \n");
        }
    }
}