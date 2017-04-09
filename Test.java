package semanticNet;

//import makeTriplicity.*;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class Test {

	public static void main(String[] args) {
		Graph<MyNode,MyEdge> graph = new DirectedSparseGraph<MyNode,MyEdge>();
		MyNode n1 = new MyNode("n1");
		MyNode n2 = new MyNode("n2");
		MyNode n3 = new MyNode("n3");
		String str = "Hello";
		str += "\r\n";
		str += "World";
		graph.addEdge(new MyEdge(str), n1, n2);
		graph.addEdge(new MyEdge("e2"), n2, n3);
		
		//Layout<MyNode,MyEdge> layout = new FRLayout<MyNode,MyEdge>(graph, viewArea);

		Layout<MyNode,MyEdge> layout = new StaticLayout<MyNode,MyEdge>(graph);
		layout.setLocation(n1, new Point2D.Double(100, 100));
		layout.setLocation(n2, new Point2D.Double(200, 100));
		layout.setLocation(n3, new Point2D.Double(150, 200));

		BasicVisualizationServer<MyNode,MyEdge> panel = 
				new BasicVisualizationServer<MyNode,MyEdge>(layout, new Dimension(300, 300));

		Transformer<MyNode,Shape> nodeShapeTransformer = new Transformer<MyNode,Shape>() {
			@Override
			public Shape transform(MyNode n) {
				return new Rectangle(-15, -10, 50, 20);
			}
		};
		
		DefaultModalGraphMouse<MyNode,MyEdge> gm = 
				new DefaultModalGraphMouse<MyNode,MyEdge>();
		//gm.setMode(ModalGraphMouse.Mode.TRANSFORMING); // default
		gm.setMode(ModalGraphMouse.Mode.PICKING);  

		panel.getRenderContext().setVertexShapeTransformer(nodeShapeTransformer);
		panel.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		
		panel.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<MyNode>());
		panel.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<MyEdge>());
		panel.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<MyNode, MyEdge>());

		JFrame frame = new JFrame("Graph View: Blue Nodes");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}

}
