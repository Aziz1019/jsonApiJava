package dao;

import model.Book;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BookDao {
    public static List<Book> getBookList(){
        Connection connection = PostgresConnection.getInstance();
        List<Book> books = new LinkedList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id, name, author, price from book order by id");
            while (resultSet.next()){
                books.add(new Book(resultSet.getLong(1),resultSet.getString(2),resultSet.getString(3)
                        ,resultSet.getDouble(4)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return books;
    }

    public static Optional<Book> findById(long id){
        Connection connection = PostgresConnection.getInstance();
        Optional<Book> book = Optional.empty();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id, name, author, price from book where id = "+id);
            if (resultSet.next()) {
                book = Optional.of(new Book(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3)
                        , resultSet.getDouble(4)));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return book;
    }

    public static Long addBook(Book book){
        Connection con = PostgresConnection.getInstance();
        try {
            PreparedStatement statement = con.prepareStatement("insert into book(name, author, price) VALUES (?,?,?) returning id");
            statement.setString(1,book.getName());
            statement.setString(2,book.getAuthor());
            statement.setDouble(3,book.getPrice());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getLong(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    public static boolean delete(long id){
        Connection connection = PostgresConnection.getInstance();
        try {
            PreparedStatement statement = connection.prepareStatement("delete from book where  id = ?");
            statement.setLong(1,id);
            int i = statement.executeUpdate();
            return i>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean update(Book book){
        Connection connection = PostgresConnection.getInstance();
        try {
            PreparedStatement statement = connection.prepareStatement("update book set name = ?,author = ?, price = ? where id = ?");
            statement.setString(1,book.getName());
            statement.setString(2,book.getAuthor());
            statement.setDouble(3,book.getPrice());
            statement.setLong(4,book.getId());
            int i = statement.executeUpdate();
            return i>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
