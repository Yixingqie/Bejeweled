/**
 * BejeweledGUI.java (Skeleton)
 * Provide the GUI for the Bejeweled game
 */
import javax.swing.*;
import javax.swing.JComponent;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;


public class BejeweledGUI {
	// the name of the configuration file
   private final String CONFIGFILE = "config.txt";
   private final Color BACKGROUNDCOLOUR = new Color(255, 255, 200);
	
   private JLabel[][] slots;
   private JFrame mainFrame;
   private ImageIcon[] pieceIcon;
   private JButton endGameButton;
   private JTextField score;
   private JTextField numMoveLeft;
   
   private JTextField lives;///////////////////
   private JTextField goal;///////////////////////////
   public JButton rearrange;//////////////////////////////
   public JButton hint;/////////////////////////////////
   public JTextField highScore;//////////////////////////
   public JTextField name;///////////////////////////
   public String topName;
   public String topScore;
  
   public int numMoves;
   public int lifeLines;
   public int userLives;
   public int userGoal;
   public int userMoves;
   boolean complete = false;
	
   private String logoIcon;
   private String[] iconFile;
	
/**
* Number of different piece styles
*/
   public final int NUMPIECESTYLE = 7;

/**
* Number of rows on the game board
*/
   public final int NUMROW = 8;

/**
* Number of colums on the game board
*/
   public final int NUMCOL = 8;

/**
* Constants defining the demensions of the different components
* on the GUI
*/    
   private final int PIECESIZE = 70;
   private final int PLAYPANEWIDTH = NUMCOL * PIECESIZE;
   private final int PLAYPANEHEIGHT = NUMROW * PIECESIZE;

   private final int INFOPANEWIDTH = 2 * PIECESIZE;
   private final int INFOPANEHEIGHT = PLAYPANEHEIGHT;

   private final int LOGOHEIGHT = 2 * PIECESIZE;
   private final int LOGOWIDTH = PLAYPANEWIDTH + INFOPANEWIDTH;

   private final int FRAMEWIDTH = (int)(LOGOWIDTH * 1.03);
   private final int FRAMEHEIGHT = (int)((LOGOHEIGHT + PLAYPANEHEIGHT) * 1.1);

// Constructor:  BejeweledGUI
// - intialize variables from config files
// - initialize the imageIcon array
// - initialize the slots array
// - create the main frame
   public BejeweledGUI () {
      initConfig();
      initImageIcon();
      initSlots();
      createMainFrame();
   }

   private void initConfig() {
    	// TO DO: 
   	// initialize the following variables with information read from the config file 
   	// - logoIcon
   	// - iconFile 
      try{ //tries to read config file
         BufferedReader in = new BufferedReader (new FileReader(CONFIGFILE));
         logoIcon = in.readLine(); //reads first line for the logo
         iconFile = new String [NUMPIECESTYLE]; //sets up array
         
         for (int i = 0; i < NUMPIECESTYLE; i++){
            iconFile[i] = in.readLine();//implements the lines into the array
         }
         
         in.close();
      } 
      catch (IOException iox){ //if can't read, outputs error
         System.out.println("Error reading");
      } 
   }

// initImageIcon
// Initialize pieceIcon arrays with graphic files
   private void initImageIcon() {
      pieceIcon = new ImageIcon[NUMPIECESTYLE];
      for (int i = 0; i < NUMPIECESTYLE; i++) {
         pieceIcon[i] = new ImageIcon(iconFile[i]);
      }
   }

// initSlots
// initialize the array of JLabels
   private void initSlots() {
      slots = new JLabel[NUMROW][NUMCOL];
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            slots [i] [j] = new JLabel ();
         // slots[i][j].setFont(new Font("SansSerif", Font.BOLD, 18));
            slots[i][j].setPreferredSize(new Dimension(PIECESIZE, PIECESIZE));
            slots [i] [j].setHorizontalAlignment (SwingConstants.CENTER);      
         }
      }
   }

// createPlayPanel
   private JPanel createPlayPanel() {
      JPanel panel = new JPanel(); 
      panel.setPreferredSize(new Dimension(PLAYPANEWIDTH, PLAYPANEHEIGHT));
      panel.setBackground(BACKGROUNDCOLOUR);
      panel.setLayout(new GridLayout(NUMROW, NUMCOL));
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            panel.add(slots[i][j]);
         }
      }
      return panel;    
   }

// createInfoPanel
   private JPanel createInfoPanel() {
   
      JPanel panel = new JPanel();
      panel.setPreferredSize(new Dimension(INFOPANEWIDTH, INFOPANEHEIGHT));
      panel.setBackground (BACKGROUNDCOLOUR);
      panel.setBorder (new LineBorder (Color.white)); 
   
      Font headingFont = new Font ("Serif", Font.PLAIN, 24);
      Font regularFont = new Font ("Serif", Font.BOLD, 16);
      Font smallerFont = new Font ("Serif", Font.BOLD, 14);//////////////////
   
   // Create a panel for the scoreboard
      JPanel scorePanel = new JPanel();
      scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
      scorePanel.setBackground(BACKGROUNDCOLOUR);
   
     // Create the label to display "Score" heading
      JLabel scoreLabel = new JLabel ("     Score     ", JLabel.CENTER);
      scoreLabel.setFont(headingFont);
      scoreLabel.setAlignmentX (Component.CENTER_ALIGNMENT);
   //nextLabel.setForeground(Color.white);
   
      score = new JTextField();
      score.setFont(regularFont);
      score.setText("0");
      score.setEditable(false);
      score.setHorizontalAlignment (JTextField.CENTER);
      score.setBackground(BACKGROUNDCOLOUR);
      
      scorePanel.add(scoreLabel);
      scorePanel.add(score);
      
           
      ///////////////////////////////////////////////////////Extension Starts/////////////////////////////////////////////
      // Create a panel for the resetboard
      JPanel livesPanel = new JPanel();
      livesPanel.setLayout(new BoxLayout(livesPanel, BoxLayout.Y_AXIS));
      livesPanel.setBackground(BACKGROUNDCOLOUR);
   
     // Create the label to display "Life Line" heading
      JLabel livesLabel = new JLabel ("   Life Lines  ", JLabel.CENTER);
      livesLabel.setFont(headingFont);
      livesLabel.setAlignmentX (Component.CENTER_ALIGNMENT);
    //nextLabel.setForeground(Color.white);
   
      lives = new JTextField();
      lives.setFont(regularFont);
      lives.setText("0");
      lives.setEditable(false);
      lives.setHorizontalAlignment (JTextField.CENTER);
      lives.setBackground(BACKGROUNDCOLOUR);
      
      livesPanel.add(livesLabel);
      livesPanel.add(lives);
       
      // Create a panel for the goal
      JPanel goalPanel = new JPanel();
      goalPanel.setLayout(new BoxLayout(goalPanel, BoxLayout.Y_AXIS));
      goalPanel.setBackground(BACKGROUNDCOLOUR);
   
     // Create the label to display "Goal" heading
      JLabel goalLabel = new JLabel ("  MIN Goal  ", JLabel.CENTER);
      goalLabel.setFont(headingFont);
      goalLabel.setAlignmentX (Component.CENTER_ALIGNMENT);
    //nextLabel.setForeground(Color.white);
   
      goal = new JTextField();
      goal.setFont(regularFont);
      goal.setText("0");
      goal.setEditable(false);
      goal.setHorizontalAlignment (JTextField.CENTER);
      goal.setBackground(BACKGROUNDCOLOUR);
      
      goalPanel.add(goalLabel);
      goalPanel.add(goal);
      
      JLabel emptyLabel3 = new JLabel (" ", JLabel.CENTER);
      emptyLabel3.setFont(headingFont);
      emptyLabel3.setAlignmentX (Component.CENTER_ALIGNMENT);
      JLabel emptyLabel4 = new JLabel (" ", JLabel.CENTER);
      emptyLabel4.setFont(headingFont);
      emptyLabel4.setAlignmentX (Component.CENTER_ALIGNMENT);
      
       // Create a panel for the highscore
      JPanel hScorePanel = new JPanel();
      hScorePanel.setLayout(new BoxLayout(hScorePanel, BoxLayout.Y_AXIS));
      hScorePanel.setBackground(BACKGROUNDCOLOUR);
   
     // Create the label to display "highscore" heading
      JLabel hScoreLabel = new JLabel ("   High Score  ", JLabel.CENTER);
      hScoreLabel.setFont(smallerFont);
      hScoreLabel.setAlignmentX (Component.CENTER_ALIGNMENT);
    //nextLabel.setForeground(Color.white);
   
      highScore = new JTextField();
      highScore.setFont(smallerFont);
      highScore.setText("0");
      highScore.setEditable(false);
      highScore.setHorizontalAlignment (JTextField.CENTER);
      highScore.setBackground(BACKGROUNDCOLOUR);
      
      hScorePanel.add(emptyLabel3);
      hScorePanel.add(emptyLabel4);
      hScorePanel.add(hScoreLabel);
      hScorePanel.add(highScore);
      
       // Create a panel for the name
      JPanel namePanel = new JPanel();
      namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
      namePanel.setBackground(BACKGROUNDCOLOUR);
   
     // Create the label to display "name" heading
      JLabel nameLabel = new JLabel ("         by          ", JLabel.CENTER);
      nameLabel.setFont(smallerFont);
      nameLabel.setAlignmentX (Component.CENTER_ALIGNMENT);
    //nextLabel.setForeground(Color.white);
   
      name = new JTextField();
      name.setFont(smallerFont);
      name.setText("0");
      name.setEditable(false);
      name.setHorizontalAlignment (JTextField.CENTER);
      name.setBackground(BACKGROUNDCOLOUR);
      
      namePanel.add(nameLabel);
      namePanel.add(name);

   //////////////////////////////////////////////////////////////////Extensions end////////////////////////////////////////
   
      JPanel moveLeftPanel = new JPanel();
      moveLeftPanel.setLayout(new BoxLayout(moveLeftPanel, BoxLayout.Y_AXIS));
      moveLeftPanel.setBackground(BACKGROUNDCOLOUR);
   
   // Create the label to display "Moves Left" heading
      JLabel moveLeftLabel = new JLabel ("Moves Left", JLabel.CENTER);
      moveLeftLabel.setFont(headingFont);
      moveLeftLabel.setAlignmentX (Component.CENTER_ALIGNMENT);
   
      numMoveLeft = new JTextField();
      numMoveLeft.setFont(regularFont);
      numMoveLeft.setText("0");
      numMoveLeft.setEditable(false);
      numMoveLeft.setHorizontalAlignment (JTextField.CENTER);
      numMoveLeft.setBackground(BACKGROUNDCOLOUR);
      
      JLabel emptyLabel1 = new JLabel (" ", JLabel.CENTER);
      emptyLabel1.setFont(headingFont);
      emptyLabel1.setAlignmentX (Component.CENTER_ALIGNMENT); 
          
      JLabel emptyLabel2 = new JLabel (" ", JLabel.CENTER);
      emptyLabel2.setFont(headingFont);
      emptyLabel2.setAlignmentX (Component.CENTER_ALIGNMENT);
   
      moveLeftPanel.add(emptyLabel1);
      moveLeftPanel.add(moveLeftLabel);
      moveLeftPanel.add(numMoveLeft);
      moveLeftPanel.add(emptyLabel2);
   	
      endGameButton = new JButton("End Game");
      
      ///////////////////////////////////////////EXTENSIONS
      rearrange = new JButton("Rearrange");////////////////////////////////////
      hint = new JButton("Hint");////////////////////////////////////
   
      panel.add(scorePanel);
      panel.add(moveLeftPanel);
      panel.add(endGameButton);
      
      panel.add(rearrange);//////////////////////////
      panel.add(hint);//////////////////////////
      panel.add(livesPanel);/////////////////////////////////
      panel.add(goalPanel);///////////////
      panel.add(hScorePanel);///////////////////////
      panel.add(namePanel);///////////////
   	    
      return panel;
   }

// createMainFrame
   private void createMainFrame() {
   
   // Create the main Frame
      mainFrame = new JFrame ("Bejeweled");
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      JPanel panel = (JPanel)mainFrame.getContentPane();
      panel.setLayout (new BoxLayout(panel,BoxLayout.Y_AXIS));
   
   // Create the panel for the logo
      JPanel logoPane = new JPanel();
      logoPane.setPreferredSize(new Dimension (LOGOWIDTH, LOGOHEIGHT));
      logoPane.setBackground (BACKGROUNDCOLOUR);
      
      JLabel logo = new JLabel();
      logo.setIcon(new ImageIcon(logoIcon));
      logoPane.add(logo);
   
   // Create the bottom Panel which contains the play panel and info Panel
      JPanel bottomPane = new JPanel();
      bottomPane.setLayout(new BoxLayout(bottomPane,BoxLayout.X_AXIS));
      bottomPane.setPreferredSize(new Dimension(PLAYPANEWIDTH + INFOPANEWIDTH, PLAYPANEHEIGHT));
      bottomPane.add(createPlayPanel());
      bottomPane.add(createInfoPanel());
   
   // Add the logo and bottom panel to the main frame
      panel.add(logoPane);
      panel.add(bottomPane);
   
      mainFrame.setContentPane(panel);
   //   mainFrame.setPreferredSize(new Dimension(FRAMEWIDTH, FRAMEHEIGHT));
      mainFrame.setSize(FRAMEWIDTH, FRAMEHEIGHT);
      mainFrame.setVisible(true);
   }

/**
* Returns the column number of where the given JLabel is on
* 
* @param  label the label whose column number to be requested
* @return the column number
*/
   public int getRow(JLabel label) {
      int result = -1;
      for (int i = 0; i < NUMROW && result == -1; i++) {
         for (int j = 0; j < NUMCOL && result == -1; j++) {
            if (slots[i][j] == label) {
               result = i;
            }
         }
      }
      return result;
   }

/**
* Returns the column number of where the given JLabel is on
* 
* @param  label the label whose column number to be requested
* @return the column number
*/
   public int getColumn(JLabel label) {
      int result = -1;
      for (int i = 0; i < NUMROW && result == -1; i++) {
         for (int j = 0; j < NUMCOL && result == -1; j++) {
            if (slots[i][j] == label) {
               result = j;
            }
         }
      }
      return result;
   }

   public void addListener (BejeweledListener listener) {
   	// add listener for each slot on the game board
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            slots [i] [j].addMouseListener (listener);
         }
      }
      
   	// add listener for the button
      endGameButton.addMouseListener(listener);////////////////////////////////adds listener to endGame button
      endGameButton.setName("endGame"); ///gives the end game button a name
      
      rearrange.addMouseListener(listener);//////////////////////////
      rearrange.setName("rearrange"); //gives the rearrange button a name
      
      hint.addMouseListener(listener);//////////////////////////
      hint.setName("hint"); //gives the hint button a name
   }

/**
* Display the specified player icon on the specified slot
* 
* @param row row of the slot
* @param col column of the slot
* @param piece index of the piece to be displayed
*/

   public void setPiece(int row, int col, int piece) {
      slots[row][col].setIcon(pieceIcon[piece]);
   }

/**
* Highlight the specified slot with the specified colour
* 
* @param row row of the slot
* @param col column of the slot
* @param colour colour used to highlight the slot
*/
   public void highlightSlot(int row, int col, Color colour) {
      slots[row][col].setBorder (new LineBorder (colour));   
   }

/**
* Unhighlight the specified slot to the default grid colour
* 
* @param row row of the slot
* @param col column of the slot
*/
   public void unhighlightSlot(int row, int col) {
      slots[row][col].setBorder (new LineBorder (BACKGROUNDCOLOUR));   
   }

/**
* Display the score on the corresponding textfield
* 
* @param point the score to be displayed
*/
   public void setScore(int point) {
      score.setText(point+"");
   }
   
   
   ////////////////////////////////////////////////////////EXTENSION STARTS///////////////////////////////////////////
   
   public void setHighScore(int point) {  ///method to set number of lifelines
      highScore.setText(point+"");
   }
   
   public void setName(String nameFound) {  ///method to set number of lifelines
      name.setText(nameFound);
   }
   
   public void setLifeLines(int point) {  ///method to set number of lifelines
      lives.setText(point+"");
   }

   public void setGoal(int point) { //method to set the goal
      goal.setText(point+"");
      userGoal = point;
   }
  
   public void goalCheck(int score, int moves){ //checks if the goal is met
      ImageIcon icon = new ImageIcon("smile.png");
      ImageIcon icon2 = new ImageIcon("madface.png");
   
      double val = (score /userGoal) * 100; 
      if (val >= 100 && !complete){
         JOptionPane.showMessageDialog(null, " You Just Beat The Goal", "Completed The Goal", JOptionPane.PLAIN_MESSAGE,icon);
         complete = true;
      }else if (val < 100 && moves == 0){
         JOptionPane.showMessageDialog(null, " You Did Not Complete The Goal", "Failed To Complete The Goal", JOptionPane.PLAIN_MESSAGE, icon2);
      }
   }
	
   /////////////////////////////////////EXTENSION ENDS///////////////////////////////////////////////////////
   
   
/**
* Display the number of moves left on the corresponding textfield
* 
* @param num number of moves left to be displayed
*/
   public void setMoveLeft(int num) {
      numMoveLeft.setText(num+"");
   }	
  
/**
* Reset the game board (clear all the pieces on the game board)
* 
*/
   public void resetGameBoard() {
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            slots[i][j].setIcon(null);
         }
      }
   }
   
///////////////////////////EXTENSIONS STARTS/////////////////////////////////////////////////////////
   
   //when user runs out of life lines, then goes to this method
   public void showOutOfLifeLines(){///////////////////////displays when they run out of life lines
      JOptionPane.showMessageDialog(null, " You ran out of Life Lines", "Out of Life Lines", JOptionPane.PLAIN_MESSAGE, null); 
   }
   
   //if hints arent available, goes to this method
   public void showNoMoves(){///////////////////////outputs when hint is unavailable
      JOptionPane.showMessageDialog(null, " No combinations", "No Combinations", JOptionPane.PLAIN_MESSAGE, null); 
   }
   
   //if chosen custom, then shows prompts to allow user inputs
   public void custom(){  
      String moves = JOptionPane.showInputDialog(null, "Enter Number of Moves:", "Number of Moves",
         JOptionPane.WARNING_MESSAGE);
      String lives = JOptionPane.showInputDialog(null, "Enter Number of Life Lines", "Number of Life Lines",
         JOptionPane.WARNING_MESSAGE);
      String goal = JOptionPane.showInputDialog(null, "Enter Your Goal", "Goal",
         JOptionPane.WARNING_MESSAGE);
        
        //makes sure that the inputs are not empty
      if(!(moves.isEmpty() && lives.isEmpty() && goal.isEmpty())){
         int tempmove = Integer.parseInt(moves);
         int templives = Integer.parseInt(lives);
         int tempgoal = Integer.parseInt(goal);
         String[] buttons = { "Confirm", "Go Back to Levels"};
      
         int check = JOptionPane.showOptionDialog(null, "Confirm Selection: \nUser Moves: "+tempmove+ "\nUser Life Lines: "+
             templives +"\nGoal: "+ tempgoal,"Levels",
             JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[1]);
      
      //checks which button they press
         switch(check){
            case -1:
            case 1:
               levels();
               break;
            case 0:
               userMoves = tempmove;
               userLives = templives;
               userGoal = tempgoal;
               numMoves = userMoves;
               lifeLines = userLives;
               setGoal(userGoal);
               break;
         }
      }else {
         custom();
      }
   }
   
   //method to display the levels available
   public void levels(){ 
      String[] buttons = { "Standard", "Easy", "Medium", "Hard", "Custom" };
   
      int selection = JOptionPane.showOptionDialog(null, "Choose a Level", "Levels",
         JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[4]);
         //checks which level they press
      switch(selection){
         case -1:
            levels();
            break;
         case 0:
            numMoves = 10;
            lifeLines = 3;
            setGoal(80);
            break;
         case 1:
            numMoves = 20;
            lifeLines = 3;
            setGoal(100);
            break;
         case 2:
            numMoves = 20;
            lifeLines = 2;
            setGoal(150);
            break;
         case 3:
            numMoves = 20;
            lifeLines = 1;
            setGoal(180);
            break;
         case 4:
            custom();
            break;
      }
   }
   
   //method to show popup to ask for their intials
   public String highScoreAsking(int score){
       String highName = JOptionPane.showInputDialog(null, "You Beat the HIGH SCORE!/n"+"Score  of : "+score+"\nEnter your Initals", "Number of Moves",
                JOptionPane.WARNING_MESSAGE);
       if(highName.isEmpty()){
         highScoreAsking(score);
       }
        return highName;

   
   }
   
   //updates high score
//    public void highScoreUpdate(){
//       try{
//       String file = "topScore.txt";
//          BufferedReader in  = new BufferedReader(new FileReader(file));
//          String input = in.readLine();
//          int scoreFound = Integer.parseInt(input);
//          String foundName = in.readLine();
//          setHighScore(scoreFound);
//          setName(foundName);        
//          in.close();
//                
//       }catch(IOException iox){
//       
//       }
// 
//    
//    }
   // //reads the text file
//    public void highScoreCheck(int score){////////Keeps track of top score
//       try{
//       String file = "topScore.txt";
//          BufferedWriter out = new BufferedWriter(new FileWriter(file));
//          BufferedReader in  = new BufferedReader(new FileReader(file));
//          String input = in.readLine();
//          int scoreFound = Integer.parseInt(input);
//             String theirName;
//             if (score > scoreFound){
//                theirName =highScoreAsking(score); 
//                setName(theirName);     
//             }else {
//                JOptionPane.showMessageDialog(null, " You Did Not Beat The High Score", "Failed To Beat high Score", JOptionPane.PLAIN_MESSAGE);
//                
//             }        
//          in.close();
//          out.close();
//                
//       }catch(IOException iox){
//       
//       }
//    }
///////////////////////////////////////////////////////////////EXTENSION ENDS///////////////////////////////////

/**
* Display a pop up window displaying the message about invalid move
* 
*/
   public void showInvalidMoveMessage(){
      JOptionPane.showMessageDialog(null, " This move is invalid", "Invalid Move", JOptionPane.PLAIN_MESSAGE, null); 
   }

/**
* Display a pop up window specifying the size of the chain(s) that is (are) formed after the swap
* 
* @param chainSize the size of the chain(s) that is (are) formed
*/
   public void showChainSizeMessage(int chainSize){
      JOptionPane.showMessageDialog(null, "Chain(s) with total size of " + chainSize + " is (are) formed.", "Chain Formed!", JOptionPane.PLAIN_MESSAGE, null); 
   }
 
/**
* Display a pop up window specifying the game is over with the score and number of moves used
* 
* @param point the score earned in the game
* @param numMove the number of moves used in the game
*/
   public void showGameOverMessage(int point, int numMove){
      JOptionPane.showMessageDialog(null, "Game Over!\nYour score is " + point + " in " + numMove + " moves.", "Game Over!", JOptionPane.PLAIN_MESSAGE, null); 
      System.exit (0);
   }

   public static void main (String[] args) {
      BejeweledGUI gui = new BejeweledGUI ();
      Bejeweled game = new Bejeweled (gui);
      BejeweledListener listener = new BejeweledListener (game, gui);
   }

}