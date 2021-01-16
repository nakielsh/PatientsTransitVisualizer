package ptv.models.reader;

import javafx.geometry.Point2D;
import ptv.models.data.Patient;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class PatientsFileReader {


    private int lineNumber;
    private Map<Integer, Patient> loadedPatients;

    private final int MAXID = (int)1e9;
    private final int MAXCOORD = (int)1e9;

    public PatientsFileReader() {
        initialize();
    }

    private void initialize() {
        lineNumber = 1;
        loadedPatients = new HashMap<>();
    }

    public List<Patient> readFile(String filePath) throws IllegalArgumentException, FileNotFoundException {
        if (filePath == null || filePath.length() == 0) {
            throw new IllegalArgumentException("FilePath cannot be null or empty String");
        }

        Scanner fileScanner = new Scanner(new File(filePath));
        List<Patient> patientsList = new ArrayList<>();
        int readingType = 0;

        while (fileScanner.hasNext()) {
            String line = fileScanner.nextLine();
            String[] splittedLine = line.split("\\s+\\|\\s+");

            if (line.length() == 0) {
                lineNumber++;
                continue;
            } else if (line.charAt(0) == '#') {
                lineNumber++;
                readingType++;
                continue;
            }

            if (readingType == 0) {
                throw new IllegalArgumentException("Expected first line starting with '#' in file " + filePath);
            } else if (readingType == 1) {
                readLine(splittedLine, patientsList);
            } else {
                throw new IllegalArgumentException("More than one header starting with '#' in file " + filePath + ". Expected one header.");
            }
            lineNumber++;
        }
        if (loadedPatients.size() == 0) {
            throw new IllegalArgumentException("File does not contain any patients.");
        }

        initialize();
        return patientsList;


    }

    private void readLine(String[] line, List<Patient> patients) throws IllegalArgumentException {
        if (line.length != 3) {
            throw new IllegalArgumentException("Line: " + lineNumber + ". Wrong number of sections. Expected 3 sections.");
        }
        int id;
        double x;
        double y;

        try {
            id = Integer.parseInt("+" + line[0]);
            x = Double.parseDouble(line[1]);
            y = Double.parseDouble(line[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Line: " + lineNumber + ". Wrong data format. Should be:\n " +
                            "id - not negative integer,\n " +
                            "x and y - float number."
            );
        }

        if(id >= MAXID){
            throw new IllegalArgumentException("Line: " + lineNumber + ". Facility's id should be smaller than " + MAXID);
        }
        if(x >= MAXCOORD && y >= MAXCOORD){
            throw new IllegalArgumentException("Line: " + lineNumber + ". Facility's coordinates should be smaller than " + MAXCOORD);
        }

        if (canLoadPatient(id)) {
            Patient patient = new Patient(id, new Point2D(x, y));
            patients.add(patient);
            loadedPatients.put(id, patient);
        } else {
            throw new IllegalArgumentException("Line: " + lineNumber + ". Patient's id should be unique.");
        }
    }

    private boolean canLoadPatient(int id) {
        return !loadedPatients.containsKey(id);
    }
}
