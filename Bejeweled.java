/**
* Bejeweled.java (Skeleton)
BY: YIXING QIE
TITLE: DONUT SWAP
*
* This class represents a Bejeweled (TM)
* game, which allows player to make moves
* by swapping two pieces. Chains formed after
* valid moves disappears and the pieces on top
* fall to fill in the gap, and new random pieces
* fill in the empty slots.  Game ends after a
* certain number of moves or player chooses to 
* end the game.
*/

import java.awt.Color;

public class Bejeweled {

/* 
 * Constants
 */  
   final Color COLOUR_DELETE = Color.RED;
   final Color COLOUR_SELECT = Color.BLACK;
   final Color COLOUR_HINT = Color.BLUE;/////////////////////

   final int CHAIN_REQ = 3;	// minimum size required to form a chain
   final int NUMMOVE = 10;		// number of moves to be play in one game
   final int EMPTY = -1; 		// represents a slot on the game board where the piece has disappear  

   final int NUMPIECESTYLE;   // number of different piece style
   final int NUMROW;		  		// number of rows in the game board
   final int NUMCOL;	 	  		// number of columns in the game board
   
   
   final int MOVES;/////////////////////
   final int BONUS;
/* 
 * Global variables
 */   
   BejeweledGUI gui;	// the object referring to the GUI, use it when calling methods to update the GUI

   int board[][];		// the 2D array representing the current content of the game board

   boolean firstSelection;		// indicate if the current selection is the selection of the first piece
   int slot1Row, slot1Col;		// store the location of the first selection
      
   int lifeLines;
   
   int score;						// current score of the game
   int numMoveLeft;				// number of move left for the game

/**************************
 * Constructor: Bejeweled
 **************************/
   public Bejeweled(BejeweledGUI gui) {
      this.gui = gui;
      gui.levels(); //calls the levels method--to choose a level
     // gui.highScoreUpdate();
      NUMPIECESTYLE = gui.NUMPIECESTYLE;
      NUMROW = gui.NUMROW;
      NUMCOL = gui.NUMCOL;
      MOVES = gui.numMoves;//imports the number of moves selected
      BONUS = gui.lifeLines; //imports the number of life lines
      firstSelection = true; //sets first selection to true
      
      lifeLines = BONUS;
      score = 0; //initalizes score to 0
      numMoveLeft = MOVES; //intializes the number of moves to the one selected
      initBoard(); //calls the method it initialize the board
      gui.setScore(score); //displays current score
      gui.setLifeLines(lifeLines);  //display current life Lines
      gui.setMoveLeft(numMoveLeft); //displays current number of moves left
      scan();/////////SCAN BEGINNING //automatically scans if there are any chains already formed
   }			
/*****************************************************
 * play
 * This method is called when a piece is clicked.  
 * Parameter "row" and "column" is the location of the 
 * piece that is clicked by the player
 *****************************************************/
   public void play (int row, int column) {
      gui.highlightSlot(row, column,COLOUR_SELECT);
      if (firstSelection){
         slot1Row = row; //Puts row in global Variable
         slot1Col = column; //Puts column in global Variable
         firstSelection = !firstSelection; //makes first selection false
      }else {
         firstSelection = !firstSelection; //makes first selection true
         if (adjacentPieces(slot1Row, slot1Col, row, column)){ //call the adjacentPieces method -- if adjacent, its true
            swap(slot1Row, slot1Col, row, column); //swaps the 2 pieces
            if (verifyChain(slot1Row, slot1Col) >= CHAIN_REQ || verifyChain(row, column) >= CHAIN_REQ){ //if chain is formed, continues to if structure
            //intializes the variables
               int up1 = checkUp(slot1Row,slot1Col), down1 = checkDown(slot1Row,slot1Col); 
               int up2 = checkUp(row,column), down2 = checkDown(row,column);
               int left1 = checkLeft(slot1Row,slot1Col), right1 = checkRight(slot1Row,slot1Col);
               int left2 = checkLeft(row,column), right2 = checkRight(row,column);
               int chainSize = 0;         
            /////////SWAPPING PIECES
               gui.setPiece(slot1Row, slot1Col, board[slot1Row][slot1Col]);
               gui.setPiece(row, column, board[row][column]);   					
            ///////Sets the pieces that create a chain to -1
               verticalDelete(slot1Row, slot1Col, up1, down1); //vertically
               horizontalDelete(slot1Row, slot1Col, left1, right1); //horizontally
               verticalDelete(row, column, up2, down2); //vertically
               horizontalDelete(row, column, left2, right2);   //Horizontally
            /////KEEP SCORE AND HIGHLIGHT DELETES           
               for(int i = 0; i< NUMROW; i++){ //runs through the rows 
                  for(int j = 0; j < NUMCOL; j++){ //runs through the columns
                     if(board[i][j] == EMPTY){ //find the empty slots
                        chainSize++; //adds up the number of slots found 
                        gui.highlightSlot(i, j,COLOUR_DELETE); //highlights the slots to be deleted
                     }
                  }
               }
               gui.showChainSizeMessage(chainSize); //Shows the chain Size message
               score += chainSize;             //adds the chainSize to the total Score
               gui.setScore(score);            //Displays the score                 
               shiftDown();							//goes to the shift down method
               newIcon();								//Sets the -1 to new random icons
               guiUpdate();							//updates the entire board again
               numMoveLeft--;   						//Subtracts the number of moves left
               scan();  ///////SCAN AFTERS
               gui.goalCheck(score, numMoveLeft);
            }else{
               gui.showInvalidMoveMessage();  //If doesn't create chain, show error
               swap(slot1Row, slot1Col, row, column); //switches the pieces back
            }                 
         }else {
            gui.showInvalidMoveMessage();  //If not adjacent, shows the error
         }     
         gui.unhighlightSlot(slot1Row, slot1Col); //unselects first selection
         gui.unhighlightSlot(row, column); //unselects second selection
         gui.setMoveLeft(numMoveLeft);      //updates number of moves left
      }   
      if(numMoveLeft == 0){       ////EXITS WHEN 0
         endGame();  //goes to end Game method
         gui.resetGameBoard();	//Resets the board
      }                     
   }

   public void initBoard(){ //Method to intialize the board
      int piece;  //varaible of piece
      board = new int [NUMROW][NUMCOL];  //initaliziing the board array
      for(int i = 0; i < NUMROW; i++){ //runs through the rows
         for (int j = 0; j < NUMCOL; j++){  //runs through the columns
            piece = (int)(Math.random()* NUMPIECESTYLE); //randomly sets each piece of the array
            sleep();
            board[i][j] = piece; //stores board value in piece
            gui.setPiece(i, j, piece);	 //visually sets the pieces on the board
         }
      }    
   }

   public boolean adjacentPieces(int x1, int y1, int x2, int y2){ //Methosd to check if pieces are adjacent
      boolean verify = false; //verify if the chosen pieces are adjacent
   //checks the x and y components of the pieces
      if ((Math.abs(x1-x2)==1 && y1 == y2) || (Math.abs(y1-y2)==1 && x1 == x2)){
         verify = true; //if adjacent, sets true
      } 
      return verify; //returns the boolean
   }   

   public void swap(int x1, int y1, int x2, int y2){ //Method to swap the pieces logically
      int temp = board[x1][y1]; //temporarily stores the first piece value
      board[x1][y1] = board [x2][y2]; //asssigns first piece to second
      board[x2][y2] = temp;       //assigns second piece to temporary
   }

   public int verifyChain(int x, int y){ //Method to check is a chain is formed after a swap
   //Intializing varaibles    
      int count = 0;
      int horizontal = checkLeft(x,y) + checkRight(x,y) +1;
      int vertical = checkUp(x,y) + checkDown(x,y)+1;
      if (horizontal >= CHAIN_REQ){ // if horizontal chain is formed
         count += horizontal; //adds the number of pieces to total count
      }
      if (vertical >= CHAIN_REQ){ //if vertical chain is formed
         count += vertical; //adds the number of pieces to total coun
      }
      return count;
   }

   public int checkLeft (int x, int y){ //Method to check number of same piece icons on the left
   //Initalizing the variables
      int icon = board[x][y];
      int col;
      col = y;
      int count = EMPTY;
   //while loop to keep track of number of same pieces
      while (col >= 0 && board[x][col] == icon){ 
         count++;    //keeps track of number of icons
         col--;  //continues to move to the left
      }   
      return count; 
   }

   public int checkRight (int x, int y){ //Method to check number of same piece icons on the right
   //Initalizing the variables      
      int icon = board[x][y];
      int col;
      col = y;
      int count = EMPTY;
   //while loop to keep track of number of same pieces
      while (col < NUMCOL && board[x][col] == icon){
         count++;
         col++;
      }   
      return count;
   }

   public int checkUp (int x, int y){ //Method to check number of same piece icons above
   //Initalizing the variables      
      int icon = board[x][y];
      int row;
      row = x;
      int count = EMPTY;
   //while loop to keep track of number of same pieces
      while (row >= 0 && board[row][y] == icon){
         count++;  
         row--;
      } 
      return count;
   }

   public int checkDown (int x, int y){  //Method to check number of same piece icons below
   //Initalizing the variables
      int icon = board[x][y];
      int row;
      row = x;
      int count = EMPTY;
   //while loop to keep track of number of same pieces
      while (row < NUMROW && board[row][y] == icon){
         count++;   
         row++;
      }     
      return count;  
   }

   public void verticalDelete(int x, int y, int up, int down){ //Vertical deleting the icons with same type
      if(up + down + 1 >= CHAIN_REQ){ //checks if a chain is formed
         board[x][y]= EMPTY;  //sets the first icon to -1
         for (int i = 1; i <= up; i++){ //runs up and deletes the same icon 
            board[x-i][y] = EMPTY; //sets logic array position to -1
         }
         for (int i = 1; i <= down; i++){ //runs dowm and deletes same icon
            board[x+i][y] = EMPTY; //sets logic array to -1
         }
      }
   }

   public void horizontalDelete(int x, int y, int left, int right){ //Deleting horizontally of the same icons
      if(left + right + 1 >= CHAIN_REQ){//Checks if the chain is formed horizontally
         board[x][y]= EMPTY; //sets the first icon to -1
         for (int i = 1; i <= left; i++){ //runs left and deletes number of icons 
            board[x][y-i] = EMPTY; //sets them to -1
         }
         for (int i = 1; i <= right; i++){   //runs right and deletes number of icons
            board[x][y+i] = EMPTY; //sets bthem to -1
         }
      }
   }

   public void shiftDown(){ //Shifting down method
   //Variable Declarations
      boolean found = false;
      int count = 0;
      int row = 0;
      for (int i = 0; i<NUMCOL; i++){ //running through columns      
         int j = NUMROW -1;  
         found = false;  //resets the found
         count = 0; //resets the count                                                                                                                                 ///CANT READ IT AND AFFECTS SHIFT DOWN
         while(j >= 0){ //running through rows
            if(board[j][i] == EMPTY){ //if array is -1
               count++; //adds 1
               if(!found){ //if not found 
                  row = j; //keeps track of the first placement
                  found = true; //changes found to true;
               }     
            }
            if(found && board[j][i] != EMPTY){ // if -1 is found in the column
               for(int k = row; k >= count; k--){ //starts at the found row and counts down
                  board[k][i] = board[k-count][i];
               }
               for (int k = 0; k < count; k++){ //starts at top and fills in the -1 after shifting down
                  board[k][i] = EMPTY;
               }
               found = false;  //resets the found
               count = 0; //resets the count
               j = NUMROW;
            }    
            j--; 
         }    
      } 
   }

   public void newIcon(){ //after shifting, replaces the -1 with new icons
      for (int i = 0; i<NUMROW; i++){
         for(int j= 0; j < NUMCOL; j++){
            if(board[i][j] == EMPTY){ //finds the -1
               board[i][j] = (int)(Math.random()*NUMPIECESTYLE);  //replaces with random icon
            }
         }
      }   
   }

   public void guiUpdate(){ //Updating the board after method
      for (int i = 0; i<NUMROW; i++){ 
         for(int j= 0; j < NUMCOL; j++){
            gui.setPiece(i, j, board[i][j]); //reassigns the pieces
            gui.unhighlightSlot(i, j); //unhighlights all the slots
         }
      }
   }
/*****************************************************
 * endGame
 * This method is called when the player clicks on the
 * "End Game" button
 *****************************************************/
   public void endGame() { //end game method
      int moves; 
      moves = MOVES - numMoveLeft; //calculate moves spent
      gui.showGameOverMessage(score, moves); //outputs the score and moves
      //gui.highScoreCheck(score);
   }
   
   
   
   //////////////////////*********************************************************EXTENSIONS START
   public void sleep(){ ///////Sleep Method////Causes the animations in the very beginning
      try{ //tries to sleep
         Thread.sleep(15); //sleep duration of 15 milliseconds
      }catch(InterruptedException ex){ //if does work, catch
         Thread.currentThread().interrupt(); //gives interrupt
      }  
   }
   
   public boolean autoColumn(){ ///////COLUMNS ////AUTO DELETION /////WORKS READING SIDEWAYS
   //initalizing the variables
      int col = 0;
      int count = 0;
      int prevIcon;
      boolean found = false;
   //runs through the rows
      for (int i = 0; i < NUMROW; i++){
         prevIcon = board[i][0]; //keeps track of first icon type
         col = 0; //intialize col to 0
         for (int j = 1; j < NUMCOL; j++){ //runs through the columns
            if(board[i][j] == prevIcon){ //if current icon is same as previous...
               count++;     //...add to count
            }
            if(board[i][j] != prevIcon || j == NUMCOL -1 ){ //if current isn't same as previous or had reached the end of columns..
               if(count >= 2){ //if a chain if formed more than 2
                  found = true; //found is true
                  for(int k = 0; k <= count; k++){ //runs through the count
                     board[i][col + k] += 10; //makes all of the chain to itself plus 10
                  }         
               }
               col = j; //sets the new position indicator to current current
               count = 0; //resets count
               prevIcon = board[i][j]; //sets the previous icon to current
            }                       
         }
      }
      return found; //returns true or false
   }
   public boolean autoRow(){     //  //ROWS    /////AUTODELETION WORKS ////READ UP AND DOWN
   //initalizes the variables
      int prevIcon1;
      boolean found = false;
      int count1 = 0;     
      int rw = 0;
      int temp2 = -3;
      for (int i = 0; i < NUMCOL; i++){ //run left and right
         if(board[0][i] >= 10){
            prevIcon1 =  board[0][i]-10; //if the piece has already been found, then finds its absolute value  by subtracting  10
         }
         else{
            prevIcon1 = board[0][i]; //otherwise, the piece is assigns
         }
         rw=0; //intializing first found
         for (int j = 1; j < NUMROW; j++){  //running up and down     
            if(board[j][i] >= 10){ //if the piece has already been found, then finds its absolute value  by subtracting  10
               temp2 = (board[j][i])-10;
            }
            else{
               temp2 = board[j][i];  //otherwise, the piece is assigns
            }
         
            if(temp2 == prevIcon1){ //if next chain is formed then add one
               count1++;              
            } 
            if (temp2 != prevIcon1 || j == NUMROW -1){ // automatic deleting
               if(count1 >= 2){ //checks if proper chain is formed
                  found = true;
                  for(int k = 0; k <= count1; k++){ //sets the piece to itself plus 10
                     if(board[rw+k][i] < 10){
                        board[rw+k][i] += 10;
                     }
                  }  
               }
               count1 = 0;
               rw = j;
               prevIcon1 = temp2;
            }
         }
      } 
      return found;
   }

   public boolean run(){ //run method
      boolean verify =false;
      autoColumn(); // calls autoColumn method
      autoRow(); //calls the autoRow method
      if (autoColumn() || autoRow()){ //checks if theyre true
         autoColumn(); //rechecks them
         autoRow(); 
         verify = true;
      }
      return verify;
   }

   public void scan(){ //////Automatic deletion    
      while(run()){      //checks for chains in the columns and rows//if true continue
         int countSc = 0;  //initalizes count
         for(int i = 0; i < NUMROW; i++){ //runs through rows
            for(int j = 0; j < NUMCOL; j++){ //runs through columns
               if (board[i][j] >= 10){ //if the icon is greater than 10
                  countSc++; //add to score
                  board[i][j] = EMPTY; //converts to -1
                  gui.highlightSlot(i, j,COLOUR_DELETE); //highlights the piece
               }
            }
         } 
         score+=countSc; //updates score logically
         gui.showChainSizeMessage(countSc); //shows chain size message
         gui.setScore(score);  // updates score visually                
         shiftDown();  //goes to shift down method
         newIcon();  //replaces icons at the top
         guiUpdate();  //visually updates the board
      }        
   }
   
   public void rearrange(){ //Rearranges the board
      if(lifeLines!= 0){
         lifeLines--;
         gui.setLifeLines(lifeLines);
         int piece;  //varaible of piece
         board = new int [NUMROW][NUMCOL];  //initaliziing the board array
         for(int i = 0; i < NUMROW; i++){ //runs through the rows
            for (int j = 0; j < NUMCOL; j++){  //runs through the columns
               piece = (int)(Math.random()* NUMPIECESTYLE); //randomly sets each piece of the array
               board[i][j] = piece; //stores board value in piece
               gui.setPiece(i, j, piece);	 //visually sets the pieces on the board
            }
         }    
         scan();
      }else {
         gui.showOutOfLifeLines();  
      }     
   }
   
   public void hint(){ //gives hints
      boolean found = false;
      int x, y;
      int x1, y1;
      if(lifeLines != 0){ //checks if they have enough life lines
         lifeLines--;
         gui.setLifeLines(lifeLines);
         for (int i = 0; i< NUMROW && !found; i++){ //runs thru the rows 
            for(int j = 0; j < NUMCOL && !found; j++){ //runs thru columns
               if(j != NUMCOL -1 && i != NUMROW-1){
               //initialize variables
                  x = i;
                  y = j;
                  x1 = i; 
                  y1 = j+1;
                  swap(x, y, x1, y1); //swap
                  //checks if left right chain is available and highlights
                  if (verifyChain(x, y) >= CHAIN_REQ || verifyChain(x1, y1)>= CHAIN_REQ){
                     found = true;
                     gui.highlightSlot(x, y, COLOUR_HINT);
                     gui.highlightSlot(x1, y1, COLOUR_HINT);
                  }
                  swap(x, y, x1, y1);  //swap back   
                  int x2 = i +1; 
                  int y2 = j;
                  swap(x, y, x2, y2);
                  //checks if left right chain is available and highlights
                  if (!found && (verifyChain(x, y) >= CHAIN_REQ || verifyChain(x2, y2)>= CHAIN_REQ)){
                     found = true;
                     gui.highlightSlot(x, y, COLOUR_HINT);
                     gui.highlightSlot(x2, y2, COLOUR_HINT);
                  }
                  swap(x, y, x2, y2); //swap
               }
            }
         }
         if(!found){//checks if no hints are available because of no combinations
            gui.showNoMoves();
            lifeLines++;
         }
      }else {
         gui.showOutOfLifeLines(); //shows out of lifeLines if used all
      }
   }
/////////////////////////////////////////////*****************************************EXTENSIONS END
}
