package api;

import java.io.Serializable;
import java.util.Objects;

public class Edge implements edge_data, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final int source;
    private final int destination;
    private int tag;
    private double weight;
    private String info;

    public Edge(int source, int destination, double weight){
        this.source = source;
        this.destination = destination;
        this.weight = weight;
        this.info = "";
        this.tag = 0;
    }

    @Override
    public int getSrc() {
        return this.source;
    }

    @Override
    public int getDest() {
        return this.destination;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, destination, weight, tag, info);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof Edge))
            return false;

        Edge e = (Edge) obj;

        return e.getWeight() == this.getWeight()
                && e.getDest() == this.getDest()
                && e.getSrc() == this.getSrc()
                && e.getInfo().equals(this.getInfo())
                && e.getTag() == this.getTag();

    }

    @Override
    public String toString() {
        return "{s:"+getSrc()+" -> d:"+getDest()+", w:"+getWeight()+"}";
    }
}