package api;

import java.io.Serializable;
import java.util.*;

public class DWGraph_DS implements directed_weighted_graph, Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HashMap<Integer, node_data> graph;
    private HashMap<Integer, HashMap<Integer, edge_data>> neighbors;
    private HashMap<Integer, List<Integer>> connected_to;
    
    private int edgeCounter;
    private int mc;

    public DWGraph_DS() {
        this.graph = new HashMap<>();
        this.neighbors = new HashMap<>();
        this.connected_to = new HashMap<>();

        edgeCounter = 0;
        mc = 0;
    }

    /**
     * returns the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        if(this.graph.containsKey(key))
            return this.graph.get(key);
        return null;
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if(this.neighbors.get(src).containsKey(dest)
            && src != dest)
            return this.neighbors.get(src).get(dest);
        return null;
    }

    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if(!this.graph.containsKey(n.getKey())) {
        	
            this.graph.put(n.getKey(), n);
            this.neighbors.put(n.getKey(), new HashMap<>());
            this.connected_to.put(n.getKey(), new ArrayList<>());

            mc++;
        }
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if(w >= 0
                && src != dest
                && !neighbors.get(src).containsKey(dest)
                && graph.containsKey(src) && graph.containsKey(dest)){
        	
            edge_data edge =  new Edge(src,dest,w);
            neighbors.get(src).put(dest,edge);
            
            connected_to.get(dest).add(src);
            
            edgeCounter++;
            mc++;
        }
        
        /*
         * we might need to consider updating weight if and edge
         * already exists between src and dest.
         */

    }

	/**
	 * This method returns a pointer (shallow copy) for the
	 * collection representing all the nodes in the graph. 
	 * Note: this method should run in O(1) time.
	 * @return Collection<node_data>
	 */
    @Override
    public Collection<node_data> getV() {
        return this.graph.values();
    }

	/**
	 * This method returns a pointer (shallow copy) for the
	 * collection representing all the edges getting out of 
	 * the given node (all the edges starting (source) at the given node). 
	 * Note: this method should run in O(k) time, k being the collection size.
	 * @return Collection<edge_data>
	 */
    @Override
    public Collection<edge_data> getE(int node_id) {
        return this.neighbors.get(node_id).values(); //o(k)
    }

	/**
	 * Deletes the node (with the given ID) from the graph -
	 * and removes all edges which starts or ends at this node.
	 * This method should run in O(k), V.degree=k, as all the edges should be removed.
	 * @return the data of the removed node (null if none). 
	 * @param key
	 */
    @Override
    public node_data removeNode(int key) {

    	if (this.graph.containsKey(key)) {

    		for (Integer v : this.connected_to.get(key)) {
				
    			this.neighbors.get(v).remove(key);
    			
    			this.edgeCounter--;
    			this.mc++;
    			
			}
    		
    		//remove all neighbors
    		this.edgeCounter -= this.neighbors.get(key).size();
    		this.mc += this.neighbors.get(key).size();

    		this.neighbors.get(key).clear();
    		
    		return this.graph.remove(key);
    		//return node
    		
    	}
    	
    	return null;
    	
    }

	/**
	 * Deletes the edge from the graph,
	 * Note: this method should run in O(1) time.
	 * @param src
	 * @param dest
	 * @return the data of the removed edge (null if none).
	 */
    @Override
    public edge_data removeEdge(int src, int dest) {
    	
        if(src != dest && this.neighbors.get(src).containsKey(dest)){
        	
            this.neighbors.get(src).remove(dest);
            
            this.connected_to.get(dest).remove(src);
            
            mc++;
            edgeCounter--;
        }
        
        return null;
    }

    /** Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     * @return
     */
    @Override
    public int nodeSize() {
        return this.graph.size();
    }

    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     * @return
     */
    @Override
    public int edgeSize() {
        return this.edgeCounter;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     * @return
     */
    @Override
    public int getMC() {
        return this.mc;
    }

    @Override
    public int hashCode() {
        return Objects.hash(graph, neighbors, connected_to, edgeCounter, mc);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (!(obj instanceof DWGraph_DS))
            return false;

        DWGraph_DS g = (DWGraph_DS) obj;

        return g.getMC() == this.getMC()
                && g.edgeSize() == this.edgeSize()
                && g.nodeSize() == this.nodeSize()
                && Objects.equals(g.neighbors, this.neighbors)
                && Objects.equals(g.graph, this.graph)
                && Objects.equals(g.connected_to, this.connected_to);

    }

}
