package ex1;

import java.io.*;
import java.util.*;

/**
 * This class implements weighted_graph_algorithms interface.
 * each WGraph_Algo is initialized by a weighted_graph implemented in WGraph_DS.
 * @author Adi Dahari.
 */
public class WGraph_Algo implements weighted_graph_algorithms {
    private weighted_graph wg;

    /**
     * [Empty Constructor]
     * Initializing an empty weighted_graph by an empty constructor made in WGraph_DS.
     */
    public WGraph_Algo(){
        wg = new WGraph_DS();
    }
    /**
     * [Main Constructor]
     * Initializing a weighted_graph by a copy constructor made in WGraph_DS.
     */
    public WGraph_Algo(weighted_graph g){
        wg = new WGraph_DS(g);
    }

    /**
     * equals & hashCode() auto-generated for checking whether
     * 2 weighted_graph_algorithms objects are equal by their weighted_graph.
     * using equals method auto-generated and fixed in WGraph_DS implementation.
     * @param o = object to check against this weighted_graph_algorithms.
     * @return true - if equal.
     *         false - else
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WGraph_Algo)) return false;
        WGraph_Algo that = (WGraph_Algo) o;
        return Objects.equals(wg, that.wg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wg);
    }

    /**
     * [Copy Constructor]
     * Deep-copy method of a weighted_graph_algorithms object, uses the copy method
     * implemented in the following lines of code.
     * @param wga = weighted_graph_algorithms object to be deep copied to this weighted_graph_algorithms.
     */
    public WGraph_Algo(weighted_graph_algorithms wga){
        wg = wga.copy();
    }

    /**
     * initializing this weighted graph of the class by a given weighted_graph object.
     * @param g = weighted_graph to be initialized for the method implemented in this class.
     */
    @Override
    public void init(weighted_graph g) {
        wg = g;
    }

    /**
     * Method that helps making changes easily in the weighted_graph initialized.
     * using this method gives access to the user for making changes which actually being made
     * in the weighted_graph implementation.
     * @return weighted_graph initialized in this class.
     */
    @Override
    public weighted_graph getGraph() {
        return wg;
    }

    /**
     * Method to deep-copy the initialized weighted_graph of this class.
     * using deep-copy method made in WGraph_DS.
     * @return weighted_graph - a deep copy of the underlying graph of which this class works on.
     */
    @Override
    public weighted_graph copy() {
        return new WGraph_DS(wg);
    }

    /**
     * Method imported from ex0 - checking whether the underlying graph of this class
     * is connected or not using BFS algorithm.
     * minor changes made to fit the implementation of the weighted_graph interface,
     * as it implemented in a bit different way that "graph" interface did in ex0.
     * 2 conditions checked before applying the connection-checking algorithm:
     * if ( graph's size <= 1 ) it is connected.
     * while loop - for graphs which contain keys bigger than 0.
     * @return true - if the whole weighted_graph initialized is connected.
     *         false - else.
     */
    @Override
    public boolean isConnected() {
        if(wg.nodeSize() == 1 || wg.nodeSize() == 0) return true;
        node_info n = wg.getNode(0);
        int i = 1;
        while(n == null){
            n = wg.getNode(i);
            i++;
        }

        return DIJKSTRA.bfsConnection(wg, n);
    }

    /**
     * Method based on Dijkstra Algorithm showed in lecture.
     * returns the shortest path's weight by the tag value of the dest node.
     * for further information look in class DIJKSTRA.
     * 3 conditions checked before applying algorithm:
     * 1. size of the graph - if same key checked  returns 0.0
     *    ( a path of any node to itself weighs 0 ).
     * 2.
     * @param src - start node
     * @param dest - end (target) node
     * @return double - the lowest-weighted path from src node to dest node.
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if(wg.nodeSize() == 0 || wg.getNode(src) == null || wg.getNode(dest) == null) return -1;
        if(src == dest) return 0.0;
        List<node_info> ans = DIJKSTRA.dijkstraPath(wg, src, dest);
        if(ans == null || wg.getNode(dest).getTag() == Double.POSITIVE_INFINITY || ans.size() == 1) return -1;
        return wg.getNode(dest).getTag();
    }

    /**
     * Method based on Dijkstra Algorithm for getting an ordered list of the lowest-weighted path from src to dest.
     * this method uses the same algorithm as the shortestPathDist.
     * for further information regarding the algorithm itself - see DIJKSTRA class.
     * @param src - start node
     * @param dest - end (target) node
     * @return List<node_info> - contains the ordered path of nodes from src to dest.
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if(wg.nodeSize() == 0 || wg.getNode(src) == null || wg.getNode(dest) == null) return null;
        if(src == dest){
            List<node_info> ans = new LinkedList<>();
            ans.add(wg.getNode(src));
            return ans;
        }
        return DIJKSTRA.dijkstraPath(wg, src, dest);
    }

    /**
     * Method for saving the underlying weighted_graph initialized in this class.
     * first check is for assuring the parent folder/s of the destination path exists,
     * if don't, creating the needed folders before saving the file itself.
     * by that check i prevent any exception related to non-existed folders.
     * after checking the folders, using FileOutputStream and ObjectOutputStream methods,
     * saving the graph as it is into the destination path.
     * each exception possible declared in a catch condition, and if occurs - returns false.
     * @param file - the file name (may include a relative path).
     * @return true - if graph saved successfully.
     *         false - else.
     */
    @Override
    public boolean save(String file) {
        try{
            File dirCheck = new File(file);
            if (dirCheck.getParent() != null && !(new File(dirCheck.getParent()).exists())) {
                System.out.println("Creating missing parent folders...");
                File dirs = new File(dirCheck.getParent());
                if (!dirs.mkdirs()) {
                    System.err.println("Could not create parent folders.");
                    return false;
                }
                dirCheck.delete();
            }
            System.out.println("Saving Weighted Graph to a file...");
            FileOutputStream wgSave = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(wgSave);
            oos.writeObject(wg);
            oos.close();
            wgSave.close();
            System.out.println("File successfully saved!");
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method for loading a weighted_graph into this class, and initializing it.
     * using the FileInputStream and ObjectInputStream methods.
     * each exception possible declared in a catch condition,
     * and if occurs - returns false and no changes applied to the underlying weighted_graph initialized before.
     * @param file - name and path of the desired graph file.
     * @return true - if given file path loaded successfully.
     *         false - else.
     */
    @Override
    public boolean load(String file) {
        weighted_graph g = null;
        try{
            System.out.println("Loading Weighted Graph from file...");
            FileInputStream wgLoad = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(wgLoad);
            System.out.println("Initializing loaded graph...");
            wg = (weighted_graph) ois.readObject();
            ois.close();
            wgLoad.close();
            System.out.println("Loaded graph initialized successfully!");
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("File not found! check input path");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
