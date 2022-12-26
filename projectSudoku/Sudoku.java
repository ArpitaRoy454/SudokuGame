package projectSudoku;

import java.awt.EventQueue;
import java.util.Random;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

import java.awt.Font;


public class Sudoku {

	private JFrame frame;
	boolean isStarted = false;
	int prevBoard[][] = new int[9][9];

	private final Timer timer = new Timer(1000, (ActionListener) null);
	final JLabel timerLabel = new JLabel("Time Left:");
	final JButton startButton = new JButton("Start");
	final JButton submitButton = new JButton("Submit");
	final JTextField grid[][] = new JTextField[9][9];
	int timeCount = -1;



	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Sudoku window = new Sudoku();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e);
				}
			}
		});
	}

	//Use to convert counter into time.
	public String countToTime(int count){
		String min = Integer.toString(count/60);
		String sec = Integer.toString(count%60);
		if(Integer.parseInt(min) == 0) min = "0"+min;
		if(Integer.parseInt(sec)/10 == 0) sec = "0"+sec;
		return min+":"+sec;
	}

	//Event handler work when game is over.
	public void gameOver(){
		timerLabel.setVisible(false);
		timer.stop();
		prevBoard = SudokuSolver.solve(prevBoard);
		boolean isFine = true;
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if(grid[i][j].getText().equals("")){
					isFine = false;
					break;
				}
				else if(Integer.parseInt(grid[i][j].getText()) != prevBoard[i][j]){
					isFine = false;
					break;
				}
			}
		}
		if(isFine && isStarted) JOptionPane.showMessageDialog(null, "You Won.");
		else JOptionPane.showMessageDialog(null, "You Lose.");
		isStarted = false;
		startButton.setText("Start");
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				grid[i][j].setText("");
				grid[i][j].setEditable(false);
			}
		}
	}
	


	public Sudoku() {
		initialize();
	}


	private void initialize() {
		timer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				timeCount--;
				timerLabel.setText("Time Left- "+countToTime(timeCount));
				if(timeCount == 0) gameOver();
			}
		});


		frame = new JFrame();
		frame.setBounds(100, 100, 668, 438);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		//////////////////////////////////GRID START /////////////////////////////////////////////////////////////


		int h = 12, w = 13, hi = 39, wi = 37;

		for(int i = 0; i < 9 ; i++){
			if(i%3 == 0 && i!=0) w += 13;

			for(int j = 0; j < 9; j++){
				if(j%3 == 0 && j!=0) h += 11;

				grid[i][j] = new JTextField();
				grid[i][j].setColumns(10);
				grid[i][j].setBounds(h, w, 38, 37);
				frame.getContentPane().add(grid[i][j]);
				h += hi;
			}
			h = 12;
			w += wi;
		}

		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				grid[i][j].setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.BOLD, 22));
				grid[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				grid[i][j].setEditable(false);
			}
		}
		//////////////////////////////////  GRID END  /////////////////////////////////////////////////////////////

		submitButton.setVisible(false);
		timerLabel.setVisible(false);
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				gameOver();
			}
		});
		submitButton.setFont(new Font("Calibri Light", Font.BOLD, 18));
		submitButton.setBounds(435, 270, 155, 37);
		frame.getContentPane().add(submitButton);

		JLabel difficultyLabel = new JLabel("Select difficulty:");
		difficultyLabel.setFont(new Font("Calibri Light", Font.BOLD, 16));
		difficultyLabel.setBounds(435, 70, 155, 24);
		frame.getContentPane().add(difficultyLabel);

		timerLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 13));
		timerLabel.setFont(new Font("Calibri Light", Font.BOLD, 16));
		timerLabel.setBounds(435, 13, 176, 16);
		frame.getContentPane().add(timerLabel);

		final JRadioButton easyButton = new JRadioButton("Easy");
		easyButton.setFont(new Font("Calibri Light", Font.BOLD, 13));
		easyButton.setBounds(435, 103, 127, 25);
		frame.getContentPane().add(easyButton);

		final JRadioButton mediumButton = new JRadioButton("Medium");
		mediumButton.setFont(new Font("Calibri Light", Font.BOLD, 13));
		mediumButton.setBounds(435, 133, 127, 25);
		frame.getContentPane().add(mediumButton);

		final JRadioButton hardButton = new JRadioButton("Hard");
		hardButton.setFont(new Font("Calibri Light", Font.BOLD, 13));
		hardButton.setBounds(435, 163, 127, 25);
		frame.getContentPane().add(hardButton);

		ButtonGroup bg = new ButtonGroup();
		bg.add(easyButton);
		bg.add(mediumButton);
		bg.add(hardButton);
		bg.setSelected(mediumButton.getModel(), true);



		startButton.setBounds(435, 206, 155, 37);
		frame.getContentPane().add(startButton);
		startButton.setFont(new Font("Calibri Light", Font.BOLD, 18));
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(isStarted){
					isStarted = false;
					prevBoard = SudokuSolver.solve(prevBoard);
					for(int i = 0; i < 9; i++){
						for(int j = 0; j < 9; j++){
							grid[i][j].setEditable(false);
							grid[i][j].setText(Integer.toString(prevBoard[i][j]));
						}
					}
					startButton.setText("Start");
					timer.stop();
					timerLabel.setVisible(false);
					submitButton.setVisible(false);
				}
				else{
					int difficulty = 1; // Default is medium.
					if(easyButton.isSelected()) difficulty = 0;
					else if (hardButton.isSelected()) difficulty = 2;
					else difficulty = 1;

					if(difficulty == 0) timeCount = 600;
					else if(difficulty == 1) timeCount = 360;
					else timeCount = 180;

					int board[][] = new int[9][9];
					do{
						board = SudokuGenerator.generate(difficulty);
					}while(board[0][0] == -1);
					for(int i = 0; i < 9; i++){
						for(int j = 0; j < 9; j++){
							prevBoard[i][j] = board[i][j];
						}
					}
					for(int i = 0; i < 9; i++){
						for(int j = 0; j < 9; j++){
							if(board[i][j] != 0){
								grid[i][j].setText(Integer.toString(board[i][j]));
							}
							else {
								grid[i][j].setText("");
								grid[i][j].setEditable(true);
							}
						}
					}
					submitButton.setVisible(true);
					startButton.setText("Give up!");
					timerLabel.setVisible(true);
					timer.start();
					isStarted = true;
				}
			}
		});
	}
}

/*Point class Start here */
class Point {
    
    int x, y;
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
}



/*SudokuGenerator class Start here */

 class SudokuGenerator {
	private static int board[][] = new int[9][9];

	//To make a simple solved puzzle for seed.
	public static void generateSeed(){
		board = SudokuSolver.solve(board);
	}

	//To get a new permutation of seed puzzle.
	public static void generatePermutation(int select){
		int s0, s1, min = 0, max = 2;
		Random r = new Random();
		for(int i = 0; i < 3; i++){
			s0 = r.nextInt(max - min + 1) + min;
			do{
				s1 = r.nextInt(max - min + 1) + min;
			}while(s1 == s0);
			max += 3; min += 3;
			if(select == 0) permutationCol(s0, s1);
			else permutationRow(s0, s1);
		}
	}

	//To get permutation with 3x3 columns.
	public static void permutationCol(int s0, int s1){
		for(int i = 0; i < 9; i++){
			int temp = board[i][s0];
			board[i][s0] = board[i][s1];
			board[i][s1] = temp;
		}
	}

	//To get permutation with 3x3 rows.
	public static void permutationRow(int s0, int s1){
		for(int i = 0; i < 9; i++){
			int temp = board[s0][i];
			board[s0][i] = board[s1][i];
			board[s1][i] = temp;
		}
	}

	//To get permutation with 3x9 rows.
	public static void exchangeRows(int s0, int s1){
		   for(int i = 0; i < 3; i++)
		   {
		      for(int j = 0; j < 9; j++)
		      {
		         int temp = board[s0][j];
		         board[s0][j] = board[s1][j];
		         board[s1][j] = temp;
		      }
		      s0++;
		      s1++;
		   }
	}

	//To get permutation with 9x3 columns.
	public static void exchangeCols(int s0, int s1){
		   for(int i = 0; i < 3; i++)
		   {
		      for(int j = 0; j < 9; j++)
		      {
		         int temp = board[j][s0];
		         board[j][s0] = board[j][s1];
		         board[j][s1] = temp;
		      }
		      s0++;
		      s1++;
		   }
	}

	//To transpose the board.
	public static void transpose(){
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				int temp = board[i][j];
				board[i][j] = board[j][i];
				board[j][i] = temp;
			}
		}
	}

	//To clear space at (s0, s1)
	public static int clearNum(int s0, int s1)
	{
		int count = 9;
		for(int i = 1; i <= 9; i++){
			if(!SudokuSolver.isMoveValid(s0, s1, i, board)) count--;
			if(count == 1){
				board[s0][s1] = 0;
				return 1;
			}
		}
		return 0;
	}

	public static int[][] generate(int difficulty){
		Random r = new Random();

		/*int maxCount = -1;
		if(difficulty == 0) maxCount += 45 + r.nextInt(6);//46
		else if(difficulty == 1) maxCount += 50 + r.nextInt(6);
		else maxCount += 57 + r.nextInt(6);*/
		int maxCount = 43;

		//Reset the old puzzle.
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				board[i][j] = 0;
			}
		}

		int count = 0, s0, s1;
		int choice[] = {0, 3, 6};

		generateSeed();
		if(r.nextInt(2) == 0) transpose();
		generatePermutation(1);
		generatePermutation(0);


		for(int i = 0; i < 2; i++){
			s0 = choice[r.nextInt(3)];
			do{
				s1 = choice[r.nextInt(3)];
			}while(s0 == s1);

			if(i % 2 == 0) exchangeRows(s0, s1);
			else exchangeCols(s0, s1);
		}
		while(count < maxCount){
			s0 = r.nextInt(9);
			s1 = r.nextInt(9);
			count += clearNum(s0, s1);
		}
		return board;
	}
}

/*SudokuSolver class Start here */



 class SudokuSolver {

	//To check that no move have been made at this block.
	public static boolean isBlockValid(int x, int y, int board[][]){
		return board[x][y] == 0;
	}

	//To check if the game is completed or not.
	public static boolean isComplete(int board[][]){
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if(board[i][j] == 0) return false;
			}
		}
		return true;
	}

	//To check if current move is valid or not.
	public static boolean isMoveValid(int x, int y, int move, int board[][]){

		//To check if the number entered is valid or not.
		if(x < 0 || y < 0 || x > 9 || y > 9) return false;

		//To check the column constraint.
		for(int i = 0; i < 9; i++){
			if(board[i][y] == move) return false;
		}

		//To check the row constraint.
		for(int i = 0; i < 9; i++){
			if(board[x][i] == move) return false;
		}

		//To check the sub-box.
		for(int i = (x/3)*3; i < (x/3)*3 + 3; i++){
			for(int j = (y/3)*3; j < (y/3)*3 + 3; j++){
				if(board[i][j] == move) return false;
			}
		}

		//After all checks, return true.
		return true;
	}

	//To get the next empty point in the board, and if not return null.
	public static Point getNext(int x, int y, int board[][]){

		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if(board[i][j] == 0) return new Point(i, j);
			}
		}
		return null;
	}

	public static boolean solve(int x, int y, int move, int board[][]){

		//Get the next empty point.
		Point next = getNext(x, y, board);

		if(next != null){
			//Starting the moves.
			for(int i = 1; i < 10; i++){
				if(isMoveValid(next.x, next.y, i, board)){
					board[next.x][next.y] = i;
					if(solve(next.x, next.y, i, board)){
						return true;
					}
					board[next.x][next.y] = 0;
				}
			}
		} else return true;
		return false;
	}

	public static int[][] solve(int[][] board){
		int garbageBoard[][] = new int[9][9];
		garbageBoard[0][0] = -1;
		int i = 0 , j = 0;
		for(int k = 1; k < 10; k++){
			if(solve(i, j, k, board))
				return board;
		}
		return garbageBoard;
	}
}
