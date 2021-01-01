import java.util.*;
import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import javax.sound.sampled.*;
import  sun.audio.*; 
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;

class Board{
	public static int [][]board;
	public static int [][]shownBoard;
	public static int size, nM;
	public Board(int s, int numMines){
		board = new int[s][s];
		shownBoard = new int[s][s];
		for(int i = 0; i < s; i++){
			for(int j = 0; j < s; j++){
				shownBoard[i][j] = 1; //0 is shown, 1 is covered, 3 is flagged
				board[i][j] = 0;
			}
		}
    size = s;
    nM = numMines;
    //generate mines
    for(int it = 0; it < numMines; it++){
        int x = (int)(Math.random()*(s - 2)) + 1, y = (int)(Math.random() * (s - 2)) + 1;
        while(board[x][y] == -1){
          x = (int)(Math.random() * (s - 2)) + 1;
          y = (int)(Math.random() * (s- 2)) + 1;
        }
        board[x][y] = -1;
		for(int i = x - 1; i < x + 2; i++){
		  for(int j = y - 1; j < y + 2; j++){
			if(!(i == x && j == y) && board[i][j] != -1){
				board[i][j] += 1;
			}
		  }
		}
	}
	}

  //actual fucnitons
  public int setFlag(int row, int col){
	  if(shownBoard[row][col] == 3){
		  return 2;
	  }else if(shownBoard[row][col] == 0)return 1;
	  else {
		shownBoard[row][col] = 3;
		return 0;
	  }
  }
  public String toPrint(int x){
	String out = "";
	if(x >= 10)out += x;
	else out += x + " ";
	out += "|";
	return out;
  }
  
  public void printBoard(){
    System.out.print("    ");
    for(int i = 0; i < size; i++)System.out.print(toPrint(i + 1) + " ");
    System.out.println();
    for(int i = 0; i < size; i++){
      System.out.print(toPrint(i + 1) + " ");
      for(int j = 0; j < size; j++){
        if(shownBoard[i][j] == 0){
		  if(board[i][j] == 0)System.out.print(".   ");	
          else if(board[i][j] != -1)System.out.print(board[i][j] + "   ");
		  else System.out.print("B   ");
        }else if(shownBoard[i][j] == 3){
          System.out.print("F   ");
        }else{
          System.out.print("#   ");
        }
      }
      System.out.println();
    }
  }
  public int isWon(){
	  int c = 0;
	  for(int i = 0; i < size; i++){
		  for(int j = 0; j < size; j++){
			 if(shownBoard[i][j] == 1 || shownBoard[i][j] == 3)c++;
		  }
	  }
	  return (c <= nM ? 1 : 0);
  }
  public void dfs(int row, int col){
	if(shownBoard[row][col] == 0) return;
	if(board[row][col] == -1)return;
	if(board[row][col] > 0){shownBoard[row][col] = 0; return;}
	else {shownBoard[row][col] = 0;
	if(row > 0)dfs(row - 1, col);
	if(row < size - 1)dfs(row + 1, col);
	if(col > 0)dfs(row, col - 1);
	if(col < size - 1)dfs(row, col + 1);
	}
	return;
  }
  public boolean isSafe(int row, int col){
	  if(board[row][col] == -1)return false;
	  dfs(row, col);
	  return true;
  }
  public void isBomb(int row, int col){
	 if(board[row][col] == -1){
		for(int i = row - 1; i < row + 2; i++)
			for(int j = col - 1; j < col + 1; j++)
				if(board[i][j] != -1)board[i][j] -= 1;
	 }
  }
  public int alreadyRevealed(int row, int col){
	  if(shownBoard[row][col] == 0)return 1;
	  else if(shownBoard[row][col] == 3)return 2;
	  else return 0;
  }
}

class Main {
	public static Board b;
	public static void cls(String... arg) throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
  }
  public static int getRobustIn(String s, int t, int left, int right) throws IOException, InterruptedException {
	  cls();
	  if(t == 1)b.printBoard();
	  System.out.println(s);
	  Scanner sc = new Scanner(System.in);
	  int ret = sc.nextInt();
	  while(ret < left || ret > right){
		  cls();
		  if(t == 1)b.printBoard();
		  System.out.println(s);
		  ret = sc.nextInt();
	  }
	  return ret;
  }
  public static void main(String[] args) throws IOException, InterruptedException {
	Scanner sc = new Scanner(System.in);
    System.out.println(">> Welcome to Minesweeper!");
	System.out.println(">> What's ur name, dude?: ");
	String name = sc.next();
	if(name.toLowerCase().indexOf("jack") >= 0 || name.toLowerCase().indexOf("roronoa") >= 0 || name.toLowerCase().indexOf("1540") >= 0)name = "Troll";
	System.out.println("Wow, nice name, " + name + "!!! Would you like a tutorial for this game? (Y/N)");
	char c = sc.next().charAt(0);
	if(c == 'y' || c == 'Y') System.out.println("SIKE!!!! You aren't getting a tutorial haha!");
	else System.out.println("okie dokie!");
	TimeUnit.SECONDS.sleep(1);
	int width = getRobustIn(">> Please enter the width of the Board (8 <= width <= 25): ", 0, 8, 25);
	int abc = (int)(0.15 * (width * width));
	int abd = (int)(0.25 * (width * width));
	int numMines = getRobustIn(">> Please enter the number of mines on the board (" + abc + " <= number of mines <= " + abd + "): ", 0, abc, abd);
	b = new Board(width, numMines);
	//guessing
	int row = getRobustIn(">> Please enter the row number of your first guess: ", 1, 1, width);
	int col = getRobustIn(">> Please enter the column number of your first guess: ", 1, 1, width);
	row--;
	col--;
	b.isBomb(row, col);
	while(b.isSafe(row, col) && b.isWon() == 0){
		cls();
		b.printBoard();
		int choice = getRobustIn(">> Please enter 1 if you want to flag a block or 2 if you want to choose to reveal one: ", 1, 1, 2);
		while(choice == 1){
			cls();
			row = getRobustIn(">> Please enter the row number of your flag: ", 1, 1, width);
			row--;
			col = getRobustIn(">> Please enter the column number of your flag: ", 1, 1, width);
			col--;
			int i = b.setFlag(row, col);
			if(i == 2){System.out.println(">> You've already flagged dat.");TimeUnit.SECONDS.sleep(1);}
			else if(i == 1){System.out.println(">> You can't flag dat.");TimeUnit.SECONDS.sleep(1);}
			else{
				choice = getRobustIn(">> Please enter 1 if you want to flag a block or 2 if you want to choose to reveal one: ", 1, 1, 2);
			}
		}
		//wants to reveal
		row = getRobustIn(">> Please enter the row number of your guess: ", 1, 1, width);
		col = getRobustIn(">> Please enter the column number of your guess: ", 1, 1, width);
		row--;
		col--;
		while(b.alreadyRevealed(row, col) == 1 || b.alreadyRevealed(row, col) == 2){
			cls();
			if(b.alreadyRevealed(row, col) == 1){System.out.println(">> Already revealed, comrade.");TimeUnit.SECONDS.sleep(1);}
			else {
					int crap = getRobustIn("You've flagged that square, comrade. Would you still like to reveal it? Yes (1) or No (2)", 1, 1, 2);
					if(crap == 1){
						b.shownBoard[row][col] = 0;
						break;
					}
			}
			row = getRobustIn(">> Please enter the row number of your guess: ", 1, 1, width);
			col = getRobustIn(">> Please enter the column number of your guess: ", 1, 1, width);
			row--;
			col--;
		}
	}
	if(!b.isSafe(row, col)){
		//lost
		cls();
		System.out.println(">> GAME OVER!");
		System.out.println(">> Big Brother is very disappointed in you...perhaps next time...");
		for(int i = 0; i < width; i++)for(int j = 0; j < width; j++)b.shownBoard[i][j] = 0;
		b.printBoard();
	}else{
		//won
		System.out.println(">> Congratulations, comrade! You have defeated Goldstein's evil army, the Brotherhood! Rejoice in the name of Big Brother!");
	}
	System.out.println(">> Thanks for playing my game, comrade " + name + "!");
  }
}