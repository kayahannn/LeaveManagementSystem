import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
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

    public static boolean isValidDate(String value, Locale locale) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", locale);
        try {
            LocalDate date = LocalDate.parse(value, dateFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isDate2AfterDate1(LocalDate date1, LocalDate date2) {

        return date2.isAfter(date1);
    }

    public static void writeToFile(ArrayList userInfo) throws IOException {
        FileWriter fileWriter = new FileWriter("DB_Leave_request.csv", true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.newLine();

        for (int i = 0; i < userInfo.size(); i++) {
            bufferedWriter.write(userInfo.get(i).toString() + ",");
        }

        bufferedWriter.write("Pending");
        bufferedWriter.close();
    }

    public static ArrayList<String[]> readFromFile(BufferedReader bufferedReader) throws IOException {
        ArrayList<String[]> arrayList = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] getData = line.split(",");
            arrayList.add(getData);
        }
        return arrayList;
    }

    public static void printTable(ArrayList<String[]> arrayList) {
        System.out.printf("|%-10s|%-10s|%-10s|%-20s|%-12s|%-12s|%-12s|%-10s|%-10s|%n",
                "Number", "Name", "Surname", "E-mail", "EGN", "Start Date", "End Date", "Leave Type", "Status");
        for (int i = 0; i < arrayList.size(); i++) {
            String[] row = arrayList.get(i);
            System.out.printf("|%-10s|%-10s|%-10s|%-20s|%-12s|%-12s|%-12s|%-10s|%-10s|%n", row[6], row[0], row[1], row[2], row[3], row[4], row[5], row[7], row[8]);
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

    public static String inputLeaveType(Scanner input) {
        System.out.println("Enter Leave type");
        System.out.println("1: Paid");
        System.out.println("2: Unpaid");
        String leaveType;
        leaveType = input.next();

        if (leaveType.equalsIgnoreCase("1")) {
            leaveType = "Paid";
        } else if (leaveType.equalsIgnoreCase("2")) {
            leaveType = "Unpaid";
        } else {
            System.out.println("wrong choice!");
            leaveType = inputLeaveType(input);
        }

        return leaveType;
    }

    public static String inputEgn(Scanner input) {
        String egn;

        do {
            System.out.println("Enter your EGN : ");
            egn = input.next();
        } while (!isValidEGN(egn));

        return egn;
    }

    public static String inputStartDate(Scanner input) {
        String startDate;
        boolean isValidDate = false;

        do {
            System.out.println("Enter start date (dd.MM.yyyy): ");
            startDate = input.next();
            isValidDate = isValidDate(startDate, Locale.ENGLISH);

            if (!isValidDate) {
                System.out.println("Invalid start date format. Please enter dd.MM.yyyy format!");
            }
        }
        while (!isValidDate(startDate, Locale.ENGLISH));

        return startDate;
    }

    public static String inputEndDate(Scanner input, String startDate) {
        String endDate;
        boolean isValidDate = false;
        do {
            System.out.println("Enter end date (dd.MM.yyyy): ");
            endDate = input.next();

            isValidDate = isValidDate(endDate, Locale.ENGLISH);
            if (!isValidDate) {
                System.out.println("Invalid input. Please use dd.MM.yyyy format!");
            }
            // add check if Date2 is before Date1!
        } while (!isValidDate(endDate, Locale.ENGLISH) && isDate2AfterDate1(LocalDate.parse(startDate), LocalDate.parse(endDate)));

        return endDate;
    }

    public static String inputEmailAddress(Scanner input) {
        String mail;

        do {
            System.out.println("Enter email address: ");
            mail = input.next();
            boolean isValidEmail = isValidEmail(mail);

            if (!isValidEmail) {
                System.out.println("Invalid input. Please enter correct email address!");
            }
        }
        while (!isValidEmail(mail));

        return mail;
    }
public static boolean approveLeaveRecord(Scanner input){
    System.out.println("Approve/Reject Leave: ");
    System.out.println("1: Approve");
    System.out.println("2: Reject");
    String leaveStatus = input.next();

    if (leaveStatus.equalsIgnoreCase("1")) {
        return true;
    } else if (leaveStatus.equalsIgnoreCase("2")) {
        return false;
    } else {
        System.out.println("wrong choice!");
        approveLeaveRecord(input);
    }
    return false;
}
    public static void changeLeaveStatus(int number, boolean leaveStatus) throws IOException {
        List<String[]> lines = new ArrayList<>();
        String line;
        boolean recordExist = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("DB_Leave_request.csv"))) {

            while ((line = reader.readLine()) != null) {
                String[] getData = line.split(",");

                if (getData[6].equalsIgnoreCase(String.valueOf(number))) {
                    recordExist = true;
                    if (leaveStatus) {
                        getData[8] = "Approved";
                    } else {
                        getData[8] = "Rejected";
                    }
                }
                lines.add(getData);
            }
        }
        if(!recordExist){
            System.out.println("Record doesnt exist!");
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("DB_Leave_request.csv"))) {
            for (int i = 0; i < lines.size(); i++) {
                String[] lineData = lines.get(i);
                writer.write(String.join(",", lineData));
                writer.newLine();
            }
        }
    }

    public static int printTableByNumber() throws FileNotFoundException {
        int number = 0;
        try {
            File file = new File("UniqueID.csv");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextInt()) {
                number = scanner.nextInt();
            }
            scanner.close();
            number++;
            FileWriter writer = new FileWriter("UniqueID.csv");
            writer.write(String.valueOf(number));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return number;
    }

    public static void printMenu(Scanner input) {
        boolean quit = false;
        String fileDB = "DB_Leave_request.csv";
        while (!quit) {
            System.out.println("Menu:\n 1. Заяви отпуск\n 2. Виж всички отпуски\n 3. Виж отпуск за служител\n 4. Промени статус на отпуск\n 5. Изход\n");
            System.out.print("Въведи избор: ");
            try {
                byte menu = input.nextByte();
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

                        inputData.add(inputEmailAddress(input));
                        inputData.add(inputEgn(input));
                        String startDate = inputStartDate(input);
                        inputData.add(startDate);
                        inputData.add(inputEndDate(input, startDate));
                        try {
                            inputData.add(String.valueOf(printTableByNumber()));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        inputData.add(inputLeaveType(input));
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
                            printTable(readFromFile(readFile(fileDB)));
                        } catch (IOException ex) {
                            System.out.println(ex);
                        }
                        break;
                    case 3:
                        //task3
                        //Print requests by Name;
                        System.out.printf("Enter name: ");
                        name = input.next();
                        try {
                            printTable(getLeaveRecordsByName(name, readFile(fileDB)));
                        } catch (IOException ex) {
                        }
                        break;
                    case 4:
                        //task4
                        //Change status of Leave request by ID;
                        try {
                            printTable(readFromFile(readFile(fileDB)));
                        } catch (IOException ex) {
                            System.out.println(ex);
                        }
                        System.out.println("Enter number: ");
                        int num = input.nextInt();

                        try {
                            changeLeaveStatus(num,approveLeaveRecord(input));
                        } catch (IOException e) {

                        }
                        break;
                    case 6:
                        quit = true;
                        System.out.println("Thanks for using me!");
                        break;
                    default:
                        System.out.println("Wrong input!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input! Please enter number. \n");
                input.nextLine();
            }
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        printMenu(input);
    }
}
