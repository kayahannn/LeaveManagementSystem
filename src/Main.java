import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    //validating email address
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    // validating ENG, 10 digit
    private static final String NUMBER_REGEX = "\\d{10}";
    private static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER_REGEX);

    public static boolean isValidEGN(String number) {
        Matcher matcher = NUMBER_PATTERN.matcher(number);
        return matcher.matches();
    }

//    public static boolean isDate(String value, Locale locale) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyy", locale);
//        dateFormat.setLenient(false);
//        try {
//            Date date = dateFormat.parse(value);
//            return true;
//        } catch (ParseException e) {
//            return false;
//        }
//    }
    public static boolean isDate(String value, Locale locale) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy", locale);
        try {
            LocalDate date = LocalDate.parse(value, dateFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    public static void writeToFile(ArrayList userInfo) throws IOException {
        FileWriter fileWriter = new FileWriter("DB_Leave_request.txt", true);
        FileWriter fileWriterUid = new FileWriter("UniqueID.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        BufferedWriter bufferedWriter1 = new BufferedWriter(fileWriterUid);
        bufferedWriter.newLine();
        for (int i = 0; i < userInfo.size(); i++) {
            bufferedWriter.write(userInfo.get(i).toString() + ",");
        }
//        bufferedWriter.write(String.valueOf(nextId()) + ",");
        bufferedWriter.write("Pending");
        bufferedWriter.close();
//        bufferedWriter1.write(String.valueOf(nextId()));
        bufferedWriter1.close();
    }

    //Generate ID

//    public static synchronized int nextId() throws IOException {
//        int counter = getLastUniqueID();
//        return ++counter;
//    }

//    public static int getLastUniqueID() throws IOException {
//        File file = new File("UniqueID.txt");
//        Scanner input = new Scanner(file);
//        String line;
//        while (input.hasNext()) {
//            line = input.next();
//        }
//        return Integer.parseInt(line);
//    }

    public static ArrayList<String[]> readFromFile(BufferedReader bufferedReader) throws IOException {
        ArrayList<String[]> arrayList = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] getData = line.split("[,]+");
            arrayList.add(getData);
        }
        return arrayList;
    }

    public static void print(ArrayList<String[]> arrayList) {
        System.out.printf("|%-10s|%-10s|%-10s|%-20s|%-12s|%-12s|%-12s|%-12s|%-8s|%n",
                "UniNumber", "Name", "Surname", "E-mail", "EGN", "Start Date", "End Date", "Leave Type", "Status");
        for (int i = 0; i < arrayList.size(); i++) {
            String[] row = arrayList.get(i);
            System.out.printf("|%-10s|%-10s|%-10s|%-20s|%-12s|%-12s|%-12s|%-12s|%-8s|%n", row[6], row[0], row[0], row[1], row[2], row[3], row[4], row[5], row[7]);
        }
    }

    public static BufferedReader readFile(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        return bufferedReader;
    }

    public static ArrayList<String[]> getLeaveRecordsByName(String name, BufferedReader bufferedReader) throws IOException {
        ArrayList<String[]> arrayList = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] getData = line.split(",");
            if (getData[0].equalsIgnoreCase(name)) {
                arrayList.add(getData);
            }
        }
        return arrayList;
    }

    public static String getLeaveType(Scanner input) {
        String leaveType;
        System.out.println("Enter Leave type");
        System.out.println("1: Paid");
        System.out.println("2: Unpaid");
        leaveType = input.next();
        if (leaveType.equalsIgnoreCase("1")) {
            leaveType = "Paid";
        } else if (leaveType.equalsIgnoreCase("2")) {
            leaveType = "Unpaid";
        } else {
            System.out.println("wrong choice!");
            getLeaveType(input);
        }
        return leaveType;
    }

    public static String getEgn(Scanner input) {
        String egn;
        do {
            System.out.println("Enter your EGN : ");
            egn = input.next();
        } while (!isValidEGN(egn));
        return egn;
    }

    public static String getStartDate(Scanner input) {
        String startDate;
        do {
            System.out.println("Enter start date (dd.MM.yyyy): ");
            startDate = input.next();
        }
        while (!isDate(startDate, Locale.ENGLISH));
        return startDate;
    }

    public static String getEndDate(Scanner input) {
        String endDate;
        do {
            System.out.println("Enter end date (dd.MM.yyyy): ");
            endDate = input.next();
        } while (!isDate(endDate, Locale.ENGLISH));

        return endDate;
    }

    public static String getEmailAddr(Scanner input) {
        String mail;
        do {
            System.out.println("Enter email address: ");
            mail = input.next();
        }
        while (!isValidEmail(mail));
        return mail;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean quit = false;
        while (!quit) {
            System.out.println("Menu:\n 1. Заяви отпуск\n 2. Виж всички отпуски\n 3. Виж отпуск за служител\n 4. Промени статус на отпуск\n 5. Изход\n");
            System.out.print("Въведи избор: ");
            int menu = input.nextInt();
            switch (menu) {
                case 1:
                    //Verifications need to be added!
                    //UID for each request;
                    //task1
                    ArrayList<String> inputData = new ArrayList<>();
                    System.out.println("Enter your Name: ");
                    String name = input.next();
                    inputData.add(name);
                    System.out.println("Enter your Surname: ");
                    String surName = input.next();
                    inputData.add(surName);
                    inputData.add(getEmailAddr(input));
                    inputData.add(getEgn(input));
                    inputData.add(getStartDate(input));
                    inputData.add(getEndDate(input));
                    inputData.add(getLeaveType(input));
                    System.out.println(inputData);
                    try {
                        writeToFile(inputData);
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                    break;
                case 2:
                    //task2
                    //Print all Leave requests;
                    try {
                        print(readFromFile(readFile("DB_Leave_request.txt")));
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                    break;
                case 3:
                    //task3
                    //Print requests by Name;
                    System.out.printf("Enter Name: ");
                    name = input.next();
                    try {
                        print(getLeaveRecordsByName(name, readFile("DB_Leave_request.txt")));
                    } catch (IOException ex) {
                    }
                    break;
                case 4:
                    //task4
                    //Change status of Leave request by UID;
                    //
                    break;
                case 6:
                    quit = true;
                    System.out.println("Thanks for using me!");
                    break;
                default:
                    System.out.println("Wrong input!");
            }
        }
    }
}