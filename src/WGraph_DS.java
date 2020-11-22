package ex1;

import org.junit.platform.engine.support.hierarchical.Node;

import java.io.Serializable;
import java.util.*;

/**
 * This class implements weighted_graph interface and Serializable ( for Save&Load Methods implemented in Graph_Algo ).
 * including Nested Class that implements node_info.
 * Nested Class Edge additionally made for storing each connection in the graph with it's weight.
 * the weighted graph is implemented by 2 HashMaps:
 * 1. graph = Main map for keeping every node in the graph by it's key.
 * 2. edges = Second map, which contain nested HashMap for every node - by it's key - and every one of it's connection,
 *    each nested map of every Node(*) in the graph contains an Edge object, referred by the key of the second node,
 *    which connected by an edge to the Node(*).
 * 3. edgeCount = an int counting the edges in graph, initialized by Zero.
 * 4. modeCount = an int counting the changes being made in the graph.
 * @author Adi Dahari
 */
public class WGraph_DS implements weighted_graph, Serializable {
                /*=========================[Private Nested classes]=========================*/
    //=====================[NodeInfo]=====================//

    /**
     * This sub-class implements node_info interface and Serializable ( for Save&Load Methods implemented in Graph_Algo ).
     * each node contains:
     * 1. a. Key = an unique key for every node in the graph, no two node can have the same key.
     *    b. count = an int initialized by Zero, for auto-generating nodes - every node made, count is increased by 1.
     * 2. Info = a String for saving information in each node.
     * 3. Tag = initialized by Positive Infinity, mainly for the Dijkstra Algorithm ( implemented in an additionally made class ).
     */
    private static class NodeInfo implements node_info, Serializable{
        private int _key;
        private static int count = 0;
        private String _info = "";
        private double _tag = Double.POSITIVE_INFINITY;

        /**
         * [Empty Constructor]
         * Initializing the node by count value (explained above).
         */
        public NodeInfo(){
            _key = count++;
        }

        /**
         * [Constructor by Key]
         * Initializing a new node by a desired Key value.
         * @param key = Value for the Key of the new node made.
         */
        public NodeInfo(int key) {
            this._key = key;
        }

        /**
         * [Copy Constructor]
         * Deep-copying a given node_info for all of it's values.
         * @param n = node_info to be deep-copied.
         */
        public NodeInfo(node_info n) {
            this._key = n.getKey();
            this._info = n.getInfo();
            this._tag = n.getTag();

        }

        /**
         * Method to get node's Key value.
         * @return int = Key value of this node.
         */
        @Override
        public int getKey() {
            return _key;
        }
        /**
         * Method to get node's Info value.
         * @return String = Info of this node.
         */
        @Override
        public String getInfo() {
            return _info;
        }

        /**
         * Method for setting this node's Info String.
         * @param s = new Info inserted to this node.
         */
        @Override
        public void setInfo(String s) {
            _info = s;
        }
        /**
         * Method to get node's Tag value.
         * @return double = Tag value of this node.
         */
        @Override
        public double getTag() {
            return _tag;
        }

        /**
         * Method for setting this node's Tag value.
         * @param t - the new value of the tag.
         */
        @Override
        public void setTag(double t) {
            _tag = t;
        }

        /**
         * Method auto-generated and fixed for hashing each node by it's key.
         * @param o = Object to check equality to this node.
         * @return true - if o is a node equal to this node.
         *         false - else.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NodeInfo)) return false;
            NodeInfo nodeInfo = (NodeInfo) o;
            return _key == nodeInfo._key;
        }

        /**
         * Method auto-generated and fixed for hashing each node by it's Key value.
         * @return int - hashed value of this node's key.
         */
        @Override
        public int hashCode() {
            return Objects.hash(_key);
        }

        /**
         * Method for comparing this node to another by their Tag value.
         * will come handy in Dijkstra implementation.
         * @param n = node to compare to.
         * @return = negative - if this node's tag is smaller than the other's.
         *           0 - if both Tags are equal.
         *           positive - if this node's tag is greater than the other's.
         */
        private int compareTo(node_info n){
            return Double.compare(_tag, n.getTag());
        }

        /**
         * toString method auto-generated with a few cosmetic changes.
         * @return String - representing this node.
         */
        @Override
        public String toString() {
            return "{ key="+_key +
                    "| info=" + _info +
                    "| tag=" + _tag +
                    " }";
        }
    }
    //=====================[End - NodeInfo]=====================//
    //=====================[Edge]=====================//

    /**
     * This class additionally made for handling weighted edges,
     * implements Serializable ( for Save&Load Methods implemented in Graph_Algo ).
     * each edge represents a path between two nodes, and every path has a "cost" - weight.
     * -Note-: undirected graph -> each edge appears twice - once in each node it connects.
     * each edge contains:
     * 1. _node = the destination node_info of this edge.
     * 2. _dst = an int representing the destination-node contained in this edge.
     * 3. _weight = an double representing this edge's weight.
     */
    private static class Edge implements Serializable{
        private node_info _node;
        private int _dst;
        private double _weight;

        /**
         * [Main Constructor]
         * Initializing a new edge by 2 parameters:
         * @param n2 = Destination node's Key of the edge being made.
         * @param w = Weight of the edge being made ( must be positive double ).
         */
        public Edge(int n2, double w){
            if(w <= 0) return;
            this._dst = n2;
            this._weight = w;
        }

        /**
         * [Copy Constructor]
         * Deep-copying an edge by all of it's values.
         * @param e = Edge to be deep-copied.
         */
        public Edge(Edge e){
            _node = new NodeInfo(e._node);
            _dst = e._dst;
            _weight = e._weight;
        }
        /**
         * Method auto-generated and fixed for hashing each edge by it's destination node's key
         * @param o = Object to check equality to this edge.
         * @return true - if o is an edge equal to this edge.
         *         false - else.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Edge)) return false;
            Edge edge = (Edge) o;
            return _dst == edge._dst &&
                    Double.compare(edge._weight, _weight) == 0 &&
                    Objects.equals(_node, edge._node);
        }
        /**
         * Method auto-generated and fixed for hashing each edge by it's destination node, it's Key value, and the edge's weight.
         * @return int - hashed value of this node's key.
         */
        @Override
        public int hashCode() {
            return Objects.hash(_node, _dst, _weight);
        }

        /**
         * toString method auto-generated with a few cosmetic changes.
         * @return String - representing this edge by it's destination node's key value and it's weight.
         */
        @Override
        public String toString() {
            return "{ dst=" + _dst +
                    "| weight=" + _weight +
                    "}";
        }
    }
    //=====================[End - Edge]=====================//
                  /*=========================[End - Private Nested classes]=========================*/

    private HashMap<Integer, node_info> graph;
    private HashMap<Integer, HashMap<Integer, Edge>> edges;
    private int edgeCount = 0;
    private int modeCount = 0;

    /**
     * [Empty Constructor]
     * initializing the graph's map and the edges map.
     */
    public WGraph_DS(){
        graph = new HashMap<>();
        edges = new HashMap<>();
    }

    /**
     * [Copy Constructor]
     * initializing a deep-copy of a given weighted_graph to this graph.
     * as each weighted graph contains 2 HashMaps: graph, edges,
     * and the edges map has a nested map inside it, it is a heavy method.
     * @Complexity Worst case - O(n^2) - each node connected to all of the others in the graph.
     * @param wg = weighted_graph to be deep-copied to this weighted graph.
     */
    public WGraph_DS(weighted_graph wg){
        graph = new HashMap<>();
        edges = new HashMap<>();
        Collection<node_info> nodes = wg.getV();
        for(node_info ni : nodes) {
            int key = ni.getKey();
            graph.put(key, ni);
            edges.put(key, new HashMap<>());
            Collection<node_info> niNeighbors = wg.getV(key);
            for(node_info niN : niNeighbors){
                int niKey = niN.getKey();
                if(edges.get(niKey) == null) edges.put(niKey, new HashMap<>());
                connect(key, niKey, wg.getEdge(key, niKey));
            }
        }
        edgeCount = wg.edgeSize();
        modeCount = wg.getMC();
    }

    /**
     * Method to return a node of the graph - if exists - by it's key.
     * @Complexity O(1) - value by key using HashMap.
     * @param key - the node_id
     * @return the node_data by the node_id, null if not exists in graph.
     */
    @Override
    public node_info getNode(int key) {
        return graph.get(key);
    }

    /**
     * Method to check whether 2 given node keys are connected by an edge.
     * @Complexity O(1) - containsKey method of the nested map.
     * @param node1 = First node Key.
     * @param node2 = Second node Key.
     * @return true - if edge exists between the 2 given nodes.
     *         false - else.
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if(!graph.containsKey(node1) || !graph.containsKey(node2)) return false;
        return edges.get(node1).containsKey(node2);
    }

    /**
     * Method to return the weight of an edge between 2 given nodes - given by their Keys, if exists.
     * @Complexity O(1) - using get method of HashMap class.
     * @param node1 = First node Key.
     * @param node2 = Second node Key.
     * @return double - weight of the desired edge, if doesn't exists returns -1.
     */
    @Override
    public double getEdge(int node1, int node2) {
        if(!graph.containsKey(node1) || !graph.containsKey(node2)) return -1;
        if(!edges.get(node1).containsKey(node2)) return -1;
        return edges.get(node1).get(node2)._weight;
    }

    /**
     * Method to add a new node to this graph by a Key, only if no such node in graph with the same key value.
     * if graph changed - modeCount increased by 1.
     * @Complexity O(1) - inserting values by key to 2 HashMaps, after checking that there isn't already such key in these,
     * and building a new node by the key given ( NodeInfo Key Constructor ).
     * @param key = key value to be initialized in a new node being added to the weighted graph.
     */
    @Override
    public void addNode(int key) {
        if(graph.containsKey(key)) return;
        NodeInfo newNode = new NodeInfo(key);
        graph.put(key, newNode);
        if(edges == null) edges = new HashMap<>();
        edges.put(key, new HashMap<>());
        modeCount++;
    }

    /**
     * Method to connect 2 nodes with a weighted edge by given parameters - 2 node Keys to connect, weight value.
     * will affect only if both nodes exists in graph.
     * if such edge exists already - updating it's weight.
     * if connection creates a new edge - edgeCount increased by 1.
     * if graph changed - modeCount increased by 1.
     * @Complexity O(1) - few simple low-cost checks, and HashMap's put method used twice.
     * @param node1 = First node Key.
     * @param node2 = Second node Key.
     * @param w = double - weight of the new edge being created.
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if(node1 == node2 || graph.get(node1) == null || graph.get(node2) == null) return;
        if(!graph.containsKey(node1) || !graph.containsKey(node2) || w < 0) return;
        if(!edges.get(node1).containsKey(node2)) edgeCount++;
        edges.get(node1).put(node2, new Edge(node2, w));
        edges.get(node2).put(node1, new Edge(node1, w));
        modeCount++;
    }

    /**
     * Method that returns a pointer to a collection containing all of graph's nodes.
     * @Complexity O(1) - shallow pointer return.
     * @return Collection<node_data> - Collection of all nodes in graph.
     */
    @Override
    public Collection<node_info> getV() {
        return graph.values();
    }

    /**
     * Method that returns all of node's neighbors, given by it's key.
     * @Complexity O(k) - as k represents the node's degree.
     * @param node_id = desired node's Key.
     * @return Collection<node_data> - Collection of all node's neighbors.
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        if(!graph.containsKey(node_id)) return null;
        Collection<node_info> ans = new ArrayList<>();
        Set<Integer> neighs = edges.get(node_id).keySet();
        for(Integer i : neighs){
            ans.add(graph.get(i));
        }
        return ans;
    }

    /**
     * Method to remove a node off the graph, given by it's Key.
     * by removing a node, all of it's edges must be deleted too.
     * for every edge being deleted - edgeCount decreased by 1.
     * if graph changed - modeCount increased by 1.
     * @Complexity Worst case - O(n) - if the node being removed is connected to every other node in graph.
     * @param key = node's Key to be removed.
     * @return the node-info removed removed off the graph ( null if no such key in graph ).
     */
    @Override
    public node_info removeNode(int key) {
        if(!graph.containsKey(key)) return null;
        node_info rNode = graph.get(key);
        Set<Integer> adj = edges.get(key).keySet();
        for(Integer i : adj){
            edges.get(i).remove(key);
            edgeCount--;
        }
        graph.remove(key);
        edges.remove(key);
        modeCount++;
        return rNode;
    }

    /**
     * Method to remove an edge off the graph, by 2 node keys, only if exists.
     * if edge deleted - edgeCount decreased by 1.
     * if graph changed - modeCount increased by 1.
     * @Complexity O(1) - HashMap remove method used twice.
     * @param node1 = First node's Key.
     * @param node2 = Second node's Key.
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if(!graph.containsKey(node1)|| !graph.containsKey(node2) || !edges.get(node1).containsKey(node2)) return;
        edges.get(node1).remove(node2);
        edges.get(node2).remove(node1);
        edgeCount--;
        modeCount++;

    }

    /**
     * Method to get this graph's size ( number of nodes in it ).
     * @Complexity O(1).
     * @return int - graph's size.
     */
    @Override
    public int nodeSize() {
        return graph.size();
    }
    /**
     * Method to get this graph's number of edges.
     * @Complexity O(1).
     * @return int - graph's edge amount.
     */
    @Override
    public int edgeSize() {
        return edgeCount;
    }
    /**
     * Method to get the number of changes made in graph.
     * @Complexity O(1).
     * @return int - changes amount.
     */
    @Override
    public int getMC() {
        return modeCount;
    }

    /**
     * toString method auto-generated with a few cosmetic changes.
     * @return String - representing the graph.
     */
    @Override
    public String toString() {
        return graph +
                "\n" + edges +
                "\n" + edgeCount +
                "\n" + modeCount;
    }

    /**
     * equals & hashCode methods auto-generated for checking whether 2 objects are equal graphs or not.
     * mainly used for Save & Load methods - to check if a loaded graph is the same as the Saved one.
     * @param o = object to be compared to this graph.
     * @return true - if equal.
     *         false - else.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WGraph_DS)) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        return edgeCount == wGraph_ds.edgeCount &&
                //modeCount == wGraph_ds.modeCount &&
                Objects.equals(graph, wGraph_ds.graph) &&
                Objects.equals(edges, wGraph_ds.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(graph, edges, edgeCount, modeCount);
    }
}

