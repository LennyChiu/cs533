package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import concretecode.EndPoint;
import concretecode.Mission;
import concretecode.OvalShape;
import concretecode.Position;
import concretecode.RectShape;
import concretecode.ReturnPoint;
import concretecode.RoundRectShape;
import concretecode.Shape;
import concretecode.SolidLine;
import concretecode.StartPoint;
import concretecode.StoryLineNode;
import concretecode.StoryLineNodeConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author LennyChiu
 *
 */
public class UserInterface4{
	private static Logger logger = Logger.getLogger(UserInterface.class.getName());
	private static Integer trial;
	final static int ROWS_IN_LEFT_PANEL = 5;
	final static int COLUMNS_IN_LEFT_PANEL = 1;
	private static JFrame aFrame = new JFrame();
	private static JPanel rightPanel;
	private static JPanel leftPanel;
	
	
	public static void main(String[] args) {
		build();
	}
	
	/**
	 * Build the GUI
	 */
	public static void build()
	{
		//added code robin
		ShapeCanvas canvas = new ShapeCanvas();// create the canvas
		
		JMenuBar aMenuPanel = new JMenuBar();
		JMenu aData = dataMenu(canvas);
		aFrame.setJMenuBar(aMenuPanel);
		
		aMenuPanel.add(aData);
		
  		rightPanel = canvas;// change this to the canvas
		leftPanel = createPalette(canvas);
  		leftPanel.setLayout(new GridLayout(ROWS_IN_LEFT_PANEL,COLUMNS_IN_LEFT_PANEL));
  		aFrame.setLayout(new BorderLayout(3,3));
		aFrame.add("Center",rightPanel);
		aFrame.add("West",leftPanel);
		aFrame.setTitle("Domain Modeling Notation");
		aFrame.setSize(600,600);
		aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		aFrame.setVisible(true);
	}
	
	/**
	 * @return A JPanel that consists of the palette.
	 */
	private static JPanel createPalette(ShapeCanvas canvas)
	{
		final JButton diamondButton = new JButton("Mission"); // buttons for adding shapes
 		diamondButton.addActionListener(canvas);
 		
		final JButton startPointButton = new JButton("Start Point"); // buttons for adding shapes
 		startPointButton.addActionListener(canvas);
 		
		final JButton endPointButton = new JButton("End Point"); // buttons for adding shapes
 		endPointButton.addActionListener(canvas);
 		
		final JButton linkButton = new JButton("Link"); // buttons for adding shapes
 		linkButton.addActionListener(canvas);
 		
		final JButton deleteButton = new JButton("Delete"); // buttons for adding shapes
 		deleteButton.addActionListener(canvas);
 		
 		JPanel aPalettePanel = new JPanel();
 		aPalettePanel.add(diamondButton);
 		aPalettePanel.add(startPointButton);
 		aPalettePanel.add(endPointButton);
 		aPalettePanel.add(linkButton);
 		aPalettePanel.add(deleteButton);
		return aPalettePanel;
	}
	
	/**
	 * @return A file menu with the option to load or save a model.
	 */
	private static void canvasClear(ShapeCanvas canvas){
		canvas.aListOfNodes.clear();
		canvas.missions.clear();
		canvas.endpoints.clear();
		canvas.startpoints.clear();
		canvas.storylineconnection.clear();
		canvas.shapes.clear();
		
		canvas.elementClickedOn = null;					// records the last element you clicked on
		canvas.aStoryLineNode1 = null;
		canvas.aStoryLineNode2 = null;
		canvas.p1 = null;
        canvas.p2 = null;
        canvas.lineDrawn = false;
		canvas.linkOn = false;
		canvas.connectionMade = false;

		canvas.clickedOn = null;
		canvas.aSourcePosition = null;
		canvas.aTargetPosition = null;
		
		canvas.repaint();
		System.out.println("canvas cleared");
	}
	
	private static JMenu dataMenu(final ShapeCanvas canvas)
	{
		JMenu aData = new JMenu("File");
		aData.setMnemonic(KeyEvent.VK_D);

		final JMenuItem clearstuff = new JMenuItem("Clear");
		clearstuff.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
		clearstuff.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent pEvent){
				canvasClear(canvas);
			}
		});
		
		aData.add(clearstuff);
		
		final JMenuItem lLoaddisk = new JMenuItem("Load from Disk");
		lLoaddisk.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
		lLoaddisk.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent pEvent){
				logger.info("Selected Load from Disk");
				logger.info("Selected Load from Disk");
				try {
					
					JFileChooser aFc = new JFileChooser();
					//aFc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);					
					aFc.setCurrentDirectory(new File("user.home"));
					int value = aFc.showOpenDialog(lLoaddisk);
					if(value == JFileChooser.APPROVE_OPTION)
					{
						//file reading and canvas constructing part
						canvasClear(canvas);
						BufferedReader br = new BufferedReader(new FileReader(aFc.getSelectedFile()));
						String line = null;
						JSONParser parser = new JSONParser();
						
						//dummy stuff
						Position aInitializing = new Position(8, 8);	/**/
						// We are not implementing Return Point, so we needed a dummy object to represent it.
						Position aReturnPointPosition = new Position(9, 9);
						ReturnPoint aReturnPoint = new ReturnPoint(aReturnPointPosition);


						while ((line = br.readLine()) != null) {
							System.out.println(line);
							
							Object obj = parser.parse(line);			
							JSONObject jsonObject = (JSONObject) obj;			
							
							int id = ((Long) jsonObject.get("id")).intValue();
							int left =  ((Long) jsonObject.get("left")).intValue();
							int top =  ((Long) jsonObject.get("top")).intValue();
							String type = (String) jsonObject.get("class");
							
							//System.out.println("got so far");
							if (type.equals("mission")){
								String description = (String) jsonObject.get("description");
								
								// add the stuff
								Shape aShape = new RoundRectShape(left, top, 50, 50);
				        		StoryLineNode aStoryMission = new Mission(aInitializing, aReturnPoint, aShape);
				        		aStoryMission.setStoryLineNodeid(id);
				        		Mission aMission = (Mission) aStoryMission;
				        		aMission.setDescription(description);
				        		aMission.getShape().setShapeId(aStoryMission.getStoryLineNodeid());
				        		aShape.setShapeId(aStoryMission.getStoryLineNodeid());
				        		canvas.aListOfNodes.add(aMission);
				        		canvas.missions.add(aMission);
				        		
				        		canvas.addShape(aShape);
				        		canvas.linkOn = false;
				        		canvas.p1 = null;
								
							}
							else if (type.equals("start")){
								
								Shape aShape = new OvalShape(left, top, 30, 30);
				        		StoryLineNode aStoryStartPoint = new StartPoint(aInitializing, aShape);
				        		aStoryStartPoint.setStoryLineNodeid(id);
				        		StartPoint aStartPoint = (StartPoint) aStoryStartPoint;
				        		aStartPoint.getShape().setShapeId(aStoryStartPoint.getStoryLineNodeid());
				        		aShape.setShapeId(aStoryStartPoint.getStoryLineNodeid());
				        		canvas.aListOfNodes.add(aStartPoint);
				        		canvas.startpoints.add(aStartPoint);
				        		
				        		canvas.addShape(aShape);
				        		canvas.linkOn = false;
				        		canvas.p1 = null;
							}
							else if (type.equals("end")){
								Shape aShape = new RectShape(left, top, 10, 30);
				        		StoryLineNode aStoryEndPoint = new EndPoint(aInitializing, aShape);
				        		aStoryEndPoint.setStoryLineNodeid(id);
				        		EndPoint aEndPoint = (EndPoint) aStoryEndPoint;
				        		aEndPoint.getShape().setShapeId(aStoryEndPoint.getStoryLineNodeid());
				        		aShape.setShapeId(aStoryEndPoint.getStoryLineNodeid());
				        		canvas.aListOfNodes.add(aEndPoint);
				        		canvas.endpoints.add(aEndPoint);		        		
				        		
				        		canvas.addShape(aShape);
				        		canvas.linkOn = false;
				        		canvas.p1 = null;
							}
							else if (type.equals("link")){
								int fromId = ((Long) jsonObject.get("fromId")).intValue();
								int toId =  ((Long) jsonObject.get("toId")).intValue();
								int posX =  ((Long) jsonObject.get("posX")).intValue();
								int posY =  ((Long) jsonObject.get("posY")).intValue();
								int p1X =  ((Long) jsonObject.get("p1X")).intValue();
								int p1Y =  ((Long) jsonObject.get("p1Y")).intValue();
								int p2X =  ((Long) jsonObject.get("p2X")).intValue();
								int p2Y =  ((Long) jsonObject.get("p2Y")).intValue();
								
								Position nodePos = new Position(posX,posY);
								Point point1 = new Point(p1X,p1Y);
								Point point2 = new Point(p2X,p2Y);
								StoryLineNode n1 = null;
								StoryLineNode n2 = null;
								
				                for (StoryLineNode node : canvas.aListOfNodes ){
				                	if (node.getStoryLineNodeid() == fromId){
				                		n1 = node;
				                	}
				                	else if (node.getStoryLineNodeid() == toId){
				                		n2 = node;
				                	}
				                }
				                Shape aShape = new SolidLine(p1X,p1Y,p2X,p2Y);
				                StoryLineNode aStoryLine = new StoryLineNodeConnection (nodePos, point1, point2, aShape,n2,n1);
				                StoryLineNodeConnection connect = (StoryLineNodeConnection) aStoryLine;
				        		connect.getShape().setShapeId(aStoryLine.getStoryLineNodeid());
				        		aShape.setShapeId(aStoryLine.getStoryLineNodeid());
				        		canvas.aListOfNodes.add(connect);
				        		canvas.storylineconnection.add(connect);
				        		
				                canvas.addShape(aShape);
				                //canvas.repaint();						
							}
						}// end while
					}
					else
					{
						logger.info("Load from Disk command cancelled by user.");
					}			

				} catch (Exception e) {
					System.out.print("parse failed");
				}
			}
		});
		aData.add(lLoaddisk);
		
		final JMenuItem lExport = new JMenuItem("Save to Disk");
		lExport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		lExport.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent pEvent){
				logger.info("Selected Export");
				
				//make the json object list
				ArrayList<StoryLineNode> aListOfNodes = canvas.aListOfNodes;
				ArrayList<JSONObject> objectlist = new ArrayList<JSONObject>();

				
				for(int i = 0; i<aListOfNodes.size();i++){
				
					StoryLineNode node = aListOfNodes.get(i);
					int id = node.getStoryLineNodeid();
					int left = 10;
					int top = 10;
					String description = null;
					
					//write to the JSON
					JSONObject obj = new JSONObject();
					
					if(node instanceof Mission){
						Mission m = (Mission) node;
						left = m.getShape().getLeft();
						top = m.getShape().getTop();
						description = m.getDescription();
						
						obj.put("class", "mission");
						obj.put("description", description);

						

					}
					else if(node instanceof StartPoint){
						StartPoint s = (StartPoint) node;
						left = s.getShape().getLeft();
						top = s.getShape().getTop();
						
						obj.put("class", "start");

					}
					else if(node instanceof EndPoint){
						EndPoint e = (EndPoint) node;
						left = e.getShape().getLeft();
						top = e.getShape().getTop();
						
						obj.put("class", "end");

						
					}
					else if (node instanceof StoryLineNodeConnection){
						StoryLineNodeConnection link = (StoryLineNodeConnection) node;
						int fromId = link.getSource().getStoryLineNodeid();
						int toId = link.getTarget().getStoryLineNodeid();
						
						int posX = link.getPosition().getX();
						int posY = link.getPosition().getY();
						
//						int p1X = (int)link.getP1().getX();
//						int p1Y = (int)link.getP1().getY();
//						
//						int p2X = (int)link.getP2().getX();
//						int p2Y = (int)link.getP2().getY();
						
						int p1X = (int)link.getShape().getLeft();
						int p1Y = (int)link.getShape().getTop();
						
						int p2X = (int)link.getShape().getWidth();
						int p2Y = (int)link.getShape().getHeight();
						
						obj.put("class", "link");
						obj.put("fromId",new Integer(fromId));
						obj.put("toId",new Integer(toId));
						obj.put("posX",new Integer(posX));
						obj.put("posY",new Integer(posY));
						obj.put("p1X",new Integer(p1X));
						obj.put("p1Y",new Integer(p1Y));
						obj.put("p2X",new Integer(p2X));
						obj.put("p2Y",new Integer(p2Y));
					}
					
						
					//Make the JSON object
					obj.put("id", new Integer(id));
					obj.put("left", new Integer (left));
					obj.put("top", new Integer (top));
					objectlist.add(obj);
					
					//add the node connections

				}// end for of list of nodes
		
				// file chooser section to save file
				JFileChooser chooser = new JFileChooser();
				//chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setCurrentDirectory(new File("user.home"));
				int value = chooser.showSaveDialog(lExport);
				if (value == JFileChooser.APPROVE_OPTION) 
				{
					// Need to implement.
					try
					{
						File dataLocation = chooser.getSelectedFile();

						String newPath = dataLocation.getAbsolutePath()+ File.separator;
						logger.info("Saving User Profile to disk location: " + newPath);
						
						String filename = dataLocation.toString();
						filename = filename.replaceAll(".json", "");
						
						FileWriter file =  new FileWriter(filename + ".json");
						for (int midx = 0; midx <objectlist.size();midx++){
							file.write(objectlist.get(midx).toJSONString()+"\n");
							file.flush();
						}
						file.close();

					}
					catch(NullPointerException e)
					{
						logger.warning("Error exporting file.");
					}
					catch (IOException e){
						logger.warning("IOException");
					}
					
					File fileToSave = chooser.getCurrentDirectory();
					logger.info("Save file to " + fileToSave.getAbsolutePath());
				}
		        else 
		        {
		        	logger.info("Save/Export command cancelled by user.");
		        }				

			}// end action event
		});
		aData.add(lExport);
		return aData;
	}
	
	/* 
	 * EXTERNAL_CODE 1
	 * Adapted from: David Eck, http://math.hws.edu/eck/cs124/javanotes3/source/ShapeDraw.java, Accessed: 8, November, 2014
	 * ShapeCanvas is the canvas that will generated for drawing the shapes.
	 */
	static class ShapeCanvas extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
		public ArrayList<StoryLineNode> aListOfNodes = new ArrayList<StoryLineNode>();
		public ArrayList<Mission> missions = new ArrayList<Mission>();
		public ArrayList<EndPoint> endpoints = new ArrayList<EndPoint>();
		public ArrayList<StartPoint> startpoints = new ArrayList<StartPoint>();
		public ArrayList<StoryLineNodeConnection> storylineconnection = new ArrayList<StoryLineNodeConnection>();	// Used to store the sequence of nodes connected with each other
		public ArrayList<Shape> shapes = new ArrayList<Shape>(); 	//holds the list of shapes that are displayed on the canvas
		Color currentColor = Color.black;
		Shape elementClickedOn = null; 								// records the last element you clicked on
		StoryLineNode aStoryLineNode1 = null;
		StoryLineNode aStoryLineNode2 = null;
		Point p1 = null;
        Point p2 = null;
        boolean lineDrawn = false;
		boolean linkOn = false;
		boolean connectionMade = false;
		Position clickedOn = null;
		Position aSourcePosition = null;
		Position aTargetPosition = null;
		private Shape highlighted_shape = null;
		private Color highlighted_color = Color.gray;
		private Color color_prehighlight = null;
		
		int fromID = 0;
		int toID = 0;
		
		ShapeCanvas() {
			setBackground(Color.white);
			addMouseListener(this);
			addMouseMotionListener(this);
		}
		private boolean highlight(Shape s){			
			if(this.highlighted_shape != null){
				this.highlighted_shape.setColor(this.color_prehighlight);						
			}
			this.highlighted_shape = s;
			this.color_prehighlight = this.highlighted_shape.getColor();
			this.highlighted_shape.setColor(this.highlighted_color);
			this.highlighted_shape.draw(this.getGraphics());
			return true;
		}
		private boolean unhighlight(){
			Shape s = this.highlighted_shape;
			if(s==null)return true;
			s.setColor(this.color_prehighlight);
			this.highlighted_shape = null;
			s.draw(this.getGraphics());
			return true;
		}
		// Draw the canvas, then draw every shape in the shapes list.
		public void paintComponent(Graphics g) {
			g.setColor(getBackground());
			g.fillRect(0, 0, getSize().width, getSize().height);
			int top = shapes.size();
			
			// Draw every shape in the shapes list
			for (int i = 0; i < top; i++) {
				Shape s = (Shape) shapes.get(i);
				s.draw(g);
			}
			System.out.println("number of shapes: " + top);
		}
		
		// Response to the action event generated from the button clicks.
		// There are 3 shapes to be added, 1 link creation button, and a delete button.
		public void actionPerformed(ActionEvent evt) {
			// Test Dummy Objects
			Position aInitializing = new Position(8, 8);	/**/
			// We are not implementing Return Point, so we needed a dummy object to represent it.
			Position aReturnPointPosition = new Position(9, 9);
			ReturnPoint aReturnPoint = new ReturnPoint(aReturnPointPosition);
			
	        	String command = evt.getActionCommand();
        	   
	        	if (command.equals("Mission"))
	        	{
	        		Shape aShape = new RoundRectShape(10, 10, 50, 50);
	        		StoryLineNode aStoryMission = new Mission(aInitializing, aReturnPoint, aShape);
	        		Mission aMission = (Mission) aStoryMission;
	        		aMission.getShape().setShapeId(aStoryMission.getStoryLineNodeid());
//	        		System.out.println("Mission id: " + aMission.getStoryLineNodeid());
	        		aShape.setShapeId(aStoryMission.getStoryLineNodeid());
//	        		System.out.println("Shape id: " + aShape.getShapeId());
	        		aListOfNodes.add(aMission);
	        		missions.add(aMission);
	        		
	        		addShape(aShape);
	        		linkOn = false;
	        		p1 = null;
	        	}
	        	else if (command.equals("Start Point"))
	        	{
	        		Shape aShape = new OvalShape(10, 10, 30, 30);
	        		StoryLineNode aStoryStartPoint = new StartPoint(aInitializing, aShape);
	        		StartPoint aStartPoint = (StartPoint) aStoryStartPoint;
	        		aStartPoint.getShape().setShapeId(aStoryStartPoint.getStoryLineNodeid());
//	        		System.out.println("StartPoint id: " + aStartPoint.getStoryLineNodeid());
	        		aShape.setShapeId(aStoryStartPoint.getStoryLineNodeid());
//	        		System.out.println("Shape id: " + aShape.getShapeId());
	        		aListOfNodes.add(aStartPoint);
	        		startpoints.add(aStartPoint);
	        		
	        		addShape(aShape);
	        		linkOn = false;
	        		p1 = null;
	        	}
	        	else if (command.equals("End Point"))
	        	{
	        		Shape aShape = new RectShape(10, 10, 10, 30);
	        		StoryLineNode aStoryEndPoint = new EndPoint(aInitializing, aShape);
	        		EndPoint aEndPoint = (EndPoint) aStoryEndPoint;
	        		aEndPoint.getShape().setShapeId(aStoryEndPoint.getStoryLineNodeid());
//	        		System.out.println("EndPoint id: " + aEndPoint.getStoryLineNodeid());
	        		aShape.setShapeId(aStoryEndPoint.getStoryLineNodeid());
//	        		System.out.println("Shape id: " + aShape.getShapeId());
	        		aListOfNodes.add(aEndPoint);
	        		endpoints.add(aEndPoint);
	        		
	        		addShape(aShape);
	        		linkOn = false;
	        		p1 = null;
	        	}
	        	else if (command.equals("Link"))
	        	{
	        		System.out.println("Pressed Link: Link On = " + linkOn);
	        		linkOn = true;
	        	}
	        	else if (command.equals("Delete"))
	        	{
	        		System.out.println("Pressed Delete");
	        		linkOn = false;
	        		p1 = null;
	        		
	        		try
	        		{
	        			System.out.println("Element clicked on is: " + elementClickedOn.getClass().getSimpleName());
	        		}
	        		catch (NullPointerException e)
	        		{
	        			System.out.println("nothing was clicked on");
	        		}
	        		
	        		if(elementClickedOn != null)
	        		{
	        			deleteElement();
	        		}
	        	}
         	}
		
		// Add shape to the shape list which will then be repainted on the canvas. 
		void addShape(Shape shape) 
		{
			shape.setColor(currentColor);
			shapes.add(shape);
			repaint();
		}
		
		// Remove each deleted element from their respective list.
		void deleteElement()
		{
			int shapeid = -1;
			int missionid = -1;
			int startpointid = -1;
			int endpointid = -1;
			int lineid = -1;
			System.out.println("Step 1:");
			shapeid = elementClickedOn.getShapeId();
			System.out.println("Element clicked on shapeid: " + shapeid);
			
			for(int q = 0; q < missions.size(); q++)
			{
				// element clicked on id is equal to a shape in the mission list
				if(shapeid == missions.get(q).getShape().getShapeId())
				{
					System.out.println("Step 2:");
					missionid = missions.get(q).getShape().getShapeId();
					System.out.println("Mission id retrieve is: " + missionid);
					// removed the mission form the mission list
					missions.remove(q);
				}
			}
			
			for(int w = 0; w < startpoints.size(); w++)
			{
				// element clicked on id is equal to a shape in the startpoint list
				if(shapeid == startpoints.get(w).getShape().getShapeId())
				{
					System.out.println("Step 2:");
					startpointid = startpoints.get(w).getShape().getShapeId();
					System.out.println("Startpoint id retrieve is: " + startpointid);
					// removed the mission form the mission list
					startpoints.remove(w);
				}
			}
			
			for(int w = 0; w < endpoints.size(); w++)
			{
				// element clicked on id is equal to a shape in the endpoint list
				if(shapeid == endpoints.get(w).getShape().getShapeId())
				{
					System.out.println("Step 2:");
					endpointid = endpoints.get(w).getShape().getShapeId();
					System.out.println("Endpoint id retrieve is: " + endpointid);
					// removed the mission form the mission list
					endpoints.remove(w);
				}
			}
			
			for(int u = 0; u < storylineconnection.size(); u++)
			{
				// element clicked on id is equal to a shape in the storyline list
				if(shapeid == storylineconnection.get(u).getShape().getShapeId())
				{
					System.out.println("Step 2:");
					lineid = storylineconnection.get(u).getShape().getShapeId();
					System.out.println("StoryLine id retrieve is: " + lineid);
					// removed the mission form the mission list
					storylineconnection.remove(u);
				}
			}
			
			System.out.println("Want to delete the currently selected item: " + elementClickedOn.getClass().getSimpleName());
			for(int index = 0; index < shapes.size(); index++)
			{
					System.out.println("Step 3:");
					if(shapes.get(index).getClass().getSimpleName().equals(elementClickedOn.getClass().getSimpleName()) && (shapes.get(index).getShapeId() == missionid 
							|| shapes.get(index).getShapeId() == startpointid
							|| shapes.get(index).getShapeId() == endpointid
							|| shapes.get(index).getShapeId() == lineid))
					{
//						System.out.println("shapes size before deletion: " + shapes.size());
						System.out.println("Removed at index: " + index);
						shapes.remove(index);
//						System.out.println("shapes size after deletion: " + shapes.size());
//						break;	// deleted the wanted element, do exit loop
					}
					
					Mission mis = null;
					StartPoint sp = null;
					EndPoint ep = null;
					StoryLineNodeConnection slnc = null;
					System.out.println("aListOfNodes size before deletion: " + aListOfNodes.size());
					if(aListOfNodes.get(index).getClass().getSimpleName().equals("Mission"))
					{
						mis = (Mission) aListOfNodes.get(index);
						if(mis.getShape().getShapeId() == missionid)
						{
							System.out.println("Removed aListOfNodes at index: " + index);
							aListOfNodes.remove(index);
							break;	// deleted the wanted element, do exit loop
						}
					}
					else if(aListOfNodes.get(index).getClass().getSimpleName().equals("StartPoint"))
					{
						sp = (StartPoint) aListOfNodes.get(index);
						if(sp.getShape().getShapeId() == startpointid)
						{
							System.out.println("Removed aListOfNodes at index: " + index);
							aListOfNodes.remove(index);
							break;	// deleted the wanted element, do exit loop
						}
					}
					else if(aListOfNodes.get(index).getClass().getSimpleName().equals("EndPoint"))
					{
						ep = (EndPoint) aListOfNodes.get(index);
						if(ep.getShape().getShapeId() == endpointid)
						{
							System.out.println("Removed aListOfNodes at index: " + index);
							aListOfNodes.remove(index);
							break;	// deleted the wanted element, do exit loop
						}
					}
					else if (aListOfNodes.get(index).getClass().getSimpleName().equals("StoryLineNodeConnection"))
					{
						slnc = (StoryLineNodeConnection) aListOfNodes.get(index);
						if(slnc.getShape().getShapeId() == lineid)
						{
							System.out.println("Removed aListOfNodes at index: " + index);
							aListOfNodes.remove(index);
							break;	// deleted the wanted element, do exit loop
						}
					}
			}
			
			System.out.println("aListOfNodes size after deletion: " + aListOfNodes.size());
			
			// To see what remains in the list
//			for(int index = 0; index < shapes.size(); index++)
//			{
//				System.out.println(shapes.get(index).getClass().getSimpleName());
//			}
			elementClickedOn = null; 	// set the elementClickedOn back to null
			shapeid = -1;
			missionid = -1;
			startpointid = -1;
			endpointid = -1;
			lineid = -1;
			repaint();					// update the canvas after deleting element
		}
		
		// Adapted from: David Eck, http://math.hws.edu/eck/cs124/javanotes3/source/ShapeDraw.java, Accessed: 8, November, 2014
		// MouseListener, MouseMotionListener.
		Shape shapeDrag = null;
		// x, y coordinates of old position of mouse 
		int oldPositionX; 	
		int oldPositionY;
	      
		// David Eck, http://math.hws.edu/eck/cs124/javanotes3/source/ShapeDraw.java, Accessed: 8, November, 2014
		// Mouse dragging motion
		@Override
		public void mouseDragged(MouseEvent evt) {
			int x = evt.getX();
			int y = evt.getY();
			if (shapeDrag != null) {
				shapeDrag.moveBy(x - oldPositionX, y - oldPositionY);
				oldPositionX = x;
				oldPositionY = y;
				repaint();
			}
		}
		
		@Override
		// Adapted from: David Eck, http://math.hws.edu/eck/cs124/javanotes3/source/ShapeDraw.java, Accessed: 8, November, 2014
		// Mouse clicked and released
		// User has finished dragging the shape and released the mouse.
		// User clicks on two points consisting of other shapes and creates a line between them to demonstrate
		// a connection between these two shapes.
		public void mouseClicked(MouseEvent evt) {
			int x = evt.getX();
			int y = evt.getY();
			clickedOn = new Position(x, y);
			// Set elementClickedOn to the shape you clicked on at that point, if any.
			boolean flag_click_on_shape = false;
			for (int i = shapes.size() - 1; i >= 0; i--) { // check shapes from front to back
				Shape s = (Shape) shapes.get(i);
				if (s.containsPoint(x, y)) {
					elementClickedOn = s;	// Set element clicked on
					this.highlight(s);
					flag_click_on_shape = true;
					
					System.out.println(elementClickedOn.getClass().getSimpleName());
					//repaint();
					break;
				}
			}
			if(!flag_click_on_shape)this.unhighlight();
						
			if (shapeDrag != null) {
				shapeDrag.moveBy(x - oldPositionX, y - oldPositionY);
				if (shapeDrag.getLeft() >= getSize().width
						|| shapeDrag.getTop() >= getSize().height
						|| shapeDrag.getLeft() + shapeDrag.getWidth() < 0
						|| shapeDrag.getTop() + shapeDrag.getHeight() < 0) { 			// shape is off-screen
					shapes.remove(shapeDrag); 											// remove shape from list of shapes
				}
				
				shapeDrag = null;
				repaint();
			}
			
			System.out.println("Link On in MouseClicked Method = " + linkOn);
			if(linkOn && elementClickedOn != null)
			{
				// Drawing line
				System.out.println("Line drawn: " + lineDrawn);
	            if(p1 == null || lineDrawn){
	                if(lineDrawn){
	                    p1 = null;
	                    p2 = null;
	                    lineDrawn = false;
	                }
	                p1 = evt.getPoint();
	                System.out.println("P1: " + "("+ p1.x +","+ p1.y + ")" );
	                aSourcePosition = new Position((int) p1.getX(),(int)p1.getY());	// initial position clicked on
	                
	                fromID = elementClickedOn.getShapeId();
	                //System.out.println("LOOK " + fromID);
	                for (StoryLineNode node : aListOfNodes ){
	                	if (node.getStoryLineNodeid() == fromID){
	                		aStoryLineNode1 = node;
	                		//System.out.println("THIS "+ aStoryLineNode1.getStoryLineNodeid());
	                	}
	                }
	                
	                //aStoryLineNode1 = new StoryLineNode(aSourcePosition);
	                elementClickedOn = null;
	            }
	            else
	            {            	
	                toID = elementClickedOn.getShapeId();
	                //System.out.println("LOOK " + toID);
	                
	                for (StoryLineNode node : aListOfNodes ){
	                	if (node.getStoryLineNodeid() == toID){
	                		aStoryLineNode2 = node;
	                		//System.out.println( "THIS "+ aStoryLineNode2.getStoryLineNodeid());
	                	}
	                }
	                
	                p2 = evt.getPoint();
	                System.out.println("P2: " + "("+ p2.x +","+ p2.y + ")" );
	                //aTargetPosition = new Position((int) p2.getX(),(int)p2.getY());	// final position clicked on
	                //aStoryLineNode2 = new StoryLineNode(aTargetPosition);
	                connectionMade = true;
	                Shape aShape = new SolidLine((int)p1.getX(),(int)p1.getY(),(int)p2.getX(),(int)p2.getY());
	        		StoryLineNode aStoryLine = new StoryLineNodeConnection(clickedOn, p1, p2, aShape, aStoryLineNode2, aStoryLineNode1);
	        		StoryLineNodeConnection connect = (StoryLineNodeConnection) aStoryLine;
	        		connect.getShape().setShapeId(aStoryLine.getStoryLineNodeid());
	        		System.out.println("StoryLine id: " + connect.getStoryLineNodeid());
	        		aShape.setShapeId(aStoryLine.getStoryLineNodeid());
	        		System.out.println("Shape id: " + aShape.getShapeId());
	        		aListOfNodes.add(connect);
	        		storylineconnection.add(connect);
	        		
	                addShape(aShape);
	    			linkOn = false;				// set linkOn to false because line is already drawn
	    			lineDrawn = true; 			// set lineDrawn to true to indicate line was drawn
	    			repaint();
	            }
	            
	        	System.out.println("Connection made: " + connectionMade);
	        	// Reset both storyline nodes to null and connectionMade to false;
	        	if(connectionMade)
	        	{
	        		connectionMade = false;
	        		aStoryLineNode1 = null;
	        		aStoryLineNode2 = null;
	        	}
			}
			
			Shape aShape = null;
			String classname = "";
			for (int i = shapes.size() - 1; i >= 0; i--) { 
				Shape s = (Shape) shapes.get(i);
				if (s.containsPoint(x, y)) {
					aShape = s;
					classname = aShape.getClass().getSimpleName();
				}
			}
			
			// Checking if a RoundRectShape is clicked twice in order to generate a JOptionPane allowing the
			// user to input Mission description.
			if(evt.getClickCount() == 2 && classname.equals("RoundRectShape"))
			{
				Mission m = null;
				String description = " ";
				for(int i = 0; i < missions.size(); i++)
				{
					m = (Mission) missions.get(i);
					Shape s = m.getShape();
					if(s == aShape)
					{
						description = JOptionPane.showInputDialog("Description: " + m.getDescription());
						break;
					}
				}
				
				if(description == null)
				{
					System.out.println("Pressed cancel");
				}
				else
				{
					JOptionPane.showMessageDialog(aFrame, "Description Succesfully Updated!");
					for(int j = 0; j < missions.size(); j++)
					{
						m = (Mission) missions.get(j);
						Shape s = m.getShape();
						if(s == aShape)
						{
							m.setDescription(description);
						}
					}
					// To check all messages of each Mission
//					for(int j = missions.size() - 1; j >= 0; j--)
//					{
//						System.out.println("Index "+ j + "Description: " + missions.get(j).getDescription());
//					}
				}
	        }
		}

		// David Eck, http://math.hws.edu/eck/cs124/javanotes3/source/ShapeDraw.java, Accessed: 8, November, 2014
		// Finds the shape clicked on by the users mouse press.
		// User can now start dragging the shape.
		@Override
		public void mousePressed(MouseEvent evt) {
			int x = evt.getX(); 							// x-coordinate of point where mouse was clicked
			int y = evt.getY(); 							// y-coordinate of point
			for (int i = shapes.size() - 1; i >= 0; i--) { 	// check shapes from
															// front to back
				Shape s = (Shape) shapes.get(i);
				if (s.containsPoint(x, y)) 
				{
					System.out.println(s.getClass().getSimpleName());
					shapeDrag = s;
					oldPositionX = x;
					oldPositionY = y;
//					if (evt.isShiftDown())
//					{ 										// Bring the shape to the front by
//															// moving it to
//						shapes.remove(s); 					// the end of the list of shapes.
//						shapes.add(s);
//						repaint(); 							// repaint canvas to show shape in front of
//															// other shapes
//					}
					return;
				}
			}
		}

		@Override
		public void mouseMoved(MouseEvent evt) {}
		
		@Override
		public void mouseEntered(MouseEvent evt) {}

		@Override
		public void mouseExited(MouseEvent evt) {}
		
		@Override
		public void mouseReleased(MouseEvent evt) {
		}
		
	}// end shapecanvas
}
	
