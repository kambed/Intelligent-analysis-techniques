package pl.tiad.task1.frontend;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;
import org.jfree.chart.ChartUtilities;
import pl.tiad.task1.backend.Algorithm;
import pl.tiad.task1.backend.cfa.CuttlefishAlgorithm;
import pl.tiad.task1.backend.pso.ParticleSwarmAlgorithm;
import pl.tiad.task1.backend.utils.AccuracyStop;
import pl.tiad.task1.backend.utils.FunctionType;
import pl.tiad.task1.backend.utils.IterationStop;
import pl.tiad.task1.backend.utils.StopType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class MainFormController implements Initializable {
    public static final String MAIN_FORM_RESOURCE = "MainForm.fxml";
    public static final String MAIN_FORM_TITLE = "CFA and GL-PSO Algorithms";
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
    public TextField minXTextField;
    @FXML
    public TextField maxXToTextField;
    @FXML
    public TextField r1FactorTextField;
    @FXML
    public TextField r2FactorTextField;
    @FXML
    public TextField v1FactorTextField;
    @FXML
    public TextField v2FactorTextField;
    @FXML
    public TextField dimensionsTextField;
    @FXML
    public TextField inertionTextField;
    @FXML
    public TextField cognitionTextField;
    @FXML
    public TextField socialTextField;
    @FXML
    public TextField mutationTextField;
    @FXML
    public TextField numberOFRunsTextField;
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
        start(createCuttlefishAlgorithm(), deaChart1, deaChart2);
        resultSection.setVisible(true);
    }

    private void start(Algorithm algorithm, ImageView populationAvgChart, ImageView populationMinChart) {
        int numberOfRuns = Integer.parseInt(numberOFRunsTextField.getText());
        List<Map<String, Double>> extremums = new ArrayList<>();
        List<List<Double>> avgPopulationValues = new ArrayList<>();
        List<List<Double>> minPopulationValues = new ArrayList<>();
        List<List<Integer>> iterations = new ArrayList<>();
        for (int i = 0; i < numberOfRuns; i++) {
            extremums.add(new HashMap<>(algorithm.start()));
            avgPopulationValues.add(new ArrayList<>(algorithm.getAvgPopulationValues()));
            minPopulationValues.add(new ArrayList<>(algorithm.getMinPopulationValues()));
            iterations.add(new ArrayList<>(algorithm.getIterations()));
            if (algorithm instanceof ParticleSwarmAlgorithm) {
                algorithm = createParticleSwarmAlgorithm();
            } else {
                algorithm = createCuttlefishAlgorithm();
            }
        }
        String algorithmName = algorithm.getClass().getSimpleName();
        Map<String, Double> extremum = extremums.stream()
                .min(Comparator.comparingDouble(o -> o.get("Adaptation")))
                .orElseThrow();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n")
                .append(algorithmName)
                .append(" finished with best results:\n");
        stringBuilder.append("Lowest value in function: ")
                .append(extremum.get("Adaptation"))
                .append("\n");

        double stdDeviation = new StandardDeviation().evaluate(
                Arrays.stream(extremums.stream().map(o -> o.get("Adaptation")).toArray(Double[]::new))
                .mapToDouble(Double::doubleValue)
                .toArray()
        );
        stringBuilder.append("Standard deviation: ")
                .append(stdDeviation)
                .append("\n");

        if (stopConditionComboBox.getValue().equals("Accuracy")) {
            double successRate = minPopulationValues.stream()
                    .mapToDouble(List::size)
                    .filter(size -> size != Algorithm.getMaxIterationsForAccuracy() + 1)
                    .count() / (double) (numberOfRuns);
            stringBuilder.append("Success rate: ")
                    .append(successRate * 100)
                    .append("%")
                    .append("\n");
        }

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
            int maxIterations = (int) iterations.stream().mapToDouble(List::size).max().orElse(0.0);
            List<Double> avgValues = Arrays.asList(new Double[maxIterations]);
            List<Double> minValues = Arrays.asList(new Double[maxIterations]);
            IntStream.range(0, avgValues.size()).forEach(index -> {
                        avgValues.set(index, avgPopulationValues.stream()
                                .mapToDouble(list -> index <= list.size() - 1 ? list.get(index) : 0)
                                .sum()
                                / avgPopulationValues.size());
                        minValues.set(index, minPopulationValues.stream()
                                .mapToDouble(list -> index <= list.size() - 1 ? list.get(index) : 0)
                                .sum()
                                / minPopulationValues.size());
                    }
            );
            ChartUtilities.saveChartAsPNG(
                    new File(populationAvgChart.getId() + ".png"),
                    ChartGenerator.generatePlot(
                            Arrays.stream(IntStream.range(0, maxIterations).toArray()).boxed().toArray(Integer[]::new),
                            avgValues.toArray(Double[]::new),
                            "Average population value"
                    ),
                    400,
                    250
            );
            populationAvgChart.setImage(new Image(new FileInputStream(populationAvgChart.getId() + ".png")));

            ChartUtilities.saveChartAsPNG(
                    new File(populationMinChart.getId() + ".png"),
                    ChartGenerator.generatePlot(
                            Arrays.stream(IntStream.range(0, maxIterations).toArray()).boxed().toArray(Integer[]::new),
                            minValues.toArray(Double[]::new),
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
                Double.parseDouble(inertionTextField.getText()),
                Double.parseDouble(cognitionTextField.getText()),
                Double.parseDouble(socialTextField.getText()),
                Double.parseDouble(mutationTextField.getText())
        );
    }

    private CuttlefishAlgorithm createCuttlefishAlgorithm() {
        return new CuttlefishAlgorithm(
                stopTypeMap.get(stopConditionComboBox.getValue()).apply(Double.parseDouble(stopValueTextField.getText())),
                functionComboBox.getValue(),
                Integer.parseInt(numberOfIndividualsTextField.getText()),
                Double.parseDouble(maxXToTextField.getText()),
                Double.parseDouble(minXTextField.getText()),
                Integer.parseInt(dimensionsTextField.getText()),
                Double.parseDouble(r1FactorTextField.getText()),
                Double.parseDouble(r2FactorTextField.getText()),
                Double.parseDouble(v1FactorTextField.getText()),
                Double.parseDouble(v2FactorTextField.getText())
        );
    }
}