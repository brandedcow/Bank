import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * Manage connection to database and perform SQL statements.
 */
public class BankingSystem {
	// Connection properties
	private static String driver;
	private static String url;
	private static String username;
	private static String password;

	// JDBC Objects
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;

	/**
	 * Initialize database connection given properties file.
	 * @param filename name of properties file
	 */
	public static void init(String filename) {
		try {
			Properties props = new Properties();						// Create a new Properties object
			FileInputStream input = new FileInputStream(filename);	// Create a new FileInputStream object using our filename parameter
			props.load(input);										// Load the file contents into the Properties object
			driver = props.getProperty("jdbc.driver");				// Load the driver
			url = props.getProperty("jdbc.url");						// Load the url
			username = props.getProperty("jdbc.username");			// Load the username
			password = props.getProperty("jdbc.password");			// Load the password
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test database connection.
	 */
	public static void testConnection() {
		System.out.println(":: TEST - CONNECTING TO DATABASE");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			con.close();
			System.out.println(":: TEST - SUCCESSFULLY CONNECTED TO DATABASE");
			} catch (Exception e) {
				System.out.println(":: TEST - FAILED CONNECTED TO DATABASE");
				e.printStackTrace();
			}
	  }

	/**
	 * Create a new customer.
	 * @param name customer name
	 * @param gender customer gender
	 * @param age customer age
	 * @param pin customer pin
	 */
	public static void newCustomer(String name, String gender, String age, String pin)
	{
		try {
			con = DriverManager.getConnection(url,username,password);
			stmt = con.createStatement();
			String query = String.format("INSERT INTO P1.CUSTOMER (NAME, GENDER, AGE, PIN) VALUES ('%s','%s', %s,%s)", name, gender.toUpperCase(), age, pin);
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				String id = rs.getString(1);
				String output = String.format(":: CREATE NEW CUSTOMER #%s - SUCCESS\n",id);
				System.out.println(output);
			}
			con.close();
		} catch (Exception err) {
			System.out.println(":: CREATE NEW CUSTOMER - FAILURE");
			System.out.println(err);
			System.out.println();
		}
	}

	/**
	 * Open a new account.
	 * @param id customer id
	 * @param type type of account
	 * @param amount initial deposit amount
	 */
	public static boolean authCustomer(String id, String pin)
	{
		try {
			con = DriverManager.getConnection(url,username,password);
			stmt = con.createStatement();
			String query = String.format("SELECT * FROM P1.CUSTOMER WHERE (ID=%s AND PIN=%s)", id, pin);
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				String output = String.format(":: AUTHENTICATED CUSTOMER #%s - SUCCESS\n",id);
				System.out.println(output);
				con.close();
				return true;
			}
			con.close();
		} catch (Exception err) {
			System.out.println(":: AUTHENTICATED CUSTOMER - FAILURE");
			System.out.println(err);
			System.out.println();
		}
		return false;
	}


	/**
	 * Open a new account.
	 * @param id customer id
	 * @param type type of account
	 * @param amount initial deposit amount
	 */
	public static void openAccount(String id, String type, String amount)
	{
		try {
			con = DriverManager.getConnection(url,username,password);
			stmt = con.createStatement();
			String query = String.format("INSERT INTO P1.ACCOUNT (ID, BALANCE, TYPE) VALUES (%s, %s, '%s')", id, amount, type.toUpperCase());
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getString(1);
				String output = String.format(":: OPEN ACCOUNT #%s - SUCCESS\n",id);
				System.out.println(output);
			}
			con.close();
		} catch (Exception err) {
			System.out.println(":: OPEN ACCOUNT #%s - FAILURE");
			System.out.println(err);
			System.out.println();
		}
	}

	/**
	 * Close an account.
	 * @param accNum account number
	 */
	public static void closeAccount(String accNum)
	{
		try {
			String output = ":: CLOSE ACCOUNT - FAILURE\n";
			con = DriverManager.getConnection(url,username,password);
			stmt = con.createStatement();
			String query = String.format("UPDATE P1.ACCOUNT SET BALANCE=0, STATUS='I' WHERE (NUMBER=%s)", accNum);
			int res = stmt.executeUpdate(query);
			if (res > 0) {
				output = String.format(":: CLOSE ACCOUNT #%s - SUCCESS\n",accNum);
			}
			con.close();
			System.out.println(output);
		} catch (Exception err) {
			System.out.println(":: CLOSE ACCOUNT - FAILURE");
			System.out.println(err);
			System.out.println();
		}
	}

	/**
	 * Deposit into an account.
	 * @param accNum account number
	 * @param amount deposit amount
	 */
	public static void deposit(String accNum, String amount)
	{
		try {
			String output = ":: DEPOSIT - FAILURE\n";
			con = DriverManager.getConnection(url,username,password);
			stmt = con.createStatement();
			String query = String.format("UPDATE P1.ACCOUNT SET BALANCE=BALANCE+%s WHERE (NUMBER=%s AND STATUS='A')", amount, accNum);
			int res = stmt.executeUpdate(query);
			if (res > 0) {
				output = String.format(":: DEPOSIT INTO ACCOUNT #%s - SUCCESS\n",accNum);
			}
			con.close();
			System.out.println(output);
		} catch (Exception err) {
			System.out.println(":: DEPOSIT - FAILURE");
			System.out.println(err);
			System.out.println();
		}
	}

	/**
	 * Withdraw from an account.
	 * @param accNum account number
	 * @param amount withdraw amount
	 */
	public static void withdraw(String accNum, String amount)
	{
		try {
			String output = ":: WITHDRAW - FAILURE\n";
			con = DriverManager.getConnection(url,username,password);
			stmt = con.createStatement();
			String query = String.format("UPDATE P1.ACCOUNT SET BALANCE=BALANCE-%s WHERE (NUMBER=%s AND STATUS='A')", amount, accNum);
			int res = stmt.executeUpdate(query);
			if (res > 0) {
				output = String.format(":: WITHDRAW FROM ACCOUNT #%s - SUCCESS\n",accNum);
			}
			con.close();
			System.out.println(output);
		} catch (Exception err) {
			System.out.println(":: WITHDRAW - FAILURE");
			System.out.println(err);
			System.out.println();
		}
	}

	/**
	 * Transfer amount from source account to destination account.
	 * @param srcAccNum source account number
	 * @param destAccNum destination account number
	 * @param amount transfer amount
	 */
	public static void transfer(String srcAccNum, String destAccNum, String amount)
	{
		try {
			String output =":: TRANSFER - FAILURE\n";
			con = DriverManager.getConnection(url,username,password);
			stmt = con.createStatement();
			String query = String.format("UPDATE P1.ACCOUNT SET BALANCE=BALANCE-%s WHERE (NUMBER=%s AND STATUS='A')", amount, srcAccNum);
			int res = stmt.executeUpdate(query);
			if (res > 0) {
				query = String.format("UPDATE P1.ACCOUNT SET BALANCE=BALANCE+%s WHERE (NUMBER=%s AND STATUS='A')", amount, destAccNum);
				res = stmt.executeUpdate(query);
				if (res > 0) {
					output = String.format(":: TRANSFER FROM ACCOUNT #%s TO ACCOUNT #%s - SUCCESS\n", srcAccNum, destAccNum);
				}
			}
			con.close();
			System.out.println(output);
		} catch (Exception err) {
			System.out.println(":: TRANSFER - FAILURE");
			System.out.println(err);
			System.out.println();
		}
	}

	/**
	 * Display account summary.
	 * @param accNum account number
	 */
	public static void accountSummary(String id)
	{
		try {
			rs = null;
			String output =":: ACCOUNT SUMMARY - FAILURE\n";
			con = DriverManager.getConnection(url,username,password);
			stmt = con.createStatement();
			String query = String.format("SELECT * FROM P1.ACCOUNT WHERE (ID=%s AND STATUS='A')", id);
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				output = String.format(":: ACCOUNT SUMMARY FOR CUSTOMER #%s - SUCCESS\n", id);
				output += String.format("%-6s %-12s", "NUMBER","BALANCE");
				System.out.println(output);
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					output = String.format("%-6s %-12s", rs.getString(1), rs.getString(3));
					System.out.println(output);
				}
			} else {
				System.out.println(":: ACCOUNT SUMMARY - FAILURE\n");
			}
			con.close();
		} catch (Exception err) {
			System.out.println(":: ACCOUNT SUMMARY - FAILURE");
			System.out.println(err);
			System.out.println();
		}
	}

	/**
	 * Display Report A - Customer Information with Total Balance in Decreasing Order.
	 */
	public static void reportA()
	{
		try {
			String output =":: REPORT A - FAILURE\n";
			con = DriverManager.getConnection(url,username,password);
			stmt = con.createStatement();
			String query = "SELECT * FROM V_TOTAL_BALANCE";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				rs = stmt.executeQuery(query);
				output = ":: REPORT A - SUCCESS\n";
				output += String.format("%-5s %-12s %-4s %-6s %-12s\n", "ID","NAME","AGE","GENDER","BALANCE");
				System.out.println(output);
				while(rs.next()) {
					output = String.format("%-5s %-12s %-4s %-6s %-12s", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
					System.out.println(output);
				}
			} else {
				System.out.println(output);
			}
			con.close();
		} catch (Exception err) {
			System.out.println(":: REPORT A - FAILURE");
			System.out.println(err);
			System.out.println();
		}
	}

	/**
	 * Display Report B - Customer Information with Total Balance in Decreasing Order.
	 * @param min minimum age
	 * @param max maximum age
	 */
	public static void reportB(String min, String max)
	{
		try {
			rs = null;
			String output =":: REPORT B - FAILURE\n";
			con = DriverManager.getConnection(url,username,password);
			stmt = con.createStatement();
			String query = String.format("SELECT AVG(BALANCE) FROM V_TOTAL_BALANCE WHERE AGE>=%s AND AGE<=%s",min, max);
			rs = stmt.executeQuery(query);
			if (rs != null) {
				output = ":: REPORT B - SUCCESS";
				System.out.println(output);
			}
			while (rs.next()) {
				output = String.format("Average Total Balance For Age %s to %s: %s", min, max, rs.getString(1));
				System.out.println(output);
			}
			con.close();
		} catch (Exception err) {
			System.out.println(":: REPORT B - FAILURE");
			System.out.println(err);
			System.out.println();
		}
	}


		/**
		 * find account - verify if account number associated with customer id
		 * @param id customer id
		 * @param number account number
		 */
		public static boolean findAccount(String id, String number)
		{
			try {
				rs = null;
				String output =":: VERIFYING ACCOUNT - FAILURE\n";
				con = DriverManager.getConnection(url,username,password);
				stmt = con.createStatement();
				String query = String.format("SELECT * FROM P1.ACCOUNT WHERE(ID=%s AND NUMBER=%s AND STATUS='A')", id, number);
				rs = stmt.executeQuery(query);
				if (rs.next()) {
					output =":: VERIFYING ACCOUNT - SUCCESS";
					System.out.println(output);
					return true;
				} else {
					System.out.println(output);
				}
				con.close();
			} catch (Exception err) {
				System.out.println(":: VERIFYING ACCOUNT - FAILURE");
				System.out.println(err);
				System.out.println();
			}
			return false;
		}
}
