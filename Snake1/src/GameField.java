import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.AccessController;
import java.util.Random;

class GameField extends JPanel implements ActionListener {
    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private Image dot;
    private Image apple;
    private int appleX;
    private int appleY;
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private int dots;
    private Timer timer;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;
    private int coin = 0;
    private boolean finish = false;
    private Database db = new Database();
    private int max = db.getMax();

    public GameField() {
        startGame();
    }

    public GameField(int i) {
        firstScreen();
    }

    public void startGame(){
        removeAll();
        this.setBackground(Color.green);
        this.loadImages();
        this.initGame();
        this.addKeyListener(new FieldkeyListenner(this));
        this.setFocusable(true);
    }

    public void firstScreen(){
        this.setBackground(Color.black);
        JButton start = new JButton("Start");
        start.setSize(80, 20);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        start.setVisible(true);

        this.add(start);
        this.setFocusable(true);

    }



    private void restartGame(){
        GameField gameField = new GameField();
        MainWindow mw =  new MainWindow();
        mw.add(gameField);
        mw.setVisible(true);
    }

    public void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i * DOT_SIZE;
            y[i] = 48;
        }
        timer = new Timer(250, this);
        timer.start();
        createApple();

    }

    public void createApple() {
        appleX = new Random().nextInt(20) * DOT_SIZE;
        appleY = new Random().nextInt(20) * DOT_SIZE;
    }

    public void loadImages() {
        ImageIcon iia = new ImageIcon("apples.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("maps.png");
        dot = iid.getImage();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            String feedback = "Score: " + this.coin;
            Font fr = new Font("Comic Sans MS Bold Italic", Font.BOLD, 15);
            g.setColor(Color.black);
            g.setFont(fr);
            g.drawString(feedback, 10, 10);
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);
            }
        } else {
            String str = "Game Over!";
            String feedback = "Your score: " + this.coin;
            if (max < this.coin) {
                db.insertScore(this.coin);
                max = this.coin;
            }
            String high = "The highest score is: " + max;
            Font f = new Font("Comic Sans MS Bold Italic", Font.BOLD, 22);
            Font fr = new Font("Comic Sans MS Bold Italic", Font.BOLD, 15);

            g.setColor(Color.red);
            g.setFont(f);
            g.drawString(str, 100, 80);
            g.setColor(Color.yellow);
            g.drawString(feedback, 91, 135);

            g.setColor(Color.black);
            g.setFont(fr);
            g.drawString(high, 75, 180);

            JButton b = new JButton("Restart");
            b.setSize(100, 20);
            b.setLocation(120, 225);

            JButton to_finish = new JButton("Exit");
            to_finish.setSize(80, 20);
            to_finish.setLocation(130, 265);

            to_finish.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(1);
                }
            });
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    restartGame();
                }
            });
            if (!finish) {
                this.add(b);
                this.add(to_finish);
                finish = true;

            }
        }
    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (left) {
            x[0] -= DOT_SIZE;
        }
        if (right) {
            x[0] += DOT_SIZE;
        }
        if (up) {
            y[0] -= DOT_SIZE;
        }
        if (down) {
            y[0] += DOT_SIZE;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            this.coin++;
            if (dots % 10 == 0) {
                timer.setDelay(timer.getDelay() - 50);
            }
            createApple();
        }
    }

    public void checkCollisions() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }

        if (x[0] > SIZE) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
        if (y[0] > SIZE) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollisions();
            move();
        }

        repaint();
    }

    static class FieldkeyListenner extends KeyAdapter {
        private GameField gameField;

        public FieldkeyListenner(GameField gameField) {
            this.gameField = gameField;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();

//            if (key == KeyEvent.BUTTON1_DOWN_MASK) {
//
//            }

            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT && !gameField.right) {
                gameField.left = true;
                gameField.up = false;
                gameField.down = false;
            }

            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT && !gameField.left) {
                gameField.right = true;
                gameField.up = false;
                gameField.down = false;
            }

            if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP && !gameField.down) {
                gameField.left = false;
                gameField.up = true;
                gameField.right = false;
            }

            if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN && !gameField.up) {
                gameField.left = false;
                gameField.down = true;
                gameField.right = false;
            }

            if (key == KeyEvent.VK_P) {
                gameField.timer.stop();
            }

            if (key == KeyEvent.VK_O) {
                gameField.timer.start();
            }
        }
    }
}
