package HomeWork05;

import java.util.ArrayList;
import java.util.Arrays;

public class Race {
    private ArrayList<Stage> stages;

    public ArrayList<Stage> getStages() {
        return stages;
    }

    public Race(Stage... stages) {
        this.stages = new ArrayList<>(Arrays.asList(stages));
    }

    private int CARS_FINISHED = 1;

    public int getCarsFinished() {
        return CARS_FINISHED;
    }

    public void setCarsFinished(int carsFinished) {
        CARS_FINISHED = carsFinished;
    }
}
