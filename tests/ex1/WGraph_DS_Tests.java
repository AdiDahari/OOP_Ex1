package ex1;

import ex1.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


public class WGraph_DS_Tests {
    private static Random _rnd = null;

    public static weighted_graph graph_creator(int v_size, int e_size, int seed) {
        weighted_graph g = new WGraph_DS();
        _rnd = new Random(seed);
        for (int i = 0; i < v_size; i++) {
            g.addNode(i);
        }
        // Iterator<node_data> itr = V.iterator(); // Iterator is a more elegant and generic way, but KIS is more important
        int[] nodes = nodes(g);
        while (g.edgeSize() < e_size) {
            int a = nextRnd(0, v_size);
            int b = nextRnd(0, v_size);
            int i = nodes[a];
            int j = nodes[b];
            double w = _rnd.nextDouble();
            g.connect(i, j, w);
        }
        return g;
    }

    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0 + min, (double) max);
        int ans = (int) v;
        return ans;
    }

    private static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max - min;
        double ans = d * dx + min;
        return ans;
    }

    /**
     * Simple method for returning an array with all the node_data of the graph,
     * Note: this should be using an Iterator<node_edge> to be fixed in Ex1
     *
     * @param g
     * @return
     */
    private static int[] nodes(weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_info> V = g.getV();
        node_info[] nodes = new node_info[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for (int i = 0; i < size; i++) {
            ans[i] = nodes[i].getKey();
        }
        Arrays.sort(ans);
        return ans;
    }

    public weighted_graph wgraphCreator(int v_size, int e_size) {
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < v_size; i++) {
            g.addNode(i);
        }
        for (int i = 0; i < e_size; i++) {
            g.connect(i, i + 1, 1);
        }
        return g;
    }

    public weighted_graph wgraphCreator(int v_size, int e_size, double e_weight) {
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < v_size; i++) {
            g.addNode(i);
        }
        for (int i = 0; i < e_size; i++) {
            g.connect(i, i + 1, e_weight);
        }
        return g;
    }

    public weighted_graph wgraphCreatorBig(int v_size, int e_size, double e_weight) {
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < v_size; i++) {
            g.addNode(i);
        }
        for (int i = 0; i < e_size; i++) {
            g.connect(i, i + 1, e_weight);
            g.connect(i, i + 2, e_weight);
            g.connect(i, i + 3, e_weight);
            g.connect(i, i + 4, e_weight);
            g.connect(i, i + 5, e_weight);
            g.connect(i, i + 6, e_weight);
            g.connect(i, i + 7, e_weight);
            g.connect(i, i + 8, e_weight);
            g.connect(i, i + 9, e_weight);
            g.connect(i, i + 10, e_weight);
        }
        return g;
    }

    @Test
    void basicWGrapgTest() {
        weighted_graph wg = wgraphCreator(1, 0);
        assertEquals(wg.nodeSize(), 1);
        assertEquals(wg.edgeSize(), 0);
        wg.addNode(1);
        wg.connect(0, 1, 1);
        assertEquals(wg.edgeSize(), 1);
        assertEquals(wg.nodeSize(), 2);
    }

    @Test
    void WGraph_AlgoTest1() {
        weighted_graph wg = wgraphCreator(100, 0);
        for (int i = 0; i < wg.nodeSize() - 1; i++) {
            wg.connect(i, i + 1, 1);
        }
        weighted_graph_algorithms wga = new WGraph_Algo();
        wga.init(wg);
        assertTrue(wga.isConnected());
        assertEquals(99, wga.shortestPathDist(0, 99));
        wg.connect(97, 99, 1);
        wga.init(wg);
        assertEquals(98, wga.shortestPathDist(0, 99));
        wg.connect(0, 99, 1);
        wga.init(wg);
        assertEquals(1, wga.shortestPathDist(0, 99));
        assertEquals(2, wga.shortestPathDist(0, 98));
    }

    @Test
    void WGraph_AlgoTest2() {
        weighted_graph wg = wgraphCreator(100, 99);
        weighted_graph_algorithms wga = new WGraph_Algo();
        wga.init(wg);
        assertTrue(wga.isConnected());
        wg.removeNode(50);
        wga.init(wg);
        assertFalse(wga.isConnected());
        double d = wga.shortestPathDist(0, 99);
        assertEquals(d, -1);
        wg.addNode(50);
        wg.connect(50, 49, 1);
        wg.connect(50, 51, 1);
        wga.init(wg);
        assertTrue(wga.isConnected());
        assertEquals(99, wga.shortestPathDist(0, 99));
    }

    @Test
    void WGraph_AlgoTest3() {
        weighted_graph_algorithms wga = new WGraph_Algo(new WGraph_DS());
        assertTrue(wga.isConnected());
        wga.getGraph().addNode(0);
        assertTrue(wga.isConnected());
        wga.init(wgraphCreator(10, 9));
        assertTrue(wga.isConnected());
        assertEquals(-1, wga.shortestPathDist(0, 10));
        assertEquals(9, wga.shortestPathDist(0, 9));
        wga.getGraph().removeEdge(8, 9);
        assertEquals(8, wga.shortestPathDist(0, 8));
        assertEquals(-1, wga.shortestPathDist(0, 9));
        wga.getGraph().connect(8, 9, 10);
        assertEquals(18, wga.shortestPathDist(0, 9));
        wga.getGraph().removeEdge(1, 2);
        assertFalse(wga.isConnected());
    }

    @Test
    void WGraph_AlgoTest4() {
        weighted_graph wg = wgraphCreator(10000, 9999);
        weighted_graph_algorithms wga = new WGraph_Algo();
        wga.init(wg);
        assertTrue(wga.isConnected());
        assertEquals(9999, wga.shortestPathDist(0, 9999));
        wga.getGraph().connect(0, 9999, 1);
        assertEquals(1, wga.shortestPathDist(0, 9999));
        wga.getGraph().removeEdge(0, 9999);
        wga.getGraph().addNode(10000);
        assertFalse(wga.isConnected());
        wga.getGraph().connect(9999, 10000, -1);
        assertFalse(wga.isConnected());
        wga.getGraph().connect(9999, 10000, 1);
        assertTrue(wga.isConnected());
        assertEquals(10000, wga.shortestPathDist(0, 10000));
        wga.getGraph().connect(9999, 10000, 0.5);
        assertEquals(9999.5, wga.shortestPathDist(0, 10000));
        wga.getGraph().connect(0, 10000, 10000);
        assertEquals(0.5, wga.shortestPathDist(9999, 10000));
        assertEquals(1, wga.shortestPathDist(9998, 9999));
        assertEquals(9999.5, wga.shortestPathDist(0, 10000));
    }

    @Test
    void WGraph_AlgoTest5() {
        weighted_graph wg = wgraphCreator(4, 3);
        wg.connect(0, 3, 14);
        weighted_graph_algorithms wga = new WGraph_Algo();
        wga.init(wg);
        assertEquals(3, wga.shortestPathDist(0, 3));
        wga.getGraph().connect(2, 3, 14);
        assertEquals(14, wga.shortestPathDist(0, 3));
        wga.getGraph().connect(2, 3, 0.5);
        assertEquals(2.5, wga.shortestPathDist(0, 3));
        System.out.println(wga.shortestPath(0, 3));
        assertEquals(4, wga.shortestPath(0, 3).size());
        weighted_graph wg2 = wgraphCreator(20, 19);
        wg2.connect(0, 19, 1);
        wga.init(wg2);
        assertEquals(1, wga.shortestPathDist(0, 19));
        wga.getGraph().connect(0, 19, 19);
        assertEquals(19, wga.shortestPathDist(0, 19));
        wga.getGraph().connect(0, 19, -500);
        assertEquals(19, wga.shortestPathDist(0, 19));

    }

    @Test
    void WGraph_AlgoTest6() {
        weighted_graph wg = wgraphCreator(10, 9);
        System.out.println(wg);
        wg.connect(8, 9, 0.5);
        wg.connect(0, 9, 101);
        weighted_graph_algorithms wga = new WGraph_Algo();
        wga.init(wg);
        System.out.println(wg);
        wga.save("C:/Users/Adi Dahari/Desktop/Ex1/Test.bin");
        wga.init(wga.copy());
        wga.save("C:/Users/Adi Dahari/Desktop/Ex1/newFolder/Test.bin");
        wga.load("C:/Users/Adi Dahari/Desktop/Ex1/Test.bin");
        System.out.println(wg);
        assertEquals(8.5, wga.shortestPathDist(0, 9));
    }

    @Test
    void WGraph_Algo_Test7() {
        weighted_graph wg = wgraphCreator(100000, 99999, 0.001);
        weighted_graph_algorithms wga = new WGraph_Algo();
        wga.init(wg);
        assertTrue(wga.isConnected());
        wga.save("C:/Users/Adi Dahari/Desktop/Ex1/Test.bin");
        weighted_graph wg1 = new WGraph_DS();
        weighted_graph_algorithms wga1 = new WGraph_Algo();
        wga1.load("C:/Users/Adi Dahari/Desktop/Ex1/Test.bin");
        assertTrue(wga1.isConnected());
        assertEquals(99.999, wga1.shortestPathDist(0, 99999), 0.00001);
        wga1.getGraph().connect(0, 99999, 1);
        assertEquals(1, wga1.shortestPathDist(0, 99999), 0.00001);
        wga1.getGraph().removeEdge(99998, 99999);
        wga1.getGraph().removeEdge(99998, 99997);
        assertFalse(wga1.isConnected());
        assertEquals(-1, wga1.shortestPathDist(0, 99998));
        assertEquals(-1, wga1.shortestPathDist(99999, 99998));
    }

    @Test
    void SaveLoadTest() {
        weighted_graph wg = wgraphCreator(10, 9, 5);
        weighted_graph_algorithms wga = new WGraph_Algo();
        wga.init(wg);
        wga.save("C:/Users/Adi Dahari/Desktop/Ex1/SaveLoadTest.bin");
        weighted_graph_algorithms wga2 = new WGraph_Algo();
        wga2.load("C:/Users/Adi Dahari/Desktop/Ex1/SaveLoadTest.bin");
        assertEquals(wga, wga2);
        wga2.getGraph().removeNode(1);
        assertNotEquals(wga, wga2);
        //wga.load("C:/Users/Adi Dahari/Desktop/Ex1/NoSuchFile");
        assertNotEquals(wga, wga2);
        wga2.getGraph().addNode(1);
        wga2.getGraph().connect(1, 0, 5);
        wga2.getGraph().connect(1, 2, 5);
        assertEquals(wga, wga2);
    }

    @Test
    void DijkstraTests() {
        weighted_graph wg = wgraphCreator(1000, 999, 1);
        weighted_graph_algorithms wga = new WGraph_Algo();
        wga.init(wg);
        assertEquals(999, wga.shortestPathDist(0, 999));
        wga.getGraph().connect(499, 999, 1);
        assertEquals(500, wga.shortestPathDist(0, 999));
        wga.getGraph().removeEdge(499, 999);
        wga.getGraph().removeEdge(499, 500);
        wga.getGraph().connect(0, 999, 1);
        assertEquals(500, wga.shortestPathDist(0, 500));
        wga.getGraph().connect(499, 500, 1);
        wga.getGraph().removeEdge(0, 1);
        assertEquals(999, wga.shortestPathDist(0, 1));
        wga.getGraph().connect(0, 2, 0.1);
        assertEquals(1.1, wga.shortestPathDist(0, 1));
        System.out.println(wga.shortestPath(0, 1));
        wga.getGraph().addNode(1000);
        assertEquals(-1, wga.shortestPathDist(0, 1000));
    }

    @Test
    void bigTest() {
        weighted_graph wg = wgraphCreatorBig(1000000, 999910, 1);
        weighted_graph_algorithms wga = new WGraph_Algo();
        wga.init(wg);

    }

    @Test
    void bigTest2() {
        weighted_graph wg = graph_creator(1000000, 10000000, 1);
        weighted_graph_algorithms wga = new WGraph_Algo();
        wga.init(wg);

    }

    @Test
    void ComparatorTest() {
        weighted_graph wg = wgraphCreatorBig(10, 1, 1);
        wg.connect(0, 2, 2);
        wg.connect(1, 2, 1);
        wg.connect(0, 3, 3);
        wg.connect(0, 4, 4);
        wg.connect(0, 5, 5);
        wg.connect(0, 1, 100);
        weighted_graph_algorithms wga = new WGraph_Algo();
        wga.init(wg);
        assertEquals(3, wga.shortestPathDist(0, 1));
    }
}
