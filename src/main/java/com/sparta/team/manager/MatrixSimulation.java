package com.sparta.team.manager;

import com.sparta.team.display.DisplayManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MatrixSimulation {

    static final String LOG_PROPERTIES_FILE = "resources/log4j.properties";
    static Logger log = Logger.getLogger(MatrixSimulation.class.getName());


    private final int RABBITMATURITY = 3;
    private final int RABBITLIFESPAN = 60;

    private List<Long> femaleRabbitsByAge = new ArrayList<Long>(Collections.nCopies(RABBITLIFESPAN, 0l));
    private List<Long> maleRabbitsByAge = new ArrayList<Long>(Collections.nCopies(RABBITLIFESPAN, 0l));

    private long maleRabbitsLived = 0;
    private long femaleRabbitsLived = 0;

    private long maleRabbitsAlive = 0;
    private long femaleRabbitsAlive = 0;



    public void startSimulation(int numberOfMonths, int displayOutputType){

        initialiseLogging();
        populateInitialGeneration();
        DisplayManager displayManager = new DisplayManager(displayOutputType);

        //flow of time
        for(int i = 0; i < numberOfMonths; i++){
            log.trace("------------------------------");
            log.trace("Generation " + i + " start");
            log.trace("---------------");

            update();

            log.trace("---------------");
            log.trace("Generation " + i + " end");
            log.trace("------------------------------");


            if(displayOutputType == 1){
                displayManager.displayTimeElapsed(i);
                displayManager.displayRabbitsAlive(maleRabbitsAlive, femaleRabbitsAlive);
                displayManager.displayRabbitsLived(maleRabbitsLived, femaleRabbitsLived);
            }
        }

        if(displayOutputType == 2){
            displayManager.displayTimeElapsed(numberOfMonths);
            displayManager.displayRabbitsAlive(maleRabbitsAlive, femaleRabbitsAlive);
            displayManager.displayRabbitsLived(maleRabbitsLived, femaleRabbitsLived);
        }
        displayManager.writerClose();
    }



    public void populateInitialGeneration(){

        log.trace("Adam and Eve added");
        femaleRabbitsByAge.set(0, 1l);
        maleRabbitsByAge.set(0, 1l);

        femaleRabbitsLived++;
        maleRabbitsLived++;
    }



    public void update(){

        log.trace("Female rabbit population at start: " + femaleRabbitsByAge.toString());
        log.trace("Male rabbit population at start: " + maleRabbitsByAge.toString());

        long numberOfCouples = getRabbitCouples();
        log.trace("Number of couples available: " + numberOfCouples);

        ageRabbits();
        log.trace("Female rabbit population after aging: " + femaleRabbitsByAge.toString());
        log.trace("Male rabbit population after aging: " + maleRabbitsByAge.toString());

        generateNewGeneration(numberOfCouples);
        log.trace("Female rabbit population after new population born: " + femaleRabbitsByAge.toString());
        log.trace("Male rabbit population after new population born: " + maleRabbitsByAge.toString());

        removeDeadRabbits();
        log.trace("Female rabbit population after funerals: " + femaleRabbitsByAge.toString());
        log.trace("Male rabbit population after funerals: " + maleRabbitsByAge.toString());
        updateAliveRabbits();



    }


    public long getRabbitCouples(){

        long numberOfMatureFemaleRabbits = 0;
        long numberOfMatureMaleRabbits = 0;

        // add up all the values between index 3 and the last index
        for(int i = RABBITMATURITY; i < femaleRabbitsByAge.size(); i++){
            numberOfMatureFemaleRabbits += femaleRabbitsByAge.get(i);
            numberOfMatureMaleRabbits += maleRabbitsByAge.get(i);
        }

        long numberOfCouples = 0;

        //determine how many times we have to breed
        if(numberOfMatureFemaleRabbits < numberOfMatureMaleRabbits){
            numberOfCouples = numberOfMatureFemaleRabbits;
        }
        else{
            numberOfCouples = numberOfMatureMaleRabbits;
        }


        return numberOfCouples;
    }






    public void ageRabbits(){
        femaleRabbitsByAge.add(0, 0l);
        maleRabbitsByAge.add(0, 0l);
    }






    public void generateNewGeneration(long numberOfCouples)
    {
        long newFemaleGeneration =  0;
        long newMaleGeneration = 0;

        Random random = new Random();
        int numberOfNewRabbits;

        boolean coupleSuccess;

        //iterate over all the rabbit couples
        log.trace("---------");
        for(int i = 0; i < numberOfCouples; i++) {
            //will the couple be successful?

            log.trace("Couple " + i + " trying for kids");
            coupleSuccess = random.nextBoolean();
            log.trace("was successful: " + coupleSuccess);
            if(coupleSuccess) {
                //one couple makes new rabbits

                //picking number of rabbits
                numberOfNewRabbits = random.nextInt(14) + 1;
                log.trace("they had " + numberOfNewRabbits + " kids");
                for (int j = 0; j < numberOfNewRabbits; j++) {

                    boolean isFemale = random.nextBoolean();

                    if (isFemale) {
                        newFemaleGeneration++;
                        femaleRabbitsLived++;
                    } else {
                        newMaleGeneration++;
                        maleRabbitsLived++;
                    }
                }
            }
            log.trace("---");
        }
        log.trace("---------");

        femaleRabbitsByAge.set(0,newFemaleGeneration);
        maleRabbitsByAge.set(0,newMaleGeneration);

    }





    public void removeDeadRabbits(){
        femaleRabbitsByAge.remove(femaleRabbitsByAge.size()-1);
        maleRabbitsByAge.remove(maleRabbitsByAge.size()-1);
    }


    public void updateAliveRabbits(){
        long newFemaleRabbitsAlive = 0;
        long newMaleRabbitsAlive = 0;

        for(long l : femaleRabbitsByAge){
            newFemaleRabbitsAlive += l;
        }

        for(long l : maleRabbitsByAge){
            newMaleRabbitsAlive += l;
        }

        femaleRabbitsAlive = newFemaleRabbitsAlive;
        maleRabbitsAlive = newMaleRabbitsAlive;
    }



    public static void initialiseLogging() {
        PropertyConfigurator.configure(LOG_PROPERTIES_FILE);
    }





    public int getRABBITMATURITY() {
        return RABBITMATURITY;
    }

    public int getRABBITLIFESPAN() {
        return RABBITLIFESPAN;
    }

    public List<Long> getFemaleRabbitsByAge() {
        return femaleRabbitsByAge;
    }

    public List<Long> getMaleRabbitsByAge() {
        return maleRabbitsByAge;
    }

    public long getMaleRabbitsLived() {
        return maleRabbitsLived;
    }

    public long getFemaleRabbitsLived() {
        return femaleRabbitsLived;
    }

    public long getMaleRabbitsAlive() {
        return maleRabbitsAlive;
    }

    public long getFemaleRabbitsAlive() {
        return femaleRabbitsAlive;
    }
}