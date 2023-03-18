package pl.tiad.task1.frontend;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.jfree.chart.ChartUtilities;
import pl.tiad.task1.backend.Algorithm;
import pl.tiad.task1.backend.de.DifferentialEvolutionAlgorithm;
import pl.tiad.task1.backend.pso.ParticleSwarmAlgorithm;
import pl.tiad.task1.backend.utils.AccuracyStop;
import pl.tiad.task1.backend.utils.FunctionType;
import pl.tiad.task1.backend.utils.IterationStop;
import pl.tiad.task1.backend.utils.StopType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

public class MainFormController implements Initializable {
    public static final String MAIN_FORM_RESOURCE = "MainForm.fxml";
    public static final String MAIN_FORM_TITLE = "DE and PSO Algorithms";
    private final Map<String, Function<Double, StopType>> stopTypeMap = Map.of(
            "Accuracy", AccuracyStop::new,
            "Iteration", number -> new IterationStop(number.intValue())
    );
    @FXML
    public ComboBox<String> stopConditionComboBox;
    @FXML
    public TextField stopValueTextField;
    @FXML
    public ImageView psoChart1;
    @FXML
    public ImageView psoChart2;
    @FXML
    public ImageView deaChart1;
    @FXML
    public ImageView deaChart2;
    @FXML
    public ComboBox<FunctionType> functionComboBox;
    @FXML
    public TextField numberOfIndividualsTextField;
    @FXML
    public TextField numberOfParticlesTextField;
    @FXML
    public TextField minXTextField;
    @FXML
    public TextField maxXToTextField;
    @FXML
    public TextField dimensionsTextField;
    @FXML
    public TextField amplificationFactorTextField;
    @FXML
    public TextField crossoverProbabilityTextField;
    @FXML
    public TextField intentionTextField;
    @FXML
    public TextField cognitionTextField;
    @FXML
    public TextField socialTextField;
    @FXML
    public TextArea consoleArea;
    @FXML
    public VBox resultSection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stopConditionComboBox.getItems().addAll(stopTypeMap.keySet());
        functionComboBox.getItems().addAll(FunctionType.values());
    }

    public void start() {
        start(createParticleSwarmAlgorithm(), psoChart1, psoChart2);
        start(createDifferentialEvolutionAlgorithm(), deaChart1, deaChart2);
        resultSection.setVisible(true);
    }

    private void start(Algorithm algorithm, ImageView populationAvgChart, ImageView populationMinChart) {
        String algorithmName = algorithm.getClass().getSimpleName();
        Map<String, Double> extremum = algorithm.start();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n")
                .append(algorithmName)
                .append(" finished with results:\n");
        stringBuilder.append("Lowest value in function: ")
                .append(extremum.get("Adaptation"))
                .append("\n");
        int i = 0;
        do {
            stringBuilder.append("X")
                    .append(i + 1)
                    .append(": ")
                    .append(extremum.get("X" + i))
                    .append("\n");
            i++;
        } while (extremum.get("X" + i) != null);
        consoleArea.appendText(stringBuilder.toString());
        try {
            ChartUtilities.saveChartAsPNG(
                    new File(populationAvgChart.getId() + ".png"),
                    ChartGenerator.generatePlot(
                            algorithm.getIterations().toArray(new Integer[0]),
                            algorithm.getAvgPopulationValues().toArray(new Double[0]),
                            "Average population value"
                    ),
                    400,
                    250
            );
            populationAvgChart.setImage(new Image(new FileInputStream(populationAvgChart.getId() + ".png")));

            ChartUtilities.saveChartAsPNG(
                    new File(populationMinChart.getId() + ".png"),
                    ChartGenerator.generatePlot(
                            algorithm.getIterations().toArray(new Integer[0]),
                            algorithm.getMinPopulationValues().toArray(new Double[0]),
                            "Minimum population value"
                    ),
                    400,
                    250
            );
            populationMinChart.setImage(new Image(new FileInputStream(populationMinChart.getId() + ".png")));
        } catch (IOException e) {
            consoleArea.appendText("Error while generation " + algorithm.getClass().getSimpleName() + " charts. \n");
        }
    }

    private ParticleSwarmAlgorithm createParticleSwarmAlgorithm() {
        return new ParticleSwarmAlgorithm(
                stopTypeMap.get(stopConditionComboBox.getValue()).apply(Double.parseDouble(stopValueTextField.getText())),
                functionComboBox.getValue(),
                Integer.parseInt(numberOfIndividualsTextField.getText()),
                Double.parseDouble(maxXToTextField.getText()),
                Double.parseDouble(minXTextField.getText()),
                Integer.parseInt(dimensionsTextField.getText()),
                Double.parseDouble(intentionTextField.getText()),
                Double.parseDouble(cognitionTextField.getText()),
                Double.parseDouble(socialTextField.getText())
        );
    }

    private DifferentialEvolutionAlgorithm createDifferentialEvolutionAlgorithm() {
        return new DifferentialEvolutionAlgorithm(
                stopTypeMap.get(stopConditionComboBox.getValue()).apply(Double.parseDouble(stopValueTextField.getText())),
                functionComboBox.getValue(),
                Integer.parseInt(numberOfParticlesTextField.getText()),
                Double.parseDouble(maxXToTextField.getText()),
                Double.parseDouble(minXTextField.getText()),
                Integer.parseInt(dimensionsTextField.getText()),
                Double.parseDouble(amplificationFactorTextField.getText()),
                Double.parseDouble(crossoverProbabilityTextField.getText())
        );
    }
}