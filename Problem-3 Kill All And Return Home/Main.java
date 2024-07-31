import java.util.Scanner;
public class Main{
    public static boolean isSafe(int[][] chessBoard, int row, int col) {
        return row >= 0 && col >= 0 && row < chessBoard.length && col < chessBoard[0].length;
    }

    public static int getNumberOfPaths(int[][] chessBoard, int startRow, int startCol, int curRow, int curCol, char curDir, boolean isInitialCall) {
        /**
         * Base case
         * If we reach back to the starting cell, we have found a new path so we return 1.
         * isInitialCall ensures that base condition doesn't stand true in the very first call.
         **/
        if (curRow == startRow && curCol == startCol && !isInitialCall) {
            return 1;
        }

        // Change position according to the direction
        int nextRow = curRow, nextCol = curCol;
        if (curDir == 'D') nextRow++;
        else if (curDir == 'U') nextRow--;
        else if (curDir == 'R') nextCol++;
        else if (curDir == 'L') nextCol--;

        int kill = 0, jumpOver = 0;

        if (isSafe(chessBoard, nextRow, nextCol)) { // Check if new cell doesn't go out of chessBoard
            // Recursive call for jumping over the cell
            jumpOver = getNumberOfPaths(chessBoard, startRow, startCol, nextRow, nextCol, curDir, false);

            // Handle the "kill" case
            if (chessBoard[nextRow][nextCol] == 1) {
                chessBoard[nextRow][nextCol] = 0; // Kill the soldier and mark the cell as empty

                // Change direction after killing a soldier
                char newDir = curDir;
                if (curDir == 'D') newDir = 'R';
                else if (curDir == 'U') newDir = 'L';
                else if (curDir == 'R') newDir = 'U';
                else if (curDir == 'L') newDir = 'D';

                kill = getNumberOfPaths(chessBoard, startRow, startCol, nextRow, nextCol, newDir, false);

                chessBoard[nextRow][nextCol] = 1; // Backtrack: restore the soldier
            }
        }

        return kill + jumpOver;
    }

	public static void main(String[] args) {
	    // Taking input using Scanner class
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt(); // number of coordinates
		int[][] coordinates = new int[n][2]; // 2D array to store coordinates
		for(int i = 0; i < n; i++){
		    coordinates[i][0] = sc.nextInt();
		    coordinates[i][1] = sc.nextInt(); // taking input for coordinates
		}
		// starting cell coordinates
		int startingRow = sc.nextInt();
		int startingCol = sc.nextInt();
		
		// Find max row and max column number out of coordinates to determine the size of the chessBoard
		int maxRow = -1;
		int maxCol = -1;
		for(int i = 0; i < n; i++){
		    maxRow = Math.max(maxRow, coordinates[i][0]);
		    maxCol = Math.max(maxCol, coordinates[i][1]);
		}		
		// maxRow and maxCol are values for chessboard's rows and columns
		int[][] chessBoard = new int[maxRow + 1][maxCol + 1];
		// Placing soldiers on the chessBoard
		for(int i = 0; i < n; i++){
		    int r = coordinates[i][0];
		    int c = coordinates[i][1];
		    chessBoard[r][c] = 1;
		}
		// Call the function for finding the total number of paths
		int paths = getNumberOfPaths(chessBoard, startingRow, startingCol, startingRow, startingCol, 'D', true);
		// Print output
		System.out.println("Thanks. There are "+paths+" unique paths for your ‘special_castle.’");
		
		sc.close();
	}
}