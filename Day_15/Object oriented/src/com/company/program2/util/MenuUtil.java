package com.company.program2.util;

import com.company.program2.datasource.StudentDataSource;
import com.company.program2.util.FTP.FTPClientUtil;
import com.company.program2.util.ServerSocket.ServerSocketUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class MenuUtil {
  private final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
  private final Properties properties = new Properties();

  private StudentDataSource studentDataSource;
  private FTPClientUtil ftpClientUtil;

  public MenuUtil() {
    try {
      properties.load(new FileInputStream("config.properties"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void clearTerminal() {
    System.out.print("\033[H\033[2J");
  }

  public void pressAnyKeyToContinue() {
    try {
      System.out.print("Press enter to continue. ");
      bufferedReader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void printMenu() {
    clearTerminal();
    System.out.println("MENU");
    System.out.println("1. Start Socket Server and Client DB -> config.properties -> Multi Thread -> Input ke Database (Thread Tersendiri)");
    System.out.println("2. FTP and DB Connection Started -> config.properties");
    System.out.println("3. Get Data from FTP Server");
//    -> List semua folder yg di Home Directory -> Masuk ke directorynya
//	  -> Ambil file yg sebelumnya dibuat mahasiswa.json -> ftpDownload
    System.out.println("4. Send Data to DB");
//    -> Input Nama file yg sudah di download (mahasiswa.json)
//    -> Data dari Menu No. 3 (mahasiswa.json) di parsing dari JSON ke Field lalu insert ke DB
    System.out.println("5. Input, Edit and Delete Data from DB");
//    Input
//    -> Langsung Input Data dan tulis ke Database
//    Edit
//    -> List Semua Data dengan (Select *)
//	  -> Pilih data yang akan di edit
//    Delete
//    -> List Semua Data dengan (Select *)
//	  -> Pilih data yang akan di delete
    System.out.println("6. Send Report to FTP Server");
//   	-> lakukan select * , data tersebut diarrange menjadi CSV
//    -> Send ke FTP Server
    System.out.println("7. Exit - Close all Connection");
  }

  public void uploadJSONFileToDB() {
    String fileName = "";
    String line = "";

    System.out.print("Enter the file name: ");
    try {
      fileName = bufferedReader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      line = new BufferedReader(new FileReader(fileName)).readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }

    Object object = null;
    try {
      object = new JSONParser().parse(line);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    JSONObject jsonObject = (JSONObject) object;

    jsonObject.keySet().forEach(key -> {
      JSONObject student = (JSONObject) jsonObject.get(key);

      String fullname = (String) student.get("fullname");
      String address = (String) student.get("address");
      String status = (String) student.get("status");

      Map grades = (Map) student.get("grades");
      int physics = (int) (long) grades.get("physics");
      int calculus = (int) (long) grades.get("calculus");
      int biologi = (int) (long) grades.get("biologi");

      String sql = String.format("INSERT INTO studentsnew (fullname, address, status, gradesphysics, gradescalculus, gradesbiologi) VALUES ('%s', '%s', '%s', '%d', '%d', '%d')", fullname, address, status, physics, calculus, biologi);
      this.studentDataSource.insertStudent(sql);
    });

  }

  public void printDBClientMenu() {
    clearTerminal();
    System.out.println("MENU:");
    System.out.println("1. INSERT");
    System.out.println("2. UPDATE");
    System.out.println("3. DELETE");
    System.out.println("4. Exit");
  }

  public void showInsertMenu() {
    clearTerminal();

    String fullname = "";
    String address = "";
    String status = "";
    int physics = 0;
    int calculus = 0;
    int biologi = 0;

    try {
      System.out.print("Enter the fullname: ");
      fullname = bufferedReader.readLine();
      System.out.print("Enter the address: ");
      address = bufferedReader.readLine();
      System.out.print("Enter the status: ");
      status = bufferedReader.readLine();
      System.out.print("Enter the physics' grade: ");
      physics = Integer.parseInt(bufferedReader.readLine());
      System.out.print("Enter the calculus' grade: ");
      calculus = Integer.parseInt(bufferedReader.readLine());
      System.out.print("Enter the biologi's grade: ");
      biologi = Integer.parseInt(bufferedReader.readLine());

      String sql = String.format("INSERT INTO studentsnew (fullname, address, status, gradesphysics, gradescalculus, gradesbiologi) VALUES ('%s', '%s', '%s', '%d', '%d', '%d')", fullname, address, status, physics, calculus, biologi);
      this.studentDataSource.insertStudent(sql);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void printStudent(int id, String fullname, String address, String status, int physics, int calculus, int biologi) {
    String format = "%-2s  |  %-15s  |  %-13s  |  %-10s  |  %-7d  |  %-8d  |  %-7d  %n";
    System.out.printf(format, id, fullname, address, status, physics, calculus, biologi);
  }

  public void printStudents() {
    ResultSet students = this.studentDataSource.selectAllStudents();

    System.out.println("ID  |     Full Name     |     Address     |    Status    |  Physics  |  Calculus  |  Biologi");

    while (true) {
      try {
        if (!students.next()) break;
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }

      try {
        int id = students.getInt("id");
        String fullname = students.getString("fullname");
        String address = students.getString("address");
        String status = students.getString("status");
        int physics = students.getInt("gradesphysics");
        int calculus = students.getInt("gradescalculus");
        int biologi = students.getInt("gradesbiologi");

        printStudent(id, fullname, address, status, physics, calculus, biologi);

      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }

    }
  }

  public void showUpdateMenu() {
    printStudents();

    int id = 0;
    String fullname = "";
    String address = "";
    String status = "";
    int physics = 0;
    int calculus = 0;
    int biologi = 0;

    try {
      System.out.print("Enter the ID: ");
      id = Integer.parseInt(bufferedReader.readLine());
      System.out.print("Enter the full name: ");
      fullname = bufferedReader.readLine();
      System.out.print("Enter the new address: ");
      address = bufferedReader.readLine();
      System.out.print("Enter the status: ");
      status = bufferedReader.readLine();
      System.out.print("Enter the physics' grade: ");
      physics = Integer.parseInt(bufferedReader.readLine());
      System.out.print("Enter the calculus' grade: ");
      calculus = Integer.parseInt(bufferedReader.readLine());
      System.out.print("Enter the biologi's grade: ");
      biologi = Integer.parseInt(bufferedReader.readLine());
    } catch (IOException e) {
      e.printStackTrace();
    }

    String sql = String.format("UPDATE studentsnew SET fullname='%s', address='%s', status='%s', gradesphysics=%d, gradescalculus=%d, gradesbiologi=%d WHERE id=%d", fullname, address, status, physics, calculus, biologi, id);
    this.studentDataSource.updateStudent(sql);
  }

  public void showDeleteMenu() {
    printStudents();

    int id = 0;

    try {
      System.out.print("Enter the ID: ");
      id = Integer.parseInt(bufferedReader.readLine());
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.studentDataSource.deleteStudentById(id);
  }

  public void showDBClientMenu() {
    boolean isExit = false;
    while (!isExit) {
      pressAnyKeyToContinue();

      printDBClientMenu();
      System.out.print("Enter the menu's number: ");
      String menuNumber = "";

      try {
        menuNumber = bufferedReader.readLine();
      } catch (IOException e) {
        e.printStackTrace();
      }

      switch (menuNumber) {
        case "1":
          showInsertMenu();
          break;
        case "2":
          showUpdateMenu();
          break;
        case "3":
          showDeleteMenu();
          break;
        case "4":
          isExit = true;
          break;
        default:
          System.out.println("Invalid menu's number.");
      }
    }
  }

  public void sendReportToFTPServer() {
    ResultSet students = this.studentDataSource.selectAllStudents();

    JSONObject jsonObject = new JSONObject();

    while (true) {
      try {
        if (!students.next()) break;
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }

      try {
        JSONObject payload = new JSONObject();

        String fullname = students.getString("fullname");
        String name = fullname.split(" ")[0].toLowerCase();
        String address = students.getString("address");
        String status = students.getString("status");
        int physics = students.getInt("gradesphysics");
        int calculus = students.getInt("gradescalculus");
        int biologi = students.getInt("gradesbiologi");

        Map grades = new LinkedHashMap();
        grades.put("physics", physics);
        grades.put("calculus", calculus);
        grades.put("biologi", biologi);

        payload.put("fullname", fullname);
        payload.put("address", address);
        payload.put("status", status);
        payload.put("grades", grades);

        jsonObject.put(name, payload);

      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }

    }

    InputStream line = new ByteArrayInputStream(jsonObject.toJSONString().getBytes());
    this.ftpClientUtil.uploadFile("report.csv", line);
  }

  public void showMenu() {
    String DATABASE_API = properties.getProperty("DATABASE_API");
    String DATABASE = properties.getProperty("DATABASE");
    String DATABASE_HOSTNAME = properties.getProperty("DATABASE_HOSTNAME");
    int DATABASE_PORT = Integer.parseInt(properties.getProperty("DATABASE_PORT"));
    String DATABASE_SCHEMA = properties.getProperty("DATABASE_SCHEMA");
    String DATABASE_USER = properties.getProperty("DATABASE_USER");
    String DATABASE_PASSWORD = properties.getProperty("DATABASE_PASSWORD");

    boolean isExit = false;
    while (!isExit) {
      String menuNumber = "";

      pressAnyKeyToContinue();
      printMenu();

      try {
        System.out.print("Enter the menu's number: ");
        menuNumber = bufferedReader.readLine();
      } catch (IOException e) {
        e.printStackTrace();
      }

      switch (menuNumber) {
        case "1":
          ServerSocketUtil serverSocketUtil = new ServerSocketUtil(properties);
          Thread thread = new Thread(serverSocketUtil);
          thread.start();
          break;
        case "2":
          this.studentDataSource = new StudentDataSource(DATABASE_API, DATABASE, DATABASE_HOSTNAME, DATABASE_PORT, DATABASE_SCHEMA, DATABASE_USER, DATABASE_PASSWORD);
          this.ftpClientUtil = new FTPClientUtil(properties);
          break;
        case "3":
          this.ftpClientUtil.downloadMenu();
          break;
        case "4":
          uploadJSONFileToDB();
          break;
        case "5":
          showDBClientMenu();
          break;
        case "6":
          sendReportToFTPServer();
          break;
        case "7":
          this.ftpClientUtil.close();
          this.studentDataSource.close();
          isExit = true;
          break;
        default:
          System.out.println("Invalid menu number.");
      }
    }
  }
}