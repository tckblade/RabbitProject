package com.sparta.team.display;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DisplayManager implements DisplayManagerInterface {

    static final String LOG_PROPERTIES_FILE_DISPLAY = "resources/log4j.properties";
    static Logger log = Logger.getLogger(DisplayManager.class.getName());

    private long aliveM = 0;
    private long aliveF = 0;
    private long livedM = 0;
    private long livedF = 0;
    private int type;
    private Writer writer = null;

    public DisplayManager(int type) {
        this.type = type;
        initialiseLogging();
        initialWriter();
    }

    public static void initialiseLogging() {
        PropertyConfigurator.configure(LOG_PROPERTIES_FILE_DISPLAY);
    }

    @Override
    public void displayTimeElapsed(int time) {
        if (type == 1) {
            writeToFile("_                                                _");
            writeToFile("____________Month " + time + " Results____________");
        } else {
            writeToFile("_                                                _");
            writeToFile("______________Simulation End Results______________");
        }
        writeToFile("Simulation running for [" + (time / 12) + " years " + (time % 12) + " months]");
    }

    private void displayMaleRabbitsAlive(String animalType, long rabbits) {
        writeToFile("Male " + animalType + " alive [" + rabbits + "]");
    }

    private void displayFemaleRabbitsAlive(String animalType, long rabbits) {
        writeToFile("Female " + animalType + " alive [" + rabbits + "]");
    }

    private void displayMaleRabbitsLived(String animalType, long rabbits) {
        writeToFile("Male " + animalType + " lived [" + rabbits + "] has died [" + (rabbits - aliveM) + "]");
    }

    private void displayFemaleRabbitsLived(String animalType, long rabbits) {
        writeToFile("Female " + animalType + " lived [" + rabbits + "] has died [" + (rabbits - aliveF) + "]");
    }

    @Override
    public void displayAnimalsLived(String animalType, long maleRabbits, long femaleRabbits) {
        livedM = maleRabbits;
        livedF = femaleRabbits;
        displayMaleRabbitsLived(animalType, maleRabbits);
        displayFemaleRabbitsLived(animalType, femaleRabbits);
    }

    @Override
    public void displayAnimalsAlive(String animalType, long maleRabbits, long femaleRabbits) {
        aliveM = maleRabbits;
        aliveF = femaleRabbits;
        displayMaleRabbitsAlive(animalType, maleRabbits);
        displayFemaleRabbitsAlive(animalType, femaleRabbits);
    }

    @Override
    public void displayAnimalsEaten(long lived, long alive, long eaten) {
        writeToFile("Rabbits eaten by foxes [" + eaten + "]");
        writeToFile("Rabbits that died of natural death [" + (lived - alive - eaten) + "]");
    }

    public void displayAnimalsDied(long eaten, long totalDied) {
        writeToFile("Rabbits eaten by foxes [" + eaten + "]");
        writeToFile("Rabbits that died of natural causes [" + (totalDied - eaten) + "]");
    }

    @Override
    public void displayMessageInReport(String message) {
        writeToFile(message);
    }

    private void initialWriter() {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("SimulationResults.txt"), StandardCharsets.UTF_8));
        } catch (IOException ex) {
            log.debug(ex);
        }
    }

    private void writeToFile(String line) {
        try {
            writer.write(line + "\n");
        } catch (IOException e) {
            log.debug(e);
        }
    }

    public void writerClose() {
        System.out.println("Please check results file for results.");
        writeToFile("______________Simulation Finished______________");
        writeToFile("_                                             _");
        log.debug("Simulation Finished");
        try {
            writer.close();
        } catch (IOException e) {
            log.debug(e);
        }
    }
}