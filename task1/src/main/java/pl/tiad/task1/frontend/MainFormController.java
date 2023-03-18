package pl.tiad.task1.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jfree.chart.ChartUtilities;
import pl.tiad.task1.backend.utils.FunctionType;
import pl.tiad.task1.backend.utils.AccuracyStop;
import pl.tiad.task1.backend.pso.ParticleSwarmAlgorithm;
import pl.tiad.task1.backend.utils.IterationStop;

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
            consoleArea.appendText("Logs saved saved to: %s%n".formatted(stringPath));
        }
    }

    public void start() {
        ParticleSwarmAlgorithm psa = new ParticleSwarmAlgorithm(new IterationStop(100), FunctionType.SPHERE,
                1000, 100, -100, 30, 0.9, 0.8, 0.7);
//        ParticleSwarmAlgorithm psa = new ParticleSwarmAlgorithm(new AccuracyStop(0.1), FunctionType.SPHERE,
//                1000, 100, -100, 30, 0.9, 0.8, 0.7);
//        DifferentialEvolutionAlgorithm psa = new DifferentialEvolutionAlgorithm(
//                new IterationStop(10000), FunctionType.SPHERE, 600, 100, -100,
//                30, 0.5, 0.8);
        Map<String, Double> extremum = psa.start();
        consoleArea.appendText("Lowest value in function: " + extremum.get("Adaptation") + "\n");
        int i = 0;
        do {
            consoleArea.appendText("X" + (i + 1) + ": " + extremum.get("X" + i) + "\n");
            i++;
        } while (extremum.get("X" + i) != null);
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