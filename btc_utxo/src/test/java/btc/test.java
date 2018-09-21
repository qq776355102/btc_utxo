package btc;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class test {
	//连接MySQL
	public static Connection connSQL() {
	String url = "jdbc:mysql://localhost:3306/cmc4.0_test?characterEncoding=UTF-8&noAccessToProcedureBodies=true";
	String username = "root";
	String password = "123456"; 
	Connection connection = null;
	// 加载驱动程序以连接数据库
	try { 
	Class.forName("com.mysql.jdbc.Driver" ); 
	connection = (Connection) DriverManager.getConnection( url,username, password );
	System.out.println("======>数据库连接成功");
	return connection;
	}
	//捕获加载驱动程序异常
	catch ( ClassNotFoundException cnfex ) {
	System.err.println(
	"装载 JDBC/ODBC 驱动程序失败。" );
	cnfex.printStackTrace(); 
	return null;
	} 
	//捕获连接数据库异常
	catch ( SQLException sqlex ) {
	System.err.println( "无法连接数据库" );
	sqlex.printStackTrace(); 
	return null;
	}
	}
	//释放MySQL连接
	public static void deconnSQL(Connection conn) {
	try {
	if (conn != null)
	conn.close();
	System.out.println("======>数据库断开成功");
	} catch (Exception e) {
	System.out.println("关闭数据库问题 ：");
	e.printStackTrace();
	}
	}
	public static void main(String[] args) {
		Connection connSQL = connSQL();
		System.out.println(connSQL);
	}
}
