import java.util.*;
import java.io.*;


public class Crossword{
	private static DictInterface D;
	private static StringBuilder rowStr[];
	private static StringBuilder colStr[]; 
	private static char [][] board; 
	private static char [][] newBoard;
	private static int size; 
	
	
	public static void main(String []args) throws IOException{
		String st;
		StringBuilder sb;
		D = new MyDictionary();
		
		
		Scanner scan = new Scanner(new FileInputStream(args[0]));
		
		while (scan.hasNext()){
			st = scan.nextLine();
			D.add(st);
		}
		
		scan.close();
		
		Scanner scan2 = new Scanner(new FileInputStream(args[1]));
		size = scan2.nextInt();
		board = new char[size][size];
		String line;
		
		rowStr = new StringBuilder[size];
		colStr = new StringBuilder[size];
		char c;
		String line2 = scan2.next();
		
		for(int i = 0; i < size; i++){
			rowStr[i] = new StringBuilder();
			colStr[i] = new StringBuilder();
			for(int k = 0; k < line2.length(); k++){
				c = line2.charAt(k);
				//read characters into 2 D array
				board[i][k] = c;
			}
		}
		scan2.close();
		solve(size-1, size-1, rowStr, colStr);
	}
	public static void solve(int row, int col, StringBuilder[] rowStr, StringBuilder[] colStr){

		switch(board[row][col]){
			case '+': 
				for(char c = 'a'; c <= 'z'; c++){
					if(isValid(c,row, col)){
						//append c to rowStr[row] and colStr[col]
						rowStr[row].append(c);
						colStr[col].append(c);
						if(row == size-1 && col == size-1){
							printSoln();
							return;
						}
						else{
							solve(row, col+1, rowStr, colStr);
						}
						rowStr[row].deleteCharAt(size-1);
						colStr[col].deleteCharAt(size-1);
					}	
				}
				break;
			case '-'://
				rowStr[row].append('-');
				colStr[col].append('-');
				if(row == size-1 && col == size-1){
					printSoln();
					return;
				}
				rowStr[row].deleteCharAt(size-1);
				colStr[col].deleteCharAt(size-1);
				break;
			default: //
				char c = board[row][col];
				if(isValid(c,row,col)){
						rowStr[row].append(c);
						colStr[col].append(c);
					if(row == size-1 && col == size-1){
						printSoln();
						return;
					}
					else{
						solve(row, col+1, rowStr, colStr);
					}
					rowStr[row].deleteCharAt(size-1);
					colStr[col].deleteCharAt(size-1);
				}
		}
	
	}

    private static boolean isValid(char c, int row, int col){
    	//creating new board to check so it doesn't mess up original board
    	StringBuilder newRow = new StringBuilder(size);
    	StringBuilder newCol = new StringBuilder(size);
    	int column = 0;
    	int r = 0;
    	
    	for(int i = 0; i < size; i++){
    		newBoard[i][col] = board[i][col];
    		newBoard[r][i] = board[r][i];
    	
    	}
    	newBoard[row][col] = c;
    	
    	for(int i = 0; i < size; i++){
    		if(newBoard[i][col] != '+'){
    			if(i>0){
    				if(newBoard[i-1][col] != '+'){
    					newCol.append(newBoard[i][col]);
    				}
    			}
    			else{
    				newCol.append(newBoard[i][col]);
    			}
    		}
    	}
    	
    	//Seeing where we are in the board in regards to rows
    	//row checks
    	int iC = newRow.lastIndexOf(" ");
    	if(iC == newRow.length()-1){
    		r = D.searchPrefix(newRow, 0, iC-1);
    	}
    	else if(iC == 0){
    		r = D.searchPrefix(newRow,0,newRow.length()-1);
    	}
    	else if(iC == -1){
    		r = D.searchPrefix(newRow,0,newRow.length()-1);
    		
    	}
    	else if(iC != 0){
    		if(iC > size/2){
    			r = D.searchPrefix(newRow,0,newRow.length()-1);
    		}
    	}
    	else{
    		r = D.searchPrefix(newRow, iC +1 , newRow.length()-1);
    	}
    	
    	//Seeing where are we in the board in regards to columns	
    	//column checks
    	int iR = newCol.lastIndexOf(" ");
    	if(iR == newCol.length()-1){
    		column = D.searchPrefix(newCol, 0, iR-1);
    	}
    	else if(iR == 0){
    		column = D.searchPrefix(newCol,0,newCol.length()-1);
    	}
    	else if(iR == -1){
    		column = D.searchPrefix(newCol,0,newCol.length()-1);
    		
    	}
    	else if(iR != 0){
    		if(iR > size/2){
    			column = D.searchPrefix(newCol,0,newCol.length()-1);
    		}
    	}
    	else{
    		column = D.searchPrefix(newCol, iR +1 , newCol.length()-1);
    	}
    	
    	//checking if they are valid words or prefix
    	//checking if there is something in the row or if it is a prefix
		if(iR == newCol.length()-1 || iR == 0){
			if(iR != 2 && iR !=3){ 
				return false; 
			}
		}
		
		//checking if they it is a prefix -- or if it is valid
		if(row < size -1 || col < size -1){ 
			if(iR !=1 && iR !=3){
				return false;
			}
			if(iC !=2 && iC != 3){
				return false;
			}
		}
		//checking col if they it is a prefix -- or if it is valid
		else{
			if(iR != 2 && iR !=3){
				return false;
			}
		}
		//checking row if it is a prefix -- or if it is valid
		if(iC == newRow.length()-1 && iC == 0){
			if(iC != 2 && iC !=3)
				return false;
		}
		
		else{
			if(iC !=2&&iC!=3)
				return false;
		}
		return true;
		
    	
	}
		
	//shows that the original board prints
	public static void printBoard(){
		System.out.println("Board: ");
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public static void printSoln(){
		System.out.println("Solution Found: ");
		for(int i = 0; i < size; i++)
			System.out.println(rowStr[i]);
		System.out.println();
	}
	
}
