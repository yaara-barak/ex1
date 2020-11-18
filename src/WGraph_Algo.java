package ex1;

import java.io.*;
import java.util.*;


/**
 * This class creates a graph and includes algorithms:
 * one copies, the other checks if the graph is connected,
 * how long it takes to get from one vertex to another,
 * what is the shortest path from one vertex to another,
 * save the graph to the given file name
 * and load a graph to graph algorithm
 */
public class WGraph_Algo implements weighted_graph_algorithms, java.io.Serializable {

    private weighted_graph My_Graph = new WGraph_DS();
    private HashMap<Integer, node_spd> map;

    /**
     * A default constructor
     */
    public WGraph_Algo() {
        My_Graph = new WGraph_DS();
    }


    /**
     * This private class represent a node that checked in the function 'shortestPathDist',
     * and save important information for the Dijkstra algorithm.
     * Every vertex contains the key that represents it in the graph
     * the vertex 'parent' key,
     * and its distance from the source.
     */
    static private class node_spd implements Comparable<node_spd> {
        int key;
        int tag;
        node_spd parent;
        double distance;

        public node_spd() {
            tag = 0;
            parent = null;
            distance = Double.POSITIVE_INFINITY;
        }


        public int GetKey() {
            return key;
        }

        public void SetKey(int key) {
            this.key = key;
        }

        public int GetTag() {
            return tag;
        }

        public void SetTag(int tag) {
            this.tag = tag;
        }

        public node_spd GetParent() {
            return parent;
        }

        public void SetParent(node_spd parent) {
            this.parent = parent;
        }

        public double GetDistance() {
            return distance;
        }

        public void SetDistance(double distance) {
            this.distance = distance;
        }

        /**
         *This function is needed in order to compare two nodes
         * @return  true if all the nodes fields are equals.
         */
        @Override
        public int compareTo(node_spd n1) {
            if (distance < n1.GetDistance())
                return -1;
            else if (distance > n1.GetDistance())
                return 1;
            else return 0;
        }
    }

    /**
     * This function does a shallow copy
     * (creates another pointer for the graph)
     */
    @Override
    public void init(weighted_graph g) {
        My_Graph = g;
    }

    /**
     * This function return the underlying graph of which this class works.
     */
    @Override
    public weighted_graph getGraph() {
        return My_Graph;
    }

    /**
     * This function does a deep copy by using the data structures copy constructors
     */
    @Override
    public weighted_graph copy() {
        weighted_graph temp = new WGraph_DS(My_Graph);// calling the Graph_DS constructor
        return temp;
    }


    /**
     * This function checks if the graph is connected
     * by checking every vertex and all its neighbors and pushing them to a queue (bfs algorithm)
     * @return Boolean (true or false)
     */
    @Override
    public boolean isConnected() {
        if (My_Graph.nodeSize()==0)
            return false;
        if (My_Graph.nodeSize() < 2)// If the graph contain only one vertex
            return true;//return true -> is linked.
        Iterator<node_info> i = My_Graph.getV().iterator();
        while (i.hasNext()) // This loop resets all the vertexes tags
            i.next().setTag(0);
        int counter = 0;
        Queue<node_info> temp_q = new LinkedList<>();
        node_info pointer = My_Graph.getV().iterator().next();// a random pointer on one vertex in the graph.
        pointer.setTag(1);
        temp_q.add(pointer);
        while (!temp_q.isEmpty()) {
            pointer = temp_q.poll();//pulls out the first node in the queue and checks its neighbors
            counter++;
            if (pointer.getTag() == 1) {
                for (node_info j : My_Graph.getV(pointer.getKey()))
                    if (j.getTag() == 0) {
                        j.setTag(1);
                        temp_q.add(j);
                    }
            }
        }
        if (counter == My_Graph.nodeSize())// If the loop checked all the nodes then the graph is linked.
            return true;
        return false;
    }

    /**
     *  This function returns the length of the shortest path between two vertexes on the graph
     *  by checking all the graph vertexes edges and its neighbors edges
     *  by pushing them to a priority queue (Dijkstra algorithm)
     *  @param src - start node
     *  @param dest - end (target) node
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if(My_Graph.getNode(src)==null||My_Graph.getNode(dest)==null)
            return -1;
        if(src==dest)
            return 0;
        if(My_Graph==null)
            return -1;
        map = new HashMap<>();
        PriorityQueue<node_spd> q = new PriorityQueue<>();
        node_spd SRC = new node_spd();
        SRC.SetKey(My_Graph.getNode(src).getKey());
        SRC.SetDistance(0);
        q.add(SRC);
        while (!q.isEmpty()) {
            node_spd k = q.poll();
            k.SetTag(1);
            if (k.GetTag() == 1) {
                if (k.GetKey() == dest)
                    return k.GetDistance();
                else
                    for (node_info temp : My_Graph.getV(k.GetKey())) {
                        node_spd t;
                        if (!map.containsKey(temp.getKey())) {
                            t = new node_spd();
                            t.SetKey(temp.getKey());
                            map.put(t.GetKey(), t);
                            t.SetTag(1);
                        } else
                            t = map.get(temp.getKey());
                        if (k.GetDistance() + My_Graph.getEdge(k.GetKey(), t.GetKey()) < t.GetDistance()) {
                            t.SetDistance(k.GetDistance() + My_Graph.getEdge(k.GetKey(), t.GetKey()));
                            t.SetParent(k);
                            q.add(t);
                        }
                    }
            }
            k.SetTag(2);
        }

        return -1;
    }

    /**
     * This function returns the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest,
     * by using the function "shortestPathDist" map that was created in the function.
     *@param src - start node
     *@param dest - end (target) node
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        double pathSum = shortestPathDist(src, dest);
        if (pathSum == -1)
            return null;
        LinkedList<node_info> My_path = new LinkedList<>();
        if (src == dest) {
            My_path.addFirst(My_Graph.getNode(src));
            return My_path;
        }
        node_spd temp = map.get(dest);
        My_path.addFirst(My_Graph.getNode(dest));
        while (temp.GetParent().GetKey() != src) {
            temp = temp.GetParent();
            My_path.addFirst(My_Graph.getNode(temp.GetKey()));
        }
        My_path.addFirst(My_Graph.getNode(src));
        return My_path;
    }


    /**
     * This function saves the weighted (undirected) graph to the given
     * file name
     * @param file - the file name .
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        try {
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(f);
            out.writeObject(My_Graph);
            out.close();
            f.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    /**
     * This function load a graph to graph algorithm.
     * if the file was successfully loaded - the graph will be changed,
     * if graph was not loaded the original graph should remain "as is".
     * @param file - file name
     * @return true - if the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            FileInputStream f = new FileInputStream(file);
            ObjectInputStream i = new ObjectInputStream(f);
            My_Graph = (weighted_graph) i.readObject();
            f.close();
            i.close();

        }

        catch(ClassNotFoundException |IOException e){
           e.printStackTrace();
           return false;

        }
        return true;
    }

    @Override
    public String toString() {
        return "" + My_Graph;
    }
}
