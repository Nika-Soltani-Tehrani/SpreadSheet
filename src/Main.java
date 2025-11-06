public class Main {
    public static void main(String[] args) {
        Spreadsheet sheet = new Spreadsheet();

        // 1️⃣ Test numeric content
        sheet.setCellContent("A1", "123.5");
        System.out.println("A1 value: " + sheet.getCellValue("A1"));

        // 2️⃣ Test text content (numeric string → should return 1.0)
        sheet.setCellContent("A2", "1");
        System.out.println("A2 value: " + sheet.getCellValue("A2"));

        // 3️⃣ Test text content (non-numeric)
        sheet.setCellContent("B1", "TOTAL");
        try {
            System.out.println("B1 value: " + sheet.getCellValue("B1"));
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error for B1: " + e.getMessage());
        }

        // 4️⃣ Test formula content (dummy formula for now)
        sheet.setCellContent("C1", "=A1+A2");
        System.out.println("C1 formula as text: " + sheet.getCellAsString("C1"));
        System.out.println("C1 computed value (placeholder): " + sheet.getCellValue("C1"));
    }
}
