package ex1;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms, java.io.Serializable {

    private weighted_graph My_Graph = new WGraph_DS();
    private HashMap<Integer, node_spd> map;

    public WGraph_Algo() {
        My_Graph = new WGraph_DS();
    }

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

        @Override
        public int compareTo(node_spd n1) {
            if (distance < n1.GetDistance())
                return -1;
            else if (distance > n1.GetDistance())
                return 1;
            else return 0;
        }
    }

    @Override
    public void init(weighted_graph g) {
        My_Graph = g;
    }

    @Override
    public weighted_graph getGraph() {
        return My_Graph;
    }

    @Override
    public weighted_graph copy() {
        weighted_graph temp = new WGraph_DS(My_Graph);// calling the Graph_DS constructor
        return temp;
    }

    @Override
    public boolean isConnected() {
        if (My_Graph.nodeSize()==0)
            return true;
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
