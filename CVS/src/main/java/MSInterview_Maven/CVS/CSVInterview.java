package MSInterview_Maven.CVS;
import java.sql.*;
import java.io.*;
import java.util.*;

public class CSVInterview {
	private String databaseName = null;
	private String TableName    = null;
	private String CSVname      = null;
	private Connection Connect  = null;
	private Statement sqlCmd    = null;
	private String BadCSV       = null;
	private int recieved        = 0;
	private int successful      = 0;
	private int failed          = 0;
	
	// sets the name of the database
	public void SetDBname() {
		System.out.print("Input the database name: ");
		InputStreamReader Read = new InputStreamReader(System.in);
		BufferedReader input = new BufferedReader(Read);
		try {
			databaseName = input.readLine();
			if(databaseName.endsWith(".db")) {
				return;
			}
			else {
				System.out.println("Error, must include extention .db");
				SetDBname();
			}
		} catch (IOException e) {
			System.out.println("Something went wrong, please enter the database name again.");
		}
	}
	
	// sets the name of the csv file to search
	public void SetCSVname() {
		System.out.print("Input the CSV file name: ");
		InputStreamReader Read = new InputStreamReader(System.in);
		BufferedReader input = new BufferedReader(Read);
		try {
			CSVname = input.readLine();
			BadCSV = CSVname + "-bad";
			if(CSVname.endsWith(".csv")) {
				return;
			}
			else {
				System.out.println("Error, must include extention .csv");
				SetCSVname();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// sets the name of the table in the new database
	public void SetTableName() {
		System.out.print("Input the table name: ");
		InputStreamReader Read = new InputStreamReader(System.in);
		BufferedReader input = new BufferedReader(Read);
		try {
			TableName = input.readLine();
			return;
		} catch (IOException e) {
			System.out.println("Something went wrong, please enter the table name again.");
			e.printStackTrace();
		}
	}
	
	/*
	 * this will create a database, if one does not exist
	 * with your selected file name, with one table in it.
	 */
	public void ConnectDB() {
		System.out.println("Creating Database and Table");
		try {
			Class.forName("org.sqlite.JDBC");
			Connect = DriverManager.getConnection("jdbc:sqlite:"+databaseName);
			sqlCmd = Connect.createStatement();
			String createtable = "CREATE TABLE " + TableName + 
					" (A TEXT NOT NULL," +
					" B TEXT NOT NULL," +
					" C TEXT NOT NULL," + 
					" D TEXT NOT NULL," +
					" E TEXT NOT NULL," +
					" F TEXT NOT NULL," +
					" G TEXT NOT NULL," +
					" H TEXT NOT NULL," + 
					" I TEXT NOT NULL," +
					" J TEXT NOT NULL);";
			sqlCmd.executeUpdate(createtable);	
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Error in ConnectDB");
			e.printStackTrace();	
		}
	} 
	
	// reads the lines of the csv file and sends them to the sorter
	public void readCSVline() {
		String CSVline = null;
		try {
			System.out.println(CSVname);
			System.out.println(new File(".").getAbsoluteFile());
			BufferedReader br = new BufferedReader(new FileReader(CSVname));
			while ((CSVline = br.readLine()) != null) {
				recieved++;
				sortCSVline(CSVline);	
			}
			br.close();
			Connect.close();
			sqlCmd.close();
			BufferedWriter BW = new BufferedWriter(new FileWriter("Program-log.txt"));
			BW.write("Recieved: " + recieved + "\n");
			BW.write("Successful: " + successful+ "\n");
			BW.write("Failed: " + failed+ "\n");
			BW.flush();
			BW.close();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			System.out.println("Error in readCSVline()");
		}
	}
	 // determines if the csv line is a valid entry: inserts into database if valid,
	 // sends to a "bad" csv file if not.
	 private void sortCSVline(String line){
		try {
			BufferedWriter BW = new BufferedWriter(new FileWriter(BadCSV, true));
			ArrayList<Object> Array = new ArrayList<Object>();
			StringTokenizer Token = new StringTokenizer(line, ",");
			while(Token.hasMoreTokens()) {
				String T = Token.nextToken();
				if(T.length() < 35) {
					Array.add(T);
				}
			}
			if(Array.size() < 10) {
				failed++;
				BW.write(line+"\n");
				BW.flush();
				BW.close();
			}
			else {
				successful++;
				SendtoDB(Array);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in sortCSVline(String)");
			e.printStackTrace();
		}		
	}
	
	// sends valid entries to a database.
	private void SendtoDB(ArrayList<Object> Array) {
			try {
				Connect.setAutoCommit(false);
				String insert = "INSERT INTO " + TableName + " (A,B,C,D,E,F,G,H,I,J) VALUES (?,?,?,?,?,?,?,?,?,?);";
				PreparedStatement PS = Connect.prepareStatement(insert);
				PS.setString(1, (String) Array.get(0));
				PS.setString(2, (String) Array.get(1));
				PS.setString(3, (String) Array.get(2));
				PS.setString(4, (String) Array.get(3));
				PS.setString(5, (String) Array.get(4));
				PS.setString(6, (String) Array.get(5));
				PS.setString(7, (String) Array.get(6));
				PS.setString(8, (String) Array.get(7));
				PS.setString(9, (String) Array.get(8));
				PS.setString(10, (String) Array.get(9));
				PS.executeUpdate();
				Connect.commit();
			} catch (SQLException e) {
				System.out.println("Error in function: SendtoDB");
				e.printStackTrace();
			}	
	}
}
