import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;


public class Network extends Thread{

    public Set<Integer> informedNodes;
    public Map<Integer, Node> visNodeMap;
    public List<Integer> sequence;
    public boolean visualize;


    public Network(Set<Integer> informedNodes, Map<Integer, Node> visNodeMap, List<Integer> sequence, boolean visualize){

        this.informedNodes = informedNodes;
        this.visNodeMap = visNodeMap;
        this.sequence = sequence;
        this.visualize = visualize;
    }


    public void send(ThreadNode endNode){

        Random rand = new Random();
        int r = rand.nextInt(200);

        Timer timer = new Timer();
        timer.schedule(new SendTask(endNode), r + 900);
    }

    class SendTask extends TimerTask {

        ThreadNode selectedNeighbor;

        SendTask(ThreadNode endNode){
            this.selectedNeighbor = endNode;
        }

        @Override
        public void run() {

            selectedNeighbor.inform();

            //add to informedNodes and display graph
            if (! informedNodes.contains(selectedNeighbor.value)){

                if (visualize){
                    Node visNode = visNodeMap.get(selectedNeighbor.value);
                    visNode.setAttribute("ui.class", "green");
                }

                informedNodes.add(selectedNeighbor.value);
                sequence.add(selectedNeighbor.value);
            }


        }
    }

}
