package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {
    public static Connection con = null;

    public static Connection getInstance() {
        try {
            if (con == null || con.isClosed()){
                Class.forName("org.postgresql.Driver");
                con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/products","postgres","root123");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return con;
    }
}
