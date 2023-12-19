package runbunny;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Dimension d;
    private final Font smallFont = new Font("Arial", Font.BOLD, 14);
    private boolean inGame = false;
    private boolean dying = false;

    private final int BLOCK_SIZE = 24;
    private final int N_BLOCKS = 25;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int MAX_VIRUS = 12;
    private final int BUNNY_SPEED = 6;

    private int N_VIRUS = 6;
    private int lives, score;
    private int[] dx, dy;
    private int[] virus_x, virus_y, virus_dx, virus_dy, virusSpeed;

    private Image heart, virus;
    private Image up, down, left, right;

    private int bunny_x, bunny_y, bunny_dx, bunny_dy;
    private int req_dx, req_dy;

    private final short levelData[] = {
        	19, 18, 18, 18, 18, 22,  0, 19, 18, 18, 22,  0, 19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
            17, 16, 24, 24, 24, 28,  0, 17, 16, 16, 20,  0, 17, 16, 16, 16, 24, 24, 24, 24, 24, 24, 24, 24, 20,
            17, 20,  0,  0,  0,  0,  0, 17, 16, 16, 20,  0, 17, 16, 16, 20,  0,  0,  0,  0,  0,  0,  0,  0, 21,
            17, 20,  0, 19, 18, 18, 18, 16, 16, 16, 20,  0, 17, 16, 16, 16, 18, 18, 18, 18, 18, 18, 22,  0, 21,
            17, 20,  0, 17, 16, 16, 16, 16, 16, 16, 20,  0, 25, 24, 24, 24, 24, 24, 24, 16, 16, 16, 20,  0, 21,
            17, 20,  0, 17, 16, 16, 16, 16, 16, 16, 20,  0,  0,  0,  0,  0,  0,  0,  0, 17, 16, 16, 20,  0, 21,
            17, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 18, 18, 18, 18, 18, 18, 18, 18, 16, 16, 16, 20,  0, 21,
            17, 20,  0, 17, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 21,
            17, 16, 18, 16, 16, 24, 28,  0, 17, 16, 16, 16, 24, 24, 24, 24, 24, 16, 16, 16, 16, 16, 16, 18, 20,
            17, 16, 16, 16, 20,  0,  0,  0, 17, 16, 16, 20,  0,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 18, 22,  0, 17, 16, 16, 16, 18, 18, 18, 22,  0, 17, 16, 16, 16, 16, 16, 16, 20,
            25, 24, 24, 24, 24, 24, 28,  0, 25, 24, 24, 24, 24, 24, 16, 20,  0, 17, 16, 16, 16, 24, 24, 24, 28,
             0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17, 20,  0, 17, 16, 16, 28,  0,  0,  0,  0,
            19, 18, 18, 22,  0, 19, 18, 18, 18, 18, 18, 18, 18, 18, 16, 20,  0, 17, 16, 20,  0,  0, 19, 18, 22,
            17, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 17, 16, 20,  0, 19, 16, 16, 20,
            17, 16, 24, 28,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 24, 28,  0, 17, 16, 20,  0, 17, 16, 16, 20,
            17, 20,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0,  0, 17, 16, 20,  0, 17, 16, 16, 20,
            17, 16, 18, 22,  0, 17, 16, 16, 24, 24, 24, 16, 16, 16, 18, 18, 18, 16, 16, 20,  0, 17, 16, 16, 20,
            17, 16, 16, 20,  0, 17, 16, 20,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 24, 28,  0, 17, 16, 16, 20,
            17, 16, 16, 20,  0, 17, 16, 16, 18, 22,  0, 17, 16, 16, 16, 16, 16, 20,  0,  0,  0, 17, 16, 16, 20,
            17, 16, 24, 16, 18, 16, 16, 16, 16, 20,  0, 25, 24, 24, 24, 16, 16, 16, 18, 18, 18, 16, 16, 24, 28,
            17, 20,  0, 17, 16, 16, 16, 16, 16, 20,  0,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 28,  0,  0,
            17, 20,  0, 25, 24, 24, 24, 16, 16, 20,  0, 19, 18, 18, 18, 16, 16, 16, 16, 16, 16, 28,  0,  0, 23,
            17, 20,  0,  0,  0,  0,  0, 17, 16, 16, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0, 19, 20,
            25, 24, 30,  0, 27, 26, 26, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 26, 26, 24, 28
        };

    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};
    private final int maxSpeed = 6;

    private int currentSpeed = 3;
    private short[] screenData;
    private Timer timer;

    public Game() {
        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();
    }
    
    
    private void loadImages() {
    	down = new ImageIcon("img/down.gif").getImage();
    	up = new ImageIcon("img/up.gif").getImage();
    	left = new ImageIcon("img/left.gif").getImage();
    	right = new ImageIcon("img/right.gif").getImage();
        virus = new ImageIcon("img/virus.gif").getImage();
        heart = new ImageIcon("img/heart.png").getImage();
    }
       private void initVariables() {
        screenData = new short[N_BLOCKS * N_BLOCKS];
        d = new Dimension(900, 900);
        virus_x = new int[MAX_VIRUS];
        virus_dx = new int[MAX_VIRUS];
        virus_y = new int[MAX_VIRUS];
        virus_dy = new int[MAX_VIRUS];
        virusSpeed = new int[MAX_VIRUS];
        dx = new int[4];
        dy = new int[4];
        timer = new Timer(40, this);
        timer.start();
    }

    private void playGame(Graphics2D g2d) {
        if (dying) {
            death();
        } else {
            moveBunny();
            drawBunny(g2d);
            moveVirus(g2d);
            checkMaze();
        }
    }

    private void showIntroScreen(Graphics2D g2d) {
    	String start = "Press SPACE to start";
        g2d.setColor(Color.yellow);
        g2d.drawString(start, 240, 270);
    }

    private void drawScore(Graphics2D g) {
        g.setFont(smallFont);
        g.setColor(new Color(5, 181, 79));
        String s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);
        for (int i = 0; i < lives; i++) {
            g.drawImage(heart, i * 28 + 8, SCREEN_SIZE + 1, this);
        }
    }

    private void checkMaze() {
        int i = 0;
        boolean finished = true;
        while (i < N_BLOCKS * N_BLOCKS && finished) {
            if ((screenData[i]) != 0) {
                finished = false;
            }
            i++;
        }
        if (finished) {
            score += 50;
            if (N_VIRUS < MAX_VIRUS) {
                N_VIRUS++;
            }
            if (currentSpeed < maxSpeed) {
                currentSpeed++;
            }
            initLevel();
        }
    }

    private void death() {
    	lives--;
        if (lives == 0) {
            inGame = false;
        }
        continueLevel();
    }

    private void moveVirus(Graphics2D g2d) {
        int loc;
        int count;
        for (int i = 0; i < N_VIRUS; i++) {
            if (virus_x[i] % BLOCK_SIZE == 0 && virus_y[i] % BLOCK_SIZE == 0) {
                loc = virus_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (virus_y[i] / BLOCK_SIZE);
                count = 0;
                if ((screenData[loc] & 1) == 0 && virus_dx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }
                if ((screenData[loc] & 2) == 0 && virus_dy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }
                if ((screenData[loc] & 4) == 0 && virus_dx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }
                if ((screenData[loc] & 8) == 0 && virus_dy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }
                if (count == 0) {
                    if ((screenData[loc] & 15) == 15) {
                        virus_dx[i] = 0;
                        virus_dy[i] = 0;
                    } else {
                        virus_dx[i] = -virus_dx[i];
                        virus_dy[i] = -virus_dy[i];
                    }
                } else {
                    count = (int) (Math.random() * count);
                    if (count > 3) {
                        count = 3;
                    }
                    virus_dx[i] = dx[count];
                    virus_dy[i] = dy[count];
                }
            }
            virus_x[i] = virus_x[i] + (virus_dx[i] * virusSpeed[i]);
            virus_y[i] = virus_y[i] + (virus_dy[i] * virusSpeed[i]);
            drawvirus(g2d, virus_x[i] + 1, virus_y[i] + 1);
            if (bunny_x > (virus_x[i] - 12) && bunny_x < (virus_x[i] + 12)
                    && bunny_y > (virus_y[i] - 12) && bunny_y < (virus_y[i] + 12)
                    && inGame) {
                dying = true;
            }
        }
    }

    private void drawvirus(Graphics2D g2d, int x, int y) {
    	g2d.drawImage(virus, x, y, this);
        }

    private void moveBunny() {
        int loc;
        short ch;
        if (bunny_x % BLOCK_SIZE == 0 && bunny_y % BLOCK_SIZE == 0) {
            loc = bunny_x / BLOCK_SIZE + N_BLOCKS * (int) (bunny_y / BLOCK_SIZE);
            ch = screenData[loc];
            if ((ch & 16) != 0) {
                screenData[loc] = (short) (ch & 15);
                score++;
            }
            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    bunny_dx = req_dx;
                    bunny_dy = req_dy;
                }
            }
            if ((bunny_dx == -1 && bunny_dy == 0 && (ch & 1) != 0)
                    || (bunny_dx == 1 && bunny_dy == 0 && (ch & 4) != 0)
                    || (bunny_dx == 0 && bunny_dy == -1 && (ch & 2) != 0)
                    || (bunny_dx == 0 && bunny_dy == 1 && (ch & 8) != 0)) {
                bunny_dx = 0;
                bunny_dy = 0;
            }
        } 
        bunny_x = bunny_x + BUNNY_SPEED * bunny_dx;
        bunny_y = bunny_y + BUNNY_SPEED * bunny_dy;
    }

    private void drawBunny(Graphics2D g2d) {
        if (req_dx == -1) {
        	g2d.drawImage(left, bunny_x + 1, bunny_y + 1, this);
        } else if (req_dx == 1) {
        	g2d.drawImage(right, bunny_x + 1, bunny_y + 1, this);
        } else if (req_dy == -1) {
        	g2d.drawImage(up, bunny_x + 1, bunny_y + 1, this);
        } else {
        	g2d.drawImage(down, bunny_x + 1, bunny_y + 1, this);
        }
    }

    private void drawMaze(Graphics2D g2d) {
        short i = 0;
        int x, y;
        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {
                g2d.setColor(new Color(0,72,251));
                g2d.setStroke(new BasicStroke(5));
                if ((levelData[i] == 0)) { 
                	g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                 }
                if ((screenData[i] & 1) != 0) { 
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                }
                if ((screenData[i] & 2) != 0) { 
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }
                if ((screenData[i] & 4) != 0) { 
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }
                if ((screenData[i] & 8) != 0) { 
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }
                if ((screenData[i] & 16) != 0) { 
                    g2d.setColor(new Color(255,255,255));
                    g2d.fillOval(x + 10, y + 10, 6, 6);
               }
                i++;
            }
        }
    }

    private void initGame() {
    	lives = 3;
        score = 0;
        initLevel();
        N_VIRUS = 6;
        currentSpeed = 3;
    }

    private void initLevel() {
        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }
        continueLevel();
    }

    private void continueLevel() {
    	int dx = 1;
        int random;
        for (int i = 0; i < N_VIRUS; i++) {
            virus_y[i] = 4 * BLOCK_SIZE;
            virus_x[i] = 4 * BLOCK_SIZE;
            virus_dy[i] = 0;
            virus_dx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (currentSpeed + 1));
            if (random > currentSpeed) {
                random = currentSpeed;
            }
            virusSpeed[i] = validSpeeds[random];
        }
        bunny_x = 24 * BLOCK_SIZE;
        bunny_y = 0 * BLOCK_SIZE;
        bunny_dx = 0;
        bunny_dy = 0;
        req_dx = 0;
        req_dy = 0;
        dying = false;
    }

 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);
        drawMaze(g2d);
        drawScore(g2d);
        if (inGame) {
            playGame(g2d);
        } else {
            showIntroScreen(g2d);
        }
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    class TAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (inGame) {
                if (key == KeyEvent.VK_LEFT) {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    inGame = false;
                } 
            } else {
                if (key == KeyEvent.VK_SPACE) {
                    inGame = true;
                    initGame();
                }
            }
        }
}

    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}