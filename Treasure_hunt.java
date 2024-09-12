
import edu.princeton.cs.algs4.*;

import java.awt.*;
import java.util.Random;

public class Treasure_hunt {
    private static final Color[] PLAYER_COLORS = {StdDraw.RED, StdDraw.BLUE, StdDraw.GREEN, StdDraw.PRINCETON_ORANGE};
    private static Integer[] playerSpace;
    private static Digraph spacesDigraph;

    public static void main(String[] args) {
        // Here we are reading args input number for the number of players from command line arguments

        int numPlayers = Integer.parseInt(args[0]);

        // this is to just validate that the input arges is between 1 and 4 if not provides an error 
        if (numPlayers < 1 || numPlayers > 4) {
            StdOut.println("Min player 1 and Max 4");
            return;
        }

        // here we are just Printing the welcome message and number of players given in the args input 

        StdOut.println("Welcome to the Treasure Hunt in the Amazon Jungle");
        StdOut.println("There will be " + numPlayers + " treasure hunters");
        StdOut.println("The path is filled with hidden passages and harmful pits");
        StdOut.println("The hunt begins now:");

        // here we are printing the color assigned to each player 

        for (int i = 0; i < numPlayers; i++) {
            StdOut.println("Adventurer " + (i + 1) + " will be the color " + getPlayerColorName(PLAYER_COLORS[i]));
        }

        // here we are initializing arrays and variables for game statistics

        playerSpace = new Integer[numPlayers];
        int dieRolls = 0;
        int[] playerRolls = new int[numPlayers];
        int[] commonDieRolls = new int[6 * numPlayers];
        int[] hiddenPassage = new int[numPlayers];
        int[] PitsFallen = new int[numPlayers];

        // here we are initializing arrays and variables for game statistics all arrays to zero

        for (int j = 0; j < numPlayers; j++) {
            playerSpace[j] = 0;
            playerRolls[j] = 0;
            hiddenPassage[j] = 0;
            PitsFallen[j] = 0;
        }


        In in = new In("spacesSTART.txt"); // here we are just pointing to the digraph text file 
        spacesDigraph = new Digraph(in);
        int winningSpace = spacesDigraph.V() - 1;

        // starting the canvas with 600 x 600 with double buffering 

        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(600, 600);
        StdDraw.setScale(0, 10);
        StdDraw.clear();

        // here is the game loop
        for (int i = 0; i < 100; i++) {
            // Determine the current player
            int player = i % numPlayers;

            // roll of the dies with random method 
            Random r = new Random();
            int roll = r.nextInt(6) + 1;

            StdAudio.play("diceRoll.wav"); // will just play the  rolling dice sound
            dieRolls++;
            playerRolls[player]++;
            commonDieRolls[roll * numPlayers - 1]++;

            // here we are pointing the players movement 
            StdOut.println("Adventurer " + (player + 1) + " advances " + roll + " spaces");

            // here we are calling the animateMovement class we created bellow 
            animateMovement(playerSpace[player], playerSpace[player] + roll, player, numPlayers);

            // this is to pint the players new position 

            StdOut.println("  Arrives at the location " + playerSpace[player]);

            // this will check  if the player has reached a hidden passage or a pit from the digraph text file 

            if (playerSpace[player] < winningSpace) {
                int numJumps = spacesDigraph.outdegree(playerSpace[player]);
                if (numJumps != 0) {
                    for (int bigMove : spacesDigraph.adj(playerSpace[player])) {
                        if (playerSpace[player] < bigMove) {


                            StdAudio.play("ladder_1.wav");  // Play the ladder sound with wav file

                            animateMovement(playerSpace[player], bigMove, player, numPlayers); // this will animate the player's movement
                            hiddenPassage[player]++;
                            StdOut.println("        Discovers a hidden passage to the location " + bigMove);
                        } else {

                            StdAudio.play("chute_1.wav"); // Play the sound of  chute_1 wav file  
                            animateMovement(playerSpace[player], bigMove, player, numPlayers); //and animate the player's movement
                            PitsFallen[player]++;
                            StdOut.println("         Falls into a pit and ends up at location " + bigMove); // this print a 
                        }                                                                                    //message if the players arrives at a pitfall
                    }
                }
            }

            // this loop will check  if the player has reached the winning space

            if (playerSpace[player] >= winningSpace) {
                StdOut.println("Adventurer " + (player + 1) + " found the treasure!");
                StdAudio.play("gameWin.wav");
                break;
            }


            drawBoard(spacesDigraph, playerSpace, numPlayers);  // Draw the game board after each move
            StdDraw.show();
            StdDraw.pause(3000);
        }


        StdOut.println();
        StdOut.println("End of game stats:");  // here we are printing the end game stats 
        StdOut.println();
        StdOut.println("Total die rolls for the game: " + dieRolls);
        for (int z = 0; z < numPlayers; z++) {
            StdOut.println("Total die rolls for Adventurer " + (z + 1) + ": " + playerRolls[z]);
        }


        int max = commonDieRolls[0]; // this is to find the most common die roll
        int index = 0;
        for (int p = 1; p < 6 * numPlayers; p++) {
            if (max < commonDieRolls[p]) {
                max = commonDieRolls[p];
                index = p;
            }
        }

        StdOut.println();
        StdOut.println("Most common die roll for the game is " + ((index % numPlayers) + 1));
        for (int p = 0; p < numPlayers; p++) {
            int maxPlayer = commonDieRolls[p];
            int indexPlayer = p;

            for (int q = p; q < 6 * numPlayers; q += numPlayers) {
                if (maxPlayer < commonDieRolls[q]) {
                    maxPlayer = commonDieRolls[q];
                    indexPlayer = q;
                }
            }
            StdOut.println("Most common die roll for Adventurer " + (p + 1) + " is " + ((indexPlayer % numPlayers) + 1));
        }

        StdOut.println();
        int gameLadders = 0;
        for (int l = 0; l < numPlayers; l++) {
            gameLadders += hiddenPassage[l];
        }
        StdOut.println("Total passages found for the game: " + gameLadders);
        for (int l = 0; l < numPlayers; l++) {
            StdOut.println("Total number of passages found by Adventurer " + (l + 1) + ": " + hiddenPassage[l]);
        }

        StdOut.println();
        int gameChutes = 0;
        for (int l = 0; l < numPlayers; l++) {
            gameChutes += PitsFallen[l];
        }
        StdOut.println("Total pits fallen for the game: " + gameChutes);
        for (int l = 0; l < numPlayers; l++) {
            StdOut.println("Total number of pits fallen by Adventurer " + (l + 1) + ": " + PitsFallen[l]);
        }

        StdOut.println();
    }

    // in this class we are trying to draw the grid canvas  with all the pngs and other stuff like lines 
    public static void drawBoard(Digraph spacesDigraph, Integer[] playerSpace, int numPlayers) {

        String image = "quicksand.png"; // Defined the image file names
        String image1 = "Treasure.png";
        String image2 = "cave2.png";
        String image3 = "dragon.png";


        StdDraw.clear(); // this will Clear the canvas

        int gridSize = (int) Math.ceil(Math.sqrt(spacesDigraph.V())); // here we are Calculating grid size and cell size
        double cellSize = 10.0 / gridSize;


        for (int b = 0; b < spacesDigraph.reverse().V(); b++) { // Draw cells and images on the canvas
            int row = b / gridSize;
            int col = b % gridSize;


            if (row % 2 != 0) {     // Adjust column position for alternate rows
                col = gridSize - 1 - col;
            }

            double x = col * cellSize + cellSize / 2;
            double y = row * cellSize + cellSize / 2;


            StdDraw.setPenColor(((row + col) % 2 == 0) ? StdDraw.WHITE : StdDraw.GRAY); // Set the cell color
            StdDraw.filledSquare(x, y, cellSize / 2);


            if (b == 14 || b == 27 || b == 41) {      // Draw special images on specific cells
                StdDraw.picture(x, y, image, cellSize, cellSize);
            }
            if (b == 63) {
                StdDraw.picture(x, y, image1, cellSize, cellSize);
            }
            if (b == 62) {
                StdDraw.picture(x, y, image3, cellSize, cellSize);
            }
            if (b == 3 || b == 7 || b == 21 || b == 31 || b == 37 || b == 45 || b == 50) {
                StdDraw.picture(x, y, image2, cellSize, cellSize);
            }


            for (int i = 0; i < numPlayers; i++) {   // Draw player circles on their respective positions
                if (playerSpace[i] == b) {
                    StdDraw.setPenColor(PLAYER_COLORS[i]);
                    StdDraw.filledCircle(x, y, cellSize / 4);
                }
            }


            StdDraw.setPenColor(StdDraw.BLACK);  // here we are drawing the borders of the cell, chutes and labels
            StdDraw.square(x, y, cellSize / 2);
            StdDraw.text(x, y, Integer.toString(b));
            if (b == 0) {
                StdDraw.text(x, y, "START");
            } else {
                if (b == spacesDigraph.V() - 1) {
                    StdDraw.text(x, y, "FINISH");
                } else {
                    StdDraw.text(x, y, Integer.toString(b));
                }
            }
            StdDraw.setPenColor(StdDraw.BLACK);
        }


        for (int v = 0; v < spacesDigraph.V(); v++) { // Draw edges between cells
            int startRow = v / gridSize;
            int startCol = v % gridSize;


            if (startRow % 2 != 0) {     // Adjust start column position for alternate rows
                startCol = gridSize - 1 - startCol;
            }

            for (int w : spacesDigraph.adj(v)) {
                int endRow = w / gridSize;
                int endCol = w % gridSize;


                if (endRow % 2 != 0) {    // this to adjust the  column position for the alternate rows
                    endCol = gridSize - 1 - endCol;
                }

                double startX = startCol * cellSize + cellSize / 2;
                double startY = startRow * cellSize + cellSize / 2;
                double endX = endCol * cellSize + cellSize / 2;
                double endY = endRow * cellSize + cellSize / 2;
                // Set the edge color based on the direction
                if (v < w) {
                    StdDraw.setPenColor(StdDraw.GREEN);
                } else {
                    StdDraw.setPenColor(StdDraw.RED);
                }

                StdDraw.setPenRadius(0.004);
                StdDraw.line(startX, startY, endX, endY);
            }
        }

        StdDraw.setPenColor(StdDraw.BLACK);
    }


    private static String getPlayerColorName(Color color) { // this class is to get the name like if he is adventurer 1 
        // or adventurer 2 of the player's color
        if (color.equals(StdDraw.RED)) {
            return "Red";
        } else if (color.equals(StdDraw.BLUE)) {
            return "Blue";
        } else if (color.equals(StdDraw.GREEN)) {
            return "Green";
        } else if (color.equals(StdDraw.PRINCETON_ORANGE)) {
            return "Orange";
        } else {
            return "Unknown Color";
        }
    }


    public static void animateMovement(int oldPosition, int newPosition, int player, int numPlayers) { //this to  animate the player's movement on the game board
        int direction = oldPosition < newPosition ? 1 : -1;


        for (int position = oldPosition; position != newPosition; position += direction) { // Animate movement cell by cell
            playerSpace[player] = position;
            drawBoard(spacesDigraph, playerSpace, numPlayers);
            StdDraw.show();
            StdDraw.pause(300);
        }

        playerSpace[player] = newPosition;
        drawBoard(spacesDigraph, playerSpace, numPlayers);
        StdDraw.show();
    }

    
}
