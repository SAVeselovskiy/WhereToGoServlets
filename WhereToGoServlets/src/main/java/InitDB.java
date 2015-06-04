/**
 * Created by Admin on 04.06.2015.
 */
import java.sql.*;
public class InitDB {
    private Connection connection;
    private  Statement stmt;
    private  ResultSet rs;

    public InitDB() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(
                "jdbc:postgresql://54.200.192.248:5432/WhereToGo", "postgres", "serga464");
        stmt = connection.createStatement();
    }

    public Connection getConn() {
        return connection;
    }

    public ResultSet getRs(String sql) throws SQLException {
        if (rs != null && !rs.isClosed())
            rs.close();
        rs = stmt.executeQuery(sql);
        return rs;
    }

    public void update(String sql) throws SQLException {
        stmt.executeUpdate(sql);
    }

    public void closeAll() throws SQLException {
        if (rs != null) rs.close();
        stmt.close();
        connection.close();
    }
}
