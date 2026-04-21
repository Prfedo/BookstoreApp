import com.bookstore.view.MainWindow;
import com.bookstore.database.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.createTabels();
        MainWindow window = new MainWindow();
        window.setVisible(true);
    }
}