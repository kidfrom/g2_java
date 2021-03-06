package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

  // *|*|*|*|* (5,2)
  //  |*| |*|  (1,1)(3,1)
  //  | |*| |  (2,2)
  //
  // *|*|*|*|*|*|* (7,3)
  //  |*| | | |*|  (1,1)(5,1)
  //  | |*| |*| |  (2,2)(4,2)
  //  | | |*| | |  (3,3)
  //
  // *|*|*|*|*|*|*|*|* (9,4)
  //  |*| | | | | |*|  (1,1)(7,1)
  //  | |*| | | |*| |  (2,2)(6,2)
  //  | | |*| |*| | |  (3,3)(5,3)
  //  | | | |*| | | |  (4,4)(4,4)
  private static void printInvertedTriangle(int width, String symbol) {
    if (width > 4 && width % 2 != 0) {
      for (int j = 0; j <= (width - 1) / 2; j++) {
        for (int i = 0; i < width; i++) {
          if (j == 0 || (i==j) || i+j==width-1) {
            System.out.print(symbol);
          } else {
            System.out.print(" ");
          }
          if (i == width - 1) {
            System.out.println("");
          }
        }
      }
    }
  }

//  public static void main(String[] args) {
//    printInvertedTriangle(9, "*");
//  }

  public static void main(String[] args) {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("ENTER N : ");
    Integer n = null;
    try {
      n = Integer.parseInt(br.readLine());
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.print("Enter Symbol : ");
    String symbol = null;
    try {
      symbol = br.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (n != null && symbol != null) {
      printInvertedTriangle(n, symbol);
    }
  }
}
