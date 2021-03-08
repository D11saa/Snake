import javax.swing.*;
import java.lang.reflect.Method;

public class MainWindow extends JFrame {

    public MainWindow()  {

        setTitle("Snake By Disa");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(320,345);
        setLocation(400,400);


    }

    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
        GameField g = new GameField(1);
        mw.add(g);
        mw.setVisible(true);

    }
}
