package tests;

import api.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import static org.junit.Assert.*;

public class DWGraph_DSTest {
    private static Random _rnd = null;

    @Test
    public void nodeSize() {
        directed_weighted_graph g = new DWGraph_DS();

        g.addNode(new Node(0));
        g.addNode(new Node(1));
        g.addNode(new Node(2));

        g.removeNode(2);
        g.removeNode(1);
        g.removeNode(1);
        int s = g.nodeSize();
        assertEquals(1,s);

    }

    @Test
    public void edgeSize() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new Node(0));
        g.addNode(new Node(1));
        g.addNode(new Node(2));
        g.addNode(new Node(3));

        g.connect(0,1,1); // 0 -> 1 w: 1
        g.connect(0,2,2); // 0 -> 2 w: 2
        g.connect(0,3,3); // 0 -> 3 w: 3

        g.connect(0,1,1); // 0 -> 1 w: change to -> 1 //do nothing

        int e_size =  g.edgeSize();
        assertEquals(3, e_size);

        edge_data w03 = g.getEdge(0,3);
        edge_data w30 = g.getEdge(3,0);

//        assertNotEquals(w03.getWeight(), w30.getWeight(), 0.0001);

        assertEquals(w03.getWeight(), 3, 0.0001); // 0 connected to 3 w: 3
        assertNull(w30); // 3 not connected to 0 w: 0
    }

    @Test
    public void getV() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new Node(0));
        g.addNode(new Node(1));
        g.addNode(new Node(2));
        g.addNode(new Node(3));

        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(0,3,3);
        g.connect(0,1,1);

        Collection<node_data> v = g.getV();
        Iterator<node_data> iter = v.iterator();

        while (iter.hasNext()) {
            node_data n = iter.next();
            assertNotNull(n);
        }
    }

    @Test
    public void connect() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new Node(0));
        g.addNode(new Node(1));
        g.addNode(new Node(2));
        g.addNode(new Node(3));

        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(0,3,3);

        assertEquals(g.nodeSize(), 4);
        assertEquals(g.edgeSize(), 3);

        g.removeEdge(0,1);
        assertNull(g.getEdge(1, 0)); //no edge 1->0

        g.removeEdge(2,1); //do nothing
        g.connect(0,1,1);
        g.connect(1, 0, 2);

        assertEquals(g.getEdge(0, 1).getWeight(), 1, 0.0001);
        assertEquals(g.getEdge(1, 0).getWeight(), 2, 0.0001);

    }


    @Test
    public void removeNode() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new Node(0));
        g.addNode(new Node(1));
        g.addNode(new Node(2));
        g.addNode(new Node(3));

        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(0,3,3);

        g.connect(3, 0, 3);

        g.connect(1, 2, 1);

        g.removeNode(4); //do nothing
        assertEquals(g.nodeSize(), 4);

        g.removeNode(0);
        assertNull(g.getEdge(1, 0));
        assertEquals(g.edgeSize(), 1);
        assertEquals(g.nodeSize(), 3);

        g.connect(1,2,3);
        int e = g.edgeSize();
        assertEquals(1,e);

    }

    @Test
    public void removeEdge() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new Node(0));
        g.addNode(new Node(1));
        g.addNode(new Node(2));
        g.addNode(new Node(3));

        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(0,3,3);

        g.removeEdge(0,3);
        g.removeEdge(0,3);
        edge_data w = g.getEdge(0,3);
        assertNull(w);
    }


    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0+min, (double)max);
        int ans = (int)v;
        return ans;
    }
    private static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max-min;
        double ans = d*dx+min;
        return ans;
    }
    /**
     * Simple method for returning an array with all the node_data of the graph,
     * Note: this should be using an Iterator<node_edge> to be fixed in Ex1
     * @param g
     * @return
     */
    private static int[] nodes(directed_weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_data> V = g.getV();
        node_data[] nodes = new node_data[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }
}
