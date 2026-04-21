import com.bookstore.database.BookDAO;
import com.bookstore.database.ConnectSQL;
import com.bookstore.database.DatabaseManager;
import com.bookstore.database.OrderDAO;
import com.bookstore.database.UserDAO;
import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.Order;
import com.bookstore.model.User;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseFolderSmokeTest {
    public static void main(String[] args) {
        System.out.println("=== Database folder smoke test ===");
        int passed = 0;
        int failed = 0;

        ConnectSQL connectSQL = new ConnectSQL();
        BookDAO bookDAO = new BookDAO();
        UserDAO userDAO = new UserDAO();
        OrderDAO orderDAO = new OrderDAO();

        // 1) ConnectSQL
        try {
            Connection c = connectSQL.connectSQLite();
            if (c != null && !c.isClosed()) {
                passed++;
                c.close();
                System.out.println("[PASS] ConnectSQL.connectSQLite()");
            } else {
                failed++;
                System.out.println("[FAIL] ConnectSQL.connectSQLite(): null/closed connection");
            }
        } catch (Exception e) {
            failed++;
            System.out.println("[FAIL] ConnectSQL.connectSQLite(): " + e.getMessage());
        }

        // 2) DatabaseManager
        try {
            DatabaseManager.createTabels();
            passed++;
            System.out.println("[PASS] DatabaseManager.createTabels()");
        } catch (Exception e) {
            failed++;
            System.out.println("[FAIL] DatabaseManager.createTabels(): " + e.getMessage());
        }

        // 3) BookDAO basic operations
        Book insertedBook = null;
        try {
            List<Book> before = bookDAO.getAllBooks();
            int beforeCount = before.size();
            insertedBook = new Book(0, "Smoke Book " + System.currentTimeMillis(), "Smoke Author", "Testing", 9.99, 3, "atomic_habits.jpg");
            bookDAO.addBook(insertedBook);
            List<Book> afterInsert = bookDAO.getAllBooks();
            boolean inserted = afterInsert.size() >= beforeCount + 1;

            List<Book> search = bookDAO.searchBooks("Smoke Book");
            boolean searched = !search.isEmpty();

            if (searched) {
                Book latest = search.get(search.size() - 1);
                latest.setPrice(11.11);
                bookDAO.updateBook(latest);
                bookDAO.deleteBook(latest.getId());
            }

            if (inserted && searched) {
                passed++;
                System.out.println("[PASS] BookDAO getAll/add/search/update/delete");
            } else {
                failed++;
                System.out.println("[FAIL] BookDAO operations did not return expected results");
            }
        } catch (Exception e) {
            failed++;
            System.out.println("[FAIL] BookDAO operations: " + e.getMessage());
        }

        // 4) UserDAO basic operations
        try {
            String unique = String.valueOf(System.currentTimeMillis());
            String username = "smoke_u_" + unique;
            String email = "smoke_u_" + unique + "@test.com";
            String password = "12345";

            boolean registered = userDAO.register("Smoke User", username, email, password);
            User loggedIn = userDAO.login(email, password);
            User wrongLogin = userDAO.login(email, "wrong");
            User userById = loggedIn == null ? null : userDAO.getUserById(loggedIn.getId());

            if (registered && loggedIn != null && wrongLogin == null && userById != null) {
                passed++;
                System.out.println("[PASS] UserDAO register/login/getUserById");
            } else {
                failed++;
                System.out.println("[FAIL] UserDAO operations returned unexpected results");
            }
        } catch (Exception e) {
            failed++;
            System.out.println("[FAIL] UserDAO operations: " + e.getMessage());
        }

        // 5) OrderDAO basic operations
        try {
            List<Book> books = bookDAO.getAllBooks();
            if (books.isEmpty()) {
                throw new RuntimeException("No books available to create order item");
            }

            String unique = String.valueOf(System.currentTimeMillis());
            String username = "smoke_o_" + unique;
            String email = "smoke_o_" + unique + "@test.com";
            String password = "12345";
            userDAO.register("Order User", username, email, password);
            User user = userDAO.login(email, password);
            if (user == null) {
                throw new RuntimeException("Could not create/login smoke test user for order");
            }

            List<CartItem> items = new ArrayList<>();
            items.add(new CartItem(books.get(0), 2));
            Order order = new Order(0, user.getId(), items, 0, LocalDate.now().toString());
            orderDAO.saveOrder(order);

            List<Order> userOrders = orderDAO.getOrdersByUserId(user.getId());
            if (!userOrders.isEmpty()) {
                passed++;
                System.out.println("[PASS] OrderDAO saveOrder/getOrdersByUserId");
            } else {
                failed++;
                System.out.println("[FAIL] OrderDAO returned empty order history after save");
            }
        } catch (Exception e) {
            failed++;
            System.out.println("[FAIL] OrderDAO operations: " + e.getMessage());
        }

        System.out.println("---");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("=== Smoke test done ===");
    }
}
