
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.*;

public class Assembler {

    public static FileOutputStream out;

    private static FileWriter fw;
    private static String dataToWrite = "";

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the file name: ");

        String fileName = sc.nextLine();
        
        try {

            File file = new File(fileName);
            Scanner fileReader = new Scanner(file);
        
        
            while(fileReader.hasNextLine()){
                String line = fileReader.nextLine();
                parseLine(line);
            }

            printToFile("output.hex", dataToWrite);
        }
        catch(Exception e){
            System.out.println("Error reading file");
            e.printStackTrace();
        }
    }



    private static void parseLine(String line) {
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
            case "ST": //7
                //ST SRC ADDR  
                dataToWrite +=  instruction_ST(tokens[1], tokens[2])  + "\n";
                break;
            case "CMP": //8
                //CMP OP1 OP2
                dataToWrite +=  instruction_CMP(tokens[1], tokens[2])  + "\n";
                break;
            case "JUMP": //9
                //JUMP ADDR  
                dataToWrite +=  instruction_JUMP(tokens[1])  + "\n";
                break;
            case "JE": //10
                //JE ADDR 
                dataToWrite +=  instruction_JE(tokens[1])  + "\n";
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
    
    private static String instruction_ADD(String dst, String src1, String src2){
        String instruction = "0000";
        instruction += convertRegisterStringToBinaryString(dst);
        instruction += convertRegisterStringToBinaryString(src1);
        instruction += "00";
        instruction += convertRegisterStringToBinaryString(src2);
        return instruction;
        
    }

    private static String instruction_AND(String dst, String src1, String src2){
        String instruction = "0001";
        instruction += convertRegisterStringToBinaryString(dst);
        instruction += convertRegisterStringToBinaryString(src1);
        instruction += "00";
        instruction += convertRegisterStringToBinaryString(src2);
        return instruction;
        
    }

    private static String instruction_NAND(String dst, String src1, String src2){
        String instruction = "0010";
        instruction += convertRegisterStringToBinaryString(dst);
        instruction += convertRegisterStringToBinaryString(src1);
        instruction += "00";
        instruction += convertRegisterStringToBinaryString(src2);
        return instruction;
        
    }
     


    // 7. ST SRC ADDR  
    private static String instruction_ST(String src, String addr) {
        String instructionBinaryString = "0111";
        instructionBinaryString += convertRegisterStringToBinaryString(src);
        instructionBinaryString += convertAdressStringToBinary(addr);

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
        instructionBinaryString += convertAdressStringToBinary(addr);

        return instructionBinaryString;
    }

    // 10. JE ADDR
    private static String instruction_JE(String addr) {
        String instructionBinaryString = "1010";
        instructionBinaryString += "0000";
        instructionBinaryString += convertAdressStringToBinary(addr);

        return instructionBinaryString;
    }

    private static String binaryStringToHexString(String binaryString) {
        int decimal = Integer.parseInt(binaryString, 2);
        String hexStr = Integer.toHexString(decimal);
        hexStr = hexStr.toUpperCase();
        //String hexStr = Integer.toString(decimal, 16);
        return hexStr;
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

        if(registerString.charAt(0) != 'R'){
            System.out.println("Invalid register: " + registerString);
            System.exit(0);
        }

        registerString = registerString.substring(1, registerString.length());

        try {
            int register = Integer.parseInt(registerString);

            if(register < 0 || register > 15){
                System.out.println("Invalid register: " + registerString);
                System.exit(0);
            }

            String binary = Integer.toBinaryString(register);
            while(binary.length() < 4){
                binary = "0" + binary;
            }


            return binary;


        } catch (Exception e) { //If the string afte "R" is not a number, then it will throw an exception
            System.out.println("Invalid register: " + registerString);
            System.exit(0);
        }


        return "";
    }

  
    

}