package ptv.models.path;

import ptv.models.data.Distance;
import ptv.models.data.Hospital;
import ptv.models.data.Node;
import ptv.models.data.Patient;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Dijkstra {

    public Hospital findNearestHospitalWithAvailableBeds(Hospital source){
        Map<Node, Double> nodesToSolve = new HashMap<>();
        Map<Node, Double> solvedNodes = new HashMap<>();

        nodesToSolve.put(source, 0.0);

        while(nodesToSolve.size() != 0){
            Node node = lowestNodeByDistance(nodesToSolve);
            double distance = nodesToSolve.remove(node);
            solvedNodes.put(node, distance);
            Map<Node, Distance> adjacentNodes = node.getAdjacentNodes();

            if(isHospitalWithAvailableBeds(node)){
                return (Hospital)node;
            }

            for(Map.Entry<Node, Distance> connection: adjacentNodes.entrySet()){
                Node adjacentNode = connection.getKey();
                double distanceToAdjNode = connection.getValue().getDist() + distance;

                if(!solvedNodes.containsKey(adjacentNode)){
                    if(nodesToSolve.containsKey(adjacentNode)){
                        if(distanceToAdjNode < nodesToSolve.get(adjacentNode)){
                            nodesToSolve.put(adjacentNode, distanceToAdjNode);
                        }
                    }
                    else {
                        nodesToSolve.put(adjacentNode, distanceToAdjNode);
                    }
                }
            }
        }
        return null;
    }

    private Node lowestNodeByDistance(Map<Node, Double> nodes){
        Node node = null;
        double minimum = Double.MAX_VALUE;
        for(Map.Entry<Node, Double> entry: nodes.entrySet()){
            if(entry.getValue() < minimum){
                node = entry.getKey();
                minimum = entry.getValue();
            }
        }
        return node;
    }

    private boolean isHospitalWithAvailableBeds(Node node){
        if(node instanceof Hospital && ((Hospital)node).getAvailableBeds() > 0){
            return true;
        }
        return false;
    }


    public Graph calculateShortestPathFromHospital(Graph graph, Hospital hospital){
        return null;
    }

    public Graph calculateShortestPathFromPatient(Graph graph, Patient patient){
        return null;
    }

    private void calculateMinimumDistance(Hospital fromHospital, Hospital toHospital, Integer edgeWeigh){}

    private Hospital getLowestDistanceHospital(Set<Hospital> unsettledHospitals){
        return null;
    }

}
