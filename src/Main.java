import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final String DEFAULT_PATH = "spreadsheet.s2v";

    public static void printSpreadsheet(Spreadsheet sheet) {
        Map<Coordinate, Cell> data = sheet.getSpreadsheet();

        if (data.isEmpty()) {
            System.out.println("(Spreadsheet is empty)");
            return;
        }

        // 1) Determine bounds
        int maxRow = 0;
        int maxCol = 0;

        for (Coordinate c : data.keySet()) {
            maxRow = Math.max(maxRow, c.getRow());
            maxCol = Math.max(maxCol, Spreadsheet.toIndex(c.getColumn()));
        }

        // 2) Print column headers
        System.out.print("    "); // row label space
        for (int col = 1; col <= maxCol; col++) {
            System.out.printf("%-10s", Spreadsheet.fromIndex(col));
        }
        System.out.println();

        // 3) Print each row
        for (int row = 1; row <= maxRow; row++) {
            System.out.printf("%-4d", row);

            for (int col = 1; col <= maxCol; col++) {
                Coordinate coord = new Coordinate(Spreadsheet.fromIndex(col), row);
                Cell cell = data.get(coord);

                String content = "";
                if (cell != null) {
                    content = cell.getContent().asString();
                }

                System.out.printf("%-10s", content);
            }
            System.out.println();
        }
    }

    public static void printMenu() {
        System.out.println("\n===== Spreadsheet Menu =====");
        System.out.println("1. Load spreadsheet");
        System.out.println("2. Save spreadsheet");
        System.out.println("3. Set cell content");
        System.out.println("4. Get cell value");
        System.out.println("5. Get cell content");
        System.out.println("6. Print spreadsheet");
        System.out.println("7. Delete cell");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        SpreadsheetStorage storage = new SpreadsheetStorage();
        Spreadsheet sheet = new Spreadsheet();

        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {

                    case "1": // Load
                        try {
                            sheet = storage.load(DEFAULT_PATH);
                            System.out.println("Spreadsheet loaded successfully.");
                        } catch (FileNotFoundException e) {
                            System.out.println("File not found: " + e.getMessage());
                        } catch (IOException e) {
                            System.out.println("Error loading spreadsheet: " + e.getMessage());
                        }
                        break;

                    case "2": // Save
                        try {
                            storage.save(DEFAULT_PATH, sheet);
                            System.out.println("Spreadsheet saved successfully.");
                        } catch (IOException e) {
                            System.out.println("Error saving spreadsheet: " + e.getMessage());
                        }
                        break;
                    case "3": // Set cell content
                        System.out.print("Enter cell (e.g. A1): ");
                        String coord = scanner.nextLine().trim();
                        System.out.print("Enter content (number, text, or formula): ");
                        String content = scanner.nextLine();
                        sheet.setCellContent(coord, content);
                        System.out.println("Cell updated.");
                        break;

                    case "4": // Get cell value
                        System.out.print("Enter cell: ");
                        coord = scanner.nextLine().trim();
                        Double value = sheet.getCellValue(coord);
                        System.out.println("Value of " + coord + " = " + value);
                        break;

                    case "5": // Get cell content
                        System.out.print("Enter cell: ");
                        coord = scanner.nextLine().trim();
                        Cell cell = sheet.getSpreadsheet().get(Coordinate.fromString(coord));
                        if (cell == null) {
                            System.out.println("Cell is empty.");
                        } else {
                            System.out.println("Content of " + coord + " = " +
                                    cell.getContent().asString());
                        }
                        break;

                    case "6": // Print spreadsheet
                        printSpreadsheet(sheet);
                        break;

                    case "0": // Exit
                        running = false;
                        System.out.println("Exiting program.");
                        break;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
