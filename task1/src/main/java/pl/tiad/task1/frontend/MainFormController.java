package pl.tiad.task1.frontend;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class MainFormController {
    public static final String MAIN_FORM_RESOURCE = "MainForm.fxml";
    public static final String MAIN_FORM_TITLE = "DE and PSO Algorithms";
    @FXML
    ImageView chart;
    @FXML
    ImageView chart2;

    public void start() {
//        ParticleSwarmAlgorithm psa = new ParticleSwarmAlgorithm(new AccuracyStop(0.1), FunctionType.SPHERE,
//                1000, 100, -100, 30, 0.9, 0.8, 0.7);
//        DifferentialEvolutionAlgorithm psa = new DifferentialEvolutionAlgorithm(
//                new IterationStop(10000), FunctionType.SPHERE, 600, 100, -100,
//                30, 0.5, 0.8);
    }
}