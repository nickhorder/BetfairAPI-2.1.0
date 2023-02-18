package com.nickhorder.betfairapi.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Properties;

public class TimeHandler {
    private static Properties prop = new Properties();
    private static long secondsBeforeStart;
    private static String dailyPGMStartTime;
    private static String dailyPGMEndTime;
    private static long manyHoursToNextRace;
    private static boolean programRunning;
    private static boolean moreRacingToday = true;
    private DateTimeFormatter raceTimeFormatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

    private static DateTimeFormatter programRunTimesFormatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

    //Initialize and load in Properties file
    static {
        try {
            InputStream in = TimeHandler.class.getResourceAsStream("/apingdemo.properties");
            prop.load(in);
            in.close();

            secondsBeforeStart = Long.parseLong(prop.getProperty("SECONDS_BEFORE_START"));
            manyHoursToNextRace = Long.parseLong(prop.getProperty("NEXT_RACE_LONG_AWAY"));
            dailyPGMStartTime = prop.getProperty("DAILY_PGM_START_TIME");
            dailyPGMEndTime = prop.getProperty("DAILY_PGM_END_TIME");


        } catch (IOException e) {
            System.out.println("Error loading the properties file: " + e);
        }
    }
    //Format Start/End PGM times into a console-friendly format. Has to be done here before
    //Getters are initialised later on.
    private static String startTimeInConsole = getDailyPGMStartTime().substring(2,9);
    private static String endTimeInConsole = getDailyPGMEndTime().substring(1,9);

   /*
   The method programStartStop holds the logic on whether program can start on request from the
   user, whether it continues running or shuts down.
    */
    public static boolean programStartStop(boolean moreRacingToday) throws FileNotFoundException {

        LocalDate todaysDate = LocalDate.now();
        //Build today's date with the daily start time
        String todaysProgramStartDateTime = todaysDate + dailyPGMStartTime;
        //Build today's date with the daily end time
        String todaysProgramEndDateTime = todaysDate + dailyPGMEndTime;
        // Grab the time now
        LocalTime localTime = LocalTime.now();
        //Parse both start/end date/times into LocalDateTime for calculations
        LocalDateTime programStart = LocalDateTime.parse(todaysProgramStartDateTime, programRunTimesFormatter);
        LocalDateTime programEnd = LocalDateTime.parse(todaysProgramEndDateTime, programRunTimesFormatter);
        //Calculate duration between now and 10am, now and 10pm
        Duration programStartToNow = Duration.between(localTime, programStart);
        Duration programFinishesIn = Duration.between(localTime, programEnd);
        //Convert Duration instances to Seconds
        Long secondsDifferenceToPGMStart = programStartToNow.getSeconds();
        Long secondsDifferenceToPGMClose = programFinishesIn.getSeconds();

        //If it's at or after the time the PGM starts (i.e 10am), but not less than the time
        //The program finishes, start the program.
        if (secondsDifferenceToPGMStart <= 0 && secondsDifferenceToPGMClose > 0) {
            programRunning = true;

        }


        //If it comes to the time the PGM is set to shutdown and it's still running, shut it down
        if (secondsDifferenceToPGMClose == 0) {
            System.out.println("THE TIME IS " + localTime + ".SYSTEM SHUTTING DOWN!");
            programRunning = false;
            //Write MI here
            System.exit(0);
        }
        //If there is no more racing today (decided by nextRaceSleepCalculator below), then shut down.
        if (!getMoreRacingToday())
        {
            System.out.println("No More Racing Today. SYSTEM SHUTTING DOWN!");
            programRunning = false;
            //Write MI here
            System.exit(0);
        }
        //Print seconds to console, debugging really.
        //System.out.println(secondsDifferenceToPGMStart);
        //System.out.println(secondsDifferenceToPGMClose);

        return programRunning;
    }

    /*
    The method nextRaceSleepCalculator is useful! Here we calculate the duration in seconds, to the next
    race. The reason this is done is to come back to the market very close to the off. We are trying to
    get a BSP that is as close, or the same, as the actual race BSP. This is essential for our strategy
    as proved in backtesting, to work.
    There is also a calculation done that if the next race is X hours away, likely that racing has finished
    for the day, so we set moreRacingToday to false, returning that value back to the Flow Controller which
    then calls programStartStop to commence shutdown.
    */
    public boolean nextRaceSleepCalculator(String marketStart) throws InterruptedException {

        LocalDateTime nextRaceDateTime = LocalDateTime.parse(marketStart, raceTimeFormatter);
        LocalDateTime localTime = LocalDateTime.now();
        Duration durationToNextRace = Duration.between(localTime, nextRaceDateTime);

        //Convert Duration to Seconds
        Long secondsToNextRace = durationToNextRace.getSeconds();

        System.out.println("Seconds to next race is: " + secondsToNextRace);
        Duration pauseCalculation = durationToNextRace.minusSeconds(secondsBeforeStart);

        Long pauseCalculationSeconds = pauseCalculation.getSeconds();
        Long pauseCalculationMillis = (pauseCalculationSeconds * 1000);
        System.out.println("pauseCalculationSeconds: " + pauseCalculationSeconds);

        //Check whether next race is probably tomorrow (usually set 6 hours for this)
        if ( pauseCalculationSeconds > manyHoursToNextRace){
            moreRacingToday = false;
        }
        //But if not, pause the program until the next race is X seconds away.
        else{
            moreRacingToday = true;
            System.out.println("Pause until the next race is " + secondsBeforeStart + " seconds away.");
            //Thread.sleep(pauseCalculationMillis);
            Thread.sleep(1);
        }

        return moreRacingToday;
    }


    public static boolean getProgramRunning() {
        return programRunning;
    }
    public void setProgramRunning(boolean programRunning) {
        this.programRunning = programRunning;
    }

    public static boolean getMoreRacingToday() {
        return moreRacingToday;
    }
    public void setMoreRacingToday(boolean moreRacingToday) {
        this.moreRacingToday = moreRacingToday;
    }

    public static String getDailyPGMStartTime() {
        return dailyPGMStartTime;
    }
    public void setDailyPGMStartTime(String dailyPGMStartTime) {
        this.dailyPGMStartTime = dailyPGMStartTime;
    }
    public static String getDailyPGMEndTime() {
        return dailyPGMEndTime;
    }
    public void setDailyPGMEndTime(String dailyPGMEndTime) {
        this.dailyPGMEndTime = dailyPGMEndTime;
    }

    public static String getStartTimeInConsole() {
        return startTimeInConsole;
    }
    public void setStartTimeInConsole(String startTimeInConsole) {
        this.startTimeInConsole = startTimeInConsole;
    }

    public static String getEndTimeInConsole() {
        return endTimeInConsole;
    }
    public void setEndTimeInConsole(String endTimeInConsole) {
        this.endTimeInConsole = endTimeInConsole;
    }


}
