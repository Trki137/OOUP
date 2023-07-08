package hr.fer.zemris.ooup;

public class Main {
    public static void main(String[] args) {
        Sheet sheet = new Sheet(5,5);
        sheet.print();

        sheet.set("A1","2");
        sheet.set("A2","3");
        sheet.set("A3", "A1+A2");

        sheet.set("A1", "-2");

        sheet.set("A4", "A1+15");
        sheet.set("A5", "A4+A3");

        sheet.print();

        System.out.println("=".repeat(20));

        sheet.set("A1","12");
        sheet.print();

        sheet.set("A1", "A4+3");
    }
}