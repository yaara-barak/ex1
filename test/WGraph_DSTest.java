package ex1;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


class WGraph_DSTest {

    static final long start =new Date().getTime();
    weighted_graph g;


    public weighted_graph setupGraphs() {
        weighted_graph g=new WGraph_DS();
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
        g.connect(10091998,6011967 , 9);
        g.connect(-3, 6011967, 5);
        g.connect(100, 666, 40);

     return g;
    }

    @BeforeEach
    public void resetGraph() {
        g = setupGraphs();
    }

    @Test
    void TestGetNodeBasic() {
        assertEquals( -3,g.getNode(-3).getKey(), "Check the returned node");
        assertEquals( 666,g.getNode(666).getKey(), "Check the returned node");

    }
    @Test
    void TestGetNodeNull(){
        assertNull(g.getNode(70),"The node is not in the graph");
    }
    @Test
    void TestGetNodeNegative(){
        assertEquals(-3,g.getNode(-3).getKey(),"The node is not in the graph");
    }

    @Test
    void hasEdgeBasic() {
        assertTrue(g.hasEdge(g.getNode(-3).getKey(),g.getNode(6).getKey()),"The nodes are connected");
        assertFalse(g.hasEdge(g.getNode(6).getKey(),g.getNode(100).getKey()),"The nodes are not connected");
    }
    @Test
    void hasEdgeNotExist(){
        assertFalse(g.hasEdge(g.getNode(6).getKey(),5),"One of the nodes is not exist");
        assertFalse(g.hasEdge(11,5),"One of the nodes is not exist");
    }
    @Test
    void hasEdgeSame(){
        assertFalse(g.hasEdge(g.getNode(-3).getKey(),g.getNode(-3).getKey()),"The nodes are connected");
    }

    @Test
    void getEdgeBasic() {
        assertEquals(40,g.getEdge((g.getNode(666).getKey()),g.getNode(100).getKey()),"return the correct data" );
        assertEquals(-1,g.getEdge((g.getNode(666).getKey()),g.getNode(6).getKey()),"The nodes are nor connected" );
    }
    @Test
    void getEdgeNull() {
        assertEquals(-1,g.getEdge((g.getNode(666).getKey()),5),"One of the nodes is not exist" );
        assertEquals(-1,g.getEdge(6,5),"One of the nodes is not exist" );
    }
    @Test
    void addNodeBasic() {
     int v=g.nodeSize();
        g.addNode(16);
        g.addNode(17);
        assertEquals(v+2,g.nodeSize(),"The nodes added successfully");
        assertEquals( 16,g.getNode(16).getKey(), "The node added successfully");
    }
    @Test
    void addNodeNegative() {
        int v=g.nodeSize();
        g.addNode(-6);
        assertEquals(v+1,g.nodeSize(),"The nodes added successfully");
        assertEquals( -6,g.getNode(-6).getKey(), "The node added successfully");
    }

    @Test
    void connectBasic() {
        int v=g.edgeSize();
        g.connect(g.getNode(6).getKey(), g.getNode(100).getKey(), 20);
        assertTrue(g.hasEdge(g.getNode(6).getKey(), g.getNode(100).getKey()),"The nodes connected successfully");
        assertEquals(20,g.getEdge(g.getNode(6).getKey(), g.getNode(100).getKey()),"The weight is update successfully");
        assertEquals(v+1,g.edgeSize(),"The edge size change successfully");
    }
    @Test
    void connectExist(){
        int v=g.edgeSize();
        g.connect(g.getNode(245).getKey(), g.getNode(666).getKey(), 20);
        assertEquals(20,g.getEdge(g.getNode(245).getKey(), g.getNode(666).getKey()),"The weight is update successfully");
        assertEquals(v,g.edgeSize(),"The edge size did not change");
    }
    @Test
    void connectNull(){
        g.connect(g.getNode(245).getKey(), 7, 20);
        assertEquals(-1,g.getEdge(g.getNode(245).getKey(), 7),"One of the nodes are not exist");
        assertFalse(g.hasEdge(g.getNode(245).getKey(), 7),"One of the nodes are not exist");
    }
    @Test
    void connectWrongWeight(){
        g.connect(g.getNode(245).getKey(), g.getNode(6).getKey(), -20);
        assertFalse(g.hasEdge(g.getNode(245).getKey(), g.getNode(6).getKey()),"The weight is negative");
    }

    @Test
    void getV() {
        Collection<node_info> temp= new ArrayList<>();
        temp.add(g.getNode(-3));
        temp.add(g.getNode(100));
        temp.add(g.getNode(6011967));
        temp.add(g.getNode(245));
        temp.add(g.getNode(6));
        temp.add(g.getNode(10091998));
        temp.add(g.getNode(666));
        Collection<node_info> real=g.getV();
        assertEquals(temp.toString(),real.toString());
    }

    @Test
    void removeNodeBasic() {
        int v=g.nodeSize();
        int n=g.edgeSize();
        node_info temp =g.removeNode(g.getNode(100).getKey());
        assertEquals(100,temp.getKey(),"The corect node was removed");
        assertEquals(v-1,g.nodeSize(),"The node size update successfully");
        assertEquals(n-2,g.edgeSize(),"The edge size update successfully");
        assertFalse(g.getV(g.getNode(666).getKey()).contains(100),"The node removed successfully");
        assertFalse(g.getV(g.getNode(6011967).getKey()).contains(100),"The node removed successfully");
    }
    @Test
    void removeNodeNotExist(){
        int v=g.nodeSize();
        int n=g.edgeSize();
        node_info temp =g.removeNode(7);
        assertNull(temp,"The node is not exist");
        assertEquals(v,g.nodeSize(),"The node size did not change");
        assertEquals(n,g.edgeSize(),"The edge size did not change");
    }

    @Test
    void removeEdgeBasic() {
        int n=g.edgeSize();
        g.removeEdge(g.getNode(6).getKey(),g.getNode(-3).getKey());
        assertFalse(g.hasEdge(g.getNode(6).getKey(),g.getNode(-3).getKey()),"The node removed successfully");
        assertEquals(n-1,g.edgeSize(),"The edge size update successfully");
        n=g.edgeSize();
        g.removeEdge(g.getNode(6).getKey(),g.getNode(6).getKey());
        assertEquals(n,g.edgeSize(),"The edge size did not change");
    }
    @Test
    void removeEdgeNotExist(){
        int n=g.edgeSize();
        g.removeEdge(g.getNode(6).getKey(),g.getNode(100).getKey());
        assertEquals(n,g.edgeSize(),"The edge size did not change");
        n=g.edgeSize();
        g.removeEdge(g.getNode(6).getKey(),9);
        assertEquals(n,g.edgeSize(),"node not exist->The edge size did not change");
    }

    @Test
    void nodeSize() {
        assertEquals(7,g.nodeSize(),"Node size corect");
        g.removeNode(6);
        g.removeNode(245);
        g.removeNode(100);
        assertEquals(4,g.nodeSize(),"Node size update successfully");
        g.addNode(40);
        assertEquals(5,g.nodeSize(),"Node size update successfully");
    }

    @Test
    void edgeSize() {
        assertEquals(7,g.edgeSize(),"Edge size corect");
        g.removeEdge(6,-3);
        g.removeEdge(245,666);
        assertEquals(5,g.edgeSize(),"Edge size update successfully");
        g.connect(6,100,50);
        assertEquals(6,g.edgeSize(),"Edge size update successfully");
    }

    @Test
    void getMC() {
        assertEquals(14,g.getMC(),"MC corect");
        g.connect(245,6, 20);
        g.removeEdge(6,-3);
        g.removeEdge(6,-3);
        g.removeEdge(245,666);
        g.removeNode(6);
        g.addNode(40);
        g.addNode(41);
        g.addNode(42);
        System.out.println(g.getMC());
        assertEquals(22,g.getMC(),"MC update successfully");
    }

    @AfterAll
    public static void PrintRunTime (){
        // long s= SendSteart();
        long end = new Date().getTime();
        double dt = (end - start) / 1000.0;

        System.out.println("run time of the test is " + dt);
    }


}