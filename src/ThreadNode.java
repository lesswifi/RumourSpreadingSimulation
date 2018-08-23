import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;

public class ThreadNode extends Thread {

    public int value;
    public List<ThreadNode> neighbors = new ArrayList<ThreadNode>();
    public boolean informed = false;
    public int localTime = 1;
    public int dropProbability;

    public int totalNodes;
    public Set<Integer> informedNodes;
    public Map<Integer, Set<Integer>> timeMap;
    public Network globalNetwork;


    public ThreadNode(int value, int p){
        this.value = value;
        this.dropProbability = p;
    }

    public void initData(int totalNodes, Set<Integer> informedNodes, Map<Integer,
            Set<Integer>> timeMap, Network globalNetwork){
        this.totalNodes = totalNodes;
        this.informedNodes = informedNodes;
        this.timeMap = timeMap;
        this.globalNetwork = globalNetwork;
    }


    public void inform(){
        this.informed = true;
    }


    @Override
    public void run() {

        // Loop for ten iterations.

        while (informedNodes.size() != totalNodes){

            if (this.informed){

                Random rand = new Random();
                int r1 = rand.nextInt(100);

                if (r1 > dropProbability){

                    int numOfNeighbors = neighbors.size();

                    //get random node
                    int randomIndex = rand.nextInt(numOfNeighbors);
                    ThreadNode selectedNeighbor = neighbors.get(randomIndex);

                    //inform new node

                    globalNetwork.send(selectedNeighbor);

                    //add to timemap
                    if (!timeMap.containsKey(localTime)){
                        timeMap.put(localTime, new HashSet<Integer>());
                    }
                }
            }

            localTime ++;

            //Delay between message to attempt the next transmission
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {

            }

        }
    }

}
