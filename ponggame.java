////////////////////////////////////////////////////////////////
//                                                            //
//      Matthew Athanasopoulos - Pong Game - Mar 6th 2023     //
//                                                            //
//  This is a basic pong game making use of awt and swing and //
//  has many methods, and collisions. This program uses a GUI //
//  to display the visual pong game. The game opens with a    //
//  main menu and the user can press start to play. The game  //
//  when started has two paddles and a ball. The two paddles  //
//  can be moved using Q and Z for the left, and UP and DOWN  //
//  for the right paddle. The ball moves around the screen    //
//  and bounces off walls and the paddles. When the ball hits //
//  the right or left wall, a point is given to whom scored.  //
//  The game can be paused and played with either the button  //
//  on screen, or with the ENTER key. The user can also       //
//  restart or exit the game with the buttons provided.       //
//  The game ends once a player reaches 5 points and at this  //
//  time the ball stops moving and the score is displayed     //
//  with a win message on screen. The user can play again     //
//  with the start/restart button, and the paddle location,   //
//  score, as well as ball location and velocity are reset.   //
//                                                            //
////////////////////////////////////////////////////////////////

// Importing Java Utilities

import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import javax.swing.*;
import java.awt.geom.*; 
import java.util.*;
import java.awt.Shape;

// Main Class

class PongGame extends JFrame implements ActionListener, KeyListener {
  
  // Name-constants for the various dimensions
  
  public static final int CANVAS_WIDTH = 700;
  public static final int CANVAS_HEIGHT = 700;
  
  public static final Color CANVAS_BACKGROUND = Color.BLACK;
  
  // The custom drawing canvas (extends JPanel)
  
  private DrawCanvas canvas; 
  
  // Creating objects for the buttons
  
  JButton btnRestart, btnExit, btnPauseplay;
  
  // Declaring global variables
  
  int dx, dy, paddleWidth, paddleHeight, score, score2, start, diameter;
  double tempX, tempY, temp = 0;
  double ballChange, ballChangeX, ballChangeY;  
  boolean started, pause;
  
  // Arraylist to hold user keyboard input
  
  ArrayList <String> keyList = new ArrayList <>();
  
  // Rectangles and Ellipse object for paddles and ball
  
  private Rectangle2D.Double paddle1; 
  private Ellipse2D.Double ball;
  
  // Timer and Random generator
  
  Random rand;
  Timer timer;
  
  // Constructor to set up the GUI 
  
  public PongGame() {
    
    // Initialize timer and rand objects
    
    timer =  new Timer(20, this);
    rand = new Random();
    
    // Initializing variables
    
    score = 0;
    score2 = 0;
    start = 0;
    paddleWidth = 50;
    paddleHeight = 50;
    diameter = 20;
    ballChange = Math.sqrt(25/2);
    ballChangeX =  ballChange;
    ballChangeY =  ballChange;
    pause = false;
    paddle1 = new Rectangle2D.Double(paddleWidth, CANVAS_HEIGHT/2, paddleWidth, paddleHeight);
    ball = new Ellipse2D.Double(CANVAS_WIDTH/2-diameter/2, CANVAS_HEIGHT/2-diameter/2, diameter, diameter);
    
    // Set up a panel for the buttons
    
    JPanel btnPanel = new JPanel(null);
    btnPanel.setBackground(Color.RED);
    btnPanel.setPreferredSize(new Dimension(300,CANVAS_HEIGHT));
    
    // Start/Restart button
    
    btnRestart = new JButton("Start/Restart");
    btnPanel.add(btnRestart);
    btnRestart.addActionListener(this);
    btnRestart.setBounds(200, 20, 120, 40);
    
    // Pause/Play button
    
    btnPauseplay = new JButton("Pause/Play");
    btnPanel.add(btnPauseplay);
    btnPauseplay.addActionListener(this); 
    btnPauseplay.setBounds(340, 20, 120, 40);
    
    // Exit Button
    
    btnExit = new JButton("Exit");
    btnPanel.add(btnExit);
    btnExit.addActionListener(this); 
    btnExit.setBounds(200, 20, 700, 40);
    
    // Set up a custom drawing JPanel
    
    canvas = new DrawCanvas();
    canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    
    // Add both panels to this JFrame
    
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    cp.add(canvas, BorderLayout.CENTER);
    cp.add(btnPanel, BorderLayout.EAST);
    
    // "this" JFrame fires KeyEvent
    
    addKeyListener(this);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    
    // Handle the CLOSE button
    
    // Sets title of window
    
    setTitle("Matthew Athanasopoulos - PONG game");
    
    pack();            // Packs all the components in the JFrame
    
    setVisible(true);  // Shows it
    
    requestFocus();
  }

  // Collision detection method
  
  public void isHitDetected() {
    
    // If ball hits top or bottom of screen
    
    if (ball.y < 0 || ball.y > 680) {
      ballChangeY = -ballChangeY;
    }
    
    // If ball hits left or right of screen
    
    if (ball.x < 0 || ball.x > 680) {
      ballChangeX = -ballChangeX;
      
      // If left increase score for right
      
      if (ball.x < 0) { 
        score2++; 
      }
      
      // If right increase score for left
      
      else if (ball.x > 680) { 
        score++; 
      }
    }
    
    // In the rare case the ball hits the exact corner 
    
    if ((ball.y == 0 && ball.x == 0)||(ball.y == 380 && ball.x == 0)||
        (ball.y == 680 && ball.x == 680) || (ball.y == 0 && ball.x == 680)){
      
      ballChangeX = -ballChangeX;
      ballChangeY = -ballChangeY;
    }
    
      
      // Checks if ball is hitting the front of paddle
      
      if (ball.x > paddle1.x - 24 && ball.x < paddle1.x - 74) {
        
        // Sends the ball back
        
        ballChangeX = -ballChangeX;
      
    }
  }
  
  // Overload
  
  public static boolean isHitDetected(Shape shapeA, Shape shapeB) {
    Area areaA = new Area(shapeA);
    areaA.intersect(new Area(shapeB));
    return !areaA.isEmpty();
  }
  
  // Update the coordinates of the shapes and refresh the screen  
  
  public void updateScreen() {
    
    // Checks for collision
    
    isHitDetected();
    
    // Ball direction is updated accordingly
    
    ball.x += ballChangeX;
    ball.y += ballChangeY;
    
    while ((isHitDetected(ball, paddle1) == true) && (score != 5)) {
      
      if (paddle1.x > 350) {
        ball.x -= 100;
      }
      
      else if (paddle1.x < 351) {
        ball.x += 100;
      }
      
      else if (paddle1.y > 350) {
        ball.y -= 100;
      }
      
      else if (paddle1.y < 351) {
        ball.y += 100;
      }
      
      score++;
    }
    
    if ((isHitDetected(ball, paddle1) == true) && score ==5){
    pausePlayGame(0);
    }
    
    // Repaint canvas
    
    canvas.repaint();
    
      }

  // If an action is performed
  
  public void actionPerformed(ActionEvent e) {
    
    // Check if restart button was pressed
    
    if (e.getSource()== btnRestart)
    {    
      System.out.println("Start/Restart");
      
      // Unpauses if was paused 
      
      if (pause == true) { 
        pause = false;
      }
      
      // Resetting variables

      ballChange = Math.sqrt(25/2);
      ballChangeX = ballChange;
      ballChangeY = ballChange;
      score = 0;
      score2 = 0;
      
      // For the first time to get past main menu
      
      start = 1;
      
      // Resetting the paddles locations 
      
      paddle1 = new Rectangle2D.Double(CANVAS_WIDTH/2-paddleWidth/2, CANVAS_HEIGHT/2 - 50, paddleWidth, paddleHeight);
      ball = new Ellipse2D.Double(CANVAS_WIDTH - 50, CANVAS_HEIGHT - 50, diameter, diameter);
      
      // Game has started
      
      started = true;
    }
    
    // If Pause/Play button clicked call the pausePlayGame method
    
    else if (e.getSource()==btnPauseplay) { 
      pausePlayGame(1);
    }
    
    // Check if exit button was pressed, just uses System.exit(0)
    
    else if (e.getSource()==btnExit) { 
      System.out.println("Game Exited");
      System.exit(0);
    }
    
    // If game is playing update the screen
    
    else if (e.getSource()==timer &&  started == true){
      updateScreen();   
    }
    
    // Change the focus to JFrame to receive KeyEvent
    
    requestFocus(); 
  }
  
  // Looks for if a key is pressed
  
  public void keyPressed(KeyEvent evt) {
    
    // Checks for key press and adds input to arraylist
    
    if (!keyList.contains(evt.getKeyCode()+"")) {
            keyList.add(evt.getKeyCode()+"");
        }       
    }
  
  // Looks for if a key is released
  
  public void keyReleased(KeyEvent evt) {
    
        // Removes if from arraylist if it exists
    
         if (keyList.contains(evt.getKeyCode()+"")){
             int index = keyList.indexOf(evt.getKeyCode()+"");
             keyList.remove(index);
        }   
    }
  
  // Method required as per "KeyListener", though not needed
  
  public void keyTyped(KeyEvent evt) {}
  
  // This method is used for pausing and playing the game
  
  public void pausePlayGame(int over) {
    
    // If game is not over
    
    if (over == 1) {
      
    // Pause value switches from true to false or false to true
      
    pause = !pause;
  
   // Removes other instances of enter being pressed
   
   while (keyList.contains("10")) {
      keyList.remove("10");
     }
   
   // Checks is the game is paused
   
   if (pause == true) {
     
      System.out.println("Game paused");      
     
      // If game paused, stops the ball movement, stores movement in temporary variables
     
      tempX = ballChangeX;
      tempY = ballChangeY;
      temp = ballChange;
      ballChangeX = 0;
      ballChangeY = 0;
      ballChange = 0;
    }
      
    // If game played reinstate original ball velocities
   
    if (pause == false) { 
      
      System.out.println("Game played");
      
      ballChangeX = tempX;
      ballChangeY = tempY;
      ballChange = temp;
      
    }
  }
    
    // If the game is over, ball freezes until Start/restart pressed
    
    else if (over == 0) {
      
      ballChangeX = 0;
      ballChangeY = 0;
      ballChange = 0;
      
      // Paddles are still free to move as this didn't have any negative impact
      
    }
  }

// DrawCanvas (inner class) is a JPanel used for custom drawing

  class DrawCanvas extends JPanel {
    
    // paintComponent method
    
    public void paintComponent(Graphics g) {
      
      // Calls from outer class
      
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D)g;
      
      // Background 
      
      setBackground(CANVAS_BACKGROUND); 

      // If the game is in progress
      
      if (start == 1) {
        
      // See if enter was pressed
        
      if (keyList.contains("10")){
        
        // Pauses the game
        
        pausePlayGame(1);
        }
      
      // If game is not paused
      
      if (pause == false) {
      
      // Check Q key
        
      if (keyList.contains("87")){ 
        
        // Move left paddle up
        
        if (paddle1.y > 0) {
        paddle1.y -= 5;
       }
      }
      
      // Check Z key
      
      if (keyList.contains("83")){ 
        
        // Move left paddle down
        
        if (paddle1.y < CANVAS_HEIGHT-paddleWidth) {
        paddle1.y += 5;
       }
      }
      
      if (keyList.contains("68")){ 
        
        // Move paddle right
        
        if (paddle1.x < CANVAS_WIDTH-paddleHeight) {
        paddle1.x += 5;
       }
      }
      
      if (keyList.contains("65")){ 
        
        // Move paddle left
        
        if (paddle1.x > 0) {
        paddle1.x -= 5;
       }
      }
      
      
     }
      
      // Paints the paddles and ball white
      
      g2d.setPaint(Color.WHITE);  
      g2d.fill(paddle1);
      g2d.fill(ball);
      
      // Draws text   
      
      g2d.setFont(new Font("Goudy Handtooled BT", Font.PLAIN, 40));
      
      // If game is paused and not over
      
      if (pause == true && score != 5 && score2 != 5) {
        g2d.drawString("GAME PAUSED", 250, 150);
      }
       
      // If player on left wins
      
      if (score == 10) { 
        
        // Displays winner message
        
        g2d.drawString("GAME OVER, PLAYER ON LEFT WINS", 40, 200);
        g2d.setFont(new Font("Goudy Handtooled BT", Font.PLAIN, 30));
        g2d.drawString("Score: " + score + "-" + score2 + ". Press Start/Restart to play again", 70, 300);
        
        // Calls pausePlayGame at 0, meaning game is over
        
        pausePlayGame(0);
      }
      
      // If player on right wins
      
      if (score2 == 10) {
        
        // Displays winner message
        
        g2d.drawString("GAME OVER, PLAYER ON RIGHT WINS", 30, 200);
        g2d.setFont(new Font("Goudy Handtooled BT", Font.PLAIN, 30));
        g2d.drawString("Score: " + score + "-" + score2 + ". Press Start/Restart to play again", 70, 300);
        
        // Calls pausePlayGame at 0, meaning game is over
        
        pausePlayGame(0);
      }         
      
      // Draws the scores
      
      
      g2d.drawString(score + "", 190, 50); 
      
      
      // If the game has not yet started, print the main menu
      }
      else if (start == 0) {
        
        g2d.setFont(new Font("Comic Sans", Font.PLAIN, 30));
        g2d.setPaint(Color.BLACK);
        g2d.fillRect(0,0,800,565);
        g2d.setPaint(Color.WHITE);
        g2d.drawString("AVOID",260, 60);

          }
        } 
      }

  // Main method
  
  public static void main(String[] args) {
    
    // Program prints a user input log for testing purposes
    
    System.out.println("User input log:");
    System.out.println();
      
    // Sets up the game
    
    PongGame cg = new PongGame();
    
    // Timer with a delay of 10
    
    cg.timer.start();
    
     while (true) {           
      if (cg.started) {
        
        cg.updateScreen();                    
      }
      delay(10);             
    }
  }
  
  // Delay method for timer
  
  public static void delay(int milli) {
    try {
      Thread.sleep(milli);
    }
    catch(Exception e){}
  }
}

// END OF PROGRAM
