package ptv.models.reader;

import ptv.models.data.Distance;
import ptv.models.data.Facility;
import ptv.models.data.Hospital;
import ptv.models.data.Country;

import javafx.geometry.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MapFileReader {

    private final int MAXID = 1000;

    private int lineNumber;
    private Hospital[] loadedHospitalsById;
    private Map<String, Hospital> loadedHospitalsByName;
    private Facility[] loadedFacilitiesById;
    private Map<String, Facility> loadedFacilitiesByName;
    private boolean[] loadedDistancesById;


    public MapFileReader(){
        initialize();
    }

    private void initialize(){
        lineNumber = 1;
        loadedHospitalsById = new Hospital[MAXID];
        loadedFacilitiesById = new Facility[MAXID];
        loadedDistancesById = new boolean[MAXID];
        loadedHospitalsByName = new HashMap<>();
        loadedFacilitiesByName = new HashMap<>();
    }

    public Country readFile(String filePath) throws FileNotFoundException, IllegalArgumentException{
        if (filePath == null || filePath.length() == 0){
            throw new IllegalArgumentException("FilePath cannot be null or empty String");
        }

        Scanner fileScanner = new Scanner(new File(filePath));
        Country country = new Country();
        int readingType = 0;

        while(fileScanner.hasNext()){
            String line = fileScanner.nextLine();
            String[] splittedLine = line.split("\\s+\\|\\s+");

            if(line.length() == 0){
                lineNumber++;
                continue;
            }
            else if(line.charAt(0) == '#'){
                readingType++;
                lineNumber++;
                continue;
            }

            if(readingType == 0){
                throw new IllegalArgumentException("Expected first line starting with '#' in file " + filePath);
            }
            else if(readingType == 1){
                readHospital(splittedLine, country);
            }
            else if(readingType == 2){
                readFacility(splittedLine, country);
            }
            else if(readingType == 3){
                readDistance(splittedLine);
            }
            else {
                throw new IllegalArgumentException("Too much lines starting with '#' in file " + filePath + ". Expected 3 lines");
            }
            lineNumber++;
        }
        if(loadedHospitalsByName.size() == 0){
            throw new IllegalArgumentException("File does not contain any hospital");
        }

        initialize();
        return country;
    }

    private void readHospital(String[] line, Country country){
        if(line.length != 6){
            throw new IllegalArgumentException("Line: " + lineNumber + ". Wrong number of sections. Expected 6 sections");
        }

        int id;
        String name = line[1];
        double x;
        double y;
        int beds;
        int freeBeds;

        try {
            id = Integer.parseInt("+" + line[0]);
            x = Double.parseDouble(line[2]);
            y = Double.parseDouble(line[3]);
            beds = Integer.parseInt("+" + line[4]);
            freeBeds = Integer.parseInt("+" + line[5]);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException(
                    "Line: " + lineNumber + ". Wrong data format. Should be:\n" +
                            "id, number of beds, number of free beds - not negative integer,\n" +
                            "x and y - float number"
            );
        }

        if(id >= MAXID){
            throw new IllegalArgumentException("Line: " + lineNumber + ". Hospital's id should be smaller than " + MAXID);
        }

        if(freeBeds > beds){
            throw new IllegalArgumentException("Line: " + lineNumber + ". Number of available beds cannot be bigger than total number of all beds");
        }

        if(canLoadHospital(id, name)){
            Hospital hospital = new Hospital(id, name, new Point2D(x, y), beds, freeBeds);
            country.addHospital(hospital);
            loadedHospitalsById[id] = hospital;
            loadedHospitalsByName.put(name, hospital);
        }
        else{
            throw new IllegalArgumentException("Line: " + lineNumber + ". Hospital's id and name should be unique");
        }
    }

    private void readFacility(String[] line, Country country){
        if(line.length != 4){
            throw new IllegalArgumentException("Line: " + lineNumber + ". Wrong number of sections. Expected 4 sections");
        }

        int id;
        String name = line[1];
        double x;
        double y;

        try {
            id = Integer.parseInt("+" + line[0]);
            x = Double.parseDouble(line[2]);
            y = Double.parseDouble(line[3]);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException(
                    "Line: " + lineNumber + ". Wrong data format. Should be:\n" +
                            "id - not negative integer,\n" +
                            "x and y - float number"
            );
        }

        if(id >= MAXID){
            throw new IllegalArgumentException("Line: " + lineNumber + ". Facility's id should be smaller than " + MAXID);
        }

        if(canLoadFacility(id, name)){
            Facility facility = new Facility(id, name, new Point2D(x, y));
            country.addFacility(facility);
            loadedFacilitiesById[id] = facility;
            loadedFacilitiesByName.put(name, facility);
        }
        else{
            throw new IllegalArgumentException("Line: " + lineNumber + ". Facility's id and name should be unique");
        }
    }

    private void readDistance(String[] line){
        if(line.length != 4){
            throw new IllegalArgumentException("Line: " + lineNumber + ". Wrong number of sections. Expected 4 sections");
        }

        int id;
        int hospitalId1;
        int hospitalId2;
        double duration;

        try {
            id = Integer.parseInt("+" + line[0]);
            hospitalId1 = Integer.parseInt("+" + line[1]);
            hospitalId2 = Integer.parseInt("+" + line[2]);
            duration = Double.parseDouble("+" + line[3]);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException(
                    "Line: " + lineNumber + ". Wrong data format. Should be:\n" +
                            "id and hospital's id - not negative integer,\n" +
                            "duration - not negative float number"
            );
        }

        if(id >= MAXID){
            throw new IllegalArgumentException("Line: " + lineNumber + ". Distance's id should be smaller than " + MAXID);
        }

        if(hospitalId1 == hospitalId2){
            throw new IllegalArgumentException("Line: " + lineNumber + ". Road cannot lead to the same hospital.");
        }

        if(canLoadDistance(id, hospitalId1, hospitalId2)){
            loadedHospitalsById[hospitalId1].addDistance(new Distance(id, loadedHospitalsById[hospitalId1], loadedHospitalsById[hospitalId2], duration));
            loadedHospitalsById[hospitalId2].addDistance(new Distance(id, loadedHospitalsById[hospitalId2], loadedHospitalsById[hospitalId1], duration));
            loadedDistancesById[id] = true;
        }
        else{
            throw new IllegalArgumentException("Line: " + lineNumber + ". Distance's id should be unique. Hospital's id should be real");
        }
    }

    private boolean canLoadHospital(int id, String name){
        return loadedHospitalsById[id] == null && !loadedHospitalsByName.containsKey(name);
    }

    private boolean canLoadFacility(int id, String name){
        return loadedFacilitiesById[id] == null && !loadedFacilitiesByName.containsKey(name);
    }

    private boolean canLoadDistance(int id, int hospitalId1, int hospitalId2){
        return loadedHospitalsById[hospitalId1] != null && loadedHospitalsById[hospitalId2] != null && !loadedDistancesById[id];
    }

}
