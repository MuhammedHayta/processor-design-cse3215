
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.*;

public class Assembler {

    public static FileOutputStream out;

    private static FileWriter fw;
    private static String dataToWrite = "";
    private static ArrayList<String> instructions = new ArrayList<String>();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the file name: ");
        String fileName = "input";
        // TODO: Uncomment this line to get input from user
        fileName = sc.nextLine();
        sc.close();
        try {

            File file = new File(fileName + ".txt");
            Scanner fileReader = new Scanner(file);
            fw = new FileWriter(fileName + ".hex");

            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                instructions.add(line);
                parseLine(line);
            }
            fileReader.close();
            fw.write("v2.0 raw\n");
            String stringArray[] = dataToWrite.split("\n");
            for (String s : stringArray) {
                printToFile(binaryStringToHexString(s));
            }
            fw.close();

            PrintTest(stringArray);

        } catch (Exception e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
    }

    private static void parseLine(String line) {
        line = line.replaceAll(",", "");
        String[] tokens = line.split(" ");
        switch (tokens[0]) {
            case "ADD": // 0
                dataToWrite += instruction_ADD(tokens[1], tokens[2], tokens[3]) + "\n";
                break;
            case "AND": // 1
                dataToWrite += instruction_AND(tokens[1], tokens[2], tokens[3]) + "\n";
                break;
            case "NAND": // 2
                dataToWrite += instruction_NAND(tokens[1], tokens[2], tokens[3]) + "\n";
                break;
            case "NOR": // 3
                dataToWrite += instruction_NOR(tokens[1], tokens[2], tokens[3]) + "\n";
                break;
            case "ADDI": // 4
                dataToWrite += instruction_ADDI(tokens[1], tokens[2], tokens[3]) + "\n";
                break;
            case "ANDI": // 5
                dataToWrite += instruction_ANDI(tokens[1], tokens[2], tokens[3]) + "\n";
                break;
            case "LD": // 6
                dataToWrite += instruction_LD(tokens[1], tokens[2]) + "\n";
                break;
            case "ST": // 7
                // ST SRC ADDR
                dataToWrite += instruction_ST(tokens[1], tokens[2]) + "\n";
                break;
            case "CMP": // 8
                // CMP OP1 OP2
                dataToWrite += instruction_CMP(tokens[1], tokens[2]) + "\n";
                break;
            case "JUMP": // 9
                // JUMP ADDR
                dataToWrite += instruction_JUMP(tokens[1]) + "\n";
                break;
            case "JE": // 10
                // JE ADDR 
                dataToWrite += instruction_JE(tokens[1]) + "\n";
                break;
            case "JA": // 11
                dataToWrite += instruction_JA(tokens[1]) + "\n";
                break;
            case "JB": // 12
                dataToWrite += instruction_JB(tokens[1]) + "\n";
                break;
            case "JAE": // 13
                dataToWrite += instruction_JAE(tokens[1]) + "\n";
                break;
            case "JBE": // 14
                dataToWrite += instruction_JBE(tokens[1]) + "\n";
                break;
            default:
                System.out.println("Invalid instruction" + tokens[0]);
                break;
        }
    }

    private static String instruction_ADD(String dst, String src1, String src2) {
        String instruction = "0000";
        instruction += convertRegisterStringToBinaryString(dst);
        instruction += convertRegisterStringToBinaryString(src1);
        instruction += "00";
        instruction += convertRegisterStringToBinaryString(src2);
        return instruction;

    }

    private static String instruction_AND(String dst, String src1, String src2) {
        String instruction = "0001";
        instruction += convertRegisterStringToBinaryString(dst);
        instruction += convertRegisterStringToBinaryString(src1);
        instruction += "00";
        instruction += convertRegisterStringToBinaryString(src2);
        return instruction;

    }

    private static String instruction_NAND(String dst, String src1, String src2) {
        String instruction = "0010";
        instruction += convertRegisterStringToBinaryString(dst);
        instruction += convertRegisterStringToBinaryString(src1);
        instruction += "00";
        instruction += convertRegisterStringToBinaryString(src2);
        return instruction;
    }

    // 3. NOR DST SRC1 SRC2
    private static String instruction_NOR(String dst, String src1, String src2) {
        String instruction = "0011";
        instruction += convertRegisterStringToBinaryString(dst);
        instruction += convertRegisterStringToBinaryString(src1);
        instruction += "00";
        instruction += convertRegisterStringToBinaryString(src2);
        return instruction;

    }

    // 4. ADDI DST SRC1 IMM
    private static String instruction_ADDI(String dst, String src1, String imm) {
        String instruction = "0100";
        instruction += convertRegisterStringToBinaryString(dst);
        instruction += convertRegisterStringToBinaryString(src1);
        instruction += convertImmediateToBinary(imm);
        return instruction;

    }

    // 5. ANDI DST SRC1 IMM
    private static String instruction_ANDI(String dst, String src1, String imm) {
        String instruction = "0101";
        instruction += convertRegisterStringToBinaryString(dst);
        instruction += convertRegisterStringToBinaryString(src1);
        instruction += convertImmediateToBinary(imm);
        return instruction;

    }

    // 6. LD DST ADDR
    private static String instruction_LD(String dst, String addr) {
        String instruction = "0110";
        instruction += convertRegisterStringToBinaryString(dst);
        instruction += convertAddressStringToBinary(addr);
        return instruction;

    }

    // 7. ST SRC ADDR
    private static String instruction_ST(String src, String addr) {
        String instructionBinaryString = "0111";
        instructionBinaryString += convertRegisterStringToBinaryString(src);
        instructionBinaryString += convertAddressStringToBinary(addr);

        return instructionBinaryString;
    }

    // 8. CMP OP1 OP2
    private static String instruction_CMP(String op1, String op2) {
        String instructionBinaryString = "1000";
        instructionBinaryString += convertRegisterStringToBinaryString(op1);
        instructionBinaryString += "000000";
        instructionBinaryString += convertRegisterStringToBinaryString(op2);

        return instructionBinaryString;
    }

    // 9. JUMP ADDR
    private static String instruction_JUMP(String addr) {
        String instructionBinaryString = "1001";
        instructionBinaryString += "0000";
        instructionBinaryString += convertAddressStringToBinary(addr);

        return instructionBinaryString;
    }

    // 10. JE ADDR
    private static String instruction_JE(String addr) {
        String instructionBinaryString = "1010";
        instructionBinaryString += "0000";
        instructionBinaryString += convertAddressStringToBinary(addr);

        return instructionBinaryString;
    }

    private static String instruction_JA(String addr) {
        String instruction = "10110000";
        instruction += convertAddressStringToBinary(addr);
        return instruction;
    }

    private static String instruction_JB(String addr) {
        String instruction = "11000000";
        instruction += convertAddressStringToBinary(addr);
        return instruction;

    }

    private static String instruction_JAE(String addr) {
        String instruction = "11010000";
        instruction += convertAddressStringToBinary(addr);
        return instruction;

    }

    private static String instruction_JBE(String addr) {
        String instruction = "11100000";
        instruction += convertAddressStringToBinary(addr);
        return instruction;

    }

    private static String convertAddressStringToBinary(String addr) {
        int intAddr = Integer.parseInt(addr);

        if (intAddr < -512 || intAddr > 511) {
            System.out.println("Invalid address " + addr);
            return null;
        }

        String binaryString = Integer.toBinaryString(intAddr);
        if (intAddr >= 0) {
            while (binaryString.length() < 10) {
                binaryString = "0" + binaryString;
            }
        }
        return binaryString.substring(binaryString.length() - 10, binaryString.length());
    }

    private static String convertImmediateToBinary(String immediate) {
        int intImmediate = Integer.parseInt(immediate);

        if (intImmediate < -32 || intImmediate > 31) {
            System.out.println("Invalid immediate value: " + immediate);
            System.exit(0);
        }

        String binaryString = Integer.toBinaryString(intImmediate);
        if (intImmediate >= 0) {
            while (binaryString.length() < 6) {
                binaryString = "0" + binaryString;
            }
        }
        return binaryString.substring(binaryString.length() - 6, binaryString.length());
    }

    private static String binaryStringToHexString(String binaryString) {
        binaryString = "00" + binaryString;
        String tempString = "";
        String hexStr = "";
        for (int i = binaryString.length() - 1; i >= 0; i -= 4) {
            tempString = "";
            tempString += binaryString.charAt(i - 3) + "" + binaryString.charAt(i - 2) + "" + binaryString.charAt(i - 1)
                    + "" + binaryString.charAt(i) + "";
            hexStr = fourBitToHex(tempString) + hexStr;
        }

        return hexStr;
    }

    private static String fourBitToHex(String fourBit) {
        switch (fourBit) {
            case "0000":
                return "0";
            case "0001":
                return "1";
            case "0010":
                return "2";
            case "0011":
                return "3";
            case "0100":
                return "4";
            case "0101":
                return "5";
            case "0110":
                return "6";
            case "0111":
                return "7";
            case "1000":
                return "8";
            case "1001":
                return "9";
            case "1010":
                return "A";
            case "1011":
                return "B";
            case "1100":
                return "C";
            case "1101":
                return "D";
            case "1110":
                return "E";
            case "1111":
                return "F";
            default:
                System.out.println("Invalid binary string");
                System.exit(0);
                break;
        }
        return "";
    }

    private static void printToFile(String binaryString) {
        try {

            fw.write(binaryString);
            fw.write("\n");

        } catch (Exception e) {
            System.out.println("Error writing to file");
            e.printStackTrace();
        }
    }

    private static String convertRegisterStringToBinaryString(String registerString) {

        if (registerString.charAt(0) != 'R') {
            System.out.println("Invalid register: " + registerString);
            System.exit(0);
        }

        registerString = registerString.substring(1, registerString.length());

        try {
            int register = Integer.parseInt(registerString);

            if (register < 0 || register > 15) {
                System.out.println("Invalid register: " + registerString);
                System.exit(0);
            }

            String binary = Integer.toBinaryString(register);
            while (binary.length() < 4) {
                binary = "0" + binary;
            }

            return binary;

        } catch (Exception e) {
            System.out.println("Invalid register: " + registerString);
            System.exit(0);
        }

        return "";
    }

    // Test function to print binary string
    private static void PrintTest(String[] StringArray) {
        for (int i = 0; i < StringArray.length; i++) {
            System.out.println(
                    "INSTRUCTION: " + instructions.get(i) + " BINARY: " + binaryWithSpace(StringArray[i]) + " HEX: "
                            + binaryStringToHexString(StringArray[i]));
        }
    }

    private static String binaryWithSpace(String binaryString) {
        binaryString = "00" + binaryString;
        String tempString = "";
        for (int i = 0; i < binaryString.length(); i++) {
            tempString += binaryString.charAt(i);
            if (i % 4 == 3) {
                tempString += " ";
            }
        }
        return tempString;
    }

}