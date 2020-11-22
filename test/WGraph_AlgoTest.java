package ex1.test;


import ex1.src.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WGraph_AlgoTest {
    static final long start =new Date().getTime();
    weighted_graph g;
    weighted_graph_algorithms G;

       public weighted_graph setupGraphs() {
        weighted_graph g = new WGraph_DS();
        g.addNode(-3);
        g.addNode(6);
        g.addNode(100);
        g.addNode(245);
        g.addNode(666);
        g.addNode(10091998);
        g.addNode(6011967);


        g.connect(-3, 6, 4);
        g.connect(245, 666, 12);
        g.connect(-3, 666, 11);
        g.connect(100, 6011967, 3);
        g.connect(10091998, 6011967, 9);
        g.connect(-3, 6011967, 5);
        g.connect(100, 666, 40);

        return g;
    }


        @BeforeEach
        public void resetGraph () {
            g = setupGraphs();
            G = new WGraph_Algo();
            G.init(g);
        }

        @Test
        void getGraphBasic () {
            weighted_graph ga = G.getGraph();
            assertEquals(ga, G.getGraph(), "The correct graph was returned");
        }

        @Test
        void copyBasic () {
            weighted_graph temp = G.copy();
            assertEquals(g, temp, "The graph copied successfully");
            assertEquals(g.edgeSize() + g.nodeSize(), temp.getMC(), "The MC update successfully");
            assertEquals(g.edgeSize(), temp.edgeSize(), "The edge size copied successfully");
            assertEquals(g.nodeSize(), temp.nodeSize(), "The node size copied successfully");
            g.addNode(80);
            temp = G.copy();
            assertEquals(g, temp, "The graph update and copied successfully");
        }

        @Test
        void copyEmptyGraph () {
            weighted_graph temp = new WGraph_DS();
            weighted_graph_algorithms Temp = new WGraph_Algo();
            Temp.init(temp);
            weighted_graph c = Temp.copy();
            assertEquals(temp.nodeSize(), c.nodeSize());
            assertEquals(temp.edgeSize(), c.edgeSize());
            assertEquals(temp.getMC(), c.getMC());
            assertEquals(temp.getV().size(), c.getV().size(), "The graph update and copied successfully");

        }

        @Test
        void isConnectedBasic () {
            assertTrue(G.isConnected(), "The graph is connected");
            g.removeEdge(-3, 6);
            G.init(g);
            assertFalse(G.isConnected(), "The graph is not connected");
        }

        @Test
        void isConnectedOneNode () {
            weighted_graph temp = new WGraph_DS();
            temp.addNode(4);
            weighted_graph_algorithms Temp = new WGraph_Algo();
            Temp.init(temp);
            assertTrue(Temp.isConnected(), "Graph with one node is connected");
        }

        @Test
        void isConnectedEmpty () {
            weighted_graph temp = new WGraph_DS();
            weighted_graph_algorithms Temp = new WGraph_Algo();
            Temp.init(temp);
            assertFalse(Temp.isConnected(), "Graph with one node is not connected");
        }

        @Test
        void shortestPathDistBasic () {
            assertEquals(4, G.shortestPathDist(-3, 6), "Correct distance");
            assertEquals(19, G.shortestPathDist(666, 100), "Correct distance");
            assertEquals(9, G.shortestPathDist(6011967, 6), "Correct distance");
            assertEquals(12, G.shortestPathDist(100, 6), "Correct distance");
        }

        @Test
        void shortestPathDistNull () {
            assertEquals(0, G.shortestPathDist(-3, -3), "Correct distance");
            g.addNode(90);
            G.init(g);
            assertEquals(-1, G.shortestPathDist(90, 6), "Correct distance");
            assertEquals(-1, G.shortestPathDist(17, 18), "Correct distance");
            assertEquals(0, G.shortestPathDist(-3, -3), "Correct distance");
            weighted_graph temp = new WGraph_DS();
            weighted_graph_algorithms Temp = new WGraph_Algo();
            Temp.init(temp);
            assertEquals(-1, Temp.shortestPathDist(0, 0), "Correct distance");
        }


        @Test
        void shortestPathBasic () {
            List<node_info> temp1 = G.shortestPath(-3, 6);
            List<node_info> temp2 = new LinkedList<>();
            temp2.add(g.getNode(-3));
            temp2.add(g.getNode(6));
            assertEquals(temp2.toString(), temp1.toString(), "Correct list");

            List<node_info> temp3 = G.shortestPath(666, 100);
            List<node_info> temp4 = new LinkedList<>();
            temp4.add(g.getNode(666));
            temp4.add(g.getNode(-3));
            temp4.add(g.getNode(6011967));
            temp4.add(g.getNode(100));
            assertEquals(temp3.toString(), temp4.toString(), "Correct list");

            List<node_info> temp5 = G.shortestPath(6, 6011967);
            List<node_info> temp6 = new LinkedList<>();
            temp6.add(g.getNode(6));
            temp6.add(g.getNode(-3));
            temp6.add(g.getNode(6011967));
            assertEquals(temp5.toString(), temp6.toString(), "Correct list");

            List<node_info> temp7 = G.shortestPath(6, 100);
            List<node_info> temp8 = new LinkedList<>();
            temp8.add(g.getNode(6));
            temp8.add(g.getNode(-3));
            temp8.add(g.getNode(6011967));
            temp8.add(g.getNode(100));
            assertEquals(temp8.toString(), temp7.toString(), "Correct list");
        }

        @Test
        void shortestPathNull () {
            List<node_info> temp1 = new LinkedList<>();
            temp1.add(g.getNode(-3));
            assertEquals(temp1.toString(), G.shortestPath(-3, -3).toString(), "Correct list");

            g.addNode(90);
            G.init(g);
            assertNull(G.shortestPath(90, 6), "Correct distance");

            assertNull(G.shortestPath(17, 18), "Correct distance");

        }

        @Test
        void saveAndLoad () {
            try {
                G.save("TestSave");
                assertTrue(G.save("TestSave"));

                weighted_graph temp1 = new WGraph_DS();
                temp1.addNode(100);
                temp1.addNode(200);
                temp1.addNode(300);

                weighted_graph_algorithms temp2 = new WGraph_Algo();
                temp2.init(temp1);
                String str = "TestSave";
                temp2.save(str);

                weighted_graph temp3 = new WGraph_DS();
                temp3.addNode(100);
                temp3.addNode(200);
                temp3.addNode(300);

                temp2.load(str);
                assertEquals(temp3.toString(), temp2.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Test
        void BigGraphRunTime(){
            long startTimeFinal=new Date().getTime();
            long startTime;
            long endTime;

            startTime =new Date().getTime();
            weighted_graph tempGraph = new WGraph_DS();
            for(int i=0;i<2000000;i++)
                tempGraph.addNode(i);

            for(int j=0;j<1000000;j=j+2)
                tempGraph.connect(j,j+1,j);

            endTime = new Date().getTime();
            double dt = (endTime - startTime) / 1000.0;
            assertTrue(dt<5 ,"The constructor function is efficient");


            startTime =new Date().getTime();
            weighted_graph_algorithms Temp=new WGraph_Algo();
            Temp.init(tempGraph);
            endTime = new Date().getTime();
            dt = (endTime - startTime) / 1000.0;
            assertTrue(dt<5 ,"The init function is efficient");

            startTime =new Date().getTime();
            weighted_graph temp2 = Temp.copy();
            endTime = new Date().getTime();
            dt = (endTime - startTime) / 1000.0;
            assertTrue(dt<10 ,"The copy function is efficient");

            startTime =new Date().getTime();
            double d= Temp.shortestPathDist(1,60000);
            endTime = new Date().getTime();
            dt = (endTime - startTime) / 1000.0;
            assertTrue(dt<5 ,"The shortest path dist function is efficient");

            long endTimeFinal=new Date().getTime();
            dt = (endTimeFinal - startTimeFinal) / 1000.0;
            assertTrue(dt<10 ,"All the functions efficient");
        }

        @AfterAll
        public static void PrintRunTime (){
            long end = new Date().getTime();
            double dt = (end - start) / 1000.0;

            System.out.println("run time of all the tests is " + dt);
        }


    }