package pl.tiad.task1.frontend;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import pl.tiad.task1.backend.de.DifferentialEvolutionAlgorithm;
import pl.tiad.task1.backend.pso.ParticleSwarmAlgorithm;
import pl.tiad.task1.backend.utils.AccuracyStop;
import pl.tiad.task1.backend.utils.FunctionType;
import pl.tiad.task1.backend.utils.IterationStop;
import pl.tiad.task1.backend.utils.StopType;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

public class MainFormController implements Initializable {
    public static final String MAIN_FORM_RESOURCE = "MainForm.fxml";
    public static final String MAIN_FORM_TITLE = "DE and PSO Algorithms";
    @FXML
    public ComboBox<String> stopConditionComboBox;
    @FXML
    public TextField stopValueTextField;
    @FXML
    public ImageView chart;
    @FXML
    public ImageView chart2;
    @FXML
    public ComboBox<FunctionType> functionComboBox;
    @FXML
    public TextField numberOfIndividualsParticlesTextField;
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
    private final Map<String, Function<Double, StopType>> stopTypeMap = Map.of(
            "Accuracy", AccuracyStop::new,
            "Iteration", number -> new IterationStop(number.intValue())
    );
    @FXML
    public TextArea consoleArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stopConditionComboBox.getItems().addAll(stopTypeMap.keySet());
        functionComboBox.getItems().addAll(FunctionType.values());
    }

    public void start() {
//        ParticleSwarmAlgorithm psa = new ParticleSwarmAlgorithm(new AccuracyStop(0.1), FunctionType.SPHERE,
//                1000, 100, -100, 30, 0.9, 0.8, 0.7);
//        DifferentialEvolutionAlgorithm psa = new DifferentialEvolutionAlgorithm(
//                new IterationStop(10000), FunctionType.SPHERE, 600, 100, -100,
//                30, 0.5, 0.8);
    }
}