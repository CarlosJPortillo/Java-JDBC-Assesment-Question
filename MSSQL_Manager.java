import java.sql.*;
import com.microsoft.sqlserver.jdbc.*;

//Class operates specific SQL commands to a particular database table
//Note: This class does not handle input validation, another class in the program
//would be used to do that before passing parameters into this classes methods to 
//prevent sql injection attacks and other potential errors 

public class MSSQL_Manager {
	

	private final String connectionUrl = "jdbc:sqlserver://localhost;" +
	"databaseName=Cjp_Database;integratedSecurity=true;";
	
	private final String table = "dbo.Customers";
	
	//Declare the JDBC components 
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	
	public void connect(){
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection(connectionUrl);
			statement = connection.createStatement();
			 
		}
		catch(Exception e){
			System.out.println("Couldn't connect");
		}
	}
	public void closeConnection() {
		if(connection!= null)
		{
			try {
				connection.close();
			} catch (SQLException e) {
				
				System.out.println("An error has occured with trying to close connection");
			}
		}
	}
	//Gets the current highest Id value in table
	public int getMaxID() {
		int maxId = 0;
		String SQL =  "SELECT MAX(Id) as MaxId FROM " + table;
		try {
			resultSet = statement.executeQuery(SQL);
			if(resultSet.next()) {
				maxId = resultSet.getInt("MaxId");
			}
			
		} catch (SQLException e) {
			System.out.println("An error has occured with trying to execute query");
			maxId = -2;
		}
		return maxId;
	}
	//Insert row into table
	public void Insert(String address1, String address2, String city, String state, String zip, String phone) {
		//increment to the next unique id value in table
		 int id = getMaxID() + 1;
		 if(id!= -1) {
			 String SQL = "INSERT into " + table + " (Id, Address1, Address2, City, State, Zip, Phone)\r\n" + 
			 		  "values (" + id + ",'" + address1 + "','" + address2 + "','" + city + "','" + state + "','" + 
				      zip + "','" + phone + "');";
		  
			 try {
				statement.executeUpdate(SQL);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("An error has occured with trying to execute query");
			}	
		 }
		
	}
	//Get string representation of specific row returned based on Id, since id is the only unique value in the table with no repeated values allowed
	public String getRowById(int id) {
		String rowData = "";
		String SQL = "SELECT * FROM " + table + " WHERE Id = " + id + ";";
		try {
			resultSet = statement.executeQuery(SQL);
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();
			while(resultSet.next()) {
				for (int i = 1; i <= numberOfColumns; i++) {
					rowData += resultSet.getString(i);
					if(i < numberOfColumns) {
						rowData += ", ";
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("An error has occured with trying to execute query");
		}
		return rowData;
		
	}
	public String getColumnByName(String columnName) {
		String columnData = "";
		String SQL = "SELECT * FROM dbo.Customers;";
		try {
			resultSet = statement.executeQuery(SQL);
			//boolean to check if there are more rows in resultSet
			boolean hasNext = resultSet.next();
			while(hasNext) {
				columnData += resultSet.getString(columnName);
				//fetch next row
				hasNext = resultSet.next();
				if(hasNext) {
					columnData += ", ";
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("An error has occured with trying to execute query");
		}
		return columnData;
	}
	

}
