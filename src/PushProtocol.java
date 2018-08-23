
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.*;
import java.util.*;


public class PushProtocol {

    private Map<Integer, ThreadNode> nodeGraph = new HashMap<Integer, ThreadNode>();
    private Map<Integer, Set<Integer>> timeMap = new HashMap<Integer, Set<Integer>>();
    private Map<Integer, Node> visNodeMap = new HashMap<Integer, Node>();
    private List<Integer> sequence = new ArrayList<Integer>();
    private Set<Integer> informedNodes;
    private List<Integer> allNodes;
    private Graph visGraph;
    private long totalTime;
    private Network globalNetwork;


    public long initialize(String filename, int firstKey, int p, boolean visualize){

        //init visualization


        createGraph("./resources/" + filename, p);
        allNodes = new ArrayList<Integer>(nodeGraph.keySet());

        if (visualize){
            initDisplay();
        }

        prepare(firstKey, visualize);
        push(firstKey);


        return totalTime;

    }

    private void createGraph(String filename, int p){

        try{

            FileReader file = new FileReader(filename);
            Scanner sc = new Scanner(file);

            while (sc.hasNext()){

                String nodes = sc.next();
                String[] adjacencyList = nodes.split(":");

                //create root node
                int rootName = Integer.parseInt(adjacencyList[0]);

                ThreadNode root;
                if (nodeGraph.containsKey(rootName)){
                    root = nodeGraph.get(rootName);
                }else{
                    root = new ThreadNode(rootName, p);
                    nodeGraph.put(rootName, root);
                }

                String[] neighborName = adjacencyList[1].split(",");
                List<ThreadNode> neighbors = root.neighbors;

                for (String n : neighborName){

                    int neighborKey = Integer.parseInt(n);
                    ThreadNode singleNeighbor;
                    if (nodeGraph.containsKey(neighborKey)){
                        singleNeighbor = nodeGraph.get(neighborKey);
                    }else{
                        singleNeighbor = new ThreadNode(neighborKey, p);
                        nodeGraph.put(neighborKey, singleNeighbor);
                    }
                    neighbors.add(singleNeighbor);
                }
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void initDisplay(){

        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        visGraph = new SingleGraph("real");
        visGraph.display();
        visGraph.addAttribute("ui.antialias");
        visGraph.addAttribute("stylesheet", "graph {padding : 50px;}"
                + "node {size: 20px; fill-mode: plain;}"
                + "node.red {fill-color: red;}"
                + "node.blue {fill-color: blue;}"
                + "node.green {fill-color: green;}");

        //put nodes in
        for (int i : allNodes){
            Node n = visGraph.addNode(Integer.toString(i));
            visNodeMap.put(i, n);
            n.setAttribute("ui.class", "red");
        }

        //put edges in
        for (int k : allNodes){
            ThreadNode rootNode = nodeGraph.get(k);
            for (ThreadNode j : rootNode.neighbors){
                int val = j.value;

                if (k < val){
                    String nodeA = Integer.toString(k);
                    String nodeB = Integer.toString(val);
                    visGraph.addEdge(nodeA + nodeB, nodeA, nodeB);
                }
            }
        }

    }

    private void prepare(int firstKey, boolean visualize){

        informedNodes = new HashSet<Integer>();
        int numberOfNodes = allNodes.size();
        globalNetwork = new Network(informedNodes, visNodeMap, sequence, visualize);

        //pass data to nodes
        for (Integer k : nodeGraph.keySet()){
            ThreadNode currentNode = nodeGraph.get(k);
            currentNode.initData(numberOfNodes, informedNodes, timeMap, globalNetwork);
        }

        //get and inform the first Node
        ThreadNode firstNode = nodeGraph.get(firstKey);
        firstNode.inform();
        informedNodes.add(firstKey);
        sequence.add(firstKey);

        //put the time 0 and first node into timeMap
        Set<Integer> timeZero = new HashSet<Integer>();
        timeZero.add(firstKey);
        timeMap.put(0, timeZero);

        //change the color of first node
        if (visualize){
            Node firstVisNode = visNodeMap.get(firstKey);
            firstVisNode.setAttribute("ui.class", "green");
        }

    }

    private void push(int firstKey){

        int numberOfNodes = allNodes.size();

        //start spreading
        long startTime = System.nanoTime();

        for (Integer k : nodeGraph.keySet()){
            ThreadNode startNode = nodeGraph.get(k);
            startNode.start();
        }

        globalNetwork.start();

        //check if terminates or not
        while (informedNodes.size() != numberOfNodes){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.nanoTime();
        totalTime = endTime - startTime;

        //print result
        System.out.println("-----done-----");

        try {
            File file = new File("log/output.txt");
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            for (int i : sequence) {
                bw.write(Integer.toString(i));
                bw.newLine();
            }

            bw.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        }

    }

}
