package CreateGui;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import java.util.*;
import java.awt.Polygon;
import java.io.File;
import java.io.IOException;
import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.MouseEvent;
import java.awt.Rectangle;
import javax.swing.ButtonGroup;

@SuppressWarnings("serial")
public class StringPanel extends JFrame   {
	Connection mainConnection= null;
	Statement mainStatement= null;
	ResultSet mainResultSet= null;
	List<Point> pointList=new ArrayList<Point>();
	JPanel panel,imageIcon; 
	JRadioButton rdbtnPointQuery,rdbtnRangeQuery,rdbtnSurroundingStudents,rdbtnWholeRegion,rdbtnEmergencyQuery;
	JButton btnSubmitQuery;
	JCheckBox chckbxAs;
	JCheckBox chckbxBuilding;
	JCheckBox chckbxStudent;
	TextArea textArea;
	String displayResult;
	boolean FlagRange=false;
	int flagAs=0;
	int flagStud=0;
	int flagBuild=0;
	boolean glFlag;
	String ResultText="nothing";
	int xCordi=0;
	int yCordi=0;
	JTextArea mTextX,mTextY;
	JLabel labelX,labelY;
	public String ResultTextMarker=null;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	public StringPanel() throws IOException {
		setBounds(new Rectangle(0, 0, 1100, 768));
		getContentPane().setLayout(null);
		ConnectToDB();
		class MouseListeningInImage
		{
			public MouseListeningInImage()
			{
				imageIcon.addMouseListener(new MouseListener()
				{
					@Override
					public void mouseClicked(MouseEvent e)
					{
						if((e.getModifiers() & InputEvent.BUTTON1_MASK)!=0)
						{
							
							if(displayResult.equalsIgnoreCase("drawPolygon"))
							{
								xCordi=e.getX();
								yCordi=e.getY();
								pointList.add(new Point(xCordi,yCordi));
								imageIcon=new ImagePanel(ResultText);
								imageIcon.setBounds(10,10,820,580);
								imageIcon.setLayout(null);
								panel.revalidate();
								panel.repaint();
							}
							if(displayResult.equalsIgnoreCase("Coordinates"))
							{
								xCordi=e.getX();
								yCordi=e.getY();
								glFlag=true;
								imageIcon=new ImagePanel(ResultText);	//inside calling of function paintComponent takes place.
								imageIcon.setBounds(10,10,820,580);
								imageIcon.setLayout(null);
								//panel.add(imageIcon); 
								panel.revalidate();
								panel.repaint(); 
							}
							if(displayResult.equalsIgnoreCase("Emergency"))
							{
								xCordi=e.getX();
								yCordi=e.getY();
								glFlag=true;
								ResultText="empty";
								imageIcon=new ImagePanel(ResultText);
								imageIcon.setBounds(10,10,820,580);
								imageIcon.setLayout(null);
								panel.revalidate();
								panel.repaint();
							}
							if(displayResult.equalsIgnoreCase("nearAnnSystem"))
							{
								// System.out.println("YP");
								xCordi=e.getX();
								yCordi=e.getY();
								glFlag=true;
								imageIcon=new ImagePanel();
								imageIcon.setBounds(10,10,820,580);
								imageIcon.setLayout(null);
								panel.revalidate();
								panel.repaint();
							}
							
						}
						if((e.getModifiers() & InputEvent.BUTTON3_MASK)!=0)
						{
							FlagRange=true;
							imageIcon=new ImagePanel();
							imageIcon.setBounds(10,10,820,580);
							imageIcon.setLayout(null);
							panel.revalidate();
							panel.repaint();
						}
					}
					@Override
					public void mouseEntered(MouseEvent arg0) {
						// TODO Auto-generated method stub
					}
					@Override
					public void mouseExited(MouseEvent arg0) {
						// TODO Auto-generated method stub
					}
					@Override
					public void mousePressed(MouseEvent arg0) {
						// TODO Auto-generated method stub
					}
					@Override
					public void mouseReleased(MouseEvent arg0) {
						// TODO Auto-generated method stub
					}
				});
			}
		}
		class AddMouseMotionListenerToPanel
		{
			public AddMouseMotionListenerToPanel(JPanel panel)
			{
				panel.addMouseMotionListener(new java.awt.event.MouseAdapter()
				{
					public void mouseMoved(MouseEvent e)
					{
						String x=String.valueOf(e.getPoint().getX());
						String y=String.valueOf(e.getPoint().getY());
						mTextX.setText(x);
						mTextY.setText(y);
					}
				});
				panel.addMouseListener(new java.awt.event.MouseAdapter()
				{
					public void mouseExited(MouseEvent e)
					{
						String x="";
						String y="";
						mTextX.setText(x);
						mTextY.setText(y);
					}
				});
			}
		}
		rdbtnPointQuery = new JRadioButton("Point Query");
		buttonGroup.add(rdbtnPointQuery);
		rdbtnPointQuery.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnPointQuery.setBounds(887, 234, 129, 54);
		getContentPane().add(rdbtnPointQuery);
		rdbtnPointQuery.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				textArea.setText("");
				panel.remove(imageIcon);
				imageIcon=new ImagePanel();
				imageIcon.setBounds(10,10,820,580);
				xCordi=1600;
				yCordi=1600;
				ResultText="nothing";
				displayResult="Coordinates";
				imageIcon.setLayout(null);
				panel.add(imageIcon);
				new MouseListeningInImage();
				panel.revalidate();
				panel.repaint();

			}
		}
				);
		rdbtnRangeQuery = new JRadioButton("Range Query");
		buttonGroup.add(rdbtnRangeQuery);
		rdbtnRangeQuery.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnRangeQuery.setBounds(887, 450, 151, 54);
		getContentPane().add(rdbtnRangeQuery);
		rdbtnRangeQuery.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				textArea.setText("");
				panel.remove(imageIcon);
				ResultText="nothing";
				imageIcon=new ImagePanel(ResultText);
				xCordi=1600;
				yCordi=1600;
				new AddMouseMotionListenerToPanel(imageIcon);
				imageIcon.setBounds(10,10,820,580);
				imageIcon.setLayout(null);
				displayResult="drawPolygon";
				panel.add(imageIcon);			      
				new MouseListeningInImage();
				pointList.clear();
				panel.revalidate();
				panel.repaint(); 
			}
		}
				);
		rdbtnSurroundingStudents = new JRadioButton("Surrounding Students");
		buttonGroup.add(rdbtnSurroundingStudents);
		rdbtnSurroundingStudents.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnSurroundingStudents.setBounds(887, 388, 189, 59);
		getContentPane().add(rdbtnSurroundingStudents);
		rdbtnSurroundingStudents.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				textArea.setText("");
				panel.remove(imageIcon);
				ResultText="nothing";
				imageIcon=new ImagePanel();
				xCordi=1600;
				yCordi=1600;
				new AddMouseMotionListenerToPanel(imageIcon);
				imageIcon.setBounds(10,10,820,580);
				imageIcon.setLayout(null);
				displayResult="nearAnnSystem";	    		  
				panel.add(imageIcon); 
				new MouseListeningInImage();
				panel.revalidate();
				panel.repaint(); 
			}
		}
				);

		rdbtnWholeRegion = new JRadioButton("Whole Region");
		buttonGroup.add(rdbtnWholeRegion);
		rdbtnWholeRegion.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnWholeRegion.setBounds(887, 337, 151, 48);
		getContentPane().add(rdbtnWholeRegion);
		rdbtnWholeRegion.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				textArea.setText(" ");
				panel.repaint(); 
				panel.remove(imageIcon);
				imageIcon=new ImagePanel();
				imageIcon.setBounds(10,10,820,580);
				imageIcon.setLayout(null);
				panel.add(imageIcon); 
				panel.revalidate();

			}
		}
				);
		rdbtnEmergencyQuery = new JRadioButton("Emergency Query");
		buttonGroup.add(rdbtnEmergencyQuery);
		rdbtnEmergencyQuery.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnEmergencyQuery.setBounds(887, 280, 151, 54);
		getContentPane().add(rdbtnEmergencyQuery);
		rdbtnEmergencyQuery.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				textArea.setText("");
				panel.remove(imageIcon);
				ResultText="nothing";
				imageIcon=new ImagePanel(ResultText);
				new AddMouseMotionListenerToPanel(imageIcon);
				imageIcon.setBounds(10,10,820,580);
				imageIcon.setLayout(null);
				displayResult="Emergency";
				xCordi=1600;
				yCordi=1600;
				panel.add(imageIcon); 
				new MouseListeningInImage();
				panel.revalidate();
				panel.repaint(); 
			}
		}
				);
		btnSubmitQuery = new JButton("Submit Query");
		btnSubmitQuery.setBounds(859, 511, 179, 23);
		getContentPane().add(btnSubmitQuery);
		btnSubmitQuery.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(rdbtnPointQuery.isSelected())
				{
					ResultText=rdbtnPointQuery.getText();
				}
				if(rdbtnWholeRegion.isSelected())
				{
					ResultText=rdbtnWholeRegion.getText();
				}
				if(rdbtnSurroundingStudents.isSelected())
				{
					ResultText=rdbtnSurroundingStudents.getText();
				}
				if(rdbtnRangeQuery.isSelected())
				{
					ResultText=rdbtnRangeQuery.getText();
				}
				if(rdbtnEmergencyQuery.isSelected())
				{
					ResultText=rdbtnEmergencyQuery.getText();
				}

				panel.remove(imageIcon);
				imageIcon=new ImagePanel(ResultText);	//inside calling of function paintComponent takes place.
				imageIcon.setBounds(10,10,820,580);
				imageIcon.setLayout(null);
				panel.add(imageIcon); 
				panel.revalidate();
				panel.repaint(); 
			}
		});
		panel = new JPanel(new BorderLayout());
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, new Color(64, 64, 64), new Color(64, 64, 64), new Color(64, 64, 64), new Color(64, 64, 64)));
		panel.setBounds(10, 11, 820, 580);
		getContentPane().add(panel);
		imageIcon=new ImagePanel(ResultText);
		imageIcon.setBounds(10, 10, 820, 580);
		imageIcon.setLayout(null);
		panel.add(imageIcon);
		imageIcon.setVisible(true);
		chckbxAs = new JCheckBox("AS");
		chckbxAs.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckbxAs.setBounds(846, 116, 97, 23);
		getContentPane().add(chckbxAs);
		chckbxAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chckbxAs.isSelected())
					flagAs=1;
				else
					flagAs=0;
			}
		});
		chckbxBuilding = new JCheckBox("Building");
		chckbxBuilding.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckbxBuilding.setBounds(846, 142, 97, 23);
		getContentPane().add(chckbxBuilding);
		chckbxBuilding.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chckbxBuilding.isSelected())
					flagBuild=1;
				else
					flagBuild=0;
			}
		});
		chckbxStudent = new JCheckBox("Students");
		chckbxStudent.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckbxStudent.setBounds(945, 116, 97, 23);
		getContentPane().add(chckbxStudent);
		chckbxStudent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chckbxStudent.isSelected())
					flagStud=1;
				else
					flagStud=0;
			}
		});
		JLabel lblNewJgoodiesTitle = DefaultComponentFactory.getInstance().createTitle("Active Feature Type");
		lblNewJgoodiesTitle.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewJgoodiesTitle.setBounds(840, 42, 183, 34);
		getContentPane().add(lblNewJgoodiesTitle);
		JLabel lblQuery = DefaultComponentFactory.getInstance().createTitle("Query");
		lblQuery.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblQuery.setBounds(840, 196, 90, 23);
		getContentPane().add(lblQuery);
		textArea = new TextArea();
		textArea.setBounds(10, 604, 1028, 72);
		getContentPane().add(textArea);
		mTextX=new JTextArea("",30,30);
		mTextX.setEditable(true);
		mTextX.setLineWrap(true);
		mTextX.setWrapStyleWord(true);
		mTextX.setBounds(850,450,50,20);
		panel.add(mTextX);
		mTextY=new JTextArea("",30,30);
		mTextY.setEditable(true);
		mTextY.setLineWrap(true);
		mTextY.setWrapStyleWord(true);
		mTextY.setBounds(950,450,50,20);
		panel.add(mTextY);
		labelX=new JLabel("X");
		labelX.setBounds(875,470,20,20);
		panel.add(labelX);
		labelY=new JLabel("Y");
		labelY.setBounds(975,470,20,20);
		panel.add(labelY);
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StringPanel frame = new StringPanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	//http://stackoverflow.com/questions/299495/java-swing-how-to-add-an-image-to-a-jpanel implementation.
	class ImagePanel extends JPanel {
		String result="nothing";

		/**
		 * @param args
		 */
		private BufferedImage image;
		public ImagePanel()
		{
			try
			{
				ConnectToDB();
				image=ImageIO.read(new File("C:\\University Material\\DBMS\\HW2\\map.jpg"));
				result="nothing";
			}catch(IOException e)
			{
				System.out.println("Error: "+e.toString());
			}

		}
		public ImagePanel(String ResultTextMarker)
		{
			try
			{
				ConnectToDB();
				image=ImageIO.read(new File("C:\\University Material\\DBMS\\HW2\\map.jpg"));
			}catch(IOException e)
			{
				System.out.println("Error: "+e.toString());
			}

		}
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0,820,580, null); 
			Graphics2D graphics2=(Graphics2D)g;
			if(glFlag==true)
			{
				if(displayResult.equalsIgnoreCase("Coordinates"))
				{
					showPointOnMap(graphics2);
				}
				if(displayResult.equalsIgnoreCase("Emergency"))
				{
					BrkAnnSystem(graphics2);
				}
				if(displayResult.equalsIgnoreCase("nearAnnSystem"))
				{
					ClosestAnnSystem(graphics2);

				}
				if(displayResult.equalsIgnoreCase("drawPolygon"))
				{
					if(FlagRange==false)
						drawPolygon(graphics2);
					else
						JoinPolygon(graphics2);

				}
			}

			switch(ResultText)
			{
			case "nothing":
				break;
			case "Whole Region":
				WholeRegionQuery(graphics2);
				break;
			case "Point Query":
				showPointOnMap(graphics2);
				PointQuery(graphics2);
				break;
			case "Range Query":
				JoinPolygon(graphics2);
				RangeQuery(graphics2);
				break;
			case "Surrounding Students":
				ClosestAnnSystem(graphics2);
				SurrStudQuery(graphics2);
				break;
			case "Emergency Query":
				EmergQuery(graphics2);
				break;
			}
		}
	}
	public void EmergQuery(Graphics2D graphics2d)
	{	
		String secNearAnnSys=null;
		int x=0;
		int y=0;
		int x1=0;
		int y1=0;
		int radius=0;
		int secNearAnnSysRadius=0;
		String AsId=null;
		Polygon polyStud=null;
		Polygon polyAnnSys=null;
		STRUCT struct=null;
		graphics2d.setColor(Color.RED);
		try
		{
			Statement statement=mainConnection.createStatement();
			statement.executeUpdate("delete from pointQueryTable where pt_id=2");
			statement.executeUpdate("insert into pointQueryTable values(2,SDO_GEOMETRY(2001,NULL,SDO_POINT_TYPE("+xCordi+","+yCordi+",NULL),NULL,NULL))");
			String sql="SELECT locOfasSys,radius,asid from announcement,pointQueryTable where SDO_NN(locOfasSys,Coordinates,'sdo_num_res=1')='TRUE' and pt_id=2" ;
			textArea.append(sql);
			ResultSet resSet=statement.executeQuery(sql);
			while(resSet.next())
			{
				struct = (STRUCT)resSet.getObject("locOfasSys");
				ResultSetMetaData meta = resSet.getMetaData();
				for( int col=1; col<=meta.getColumnCount(); col++)
				{
					if(meta.getColumnName(col).equalsIgnoreCase("radius"))
					{
						radius=Integer.parseInt(resSet.getString(col));	//announcement system radius
					}
					if(meta.getColumnName(col).equalsIgnoreCase("asid"))
					{
						AsId=resSet.getString(col);		//announcement system id
					}

				}
			}

			//graphics2d.setColor(Color.GREEN);
			PreparedStatement prepStatement=mainConnection.prepareStatement("SELECT locofstud from students where SDO_WITHIN_DISTANCE(locofstud,?,'distance="+radius+"')='TRUE'");
			textArea.append("SELECT std_position from students where SDO_WITHIN_DISTANCE(std_position,?,'distance="+radius+"')='TRUE'");
			prepStatement.setObject(1, struct);
			ResultSet resSet1=prepStatement.executeQuery();
			while(resSet1.next())
			{
				STRUCT struct1 = (STRUCT)resSet1.getObject("locofstud");
				JGeometry jgeometry = JGeometry.load(struct1);
				double[] ordinates = jgeometry.getPoint();		//jgeometry.getOrdinatesArray for SDO_ORDINATES_ARRAY
				x = (new Double(ordinates[0])).intValue();
				y = (new Double(ordinates[1])).intValue();																					
				PreparedStatement prepStatement2=mainConnection.prepareStatement("SELECT locOfasSys,asid,radius from announcement where SDO_NN(locOfasSys,?,'sdo_num_res=2')='TRUE' and asid<>?");
				textArea.append("SELECT locOfasSys,asid,radius from asystems where SDO_NN(locOfasSys,?,'sdo_num_res=2')='TRUE' and asid<>?");
				prepStatement2.setObject(1, struct1);
				prepStatement2.setString(2, AsId);
				ResultSet resSet2=prepStatement2.executeQuery();
				while(resSet2.next())
				{
					STRUCT struct2 = (STRUCT)resSet2.getObject("locOfasSys");	
					JGeometry jgeometry1 = JGeometry.load(struct2);
					double[] ordinates1 = jgeometry1.getPoint();		//jgeometry.getOrdinatesArray for SDO_POINT_TYPE
					x1 = (new Double(ordinates1[0])).intValue();
					y1 = (new Double(ordinates1[1])).intValue();
					ResultSetMetaData meta = resSet2.getMetaData();
					for( int col=1; col<=meta.getColumnCount(); col++)
					{
						if(meta.getColumnName(col).equalsIgnoreCase("asid"))
						{
							secNearAnnSys=resSet2.getString(col);	//2nd nearest announcement system
						}
						if(meta.getColumnName(col).equalsIgnoreCase("radius"))
						{		
							secNearAnnSysRadius=resSet2.getInt(col);
						}
					}
					if(secNearAnnSys.equalsIgnoreCase("a1psa"))
					{
						graphics2d.setColor(Color.ORANGE);
					}
					else if(secNearAnnSys.equalsIgnoreCase("a4hnb"))
					{
						graphics2d.setColor(Color.WHITE);
					}
					else if(secNearAnnSys.equalsIgnoreCase("a2ohe"))
					{
						graphics2d.setColor(Color.YELLOW);
					}
					else if(secNearAnnSys.equalsIgnoreCase("a3sgm"))
					{
						graphics2d.setColor(Color.BLUE);
					}
					else if(secNearAnnSys.equalsIgnoreCase("a6ssc"))
					{
						graphics2d.setColor(Color.MAGENTA);
					}
					else if(secNearAnnSys.equalsIgnoreCase("a5vhe"))
					{
						graphics2d.setColor(Color.PINK);
					}
					else 	
					{
						graphics2d.setColor(Color.RED);
					} 

				}
				polyStud=new Polygon();
				polyStud.addPoint(x-5, y-5);
				polyStud.addPoint(x+5, y-5);
				polyStud.addPoint(x+5, y+5);
				polyStud.addPoint(x-5, y+5);
				graphics2d.fillPolygon(polyStud);
				polyAnnSys=new Polygon();
				polyAnnSys.addPoint(x1-7, y1-7);
				polyAnnSys.addPoint(x1+8, y1-7);
				polyAnnSys.addPoint(x1+8, y1+8);
				polyAnnSys.addPoint(x1-7, y1+8);
				graphics2d.fillPolygon(polyAnnSys);
				graphics2d.drawOval(x1-secNearAnnSysRadius, y1-secNearAnnSysRadius, 2*secNearAnnSysRadius, 2*secNearAnnSysRadius);
			}

		}catch(Exception e)
		{
			System.out.println("Error: "+e.toString());
		}
	}
	public void BrkAnnSystem(Graphics2D graphics2d)
	{
		Polygon poly=null;

		graphics2d.setColor(Color.RED);
		try
		{
			Statement statement=mainConnection.createStatement();
			statement.executeUpdate("delete from pointQueryTable where pt_id=2");
			statement.executeUpdate("insert into pointQueryTable values(2,SDO_GEOMETRY(2001,NULL,SDO_POINT_TYPE("+xCordi+","+yCordi+",NULL),NULL,NULL))");
			String sql="SELECT locOfasSys,radius from announcement,pointQueryTable where SDO_NN(locOfasSys,Coordinates,'sdo_num_res=1')='TRUE' and pt_id=2" ;
			textArea.append(sql+";");
			ResultSet resSet=statement.executeQuery(sql);
			while(resSet.next())
			{
				/* Coordinates of Announcement Systems */
				STRUCT struct = (STRUCT)resSet.getObject("locOfasSys");	
				JGeometry jgeometry = JGeometry.load(struct);
				double[] ordinates = jgeometry.getPoint();		//jgeometry.getOrdinatesArray for SDO_POINT_TYPE
				int x = (new Double(ordinates[0])).intValue();
				int y = (new Double(ordinates[1])).intValue();
				//System.out.println( "(X = " + x + ", Y = " + y + ")" ); 
				poly=new Polygon();

				poly.addPoint(x-7, y-7);
				poly.addPoint(x+8, y-7);
				poly.addPoint(x+8, y+8);
				poly.addPoint(x-7, y+8);

				graphics2d.fillPolygon(poly); 
				/* Announcement systems coverage area */
				int radius;
				ResultSetMetaData meta = resSet.getMetaData();
				for( int col=1; col<=meta.getColumnCount(); col++)
				{
					if(meta.getColumnName(col).equalsIgnoreCase("radius"))
					{
						radius=Integer.parseInt(resSet.getString(col));
						graphics2d.drawOval(x-radius, y-radius, 2*radius, 2*radius);
					}
				}
			}
		}catch(SQLException e)
		{
			System.out.println("Error: "+e.toString());
		}
	}
	public void drawPolygon(Graphics2D graphics2d)
	{
		graphics2d.setColor(Color.GREEN);
		pointList.add(new Point(xCordi,yCordi));


		for(Point point: pointList)
		{	
			Polygon pointPoly=new Polygon();
			pointPoly.addPoint(point.x-2, point.y-2);
			pointPoly.addPoint(point.x+3,point.y-2);
			pointPoly.addPoint(point.x+3,point.y+3);
			pointPoly.addPoint(point.x-2,point.y+3);
			graphics2d.fillPolygon(pointPoly);
		}
	}
	//Code from Example 6 of given example in the homework. structs like geometry and point replaced with poly.
	public void WholeRegionQuery(Graphics2D graphics2d)
	{
		Polygon poly=null;
		try
		{
			if (flagStud==1){
				mainResultSet = mainStatement.executeQuery( "select * from students " );
				textArea.append("select * from students "+";");
				graphics2d.setColor(Color.green);
				while(mainResultSet.next()){
					STRUCT struct = (STRUCT)mainResultSet.getObject("locofstud");	
					JGeometry jgeometry = JGeometry.load(struct);
					double[] ordinates = jgeometry.getPoint();		//jgeometry.getOrdinatesArray for SDO_POINT_TYPE
					int x = (new Double(ordinates[0])).intValue();
					int y = (new Double(ordinates[1])).intValue();
					poly=new Polygon();
					poly.addPoint(x-5,y-5);
					poly.addPoint(x+5, y-5);
					poly.addPoint(x+5,y+5);
					poly.addPoint(x-5, y+5);
					graphics2d.fillPolygon(poly);
				}
			}
			if (flagAs==1){
				mainResultSet = mainStatement.executeQuery( "select * from announcement " );
				textArea.append("select * from announcement "+";");
				graphics2d.setColor(Color.RED);
				while(mainResultSet.next())
				{
					/* Coordinates of Announcement Systems */
					STRUCT struct = (STRUCT)mainResultSet.getObject("locofasSys");	
					JGeometry jgeometry = JGeometry.load(struct);
					double[] ordinates = jgeometry.getPoint();		//jgeometry.getOrdinatesArray for SDO_POINT_TYPE
					int x = (new Double(ordinates[0])).intValue();
					int y = (new Double(ordinates[1])).intValue();
					//System.out.println( "(X = " + x + ", Y = " + y + ")" ); 
					poly=new Polygon();

					poly.addPoint(x-7, y-7);
					poly.addPoint(x+8, y-7);
					poly.addPoint(x+8, y+8);
					poly.addPoint(x-7, y+8);

					graphics2d.fillPolygon(poly); 
					/* Announcement systems coverage area */
					int radius;
					ResultSetMetaData meta = mainResultSet.getMetaData();
					for( int col=1; col<=meta.getColumnCount(); col++)
					{
						if(meta.getColumnName(col).equalsIgnoreCase("radius"))
						{
							radius=Integer.parseInt(mainResultSet.getString(col));
							graphics2d.drawOval(x-radius, y-radius, 2*radius, 2*radius);
						}
					}
				}}
			if (flagBuild==1){
				mainResultSet = mainStatement.executeQuery( "select * from building " );
				textArea.append("select * from building "+";");
				graphics2d.setColor(Color.YELLOW);
				int counter=0;
				while(mainResultSet.next())
				{
					int[] x=new int[20];
					int[] y=new int[20];
					STRUCT struct = (STRUCT)mainResultSet.getObject("pointcordi");	
					JGeometry jgeometry = JGeometry.load(struct);
					double[] ordinates = jgeometry.getOrdinatesArray();		//jgeometry.getOrdinatesArray for SDO_POINT_TYPE
					for(int i=0;i<ordinates.length;i=i+2)
					{
						x[counter]=(new Double(ordinates[i])).intValue();
						y[counter]=(new Double(ordinates[i+1])).intValue();
						counter++;
					}
					graphics2d.drawPolygon(x, y, counter);
					counter=0;
				} }

		}catch( SQLException e)
		{
			System.out.println("Error: "+e.toString());
		}
	}
	public void ConnectToDB()
	{
		try
		{
			// loading Oracle Driver
			System.out.print("Looking for Oracle's jdbc-odbc driver ... ");
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			System.out.println(", Loaded.");
			String URL = "jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS = (PROTOCOL = TCP)(HOST = localhost)(PORT = 1521))(CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME = csci585)))";
			String userName = "sys as sysdba";
			String password = "Mannat123";
			System.out.print("Connecting to DB...");
			mainConnection = DriverManager.getConnection(URL, userName, password);
			System.out.println(", Connected!");
			mainStatement = mainConnection.createStatement();
		}
		catch (Exception e)
		{
			System.out.println( "Error while connecting to DB: "+ e.toString() );
			e.printStackTrace();
			System.exit(-1);
		}
	}
	public void showPointOnMap(Graphics2D graphics2d)
	{

		graphics2d.setColor(Color.RED);
		Polygon poly=new Polygon();
		poly.addPoint(xCordi-2, yCordi-2);
		poly.addPoint(xCordi+3,yCordi-2);
		poly.addPoint(xCordi+3, yCordi+3);
		poly.addPoint(xCordi-2, yCordi+3);
		graphics2d.fillPolygon(poly);
		graphics2d.drawOval(xCordi-50, yCordi-50, 2*50, 2*50);
		try
		{
			mainStatement.executeUpdate("delete from pointQueryTable where pt_id=1");
			mainStatement.executeUpdate("insert into pointQueryTable values(1,SDO_GEOMETRY(2001,NULL,SDO_POINT_TYPE("+xCordi+","+yCordi+",NULL),NULL,NULL))");
		}catch(SQLException e)
		{
			System.out.println("Error: "+e.toString());
		} 

	}
	public void PointQuery(Graphics2D graphics2d)
	{
		Polygon poly=null;

		graphics2d.setColor(Color.GREEN);
		try
		{
			Statement ptQueryStatement=mainConnection.createStatement();

			String sql1="SELECT Coordinates from pointQueryTable where pt_id=1";
			ResultSet resultSet1=ptQueryStatement.executeQuery(sql1);
			while(resultSet1.next())
			{
				STRUCT mapPoint=(STRUCT)resultSet1.getObject("Coordinates");

				if(flagAs==1)
				{
					PreparedStatement statement=mainConnection.prepareStatement("SELECT locofasSys,radius from announcement where SDO_WITHIN_DISTANCE(locofasSys,?,'distance=50')='TRUE'");
					statement.setObject(1, mapPoint);
					String sql="SELECT locofasSys,radius from announcement where SDO_WITHIN_DISTANCE(locofasSys,?,'distance=50')='TRUE'";
					textArea.append(sql+";");
					ResultSet resultSet12=statement.executeQuery();

					while(resultSet12.next())
					{
						/* Coordinates of Announcement Systems */
						STRUCT struct = (STRUCT)resultSet12.getObject("locofasSys");	
						JGeometry jgeometry = JGeometry.load(struct);
						double[] ordinates = jgeometry.getPoint();		//jgeometry.getOrdinatesArray for SDO_POINT_TYPE
						int x = (new Double(ordinates[0])).intValue();
						int y = (new Double(ordinates[1])).intValue();
						//System.out.println( "(X = " + x + ", Y = " + y + ")" );

						poly=new Polygon();

						poly.addPoint(x-7, y-7);
						poly.addPoint(x+8, y-7);
						poly.addPoint(x+8, y+8);
						poly.addPoint(x-7, y+8);

						graphics2d.fillPolygon(poly); 
						int radius;
						ResultSetMetaData meta = resultSet12.getMetaData();
						for( int col=1; col<=meta.getColumnCount(); col++)
						{
							if(meta.getColumnName(col).equalsIgnoreCase("radius"))
							{
								radius=Integer.parseInt(resultSet12.getString(col));
								graphics2d.drawOval(x-radius, y-radius, 2*radius, 2*radius);
							}
						}
					}
				}

				if(flagBuild==1)
				{

					int counter=0;
					PreparedStatement statement=mainConnection.prepareStatement("SELECT pointcordi from building where SDO_WITHIN_DISTANCE(pointcordi,?,'distance=50')='TRUE'");				
					String sql="SELECT pointcordi from building where SDO_WITHIN_DISTANCE(pointcordi,?,'distance=50')='TRUE'";
					textArea.append(sql+";");
					statement.setObject(1, mapPoint);
					ResultSet resSet=statement.executeQuery();
					while(resSet.next())
					{
						graphics2d.setColor(Color.GREEN);
						int[] x=new int[20];
						int[] y=new int[20];
						STRUCT struct = (STRUCT)resSet.getObject("pointcordi");	
						JGeometry jgeometry = JGeometry.load(struct);
						double[] ordinates = jgeometry.getOrdinatesArray();		//jgeometry.getOrdinatesArray for SDO_POINT_TYPE
						for(int i=0;i<ordinates.length;i=i+2)
						{
							x[counter]=(new Double(ordinates[i])).intValue();
							y[counter]=(new Double(ordinates[i+1])).intValue();
							counter++;
						}
						graphics2d.drawPolygon(x, y, counter);
						counter=0;
					}

					PreparedStatement statement1=mainConnection.prepareStatement("SELECT pointcordi from building where SDO_NN(pointcordi,?,'sdo_num_res=1')='TRUE' and SDO_WITHIN_DISTANCE(pointcordi,?,'distance=50')='TRUE'");
					textArea.append("SELECT pointcordi from building where SDO_NN(pointcordi,?,'sdo_num_res=1')='TRUE' and SDO_WITHIN_DISTANCE(pointcordi,?,'distance=50')='TRUE'"+";");
					statement1.setObject(1, mapPoint);
					statement1.setObject(2,mapPoint);
					ResultSet resSet1=statement1.executeQuery();
					while(resSet1.next())
					{
						graphics2d.setColor(Color.YELLOW);
						int[] x=new int[20];
						int[] y=new int[20];
						STRUCT struct1 = (STRUCT)resSet1.getObject("pointcordi");	
						JGeometry jgeometry1 = JGeometry.load(struct1);
						double[] ordinates1 = jgeometry1.getOrdinatesArray();		//jgeometry.getOrdinatesArray for SDO_POINT_TYPE
						for(int i=0;i<ordinates1.length;i=i+2)
						{
							//System.out.print("("+ordinates1[i]+","+ordinates1[i+1]+")");
							x[counter]=(new Double(ordinates1[i])).intValue();
							y[counter]=(new Double(ordinates1[i+1])).intValue();
							counter++;
						}
						graphics2d.drawPolygon(x, y, counter);
						counter=0;
					}

				}


				if(flagStud==1)
				{		
					PreparedStatement statement=mainConnection.prepareStatement("SELECT locofstud from students where SDO_WITHIN_DISTANCE(locofstud,?,'distance=50')='TRUE'");				
					statement.setObject(1, mapPoint);
					String Sql="SELECT locofstud from students where SDO_WITHIN_DISTANCE(locofstud,?,'distance=50')='TRUE'";
					textArea.append(Sql+";");
					ResultSet resultSet=statement.executeQuery();
					while(resultSet.next())
					{
						STRUCT struct = (STRUCT)resultSet.getObject("locofstud");
						JGeometry jgeometry = JGeometry.load(struct);
						double[] ordinates = jgeometry.getPoint();		
						int x = (new Double(ordinates[0])).intValue();
						int y = (new Double(ordinates[1])).intValue();
						//System.out.println( "(X = " + x + ", Y = " + y + ")" ); 
						poly=new Polygon();

						poly.addPoint(x-5, y-5);
						poly.addPoint(x+5, y-5);
						poly.addPoint(x+5, y+5);
						poly.addPoint(x-5, y+5);

						graphics2d.fillPolygon(poly); 
					} 
				}
			}
		}catch(SQLException e)
		{
			System.out.println("Error: "+e.toString());
			System.exit(-1);
		}	


	}
	public void JoinPolygon(Graphics2D graphics2d)
	{
		int tempx,tempy,counter=0;
		try
		{
			Statement statement=mainConnection.createStatement();
			statement.executeUpdate("delete from PolygonTab");
			String sqlStatement="insert into PolygonTab values(1,SDO_GEOMETRY(2003,NULL,NULL,SDO_ELEM_INFO_ARRAY(1,1003,1),SDO_ORDINATE_ARRAY(" ;
			textArea.append(sqlStatement+";");
			for(Point point: pointList)
			{
				tempx=point.x;
				tempy=point.y;
				sqlStatement=sqlStatement + tempx + "," + tempy;
				if(pointList.size()-counter==1)
					break;
				else
				{
					sqlStatement=sqlStatement + ",";
					counter++;
				}
			}
			sqlStatement= sqlStatement + ")))";
			statement.executeUpdate(sqlStatement);
			int pointCounter=0;
			String sql="SELECT Coordinates from PolygonTab where poly_id=1";	

			ResultSet resSet=statement.executeQuery(sql);
			graphics2d.setColor(Color.RED);
			while(resSet.next())
			{
				int[] x=new int[20];
				int[] y=new int[20];
				STRUCT struct = (STRUCT)resSet.getObject("Coordinates");
				JGeometry jgeometry = JGeometry.load(struct);
				double[] ordinates = jgeometry.getOrdinatesArray();		//jgeometry.getOrdinatesArray for SDO_POINT_TYPE
				for(int i=0;i<ordinates.length;i=i+2)
				{
					x[pointCounter]=(new Double(ordinates[i])).intValue();
					y[pointCounter]=(new Double(ordinates[i+1])).intValue();
					pointCounter++;
				}
				graphics2d.drawPolygon(x, y, pointCounter);
			} 

		}catch(SQLException e)
		{
			System.out.println("Error: "+e.toString());
		}
		FlagRange=false;

	}

	public void RangeQuery(Graphics2D graphics2d)
	{		
		Polygon poly=null;
		graphics2d.setColor(Color.RED);
		int radius=0;
		try
		{
			if(flagStud==1)
			{
				graphics2d.setColor(Color.GREEN);
				Statement statement=mainConnection.createStatement();
				String sql="SELECT locofstud FROM students,PolygonTab WHERE SDO_ANYINTERACT(Coordinates,locofstud)='TRUE'";
				textArea.append(sql);
				ResultSet resSet=statement.executeQuery(sql);
				while(resSet.next())
				{
					STRUCT struct = (STRUCT)resSet.getObject("locofstud");	
					JGeometry jgeometry = JGeometry.load(struct);
					double[] ordinates = jgeometry.getPoint();		//jgeometry.getOrdinatesArray for SDO_ORDINATES_ARRAY
					int x = (new Double(ordinates[0])).intValue();
					int y = (new Double(ordinates[1])).intValue();
					//System.out.println( "(X = " + x + ", Y = " + y + ")" ); 
					poly=new Polygon();
					poly.addPoint(x-5, y-5);
					poly.addPoint(x+5, y-5);
					poly.addPoint(x+5, y+5);
					poly.addPoint(x-5, y+5);
					graphics2d.fillPolygon(poly); 
				}
			}
			if(flagAs==1)
			{
				Statement statement=mainConnection.createStatement();
				String sql="SELECT locOfasSys,radius FROM anouncement,Polygon WHERE SDO_ANYINTERACT(Coordinates,locOfasSys)='TRUE'";
				textArea.append(sql+";");
				ResultSet resultSet=statement.executeQuery(sql);
				while(resultSet.next())
				{
					/* Coordinates of Announcement Systems */
					STRUCT struct = (STRUCT)resultSet.getObject("locOfasSys");	
					JGeometry jgeometry = JGeometry.load(struct);
					double[] ordinates = jgeometry.getPoint();		//jgeometry.getOrdinatesArray for SDO_POINT_TYPE
					int x = (new Double(ordinates[0])).intValue();
					int y = (new Double(ordinates[1])).intValue();
					poly=new Polygon();
					poly.addPoint(x-7, y-7);
					poly.addPoint(x+8, y-7);
					poly.addPoint(x+8, y+8);
					poly.addPoint(x-7, y+8);

					graphics2d.fillPolygon(poly); 
					ResultSetMetaData meta = resultSet.getMetaData();
					for( int col=1; col<=meta.getColumnCount(); col++)
					{
						if(meta.getColumnName(col).equalsIgnoreCase("radius"))
						{
							radius=Integer.parseInt(resultSet.getString(col));
						}
					}
					graphics2d.drawOval(x-radius, y-radius, 2*radius, 2*radius); 
				}
			}
			if(flagBuild==1)
			{
				int counter=0;
				Statement statement=mainConnection.createStatement();
				String sql="SELECT pointcordi FROM building,PolygonTab WHERE SDO_RELATE(pointcordi,Coordinates,'mask=anyinteract')='TRUE'";
				textArea.append(sql+";");
				ResultSet resSet=statement.executeQuery(sql);
				while(resSet.next())
				{
					int[] x=new int[20];
					int[] y=new int[20];
					STRUCT struct = (STRUCT)resSet.getObject("pointcordi");	//b_conf
					JGeometry jgeometry = JGeometry.load(struct);
					double[] ordinates = jgeometry.getOrdinatesArray();		//jgeometry.getOrdinatesArray for SDO_POINT_TYPE
					for(int i=0;i<ordinates.length;i=i+2)
					{
						x[counter]=(new Double(ordinates[i])).intValue();
						y[counter]=(new Double(ordinates[i+1])).intValue();
						counter++;
					}
					graphics2d.setColor(Color.GREEN);
					graphics2d.drawPolygon(x, y, counter);
					counter=0;
				}
			}
		}catch(SQLException e)
		{
			System.out.println("Error here: "+ e.toString());
		}
		//pointList.clear();
		FlagRange=false;
		//clearArrayListFlag=true;
	}
	public void SurrStudQuery(Graphics2D graphics2d)
	{
		Polygon poly=null;
		STRUCT struct=null;
		int radius=0;
		graphics2d.setColor(Color.RED);
		try
		{
			Statement statement=mainConnection.createStatement();
			statement.executeUpdate("delete from pointQueryTable where pt_id=2");
			textArea.append("delete from pointQueryTable where pt_id=2;");

			statement.executeUpdate("insert into pointQueryTable values(2,SDO_GEOMETRY(2001,NULL,SDO_POINT_TYPE("+xCordi+","+yCordi+",NULL),NULL,NULL))");
			textArea.append("insert into pointQueryTable values(2,SDO_GEOMETRY(2001,NULL,SDO_POINT_TYPE(xCoor+,+yCoor+,NULL),NULL,NULL));");

			String sql="SELECT locOfasSys,radius from announcement,pointQueryTable where SDO_NN(locOfasSys,Coordinates,'sdo_num_res=1')='TRUE' and pt_id=2" ;
			textArea.append(sql+";");
			ResultSet resSet=statement.executeQuery(sql);
			while(resSet.next())
			{
				/* Coordinates of Announcement Systems */
				struct = (STRUCT)resSet.getObject("locOfasSys");
				ResultSetMetaData meta = resSet.getMetaData();
				for( int col=1; col<=meta.getColumnCount(); col++)
				{
					if(meta.getColumnName(col).equalsIgnoreCase("radius"))
					{
						radius=Integer.parseInt(resSet.getString(col));
					}
				}
			}
			graphics2d.setColor(Color.green);
			PreparedStatement prepStatement=mainConnection.prepareStatement("SELECT locofstud from students where SDO_WITHIN_DISTANCE(locofstud,?,'distance="+radius+"')='TRUE'");
			textArea.append("SELECT std_position from students where SDO_WITHIN_DISTANCE(std_position,struct,'distance="+radius+"')='TRUE';");
			prepStatement.setObject(1, struct);
			ResultSet resSet1=prepStatement.executeQuery();
			while(resSet1.next())
			{
				STRUCT struct1 = (STRUCT)resSet1.getObject("locofstud");	
				JGeometry jgeometry = JGeometry.load(struct1);
				double[] ordinates = jgeometry.getPoint();		//jgeometry.getOrdinatesArray for SDO_ORDINATES_ARRAY
				int x = (new Double(ordinates[0])).intValue();
				int y = (new Double(ordinates[1])).intValue();
				poly=new Polygon();
				poly.addPoint(x-5, y-5);
				poly.addPoint(x+5, y-5);
				poly.addPoint(x+5, y+5);
				poly.addPoint(x-5, y+5);
				graphics2d.fillPolygon(poly);
			}
		}catch(SQLException e)
		{
			System.out.println("Error:"+e.toString());
		}
	}
	public void ClosestAnnSystem(Graphics2D graphics2d)
	{
		Polygon poly=null;
		graphics2d.setColor(Color.RED);
		try
		{
			Statement statement=mainConnection.createStatement();
			statement.executeUpdate("delete from pointQueryTable where pt_id=2");
			statement.executeUpdate("insert into pointQueryTable values(2,SDO_GEOMETRY(2001,NULL,SDO_POINT_TYPE("+xCordi+","+yCordi+",NULL),NULL,NULL))");
			String sql="SELECT locOfasSys,radius from announcement,pointQueryTable where SDO_NN(locOfasSys,Coordinates,'sdo_num_res=1')='TRUE' and pt_id=2" ;
			textArea.append(sql+";");
			ResultSet resultSet=statement.executeQuery(sql);
			while(resultSet.next())
			{
				/* Coordinates of Announcement Systems */
				STRUCT struct = (STRUCT)resultSet.getObject("locOfasSys");	
				JGeometry jgeometry = JGeometry.load(struct);
				double[] ordinates = jgeometry.getPoint();		//jgeometry.getOrdinatesArray for SDO_POINT_TYPE
				int x = (new Double(ordinates[0])).intValue();
				int y = (new Double(ordinates[1])).intValue(); 
				poly=new Polygon();
				poly.addPoint(x-7, y-7);
				poly.addPoint(x+8, y-7);
				poly.addPoint(x+8, y+8);
				poly.addPoint(x-7, y+8);
				graphics2d.fillPolygon(poly); 
				/* Announcement systems coverage area */
				int radius;
				ResultSetMetaData meta = resultSet.getMetaData();
				for( int col=1; col<=meta.getColumnCount(); col++)
				{
					if(meta.getColumnName(col).equalsIgnoreCase("radius"))
					{
						radius=Integer.parseInt(resultSet.getString(col));
						graphics2d.drawOval(x-radius, y-radius, 2*radius, 2*radius);
					}
				}
			}
		}catch(SQLException e)
		{
			System.out.println("Error: "+e.toString());
		}
	}
}