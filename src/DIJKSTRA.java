package ex1;

import java.util.*;
//implement of a Comparator of the type "node_info" to use in priority queue of Dijkstra.
class TagComparator implements Comparator<node_info>{

    @Override
    public int compare(node_info o1, node_info o2) {
        return Double.compare(o1.getTag(), o2.getTag());
    }
}
/**
 * this class is an additional class for Dijkstra Algorithm methods used in Graph_Algo,
 * and a BFS-based connection check genuinely made for ex0.
 * also implemented a Comparator for comparing each 2 node_infos by their tag value - package friendly.
 * 2 algorithms:
 *  dijkstraPath.
 *  bfsConnection.
 * @author Adi Dahari.
 */
public class DIJKSTRA {
    //Simple method that initialize all tag values in graph to Infinity.
    private static void tagInit(weighted_graph g){
        for(node_info n : g.getV()){
            n.setTag(Double.POSITIVE_INFINITY);
        }
    }
    //===============Methods===============//

    /**
     * Method based on Dijkstra Algorithm for finding the lowest-weighted path between every 2 nodes in the graph.
     * as the basic conditions checked in the main Algorithms class - WGraph_Algo,
     * this method gets an existing graph and 2 nodes keys, both in graph.
     * start nodes initialized by a Tag value 0.0 as its distance from himself is 0.
     * start node's parent initialized as null for breaking the reverse List creation of the path.
     * each checked nodes Tag value is set according to its parents Tag value and the edge connecting them.
     * this method implemented in a way which assures that every node gets the lowest-possible Tag value,
     * representing its path-weight from the start node.
     * Data structure used:
     * 1. visited = Boolean HashMap for marking each visited node by it's key.
     * 2. parents = node_info HashMap for saving each node's parent in the lowest-weighted path.
     * 3. pq = PriorityQueue implemented by the TagComparator made before, for the most efficient complexity.
     * 4. path = LinkedList containing the ordered path from start node to end node.
     *    this is the returned List<node_info>.
     * @Complexity O(V+E*logV) - as V stands for number of nodes, and E for number of edges.
     * @param g = weighted_graph to apply this method on.
     * @param start = start node's Key value.
     * @param end = end node's Key value.
     * @return List<node_info> - the ordered path of node_info from start to end node.
     */
    public static  List<node_info> dijkstraPath(weighted_graph g, int start, int end) {
        tagInit(g); //initializing all tag values to Infinity to prevent previous calls of the method affect this call.
        TagComparator CompareByTag = new TagComparator(); //declaring a new comparator as a new TagComparator.
        HashMap<Integer, Boolean> visited = new HashMap<>(); //for marking visited nodes.
        HashMap<Integer, node_info> parents = new HashMap<>(); //for saving each node's parent in path.
        PriorityQueue<node_info> pq = new PriorityQueue<node_info>(20, CompareByTag); //for a logical iteration over the nodes visited.
        node_info nStart = g.getNode(start);
        visited.put(start, true); /*======================*/
        nStart.setTag(0.0);       /*  Taking care of the  */
        pq.add(nStart);           /*     start node       */
        parents.put(start, null); /*======================*/
        while (!pq.isEmpty()) { //this is the main while loop being used for handling all of the nodes in graph.
            node_info n = pq.poll();
            Collection<node_info> adjList = g.getV(n.getKey());
            for (node_info nNi : adjList) { //this for-each is handling the current node's neighbors.
                double currTag = n.getTag() + g.getEdge(n.getKey(), nNi.getKey()); //this is the weight currently appears.
                if(!visited.containsKey(nNi.getKey())){
                    nNi.setTag(currTag); //setting the current tag value to the currently visited node.
                    pq.add(nNi); //if node hasn't been visited - adding it to the queue.
                    visited.put(nNi.getKey(), true); // marking the node as visited.
                    parents.put(nNi.getKey(), n);    // putting the node's parent in the hashmap.
                }
                if (currTag < nNi.getTag()) { //this if checks if the current weight of the path is lower than the existing one,
                                              // if so - updates the parent and tag of the node to the better one.
                    nNi.setTag(currTag);
                    parents.replace(nNi.getKey(), n); // if path is different, parent is different. replacing the existing one.
                    if(!pq.contains(nNi)) pq.add(nNi); // for not adding the same node twice to the pq.
                }
            }
        }
        /* Going backwards using the parents map and initializing the final path List of nodes.*/
        if(!visited.containsKey(end)) return null;
        LinkedList<node_info> path = new LinkedList<>();
        node_info dest = g.getNode(end);
        if(dest.getTag() == Double.POSITIVE_INFINITY) return null; // if the dests tag is infinity - nodes aren't connected - no path - null.
        while(dest != null){ // as start nodes parent initialized as null - stopping when finishing path creation.

            path.addFirst(dest); // as we going backwards on the path - every node added first to the list.
            dest = parents.get(dest.getKey()); //works like a "--" for ints.
        }
        return path; // returns the final LinkedList of nodes in the right order.
    }


    /**
     * a BFS'd based method for checking whether a given graph is connected or not,
     * using a LinkedList - as Queue and a boolean map for marking visited nodes.
     * using a very similar idea to the bfsPath execution, without an end node.
     * for further info about the idea look at ex0.
     * @Complexity O(V+E) - V = number of nodes in graph, E = number of edges in graph.
     * @param g - graph_algorithms implemented by Graph_Algo
     * @param start - a start node for the BFS method
     * @return if graph is connected - true. else - false
     */
    public static boolean bfsConnection(weighted_graph g, node_info start) {
        LinkedList<node_info> q = new LinkedList<>();
        HashMap<Integer, Boolean> visited = new HashMap<>();
        q.add(start);
        visited.put(start.getKey(), true);
        while (!q.isEmpty()) {
            node_info n = q.poll();
            for (node_info edge : g.getV(n.getKey())) {
                if (visited.containsKey(edge.getKey()) && !visited.get(edge.getKey())) {
                    q.add(edge);
                    visited.put(edge.getKey(), true);
                } else if (visited.get(edge.getKey()) == null || !visited.get(edge.getKey())) {
                    visited.put(edge.getKey(), true);
                    q.add(edge);
                }
            }
        }
        return visited.size() == g.nodeSize();  //checking if the visited map size equals to number of nodes in graph
        //if is - the whole graph is connected
    }

}
