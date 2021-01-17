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

public class CountryFileReader {

    private final int MAXID = 1000;
    private final int MAXCOORD = (int) 1e9;
    private final int MINCOORD = (int) -1e9;

    private int lineNumber;
    private int readingType;
    private String filePath;

    private Hospital[] loadedHospitalsById;
    private Map<String, Hospital> loadedHospitalsByName;
    private Facility[] loadedFacilitiesById;
    private Map<String, Facility> loadedFacilitiesByName;
    private boolean[] loadedDistancesById;

    private void initialize() {
        lineNumber = 1;
        readingType = 0;
        loadedHospitalsById = new Hospital[MAXID];
        loadedFacilitiesById = new Facility[MAXID];
        loadedDistancesById = new boolean[MAXID];
        loadedHospitalsByName = new HashMap<>();
        loadedFacilitiesByName = new HashMap<>();
    }

    public Country readFile(String filePath) throws FileNotFoundException, IllegalArgumentException {
        if (filePath == null || filePath.length() == 0) {
            throw new IllegalArgumentException("FilePath cannot be null or empty String");
        }

        initialize();

        this.filePath = filePath;
        Scanner fileScanner = new Scanner(new File(filePath));
        Country country = new Country();

        while (fileScanner.hasNext()) {
            readLine(fileScanner.nextLine(), country);
            lineNumber++;
        }

        if (loadedHospitalsByName.size() == 0) {
            throw new IllegalArgumentException("File does not contain any hospital");
        }

        initialize();
        return country;
    }

    private void readLine(String line, Country country) {
        String[] splittedLine = line.split("\\s+\\|\\s+");

        if (line.length() == 0) {
            return;
        } else if (line.charAt(0) == '#') {
            readingType++;
        } else {
            readSplittedLine(splittedLine, country);
        }
    }

    private void readSplittedLine(String[] splittedLine, Country country) {
        if (readingType == 0) {
            throw new IllegalArgumentException("Expected first line starting with '#' in file " + filePath);
        } else if (readingType == 1) {
            readHospital(splittedLine, country);
        } else if (readingType == 2) {
            readFacility(splittedLine, country);
        } else if (readingType == 3) {
            readDistance(splittedLine, country);
        } else {
            throw new IllegalArgumentException("Too much lines starting with '#' in file " + filePath + ". Expected 3 lines");
        }
    }

    private void readHospital(String[] line, Country country) throws IllegalArgumentException {
        if (line.length != 6) {
            throw new IllegalArgumentException("Line: " + lineNumber + ". Wrong number of sections. Expected 6 sections");
        }

        String name = line[1];

        try {
            int id = Integer.parseInt("+" + line[0]);
            double x = Double.parseDouble(line[2]);
            double y = Double.parseDouble(line[3]);
            int beds = Integer.parseInt("+" + line[4]);
            int freeBeds = Integer.parseInt("+" + line[5]);
            readHospital(id, x, y, beds, freeBeds, name, country);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Line: " + lineNumber + ". Wrong data format. Should be:\n" +
                            "id, number of beds, number of free beds - not negative integer,\n" +
                            "x and y - float number"
            );
        }
    }

    private void readHospital(int id, double x, double y, int beds, int freeBeds, String name, Country country) {
        if (id >= MAXID) {
            throw new IllegalArgumentException("Line: " + lineNumber + ". Hospital's id should be smaller than " + MAXID);
        }
        if (x >= MAXCOORD || y >= MAXCOORD) {
            throw new IllegalArgumentException("Line: " + lineNumber + ". Hospital's coordinates should be smaller than " + MAXCOORD);
        }
        if (x <= MINCOORD || y <= MINCOORD) {
            throw new IllegalArgumentException("Line: " + lineNumber + ". Hospital's coordinates should be greater than " + MINCOORD);
        }

        if (freeBeds > beds) {
            throw new IllegalArgumentException("Line: " + lineNumber + ". Number of available beds cannot be bigger than total number of all beds");
        }

        if (canLoadHospital(id, name)) {
            Hospital hospital = new Hospital(id, name, new Point2D(x, y), beds, freeBeds);
            country.addHospital(hospital);
            loadedHospitalsById[id] = hospital;
            loadedHospitalsByName.put(name, hospital);
        } else {
            throw new IllegalArgumentException("Line: " + lineNumber + ". Hospital's id and name should be unique");
        }
    }

    private void readFacility(String[] line, Country country) throws IllegalArgumentException {
        if (line.length != 4) {
            throw new IllegalArgumentException("Line: " + lineNumber + ". Wrong number of sections. Expected 4 sections");
        }

        String name = line[1];

        try {
            int id = Integer.parseInt("+" + line[0]);
            double x = Double.parseDouble(line[2]);
            double y = Double.parseDouble(line[3]);
            readFacility(id, x, y, name, country);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Line: " + lineNumber + ". Wrong data format. Should be:\n" +
                            "id - not negative integer,\n" +
                            "x and y - float number"
            );
        }
    }

    private void readFacility(int id, double x, double y, String name, Country country) {
        if (id >= MAXID) {
            throw new IllegalArgumentException("Line: " + lineNumber + ". Facility's id should be smaller than " + MAXID);
        }
        if (x >= MAXCOORD && y >= MAXCOORD) {
            throw new IllegalArgumentException("Line: " + lineNumber + ". Facility's coordinates should be smaller than " + MAXCOORD);
        }
        if (x <= MINCOORD || y <= MINCOORD) {
            throw new IllegalArgumentException("Line: " + lineNumber + ". Facility's coordinates should be greater than " + MINCOORD);
        }

        if (canLoadFacility(id, name)) {
            Facility facility = new Facility(id, name, new Point2D(x, y));
            country.addFacility(facility);
            loadedFacilitiesById[id] = facility;
            loadedFacilitiesByName.put(name, facility);
        } else {
            throw new IllegalArgumentException("Line: " + lineNumber + ". Facility's id and name should be unique");
        }
    }

    private void readDistance(String[] line, Country country) throws IllegalArgumentException {
        if (line.length != 4) {
            throw new IllegalArgumentException("Line: " + lineNumber + ". Wrong number of sections. Expected 4 sections");
        }

        try {
            int id = Integer.parseInt("+" + line[0]);
            int hospitalId1 = Integer.parseInt("+" + line[1]);
            int hospitalId2 = Integer.parseInt("+" + line[2]);
            double duration = Double.parseDouble("+" + line[3]);
            readDistance(id, hospitalId1, hospitalId2, duration, country);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Line: " + lineNumber + ". Wrong data format. Should be:\n" +
                            "id and hospital's id - not negative integer,\n" +
                            "duration - not negative float number"
            );
        }
    }

    private void readDistance(int id, int hospitalId1, int hospitalId2, double duration, Country country) {
        if (id >= MAXID) {
            throw new IllegalArgumentException("Line: " + lineNumber + ". Distance's id should be smaller than " + MAXID);
        }

        if (hospitalId1 >= MAXID && hospitalId2 >= MAXID) {
            throw new IllegalArgumentException("Line: " + lineNumber + ". Hospital's id should be smaller than " + MAXID);
        }

        if (hospitalId1 == hospitalId2) {
            throw new IllegalArgumentException("Line: " + lineNumber + ". Road cannot lead to the same hospital.");
        }

        if (canLoadDistance(id, hospitalId1, hospitalId2)) {
            Hospital h1 = loadedHospitalsById[hospitalId1];
            Hospital h2 = loadedHospitalsById[hospitalId2];
            Distance d = new Distance(id, h1, h2, duration);
            h1.addNode(h2, d);
            h2.addNode(h1, d);
            country.addDistance(d);
            loadedDistancesById[id] = true;
        } else {
            throw new IllegalArgumentException("Line: " + lineNumber + ". Distance's id should be unique. Hospital's id should be real");
        }
    }

    private boolean canLoadHospital(int id, String name) {
        return loadedHospitalsById[id] == null && !loadedHospitalsByName.containsKey(name);
    }

    private boolean canLoadFacility(int id, String name) {
        return loadedFacilitiesById[id] == null && !loadedFacilitiesByName.containsKey(name);
    }

    private boolean canLoadDistance(int id, int hospitalId1, int hospitalId2) {
        return loadedHospitalsById[hospitalId1] != null && loadedHospitalsById[hospitalId2] != null && !loadedDistancesById[id];
    }

}
