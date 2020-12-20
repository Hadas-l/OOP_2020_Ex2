package api;

import gameClient.util.Point3D;
import org.json.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * This interface represents a Directed (positive) Weighted Graph Theory Algorithms including:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected(); // strongly (all ordered pais connected)
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file); // JSON file
 * 6. Load(file); // JSON file
 *
 * @author boaz.benmoshe
 *
 */
public class DWGraph_Algo implements dw_graph_algorithms {
	
	private static directed_weighted_graph graph;
	private static HashMap<Integer, Double> dist;
	private static HashMap<Integer, Integer> prev;
	
	
    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g     */
    public void init(directed_weighted_graph g) {
    	graph = g;
    	dist = new HashMap<>();
    	prev = new HashMap<>();
	}

	public HashMap<Integer, Double> getDist() {
		return dist;
	}

	public void setDist(HashMap<Integer, Double> dist) {
		DWGraph_Algo.dist = dist;
	}

	public static HashMap<Integer, Integer> getPrev() {
		return prev;
	}

	public static void setPrev(HashMap<Integer, Integer> prev) {
		DWGraph_Algo.prev = prev;
	}

	/**
     * Return the underlying graph of which this class works.
     * @return
     */
    public directed_weighted_graph getGraph() {
		return graph;
	}
    /**
     * Compute a deep copy of this weighted graph.
     * @return
     */
    public directed_weighted_graph copy() {
    	
    	directed_weighted_graph deep_copy = new DWGraph_DS();
		
		for (node_data vertex : graph.getV()) {
			
			deep_copy.addNode(vertex);
			
		}
		
		for (node_data vertex : graph.getV()) { //for each vertex in graph
			
			for (edge_data edge : graph.getE(vertex.getKey())) { //for each neighbor of vertex
				
				deep_copy.connect(edge.getSrc(), edge.getDest(), edge.getWeight());
				
			}
		}
		
		return deep_copy;
	}

	/**
	 * Compute a deep transposed copy.
	 * @return directed_weighted_graph
	 */
	public directed_weighted_graph copy_transposed() {

		directed_weighted_graph deep_copy = new DWGraph_DS();

		for (node_data vertex : graph.getV()) {

			deep_copy.addNode(vertex);

		}

		for (node_data vertex : graph.getV()) { //for each vertex in graph

			for (edge_data edge : graph.getE(vertex.getKey())) { //for each neighbor of vertex

				deep_copy.connect(edge.getDest(), edge.getSrc(), edge.getWeight());

			}
		}

		return deep_copy;
	}


	/**
	 * Djikstra algorithm for finding shortest path in a
	 * directed weighted graph (weight >= 0)
	 *
	 * this code is implemented using djikstra pseudo code
	 * that can be found in wikipedia: https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
	 * @param source
	 */
	public void djikstra(int source) {
		PriorityQueue<node_data> q = new PriorityQueue<>();
		
		for (node_data vertex : graph.getV()) { //for each node in graph
			
			if (vertex.getKey() == source) { 
			
				dist.put(source, 0.0); //dist[source] -> 0
				prev.put(source, source); //prev[node] -> null
				vertex.setWeight(0.0); //for comperable in priority queue
				
			}
			
			else {
				
				dist.put(vertex.getKey(), Double.MAX_VALUE); //dist[node] -> inf
				prev.put(vertex.getKey(), null); //prev[node] -> null
				vertex.setWeight(Double.MAX_VALUE); //for comperable in priority queue
			}
			
			q.add(vertex);
		}
		
		while( !q.isEmpty() ) { //Q is not empty
			
			node_data u = q.poll(); // Q.poll(); minheap.poll()
			
			for (edge_data edge : graph.getE(u.getKey())) { //for each neighbor of node
				
				
				double alt = dist.get(u.getKey()) + edge.getWeight();
				
				if (alt < dist.get(edge.getDest())) {
					
					dist.put(edge.getDest(), alt);
					prev.put(edge.getDest(), u.getKey());
					
					graph.getNode(edge.getDest()).setWeight(alt);
					
					q.remove(graph.getNode(edge.getDest())); //remove neighbor from Q
					q.add(graph.getNode(edge.getDest())); //insert into correct place (decrease priority)

				}
				
			}
			
		}
		
	}

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     * @return boolean
     */
    public boolean isConnected() {
    	
    	if (graph.nodeSize() == 1 || graph.nodeSize() == 0) return true;

    	Iterator<node_data> it = graph.getV().iterator();

    	if (it.hasNext()) {
    		djikstra(it.next().getKey());
		}

    	if (!dist.containsValue(Double.MAX_VALUE)) {
    		directed_weighted_graph transposed = copy_transposed();

			DWGraph_Algo algo_transposed = new DWGraph_Algo();

    		algo_transposed.init(transposed);

			Iterator<node_data> t_it = transposed.getV().iterator();

			if (t_it.hasNext()) {
				algo_transposed.djikstra(t_it.next().getKey());

				return !algo_transposed.getDist().containsValue(Double.MAX_VALUE);
			}

		}

    	return false;
//
//    	for (node_data node : graph.getV()) {
//
//        	djikstra(node.getKey());
//
//        	break;
//    	}
//
//		return !dist.containsValue(Double.MAX_VALUE);

	}
    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    public double shortestPathDist(int src, int dest) {
    	djikstra(src);
		return dist.get(dest);
	}
    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    public List<node_data> shortestPath(int src, int dest) {

    	List<node_data> res = new ArrayList<>();
	
		List<node_data> back_res = new ArrayList<>();
	   
	   djikstra(src);

	   if (shortestPathDist(src, dest) == Double.MAX_VALUE) return null;

	   Integer current = dest;

	   //add dest to result list
	   res.add(graph.getNode(current));

	   while (prev.get(current) != null) {

			if (current == src) {
				break;
			}

			res.add(graph.getNode(prev.get(current)));
			current = prev.get(current);

	   }

	   for (int i = res.size() - 1; i >= 0; i--) {
			back_res.add(res.get(i));
		}

	   return back_res;

	}

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    public boolean save(String file) {
		try {


			JSONObject wrapper = new JSONObject(); // wrapper

			JSONArray nodes_ = new JSONArray();
			JSONArray edges_ = new JSONArray();

			for (node_data vertex : getGraph().getV()) {

				JSONObject node = new JSONObject();
				if (vertex.getLocation() != null) node.put("pos", vertex.getLocation().toString());
				else node.put("pos", "0.0,0.0,0.0");
				node.put("id", vertex.getKey());

				nodes_.put(node);

				for (edge_data ni : getGraph().getE(vertex.getKey())) {

					JSONObject edge = new JSONObject();
					edge.put("src", ni.getSrc());
					edge.put("w", ni.getWeight());
					edge.put("dest", ni.getDest());

					edges_.put(edge);

				}

			}

			FileWriter fw = new FileWriter(file);


			wrapper.put("Edges", edges_);
			wrapper.put("Nodes", nodes_);

			fw.write(wrapper.toString());
			fw.flush();

           
        } catch (IOException ex) {
            return false;
        } catch (JSONException e) {
			e.printStackTrace();
		}
		return true;
	}

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    public boolean load(String file) {

		directed_weighted_graph graph = new DWGraph_DS();

		 try {

		 	String data = new String(Files.readAllBytes(Paths.get(file)));

//			 System.out.println(data);

			 JSONObject l_g;

			 l_g = new JSONObject(data);

			 JSONArray nodes_ = l_g.getJSONArray("Nodes");
			 JSONArray edges_ = l_g.getJSONArray("Edges");

			 for (int i = 0; i < nodes_.length(); i++) {
//                System.out.println(t.get(i));
				 String pos_ = nodes_.getJSONObject(i).getString("pos");
				 int id_ = nodes_.getJSONObject(i).getInt("id");

				 String[] p_ = pos_.split(",");

				 double x = Double.parseDouble(p_[0]);
				 double y = Double.parseDouble(p_[1]);
				 double z = Double.parseDouble(p_[2]);

				 node_data n = new Node(id_);

				 n.setLocation(new Point3D(x, y, z));

				 graph.addNode(n);

			 }

			 for (int i = 0; i < edges_.length(); i++) {
//                System.out.println(t.get(i));
				 int s_ = edges_.getJSONObject(i).getInt("src");
				 int d_ = edges_.getJSONObject(i).getInt("dest");
				 double w_ = edges_.getJSONObject(i).getInt("w");

				 graph.connect(s_, d_, w_);

//                System.out.println(s_ + ", " + d_ + ", " + w_);
			 }

//			 System.out.println("graph: " + graph.edgeSize());
			 init(graph);

		}

		 catch (IOException | JSONException e) {
		 		e.printStackTrace();
	            return false;
		 }

		return true;
    }

}
