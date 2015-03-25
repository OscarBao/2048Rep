/*
Name: Oscar Bao
Username: cs8bajv
File Name: Gui2048.java
Date: 3/5/2015
*/

/** Gui2048.java */
/** PA8 Release */

import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;


public class Gui2048 extends Application
{
    private String outputBoard; // The filename for where to save the Board
    private Board board; // The 2048 Game Board

    // Fill colors for each of the Tile values
    private static final Color COLOR_EMPTY = Color.rgb(238, 228, 218, 0.35);
    private static final Color COLOR_2 = Color.rgb(238, 228, 218);
    private static final Color COLOR_4 = Color.rgb(237, 224, 200);
    private static final Color COLOR_8 = Color.rgb(242, 177, 121);
    private static final Color COLOR_16 = Color.rgb(245, 149, 99);
    private static final Color COLOR_32 = Color.rgb(246, 124, 95);
    private static final Color COLOR_64 = Color.rgb(246, 94, 59);
    private static final Color COLOR_128 = Color.rgb(237, 207, 114);
    private static final Color COLOR_256 = Color.rgb(237, 204, 97);
    private static final Color COLOR_512 = Color.rgb(237, 200, 80);
    private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
    private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
    private static final Color COLOR_OTHER = Color.BLACK;
    private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);

    private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242); // For tiles >= 8
    private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101); // For tiles < 8

    /* Add your own Instance Variables here */
    //Everything will be on this pane. Even gridPane
    StackPane scenePane = new StackPane();

    //scenePane's background color
    private static final Color BGCOLOR = Color.rgb(150, 150, 150);


    //Title variables
    private Label gameTitle = new Label();
    private static final int TITLEFONTSIZE = 50;
    private static final Font TITLEFONT = Font.font("Times New Roman", 
                                              FontWeight.BOLD,
                                              FontPosture.ITALIC,
                                              TITLEFONTSIZE);
    private static final int SCOREFONTSIZE = 30;
    private static final Font SCOREFONT = Font.font("Trebuchet",
                                                FontPosture.ITALIC,
                                                SCOREFONTSIZE);
    private static final int TITLEMARGIN = 20;

    //Grid variables
    private static final int GRIDMARGIN = 4;
    private static final int GRIDWIDTH = 600;
    private static final int GRIDHEIGHT = 600;
    private static int screenWidth = GRIDWIDTH;
    private static int screenHeight = GRIDHEIGHT;
    private int gridSize;

    GridPane gridPane = new GridPane();
    Tile[][] gameTiles;
    
    //Tiles variables
    //Tiles color sceme
    private static  Color[] colorScheme = {Color.rgb(255, 255, 255),
                        Color.rgb(225, 225, 255), Color.rgb(195, 195, 255), 
                        Color.rgb(165, 165, 255), Color.rgb(135, 135, 255),
                        Color.rgb(105, 105, 255), Color.rgb(75, 75, 255),
                        Color.rgb(45, 45, 255), Color.rgb(255, 45, 215),
                        Color.rgb(255, 45, 175), Color.rgb(255, 45, 135),
                        Color.rgb(255, 45, 95), Color.rgb(255, 45, 55),
                        Color.rgb(205, 45, 55), Color.rgb(165, 45, 55)};
    private static int TILEFONTSIZE = 25;
    private static final Font TILEFONT = Font.font("Helvetica", 
                                              TILEFONTSIZE);
                                                
    private Label gameScore = new Label();

    @Override
    public void start(Stage primaryStage)
    {
        // Process Arguments and Initialize the Game Board
        processArgs(getParameters().getRaw().toArray(new String[0]));
    
        /** Add your Code for the GUI Here */
        primaryStage.setTitle("Gui2048");

        //add the label for game title (2048)
        gameTitle = new Label("2048");
        //set the font of game title
        gameTitle.setFont(TITLEFONT);
        //add it to grid and set it to span 2 grid spaces
        gridPane.setRowIndex(gameTitle, 0);
        gridPane.setColumnIndex(gameTitle, 0);
        gridPane.setColumnSpan(gameTitle, board.GRID_SIZE/2);
        gridPane.setHalignment(gameTitle, HPos.CENTER);
        gridPane.setMargin(gameTitle, new Insets(TITLEMARGIN));
        gridPane.getChildren().add(gameTitle);

        screenHeight += TITLEFONTSIZE;
        screenHeight += TITLEMARGIN*2;

        //add the label for the game score (2048)
        gameScore = new Label("SCORE: " + board.getScore());
        //default font of gameScore to TITLEFONT, may change
        //later on if decide to
        gameScore.setFont(SCOREFONT);
        //add it to grid and set it to span 2 grid spaces
        gridPane.setRowIndex(gameScore, 0);
        gridPane.setColumnIndex(gameScore, board.GRID_SIZE - (board.GRID_SIZE/2));
        gridPane.setColumnSpan(gameScore, board.GRID_SIZE/2);
        gridPane.setHalignment(gameScore, HPos.CENTER);
        gridPane.setMargin(gameScore, new Insets(TITLEMARGIN));
        gridPane.getChildren().add(gameScore);

        //===---------------------2048 tiles--------------------===//        
        //update the size of the grid
        gridSize = board.GRID_SIZE;
        //update size of screen
        screenWidth += GRIDMARGIN*gridSize*2;
        screenHeight += GRIDMARGIN*gridSize*2;
        //initialize list of tiles
        gameTiles = new Tile[gridSize][gridSize];

        for(int y = 0; y < gameTiles.length; y++ ) {
          for(int x = 0; x < gameTiles.length; x++ ) {
            //set up the placement of the tile in the grid
            gameTiles[y][x] = new Tile(x, y + 1, 
                                  GRIDWIDTH/gridSize, GRIDHEIGHT/gridSize,
                                  "0", Color.rgb(255, 255, 255));
            //add a margin setting to the tile
            gameTiles[y][x].setMargin(GRIDMARGIN);
            gameTiles[y][x].setFont(TILEFONT);
          }
        }

        //update tiles for initial tiles
        updateTiles();

        //adds a rectangle for the background of the scene,
        Rectangle background = new Rectangle(0, 0, screenWidth, screenHeight);
        background.setFill(BGCOLOR);
        scenePane.getChildren().add(background);
        //then adds the whole grid to the scene
        scenePane.getChildren().add(gridPane);

        //===---------------------------------------------------===//

        //----------------
        //create the scene with gridpane
        //add it to the stage
        //sets the scene dimensions to tiles grid width, plus the
        //margins of the tiles grid
        Scene scene = new Scene(scenePane, screenWidth, screenHeight);
        KeyPressHandler keyPressHandler = new KeyPressHandler();
        scene.setOnKeyPressed(keyPressHandler);

        primaryStage.setScene(scene);
        primaryStage.show();


    }

    /** Add your own Instance Methods Here */
    //========================EVENT HANDLERS=========================//
      private static final int GAMEOVERSIZE = 70;
      private static final Font GAMEOVERFONT = Font.font("Comic Sans MS",
                                                  FontWeight.BOLD,
                                                  GAMEOVERSIZE);
      private static final Color FADEWHITE = Color.rgb(255, 255, 255, 0.8);
      //handles all up down left right keypresses
      private class KeyPressHandler implements EventHandler<KeyEvent> {
        private boolean moved = false;
        private  boolean done = false;

        public KeyPressHandler() {
        }
        public void handle(KeyEvent keyPressed) {
          if(!done) {
            if(board.isGameOver()) {
              //creates the game over screen
              //adds the rectangle for game over to be displayed on
              Rectangle rect = new Rectangle(0, 0, screenWidth, screenHeight);
              rect.setFill(FADEWHITE);
              scenePane.getChildren().add(rect);
              //adds a label for the game over text
              Label gameOver = new Label("Game Over!");
              gameOver.setFont(GAMEOVERFONT);
              scenePane.getChildren().add(gameOver);
              done = true;
            }
            else {
              //first updates display score to score.
              gameScore.setText("SCORE: " + board.getScore());
              //determines which direction to move in
              switch (keyPressed.getCode()) {
                case UP: 
                  board.moveAndAdd(Direction.UP);
                  //prints that you are moving up
                  System.out.println("MOVING UP");
                  updateTiles();
                  break;
                case DOWN: 
                  board.moveAndAdd(Direction.DOWN);
                  //prints that you are moving down
                  System.out.println("MOVING DOWN");
                  updateTiles();
                  break;
                case RIGHT: 
                  board.moveAndAdd(Direction.RIGHT);
                  System.out.println("MOVING RIGHT");
                  updateTiles();
                  break;
                case LEFT: 
                  board.moveAndAdd(Direction.LEFT);
                  System.out.println("MOVING LEFT");
                  updateTiles();
                  break;
              }
              if(keyPressed.getText().equals("s")) {
                System.out.println("Saving Board to " + outputBoard);
                board.saveBoard(outputBoard);
              }
            }
          }
        }
      }


    //===============================================================//
    //updates game tiles
    public void updateTiles() {
      for(int y = 0; y < gridSize; y++ ) {
        for(int x = 0; x < gridSize; x++ ) {
          //updates the data of the tile, specifically
          //the number that it corresponds to according
          //to board grid
          gameTiles[y][x].setTxt("" + board.getGrid()[y][x]);
          //update color of the grid tile after
          //updating the tile
          gameTiles[y][x].updateColor();
        }
      }
    }
    
    //class tile for easy access to rectangle drawings
    //as well as adding text to rectangle
    class Tile {
      private int x, y, width, height;
      private String txt;
      private Color color;
      private int margin;
      //instance nodes and panes
      StackPane burger;
      Rectangle rect;
      Label label;
      //ctor for tile to create a rectangle
      public Tile(int x, int y, int width, int height, 
                      String txt, Color color) {
        //update defaults
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.txt = txt;
        this.color = color;
        //done updating defaults
        //creating and initializing stack, creates stack and
        //adds it to gridPane using addToGrid
        burger = new StackPane();
        //set up rectangle and label
        rect = new Rectangle(0, 0, width, height);
        label = new Label(txt);
        //adds the "burger" to the grid
        burger.getChildren().addAll(rect, label);
        addToGrid(burger);
        updateColor();
      }
      //ctor for tile to create rectangle without text
      public Tile(int x, int y, int width, int height) {
        this(x, y, width, height, "", Color.BLACK);
      }
      
      //sets up the indices on gridpane based on x and y
      private void addToGrid(Pane tile) {
        gridPane.setRowIndex(tile, y);
        gridPane.setColumnIndex(tile, x);
        gridPane.getChildren().add(tile);
      }

      //finds out the corresponding power of two a tile's txt, 
      //to be matched with colorScheme's indices
      private int tileColorCorrespond(int tileNum) {
        int powerOfTwo = 0;
        while(tileNum > 1) {
          powerOfTwo += 1;
          tileNum/=2;
        }
        //if the colorScheme runs out before the score does,
        //just keep using the same color
        if(powerOfTwo>colorScheme.length - 1) {
          return colorScheme.length - 1;
        }
        else return powerOfTwo;
      }

      //updates text for label
      private void updateText() {
        label.setText(txt);
        if(txt.equals("0")) {
          label.setTextFill(Color.WHITE);
        }
        else {
          label.setTextFill(Color.BLACK);
        }
      }

      //public accessor/mutator methods
      public void setX(int x) {this.x = x;}
      public void setY(int y) {this.y = y;}
      public void setWidth(int width) {this.width = width;}
      public void setHeight(int height) {this.height = height;}
      //updates the color to the color scheme
      public void updateColor() {
        //sets the color to the index of color scheme corresponding to
        //the power of two that the number is on
        this.color = colorScheme[tileColorCorrespond(Integer.parseInt(txt))];
        rect.setFill(color);
      }
      //sets the margins surrounding the burger
      public void setMargin(int margin) {
        this.margin = margin;
        gridPane.setMargin(burger, new Insets(margin));
      }
      public void setTxt(String txt) {
        this.txt = txt;
        updateText();
      }
      //set the font of the text
      public void setFont(Font font) {
        label.setFont(font);
      }
      public int getX() {return x;}
      public int getY() {return y;}
      public int getWidth() {return width;}
      public int getHeight() {return height;}
      public Color getColor() {return color;}
      public String getTxt() {return txt;}
    }







    /** DO NOT EDIT BELOW */

    // The method used to process the command line arguments
    private void processArgs(String[] args)
    {
        String inputBoard = null;   // The filename for where to load the Board
        int boardSize = 0;          // The Size of the Board

        // Arguments must come in pairs
        if((args.length % 2) != 0)
        {
            printUsage();
            System.exit(-1);
        }

        // Process all the arguments 
        for(int i = 0; i < args.length; i += 2)
        {
            if(args[i].equals("-i"))
            {   // We are processing the argument that specifies
                // the input file to be used to set the board
                inputBoard = args[i + 1];
            }
            else if(args[i].equals("-o"))
            {   // We are processing the argument that specifies
                // the output file to be used to save the board
                outputBoard = args[i + 1];
            }
            else if(args[i].equals("-s"))
            {   // We are processing the argument that specifies
                // the size of the Board
                boardSize = Integer.parseInt(args[i + 1]);
            }
            else
            {   // Incorrect Argument 
                printUsage();
                System.exit(-1);
            }
        }

        // Set the default output file if none specified
        if(outputBoard == null)
            outputBoard = "2048.board";
        // Set the default Board size if none specified or less than 2
        if(boardSize < 2)
            boardSize = 4;

        // Initialize the Game Board
        try{
            if(inputBoard != null)
                board = new Board(inputBoard, new Random());
            else
                board = new Board(boardSize, new Random());
        }
        catch (Exception e)
        {
            System.out.println(e.getClass().getName() + " was thrown while creating a " +
                               "Board from file " + inputBoard);
            System.out.println("Either your Board(String, Random) " +
                               "Constructor is broken or the file isn't " +
                               "formated correctly");
            System.exit(-1);
        }
    }

    // Print the Usage Message 
    private static void printUsage()
    {
        System.out.println("Gui2048");
        System.out.println("Usage:  Gui2048 [-i|o file ...]");
        System.out.println();
        System.out.println("  Command line arguments come in pairs of the form: <command> <argument>");
        System.out.println();
        System.out.println("  -i [file]  -> Specifies a 2048 board that should be loaded");
        System.out.println();
        System.out.println("  -o [file]  -> Specifies a file that should be used to save the 2048 board");
        System.out.println("                If none specified then the default \"2048.board\" file will be used");
        System.out.println("  -s [size]  -> Specifies the size of the 2048 board if an input file hasn't been");
        System.out.println("                specified.  If both -s and -i are used, then the size of the board");
        System.out.println("                will be determined by the input file. The default size is 4.");
    }
}
