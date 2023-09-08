package alexpetmecky.contraption.misc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

class Graph {
    private final Map<String, List<Edge>> graph = new HashMap<>();
    public Graph(String fileName){
        readCSV(fileName);
    }

    private void addEdge(String source, String target, int weight) {
        List<Edge> edges = graph.getOrDefault(source, new ArrayList<>());
        edges.add(new Edge(target, weight));
        graph.put(source, edges);
    }

    private void readCSV(String fileName) {
        // Implement the logic to read the CSV file and add edges to the graph
        String line = "";
        String splitBy = ";";
        try{
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            int index=0;
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                if(index !=0){
                    String[] lineSplit = line.split(splitBy);    // use ; as separator
                    //WeightedNode tempNode = new WeightedNode(lineSplit[0],lineSplit[1],Integer.parseInt(lineSplit[2]));
                    //System.out.println("Employee [First Name=" + employee[0] + ", Last Name=" + employee[1] + ", Designation=" + employee[2] + ", Contact=" + employee[3] + ", Salary= " + employee[4] + ", City= " + employee[5] +"]");
                    //retVal.add(tempNode);
                    addEdge(lineSplit[0],lineSplit[1],Integer.parseInt(lineSplit[2]));

                }
                index+=1;

            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    List<String> shortestPath(String start, String end) {
        System.out.println("Running");
        Map<String, Integer> distance = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<Node> queue = new PriorityQueue<>();

        distance.put(start, 0);
        queue.offer(new Node(start, 0));
        System.out.println("Initial Queue... "+queue.size());
        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (visited.contains(current.vertex)) {
                continue;
            }

            visited.add(current.vertex);

            if (current.vertex.equals(end)) {
                break;
            }

            for (Edge edge : graph.getOrDefault(current.vertex, new ArrayList<>())) {
                int newDistance = distance.get(current.vertex) + edge.weight;
                if (!distance.containsKey(edge.target) || newDistance < distance.get(edge.target)) {
                    distance.put(edge.target, newDistance);
                    previous.put(edge.target, current.vertex);
                    queue.offer(new Node(edge.target, newDistance));
                }
            }
        }

        if (!previous.containsKey(end)) {
            return Collections.emptyList();
        }

        List<String> path = new ArrayList<>();
        String current = end;
        while (!current.equals(start)) {
            path.add(0, current);
            current = previous.get(current);
        }
        path.add(0, start);
        System.out.println("FINISHING...");
        System.out.println(path.size());
        for(String item:path){
            System.out.println(item);
        }
        return path;
    }

    private static class Edge {
        private final String target;
        private final int weight;

        private Edge(String target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    private static class Node implements Comparable<Node> {
        private final String vertex;
        private final int distance;

        private Node(String vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(distance, other.distance);
        }
    }
}

