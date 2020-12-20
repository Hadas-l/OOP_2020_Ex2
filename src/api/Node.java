package api;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class Node implements node_data, Serializable, Comparable<node_data>, Comparator<node_data> {

	/*
	 * Class Node will represent the information of a given node in a graph
	 * 
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	private final int key;
	private String info;
	private int tag;
	private double weight;
	private geo_location location;
	
	public Node(int key) {		
		this.key = key;
		this.info = "";
		this.tag = 0;
		this.weight = 0;
		this.location = null;
	}

	/**
	 * Returns the key (id) associated with this node.
	 * @return
	 */
	@Override
	public int getKey() {
		return this.key;
	}

	/** Returns the location of this node, if
	 * none return null.
	 * 
	 * @return
	 */
	@Override
	public geo_location getLocation() {
		return this.location;
	}

	/** Allows changing this node's location.
	 * @param p - new new location  (position) of this node.
	 */
	@Override
	public void setLocation(geo_location p) {
		this.location = p;
	}

	/**
	 * Returns the weight associated with this node.
	 * @return
	 */
	@Override
	public double getWeight() {
		return this.weight;
	}

	/**
	 * Allows changing this node's weight.
	 * @param w - the new weight
	 */
	@Override
	public void setWeight(double w) {
		this.weight = w;
	}

	/**
	 * Returns the remark (meta data) associated with this node.
	 * @return
	 */
	@Override
	public String getInfo() {
		return this.info;
	}

	/**
	 * Allows changing the remark (meta data) associated with this node.
	 * @param s
	 */
	@Override
	public void setInfo(String s) {
		this.info = s;
	}

	/**
	 * Temporal data (aka color: e,g, white, gray, black) 
	 * which can be used be algorithms 
	 * @return
	 */
	@Override
	public int getTag() {
		return this.tag;
	}

	/** 
	 * Allows setting the "tag" value for temporal marking an node - common
	 * practice for marking by algorithms.
	 * @param t - the new value of the tag
	 */
	@Override
	public void setTag(int t) {
		this.tag = t;
	}

	/**
	 * come this node to other node
	 * based on their weights.
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(node_data o) {
		Double w1 = this.getWeight();
		Double w2 = o.getWeight();

		return w1.compareTo(w2);
	}

	/**
	 * compare 2 nodes
	 * based on their weights
	 * @param o1
	 * @param o2
	 * @return
	 */
	@Override
	public int compare(node_data o1, node_data o2) {
		double w1 = o1.getWeight();
		double w2 = o2.getWeight();

		return Double.compare(w1, w2);
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, weight, tag, info, location);
	}


	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (!(obj instanceof Node))
			return false;

		Node n = (Node) obj;

		return n.getKey() == this.getKey()
				&& n.getLocation() == this.getLocation()
				&& n.getTag() == this.getTag()
				&& n.getWeight() == this.getWeight()
				&& n.getInfo().equals(this.getInfo());

	}

	@Override
	public String toString() {
		return "{k: " + getKey() + ", i: " + getInfo() + ", w: " + getWeight() + ", tag: " + getTag() + "}";
	}
}