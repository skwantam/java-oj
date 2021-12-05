// package com.tsj.oj.easy;
import java.lang.Math;
import java.util.*;

public class GameOfLife {

  public static void main(String[] args) {
    GameOfLife gol = new GameOfLife();
    int[][] board = new int[][] { { 0, 0, 0 }, { 1, 0, 1 }, { 0, 1, 1 }, { 0, 1, 0 }, };
    gol.printBoard(board);
    for(int i=0; i<10; i++){
      gol.oneStep(board);
      gol.printBoard(board);
    }
  }

  public void oneStep(int[][] board) {
    int n = board.length, m = board[0].length;

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        int neighbors = getNeighbor(i, j, board);
        if (board[i][j] == 1 && (neighbors == 2 || neighbors == 3)) {
          board[i][j] = 1;
        } else if (board[i][j] == 1) {
          board[i][j] = -1;
        } else if (board[i][j] == 0 && neighbors == 3) {
          board[i][j] = -2;
        }
      }
    }

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        if (board[i][j] == -1) {
          board[i][j] = 1;
        } else if (board[i][j] == -2) {
          board[i][j] = 0;
        }
      }
    }
  }

  public int getNeighbor(int i, int j, int[][] board) {
    int n = board.length, m = board[0].length;
    if (i < 0 || i >= n || j < 0 || j >= m)
      return 0;
    int top = Math.max(0, i - 1);
    int bottom = Math.min(n - 1, i + 1);
    int left = Math.max(0, j - 1);
    int right = Math.min(m - 1, j + 1);
    int count = 0;
    for (int x = top; x <= bottom; x++) {
      for (int y = left; y <= right; y++) {
        if (board[x][y] == 1 || board[x][y] == -1)
          count++;
      }
    }
    if (board[i][j] == 1 || board[i][j] == -1) {
      count--;
    }
    return count;
  }

  public void printBoard(int[][] board){
    System.out.println("==============================>");
    for(int i=0; i<board.length; i++){
      for(int j=0; j<board[0].length; j++){
        System.out.print(board[i][j] + " " );
      }
      System.out.println();
    }
    System.out.println("<==============================");
  }
}
