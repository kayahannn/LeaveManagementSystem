import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    private static final String NUMBER_REGEX = "\\d{10}";
    private static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER_REGEX);

    public static boolean isValidEGN(String number) {
        Matcher matcher = NUMBER_PATTERN.matcher(number);
        return matcher.matches();
    }

    public static void writeToFile(ArrayList userInfo, int uniNumber) throws IOException {
        FileWriter fileWriter = new FileWriter("DB_Leave_request.txt", true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.newLine();
        for (int i = 0; i < userInfo.size(); i++) {
            bufferedWriter.write(userInfo.get(i).toString() + ",");
        }
        bufferedWriter.write(String.valueOf(uniNumber) + ",");
        bufferedWriter.write("Pending");
        bufferedWriter.close();
    }

    public static int generateUniNumber() {
        int uniNumber = 123;

        return uniNumber;
    }

    public static ArrayList<String[]> readFromFile(BufferedReader buffer) throws IOException {
        ArrayList<String[]> arrayList = new ArrayList<>();
        String line;
        while ((line = buffer.readLine()) != null) {
            String[] getData = line.split("[,]+");
            arrayList.add(getData);
        }
        return arrayList;
    }

    public static void print(ArrayList<String[]> arrayList) {
        System.out.printf("|%-10s|%-20s|%-12s|%-8s|%n", "Name", "E-mail", "Start Date", "End Date");
        for (int i = 0; i < arrayList.size(); i++) {
            String[] row = arrayList.get(i);
            System.out.printf("|%-10s|%-20s|%-12s|%-8s|%n", row[0], row[1], row[2], row[3]);
        }
    }

    public static BufferedReader readFile() throws IOException {
        FileReader fileReader = new FileReader("DB_Leave_request.txt");
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
                    ArrayList<String> inputData = new ArrayList<>();
                    System.out.println("Enter your name: ");
                    String name = input.next();
                    inputData.add(name);
                    String mail;
                    do {
                        System.out.println("Enter email address: ");
                        mail = input.next();
                        System.out.println("Invalid email address!");
                    }
                    while (!isValidEmail(mail));
                    inputData.add(mail);
                    String egn;
                    do {
                        System.out.println("Enter your EGN: ");
                        egn = input.next();

                    } while (!isValidEGN(egn));
                    inputData.add(egn);
                    System.out.println("Enter start date: ");
                    String startDate = input.next();
                    inputData.add(startDate);
                    System.out.println("Enter end date: ");
                    String endDate = input.next();
                    inputData.add(endDate);
                    System.out.println("Enter Leave type:");
                    String leaveType = input.next();
                    inputData.add(leaveType);
                    System.out.println(inputData);
                    try {
                        writeToFile(inputData, generateUniNumber());
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                    //task1
                    break;
                case 2:
                    //task2
                    //Print all Leave requests;
                    try {
                        print(readFromFile(readFile()));
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
                        print(getLeaveRecordsByName(name, readFile()));
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