package semanticNet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import makeTriplicity.*;

public class SemanticNet {
	
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	public static final int MARGIN = 100;

	public static void main(String[] args) throws Exception {
		
		
		
		ArrayList<TripleSet> tripleSetList = new ArrayList<TripleSet>();

		tripleSetList = makeTriplicity.Main.run(0,64);

		Graph<String, String> graph = new DirectedSparseGraph<String, String>();
		Dimension viewArea = new Dimension(WIDTH, HEIGHT);
		//Layout<String, String> layout = new CircleLayout<String, String>(graph);
		Layout<String, String> layout = new FRLayout<String, String>(graph, viewArea);

		for(TripleSet tripleSet : tripleSetList){

			//locationIndex++;

			String medicine = tripleSet.getMedicineName();
			String target = tripleSet.getTargetElement().getText();
			String effect = tripleSet.getEffectElement().getText();

			System.out.println("\r\n薬剤名: " + medicine);
			System.out.println("対象: " + target);
			System.out.println("効果: " + effect);

			graph.addEdge(effect, medicine, target);
		}
		
		

		VisualizationViewer<String, String> panel = 
				new VisualizationViewer<String, String>(layout, viewArea);
		
		panel.setPreferredSize(new Dimension(WIDTH+MARGIN,HEIGHT+MARGIN));

		DefaultModalGraphMouse<MyNode,MyEdge> gm = 
				new DefaultModalGraphMouse<MyNode,MyEdge>();
		//gm.setMode(ModalGraphMouse.Mode.TRANSFORMING); // default
		gm.setMode(ModalGraphMouse.Mode.PICKING);
		
		

		panel.setGraphMouse(gm);

		Transformer<String,Shape> nodeShapeTransformer = new Transformer<String,Shape>() {
			@Override
			public Shape transform(String str) {
				//return new Rectangle(-5, -10, 60, 30);
				return new Arc2D.Double(-20, -15, 100, 30, 0, 360, Arc2D.OPEN);
			}
		};

		Transformer<String,Paint> nodeFillColor = new Transformer<String,Paint>() {
			@Override
			public Paint transform(String str) {
				return Color.YELLOW;
				//return Color.WHITE;
			}
		};

		Transformer<String,String> labeller = new Transformer<String,String>() {
			@Override
			public String transform(String str) {
				return "     "+str;
			}
		};
		
		Transformer<String,Font> nodeFont = new Transformer<String,Font>() {
			@Override
			public Font transform(String str) {
				
				return new Font("ＭＳ ゴシック", Font.BOLD, 12);
			}
		};
		
		Transformer<String,Font> edgeFont = new Transformer<String,Font>() {
			@Override
			public Font transform(String str) {
				
				return new Font("ＭＳ ゴシック", Font.BOLD, 12);
			}
		};
		
//		Transformer<String,Graph> context = new Transformer<String,Graph>() {
//			@Override
//			public Graph transform(String str) {
//				
//				return new Font("ＭＳ ゴシック", Font.BOLD, 12);
//			}
//		};
		
		 Transformer<String,Paint> pickedNodePaint = 
                 new PickableVertexPaintTransformer<String>(panel.getPickedVertexState(), 
                     Color.RED, Color.BLUE);
		
		/*-------エッジ-----------*/
		
		//ラベルの文字列
		panel.getRenderContext().setEdgeLabelTransformer(labeller);
		
		//ラベルのフォント
		panel.getRenderContext().setEdgeFontTransformer(edgeFont);
		
		//線の種類
		panel.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<String, String>());
		
		//文字の出力方法
		panel.getRenderContext().getEdgeLabelRenderer().setRotateEdgeLabels(false);
		
		//panel.getRenderContext().setEdgeDrawPaintTransformer(nodeFillColor);
		
		/*-------ノード-----------*/

		//ノードの色
		panel.getRenderContext().setVertexFillPaintTransformer(nodeFillColor);

		//ノードの形
		panel.getRenderContext().setVertexShapeTransformer(nodeShapeTransformer);
		
		//ノードのフォント
		panel.getRenderContext().setVertexFontTransformer(nodeFont);
		
		//文字をノードの中央に配置
		panel.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

		//ノードの文字列
		panel.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<String>());
		
		
		

		JFrame frame = new JFrame("SemanticNet");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);

	}



}
