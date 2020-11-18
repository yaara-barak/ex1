package ex1;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class implements an undirected weighted graph.
 * Every graph contains a map of all its vertex,
 * a counter of the number of the vertex in the graph,
 * a counter the number of the edges
 * and a counter of the number of the changes that were made in the graph .
 */
public class WGraph_DS implements weighted_graph, java.io.Serializable {

    private int vertex_num;
    private int edge_num;
    private int MC;
    private HashMap<Integer, node_info> graph_Map;

    /**
     * A default constructor
     */
    public WGraph_DS() {
        graph_Map = new HashMap<>();
        vertex_num = 0;
        edge_num = 0;
        MC = 0;
    }

    /**
     * This constructor deeply copies the graph that received.
     */
    public WGraph_DS(weighted_graph temp) {
        if (temp == null) return;
        graph_Map = new HashMap<>();
        Iterator<node_info> i = temp.getV().iterator();
        while (i.hasNext()) {
            node_info j = i.next();
            node_info k = new node(j);
            this.graph_Map.put(k.getKey(), k);
        }
        Iterator<node_info> t1 = temp.getV().iterator();
        while (t1.hasNext()) {
            node_info t2 = t1.next();
            for (node_info ni : ((node) t2).getNi()) {
                connect(t2.getKey(), ni.getKey(), temp.getEdge(ni.getKey(), t2.getKey()));
            }
            vertex_num = temp.nodeSize();
            edge_num = temp.edgeSize();
            MC = temp.nodeSize() + temp.edgeSize();
        }
    }



    /**
     * This private class implements the set of operations applicable on a
     * node (vertex) in an undirected weighted graph.
     * Every vertex contains a unique key that represents it,
     * a map of its neighbors in the graph,
     * and a map of its edges with its neighbors
     * The neighbors map and the edges map are implemented by Hash_map in order to make the functions
     * much more efficient.
     */
    static private class node implements node_info, java.io.Serializable {
        static int id = 0;
        int key = 0;
        private String info;
        private HashMap<Integer, node_info> node_Neighbors = new HashMap<>();
        private HashMap<Integer, edge> node_Edges = new HashMap<>();
        private double tag;


        public node() {
            key = id;
            id++;
            info = "";
            node_Neighbors = new HashMap<>();
            node_Edges = new HashMap<>();
            tag = 0;
        }

        /**
         * This constructor deeply copies the node.
         * This function is essential to copy a graph.
         */
        public node(node_info temp) {
            node_Neighbors = new HashMap<>();
            node_Edges = new HashMap<>();
            key = temp.getKey();
            info = temp.getInfo();

        }

        public node(int key) {
            this.key = key;
            info = "";
            node_Neighbors = new HashMap<>();
            node_Edges = new HashMap<>();
            tag = 0;

        }

        @Override
        public int getKey() {
            return key;
        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public void setInfo(String s) {
            info = s;

        }

        @Override
        public double getTag() {
            return tag;
        }

        @Override
        public void setTag(double t) {
            tag = t;
        }

        /**
         * @return a collection of the vertex neighbors
         */
        public Collection<node_info> getNi() {
            return node_Neighbors.values();
        }

        /**
         * @return a collection of the vertex edges
         */
        public Collection<edge> getE() {
            return node_Edges.values();
        }


        public boolean hasNi(int key) {
            if (node_Neighbors.containsKey(key))
                return true;
            else
                return false;
        }


        public void addNi(node_info t, double w) {
            if (!node_Neighbors.containsValue(t) && w > -1) {
                node_Neighbors.put(t.getKey(), t);
                edge temp = new edge(key, t.getKey(), w);
                node_Edges.put(t.getKey(), temp);
            }
            if (node_Neighbors.containsValue(t) && w > -1) {
                node_Edges.get(t.getKey()).setWeigh(w);
            }


        }


        public void removeNode(node_info node) {
            if ((node != null) && (node_Neighbors.containsValue(node))) {
                node_Neighbors.remove(node.getKey());
                node_Edges.remove(node.getKey());
            }
        }

        @Override
        public String toString() {
            return "" + key;
        }


        /**
         *This function is needed in order to compare two nodes
         * @return  true if all the nodes fields are equals.
         */
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof node))
                return false;
            node temp = (node) o;
            if (key == (temp.getKey()) && info.equals(temp.getInfo()) && tag == temp.getTag() && this.node_Edges.equals(temp.node_Edges))
                return true;

            return false;
        }

    }


    /**
     * This private class built and implements the set of operations applicable on a
     * edge in an undirected weighted graph.
     * Every edge contains the weight of the edge and the two key nodes the are connected.
     */
    static private class edge implements java.io.Serializable {
        private int node1;
        private int node2;
        private double weight;
        private double tag;

        /**
         * Default constructor
         */
        edge() {
            node1 = -1;
            node2 = -1;
            weight = 0;
            tag = 0;
        }

        edge(int node1, int node2, double weigh) {
            this.node1 = node1;
            this.node2 = node2;
            this.weight = weigh;
        }

        public int getNode1() {
            return node1;
        }

        public int getNode2() {
            return node2;
        }

        public double getWeigh() {
            return weight;
        }

        public void setWeigh(double weigh) {
            this.weight = weigh;
        }

        public double getTag() {
            return tag;
        }

        public void setTag(double tag) {
            this.tag = tag;
        }

        /**
         *This function is needed in order to compare two edges
         * @return  true if all the edges fields are equals.
         */
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof edge))
                return false;
            edge temp = (edge) o;
            if (node1 == (temp.getNode1()) && node2 == (temp.getNode2()) && weight == (temp.getWeigh()) && tag == (temp.getTag()))
                return true;
            return false;

        }


    }


    @Override
    public node_info getNode(int key) {
        if (graph_Map.containsKey(key))
            return graph_Map.get(key);
        return null;
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        if (node1 == node2)
            return false;
        else if (graph_Map.containsKey(node1) && graph_Map.containsKey(node2))
            return ((node) getNode(node1)).hasNi(node2);
        else
            return false;
    }

    @Override
    public double getEdge(int node1, int node2) {
        if (hasEdge(node1, node2))
            return ((node) getNode(node1)).node_Edges.get(node2).weight;
        return -1;
    }

    @Override
    public void addNode(int key) {
        if (!graph_Map.containsKey(key)) {
            node_info temp = new node(key);
            graph_Map.put(key, temp);
            MC++;
            vertex_num++;
        }
    }

    @Override
    public void connect(int node1, int node2, double w) {

        if (getEdge(node1, node2) == w)
            MC--;
        if (node1 != node2 && graph_Map.containsKey(node1) && graph_Map.containsKey(node2) && w > -1) {
            if (!hasEdge(node1, node2))
                edge_num++;
            ((node) graph_Map.get(node1)).addNi(graph_Map.get(node2), w);
            ((node) graph_Map.get(node2)).addNi(graph_Map.get(node1), w);
            MC++;
        }

    }

    @Override
    public Collection<node_info> getV() {
        return graph_Map.values();
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        if (graph_Map.containsKey(node_id))
            return ((node) graph_Map.get(node_id)).getNi();
        else
            return null;
    }

    /**
     * This function removes a received vertex,
     * and is removes it from all the neighbors list it was on .
     */
    @Override
    public node_info removeNode(int key) {
        if (!graph_Map.containsKey(key)) return null;
        node_info temp = getNode(key);
        for (node_info i : ((node) getNode(key)).getNi()) {
            ((node) i).getNi().remove(getNode(key));
            ((node) i).getE().remove(getNode(key));
            MC++;
            edge_num--;

        }
        graph_Map.remove(key);
        vertex_num--;
        MC++;
        return temp;
    }

    /**
     * This function removes the edges that exist between two vertexes.
     * and is removes it from all the edges list it was on .
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (hasEdge(node1, node2)) {
            ((node) graph_Map.get(node1)).removeNode(graph_Map.get(node2));
            ((node) graph_Map.get(node2)).removeNode(graph_Map.get(node1));
            if (node1 != node2) {
                edge_num--;
                MC++;
            }
        }

    }

    @Override
    public int nodeSize() {
        return vertex_num;
    }

    @Override
    public int edgeSize() {
        return edge_num;
    }

    @Override
    public int getMC() {
        return MC;
    }

    @Override
    public String toString() {
        return "" + graph_Map +
                "edge_num= " + edge_num +
                ", MC= " + MC;
    }


    /**
     *This function is needed in order to compare two graphs
     * @return  true if all the graph fields are equals.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WGraph_DS))
            return false;

        WGraph_DS temp = (WGraph_DS) o;
        if (graph_Map.equals(temp.graph_Map) && vertex_num == temp.vertex_num && edge_num == temp.edge_num)
            return true;

        return false;


    }

}
