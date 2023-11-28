
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.*;

public class Assembler {

    public static FileOutputStream out;

    public static void main(String[] args) {
        int x[] = { 0x283FF, 0x3FFFF, 0x22222 };
        try {
            out = new FileOutputStream("test.hex");

            // Kodun devamı buradan.

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static byte[] IntToByteArray(int data) {
        byte[] result = new byte[3];
        result[0] = (byte) ((data & 0xF0000) >> 16);
        result[1] = (byte) ((data & 0x0FF00) >> 8);
        result[2] = (byte) ((data & 0x000FF) >> 0);
        return result;
    }

    public static void main2(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the file name: ");

        String fileName = sc.nextLine();

        try {

            File file = new File(fileName);
            Scanner fileReader = new Scanner(file);

            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                parseLine(line);
            }
        } catch (Exception e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }

    }

    private static void parseLine(String line) {
        line = line.replaceAll(",", "");
        line = line.replaceAll(",", "");
        String[] tokens = line.split(" ");
        switch (tokens[0]) {
            case "ADD": // 0
                break;
            case "AND": // 1
                break;
            case "NAND": // 2
                break;
            case "NOR": // 3
                break;
            case "ADDI": // 4
                break;
            case "ANDI": // 5
                break;
            case "LD": // 6
                break;
            case "ST": // 7
                break;
            case "CMP": // 8
                break;
            case "JUMP": // 9
                break;
            case "JE": // 10
                break;
            case "JA": // 11
                break;
            case "JB": // 12
                break;
            case "JAE": // 13
                break;
            case "JBE": // 14
                break;
            default:
                System.out.println("Invalid instruction");
                break;
        }
    }

    // This function will convert the binary string to ASCII code
    private String convertStringToASCII(String binaryString) {
        // 100000000000000000 =>

        return "";
    }

    private static String instruction_JA(String addr) {

        int instruction = 0b1011 << 14;
        int intAddr = Integer.parseInt(addr);

        if (intAddr < 0 || intAddr > 0x3FF) {
            System.out.println("Invalid address");
            return null;
        }
        instruction += intAddr;
        return Integer.toBinaryString(instruction);

    }

    private static String instruction_JB(String addr) {

        int instruction = 0b1100 << 14;
        int intAddr = Integer.parseInt(addr);

        if (intAddr < 0 || intAddr > 0x3FF) {
            System.out.println("Invalid address");
            return null;
        }
        instruction += intAddr;
        return Integer.toBinaryString(instruction);

    }

    private static String instruction_JAE(String addr) {

        int instruction = 0b1101 << 14;
        int intAddr = Integer.parseInt(addr);

        if (intAddr < 0 || intAddr > 0x3FF) {
            System.out.println("Invalid address");
            return null;
        }
        instruction += intAddr;
        return Integer.toBinaryString(instruction);

    }

    private static String instruction_JBE(String addr) {

        int instruction = 0b1110 << 14;
        int intAddr = Integer.parseInt(addr);

        if (intAddr < 0 || intAddr > 0x3FF) {
            System.out.println("Invalid address");
            return null;
        }
        instruction += intAddr;
        return Integer.toBinaryString(instruction);

    }

    private static int instruction_ADD(String dst, String src1, String src2) {

        return 0;

    }

    private static void writeFile(int instruction) {
        byte b[] = IntToByteArray(instruction);
        out.write(b);

    }

    private static void PrintToFile(String fileName, int binary) {
        try {
            FileOutputStream fw = new FileOutputStream(fileName);
            fw.write(binary);

        } catch (Exception e) {
            System.out.println("Error writing to file");
            e.printStackTrace();
        }
    }

    /*
     * private byte[] stringToBinary(String input) {
     * 
     * byte[] result = new byte[input.length()];
     * 
     * for (int i = 0; i < input.length(); i++) {
     * result[i] = (byte) input.charAt(i);
     * }
     * 
     * return result;
     * }
     */

}