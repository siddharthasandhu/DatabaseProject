package CreateGui;
import java.sql.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Populate {
	Connection mainConnection= null;
	Statement mainStatement= null;
	PreparedStatement SecondStatement = null;
	ResultSet mainResultSet= null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Populate p= new Populate(args);

	}
	public Populate(String[] args){
		
		ConnectToDB();
		clearDatabase();
		insertDatabaseAnnouncement(args);
		insertDatabaseBuildings(args);
		insertDatabaseStudents(args);
	}
	public void clearDatabase(){
		try{
			mainStatement.executeUpdate("truncate table students");
			mainStatement.executeUpdate("truncate table building");
			mainStatement.executeUpdate("truncate table announcement");
			mainStatement.executeUpdate("truncate table pointQueryTable");
			mainStatement.executeUpdate("truncate PolygonTab");
		//	mainStatement.executeUpdate("drop table students");
		//	mainStatement.executeUpdate("drop table building");
		//	mainStatement.executeUpdate("drop table announcement");
		//	mainStatement.executeUpdate("drop table pointQueryTable");
		//	mainStatement.executeUpdate("create table pointQueryTable ( pt_id int,coordinates sdo_geometry)");
		//	mainStatement.executeUpdate("create table building(buildingID varchar(40) primary key,buildingName varchar(60),pointcordi sdo_geometry)");
		//	mainStatement.executeUpdate("create table students(personID varchar(40) primary key, locofstud sdo_geometry)");
		//	mainStatement.executeUpdate("create table announcement(asID varchar(40) primary key,radius int,locofasSys sdo_geometry)");
		}catch(Exception e){
			System.out.println( " Error 2: " + e.toString() );
		}
	}
	public void insertDatabaseStudents(String[] args){
		BufferedReader br = null;
		String MyArray="c:/University Material/siddharthasingh_sandhu_hw2/HW2/students.xy";
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(MyArray));	
			while ((sCurrentLine = br.readLine()) != null) {
				String[] words = (sCurrentLine.split(","));

				for(int k=0;k<words.length;k++){
					words[k]=words[k].trim();
				}
				String sqlQuery="insert into students values('"+words[0]+"',sdo_geometry(2001,null,sdo_point_type( "+words[1]+","+words[2]+",null),null,null))";
				try{
					mainStatement.executeUpdate(sqlQuery);
				}catch(Exception e){
					System.out.println( " Error 2: " + e.toString() );
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
	public void insertDatabaseBuildings(String[] args){
		BufferedReader br = null;
		String MyArray="c:/University Material/siddharthasingh_sandhu_hw2/HW2/buildings.xy";
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(MyArray));	
			while ((sCurrentLine = br.readLine()) != null) {
				String[] words = (sCurrentLine.split(","));
				String Query="";
				for(int k=0;k<words.length;k++){
					words[k]=words[k].trim();
				}
				for(int n=3;n<words.length;n++){
					if(n!=3){
						Query=Query+","+words[n];
					} else {
						Query=words[n];
					}
				}
				System.out.println(Query);
				String sqlQuery="insert into building values('"+words[0] +"','"+words[1]+"',sdo_geometry(2003,Null,null,sdo_elem_info_array(1,2003,1),sdo_ordinate_array("+Query+")))";
				try{
					mainStatement.executeUpdate(sqlQuery);
				}catch(Exception e){
					System.out.println( " Error 2: " + e.toString() );
				}
				for(int n=0;n<words.length;n++){
					System.out.println(words[n]);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
	public void insertDatabaseAnnouncement(String[] args){
		BufferedReader br = null;
		String MyArray="c:/University Material/siddharthasingh_sandhu_hw2/HW2/announcementSystems.xy";
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(MyArray));	
			while ((sCurrentLine = br.readLine()) != null) {
				String[] words = (sCurrentLine.split(","));
				String Query="";
				for(int k=0;k<words.length;k++){
					words[k]=words[k].trim();
				}
				for(int n=3;n<words.length;n++){
					if(n!=3){
						Query=Query+","+words[n];
					} else {
						Query=words[n];
					}
				}
				System.out.println(Query);
				String sqlQuery="insert into announcement values('"+words[0]+"',"+words[3]+",sdo_geometry(2001,null,sdo_point_type("+words[1]+","+words[2]+",null),null,null))";
				try{
					mainStatement.executeUpdate(sqlQuery);
				}catch(Exception e){
					System.out.println( " Error 2: " + e.toString() );
				}
				for(int n=0;n<words.length;n++){
					System.out.println(words[n]);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
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
}
