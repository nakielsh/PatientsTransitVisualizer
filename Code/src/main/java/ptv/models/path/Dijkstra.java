package ptv.models.path;

import javafx.geometry.Point2D;
import ptv.models.data.*;

import java.util.*;

public class Dijkstra {

    private final Set<Hospital> visitedHospitals;

    public Dijkstra() {
        visitedHospitals = new HashSet<>();
    }

    public Hospital findNearestHospitalFromHospital(Hospital source) {
        if (source == null) {
            throw new IllegalArgumentException("Source hospital cannot be null");
        }

        visitedHospitals.add(source);
        Map<Node, Double> nodesToSolve = new HashMap<>();
        Map<Node, Double> solvedNodes = new HashMap<>();

        nodesToSolve.put(source, 0.0);

        while (nodesToSolve.size() != 0) {
            Node node = lowestNodeByDistance(nodesToSolve);
            double distance = nodesToSolve.remove(node);
            solvedNodes.put(node, distance);
            Map<Node, Distance> adjacentNodes = node.getAdjacentNodes();

            if (node instanceof Hospital) {
                Hospital hospital = (Hospital) node;
                if (!visitedHospitals.contains(hospital)) {
                    visitedHospitals.add(hospital);
                    return (Hospital) node;
                }
            }

            checkAdjacentNodes(adjacentNodes, nodesToSolve, solvedNodes, distance);
        }
        return null;
    }

    private void checkAdjacentNodes(Map<Node, Distance> adjacentNodes, Map<Node, Double> nodesToSolve, Map<Node, Double> solvedNodes, double distance) {
        for (Map.Entry<Node, Distance> connection : adjacentNodes.entrySet()) {
            Node adjacentNode = connection.getKey();
            double distanceToAdjNode = connection.getValue().getDist() + distance;

            if (!solvedNodes.containsKey(adjacentNode)) {
                if (nodesToSolve.containsKey(adjacentNode)) {
                    if (distanceToAdjNode < nodesToSolve.get(adjacentNode)) {
                        nodesToSolve.put(adjacentNode, distanceToAdjNode);
                    }
                } else {
                    nodesToSolve.put(adjacentNode, distanceToAdjNode);
                }
            }
        }
    }

    private Node lowestNodeByDistance(Map<Node, Double> nodes) {
        Node node = null;
        double minimum = Double.MAX_VALUE;
        for (Map.Entry<Node, Double> entry : nodes.entrySet()) {
            if (entry.getValue() < minimum) {
                node = entry.getKey();
                minimum = entry.getValue();
            }
        }
        return node;
    }

    public Hospital findNearestHospitalFromPatient(Patient patient, Country country) {
        if (patient == null || country == null) {
            throw new IllegalArgumentException("Patient and country arguments cannot be null");
        }

        Point2D patientCoordinates = patient.getCoordinates();
        List<Hospital> hospitals = country.getHospitalsList();
        Hospital nearestHospital = null;
        double nearestHospitalDistance = Double.MAX_VALUE;

        for (Hospital hospital : hospitals) {
            double distance = patientCoordinates.distance(hospital.getCoordinates());
            if (distance < nearestHospitalDistance) {
                nearestHospital = hospital;
                nearestHospitalDistance = distance;
            }
        }
        return nearestHospital;
    }
}