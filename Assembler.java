
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;


public class Assembler {


    public static void main(String[] args) {
        int x = 0x283FF;

        System.err.println(Integer.toBinaryString(x));

        

                        

    }

    public static void main2 (String[] args){
        
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
        }
        catch(Exception e){
            System.out.println("Error reading file");
            e.printStackTrace();
        }


    }


    private static void parseLine(String line) {
        line = line.replaceAll(",", "");
        line = line.replaceAll(",", "");
        String[] tokens = line.split(" ");
        switch(tokens[0]){
            case "ADD": //0
                break;
            case "AND": //1
                break;
            case "NAND": //2
                break;
            case "NOR": //3
                break;
            case "ADDI": //4
                break;
            case "ANDI": //5
                break;
            case "LD": //6
                break;
            case "ST": //7
                break;
            case "CMP": //8
                break;
            case "JUMP": //9
                break;
            case "JE": //10
                break;
            case "JA": //11
                break;
            case "JB": //12
                break;
            case "JAE": //13
                break;
            case "JBE": //14
                break;
            default:
                System.out.println("Invalid instruction");
                break;
        }
    }

    

    //This function will convert the binary string to ASCII code
    private String convertStringToASCII(String binaryString) {
        //100000000000000000 => 

        return "";
    }

    
    private static int instruction_JA(String addr){
        int instruction = 0b1011<<14;
        int intAddr = Integer.parseInt(addr);
        
        instruction += intAddr;

        return instruction;
        
    }


    
    
    private static int instruction_ADD(String dst, String src1, String src2){
        
        
        return 0;
        
    }

    private static void PrintToFile(String fileName, int binary){
        try{
            FileOutputStream fw = new FileOutputStream(fileName);
            fw.write(binary);
            
        }catch(Exception e){
            System.out.println("Error writing to file");
            e.printStackTrace();
        }
    }

    /*
    private byte[] stringToBinary(String input) {
        
        byte[] result = new byte[input.length()];

        for (int i = 0; i < input.length(); i++) {
            result[i] = (byte) input.charAt(i);
        }

        return result;
    }
    */
    
    

}