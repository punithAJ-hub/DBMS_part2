import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Servlet implementation class Connect
 */
@WebServlet("/billDAO")
public class BillDAO 
{
	private static final long serialVersionUID = 1L;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private QuoteDAO quoteDAO = new QuoteDAO();
	private HttpSession session=null;
	
	public BillDAO(){}
	
	/** 
	 * @see HttpServlet#HttpServlet()
     */
    protected void connect_func() throws SQLException {
    	//uses default connection to the database
        if (connect == null || connect.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            connect = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/testdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false&user=root&password=password");
            System.out.println(connect);
        }
    }
    
    public boolean database_login(String userName, String password) throws SQLException{
    	try {
    		connect_func("root","password");
    		String sql = "select * from user where user_name = ?";
    		preparedStatement = connect.prepareStatement(sql);
    		preparedStatement.setString(1, userName);
    		ResultSet rs = preparedStatement.executeQuery();
    		return rs.next();
    	}
    	catch(SQLException e) {
    		System.out.println("failed login");
    		return false;
    	}
    }
	//connect to the database 
    public void connect_func(String username, String password) throws SQLException {
        if (connect == null || connect.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            connect = (Connection) DriverManager
  			      .getConnection("jdbc:mysql://127.0.0.1:3306/userdb?"
  			          + "useSSL=false&user=" + username + "&password=" + password);
            System.out.println(connect);
        }
    }
    
    public List<Bill> listAllTree() throws SQLException {
        List<Bill> bills = new ArrayList<Bill>();        
        String sql = "SELECT * FROM Bill";      
        connect_func();      
        statement = (Statement) connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
         
        while (resultSet.next()) {
        	int billId = resultSet.getInt("bill_id");
        	int orderId = resultSet.getInt("order_id");
            
            String status = resultSet.getString("status");
            double totalPrice = resultSet.getDouble("total_Amount");
            String note = resultSet.getString("note");
            

             
            Bill bill = new Bill(billId,orderId,totalPrice,status,note);
            bills.add(bill);
        }        
        resultSet.close();
        disconnect();        
        return bills;
    }
    
    protected void disconnect() throws SQLException {
        if (connect != null && !connect.isClosed()) {
        	connect.close();
        }
    }
    
    public int insertBill(Bill bill) throws SQLException {
    	connect_func("root","password");         
    	System.out.println("Inserting bill details to Bill Table");
		String sql = "insert into Bill(bill_id,order_id, total_Amount,status,note) values (?,?,?,?,?)";
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
			preparedStatement.setInt(1,bill.getBillId());
			preparedStatement.setInt(2, bill.getorderId());
			preparedStatement.setDouble(3,bill.getTotalPrice() );
			preparedStatement.setString(4,bill.getStatus() );
			preparedStatement.setString(5,bill.getNote() );
		preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println("Done Inserting Bill details to bill Table");
        return bill.getBillId();
    }

  
     
    
    public void updateStatus(int billId, String status) throws SQLException {
    	connect_func("root","password");         
    	System.out.println("Inserting bill details to Bill Table");
		String sql = "update Bill set status=? where bill_id = ?";
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
			preparedStatement.setString(1,status);
			preparedStatement.setInt(2, billId);
			
		preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println("Done updating status of Bill details to bill Table");
       
    }

    public boolean delete(String email) throws SQLException {
        String sql = "DELETE FROM User WHERE email = ?";        
        connect_func();
         
        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
        preparedStatement.setString(1, email);
         
        boolean rowDeleted = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
        return rowDeleted;     
    }
    
    
   
    
    public List<Bill> getBillsByClientId(int clientId) throws SQLException {
    	Bill bill = null;
    	List<Bill> bills = new ArrayList<Bill>();
        String sql = "SELECT * FROM Bill b join Orders o on o.order_id=b.order_id where o.client_id = ?";
         
        connect_func();
         
        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
        preparedStatement.setInt(1, clientId);
         
        ResultSet resultSet = preparedStatement.executeQuery();
         
        while (resultSet.next()) {
        	int billId = resultSet.getInt("bill_id");
        	int orderId = resultSet.getInt("order_id");
            String status = resultSet.getString("status");
            double totalPrice = resultSet.getDouble("total_Amount");
            String note = resultSet.getString("note");
         
            bill = new Bill(billId,orderId,totalPrice,status,note);
            bills.add(bill);
            
        }
         
        resultSet.close();
         
        return bills;
    }
    
    
    
    public int generateId() throws SQLException {
    	int id = 0;
        String sql = "SELECT MAX(bill_id) as id FROM Bill";
         
        connect_func();
         
        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
         
        ResultSet resultSet = preparedStatement.executeQuery();
         
        if (resultSet.next()) {
            id = resultSet.getInt("id");
        }
         
        resultSet.close();
    
         
        return id+1;
    }

}
