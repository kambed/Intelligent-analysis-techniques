package pl.tiad.task2.frontend;

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
import pl.tiad.task2.backend.Algorithm;
import pl.tiad.task2.backend.opso.OsmosisParticlaSwarmAlgorithm;
import pl.tiad.task2.backend.pso.ParticleSwarmAlgorithm;
import pl.tiad.task2.backend.utils.AccuracyStop;
import pl.tiad.task2.backend.utils.FunctionType;
import pl.tiad.task2.backend.utils.IterationStop;
import pl.tiad.task2.backend.utils.StopType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

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
    public ImageView opsoChart1;
    @FXML
    public ImageView opsoChart2;
    @FXML
    public ImageView epsoChart1;
    @FXML
    public ImageView epsoChart2;
    @FXML
    public ComboBox<FunctionType> functionComboBox;
    @FXML
    public TextField minXTextField;
    @FXML
    public TextField maxXToTextField;
    @FXML
    public TextField dimensionsTextField;
    @FXML
    public TextField numberOfParticlesInEachSubpopulationTextField;
    @FXML
    public TextField migrationIntervalTextField;
    @FXML
    public TextField numberOfSubPopulationsTextField;
    @FXML
    public TextField inertionTextField;
    @FXML
    public TextField cognitionTextField;
    @FXML
    public TextField socialTextField;
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
        start(createOsmosisParticleSwarmAlgorithm(), opsoChart1, opsoChart2);
        //TODO: CREATE EPSO ALGORITHM
        //start(createDifferentialEvolutionAlgorithm(), epsoChart1, epsoChart2);
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
            if (algorithm instanceof OsmosisParticlaSwarmAlgorithm) {
                algorithm = createOsmosisParticleSwarmAlgorithm();
            } else {
                //TODO: CREATE EPSO ALGORITHM
                //algorithm = createDifferentialEvolutionAlgorithm();
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

    private OsmosisParticlaSwarmAlgorithm createOsmosisParticleSwarmAlgorithm() {
        List<ParticleSwarmAlgorithm> psoSwarms = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(numberOfSubPopulationsTextField.getText()); i++) {
            psoSwarms.add(createParticleSwarmAlgorithm());
        }
        return new OsmosisParticlaSwarmAlgorithm(
                psoSwarms,
                stopTypeMap.get(stopConditionComboBox.getValue()).apply(Double.parseDouble(stopValueTextField.getText())),
                Integer.parseInt(migrationIntervalTextField.getText()),
                Integer.parseInt(dimensionsTextField.getText())
        );
    }

    private ParticleSwarmAlgorithm createParticleSwarmAlgorithm() {
        return new ParticleSwarmAlgorithm(
                functionComboBox.getValue(),
                Integer.parseInt(numberOfParticlesInEachSubpopulationTextField.getText()),
                Double.parseDouble(maxXToTextField.getText()),
                Double.parseDouble(minXTextField.getText()),
                Integer.parseInt(dimensionsTextField.getText()),
                Double.parseDouble(inertionTextField.getText()),
                Double.parseDouble(cognitionTextField.getText()),
                Double.parseDouble(socialTextField.getText())
        );
    }

    //TODO: CREATE EPSO ALGORITHM
//    private DifferentialEvolutionAlgorithm createDifferentialEvolutionAlgorithm() {
//        return new DifferentialEvolutionAlgorithm(
//                stopTypeMap.get(stopConditionComboBox.getValue()).apply(Double.parseDouble(stopValueTextField.getText())),
//                functionComboBox.getValue(),
//                Integer.parseInt(numberOfParticlesTextField.getText()),
//                Double.parseDouble(maxXToTextField.getText()),
//                Double.parseDouble(minXTextField.getText()),
//                Integer.parseInt(dimensionsTextField.getText()),
//                Double.parseDouble(amplificationFactorTextField.getText()),
//                Double.parseDouble(crossoverProbabilityTextField.getText())
//        );
//    }
}