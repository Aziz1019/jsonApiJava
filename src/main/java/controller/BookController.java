package controller;

import com.google.gson.Gson;
import dao.BookDao;
import model.Book;
import model.Message;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@WebServlet("/book")
public class BookController extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String json = null;
        if (id == null) {
            List<Book> books = BookDao.getBookList();
            Message ok = new Message(0, "ok", books);
            json = this.gson.toJson(ok);
        } else {
            Optional<Book> optionalBook = BookDao.findById(Long.parseLong(id));

            if (optionalBook.isPresent()) {
                Book book = optionalBook.get();
                Message ok = new Message(0, "ok", book);
                json = this.gson.toJson(ok);
            } else {
                Message message = new Message(404, "book not found", null);
                json = this.gson.toJson(message);
            }
        }
        PrintWriter writer = resp.getWriter();
        writer.print(json);
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        double price = Double.parseDouble(req.getParameter("price"));
        String author = req.getParameter("author");

        Book book = new Book(name, author, price);

        Long b = BookDao.addBook(book);
        String json;
        if (b > 0) {
            book.setId(b);
            Message saqlandi = new Message(1001, "Saqlandi", book);
            json = this.gson.toJson(saqlandi);
        } else {
            Message saqlanmadi = new Message(1002, "Saqlanmadi", book);
            json = this.gson.toJson(saqlanmadi);
        }
        resp.setContentType("application/json");
        resp.getWriter().print(json);
        resp.getWriter().close();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String response;
        if (id != null) {
            boolean delete = BookDao.delete(Long.parseLong(id));
            if (delete) {
                Message ok = new Message(0, "Ok", id);
                response = this.gson.toJson(ok);

            } else {
                Message ok = new Message(1003, "not deleted", id);
                response = this.gson.toJson(ok);
            }

        } else {
            Message ok = new Message(1004, "id not present", null);
            response = this.gson.toJson(ok);
        }
        resp.setContentType("application/json");
        resp.getWriter().print(response);
        resp.getWriter().close();

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));
        String name = req.getParameter("name");
        double price = Double.parseDouble(req.getParameter("price"));
        String author = req.getParameter("author");
        String response;

        Book book = new Book(id, name, author, price);
        boolean update = BookDao.update(book);
        Message ok;
        if (update){
            ok = new Message(0, "Ok", book);
        }else {
            ok = new Message(1005, "product did not update", book);
        }
        response = this.gson.toJson(ok);
        resp.setContentType("application/json");
        resp.getWriter().print(response);
        resp.getWriter().close();
    }

}
