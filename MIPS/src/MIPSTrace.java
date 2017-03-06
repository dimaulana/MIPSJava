import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Dido Maulana
 * @author Greg Berezny
 */
public class MIPSTrace {
	
	
	/**
	 * Main to run the trace.
	 */
	public static void main(String args[]) {   
		MIPSTrace mips = new MIPSTrace();
		
		try {
			mips.startTrace();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	String opcode;
	int op;
	int RegDST, ALUOp1, ALUOp0, ALUSrc, Branch, MemRead, MemWrite, RegWrite, MemtoReg;
	
	
	private Map<Integer, Integer> registers;
	private Map<Integer, Integer> memory;
	
	public void startTrace() throws Exception {
		
		registers = new HashMap<Integer, Integer>();
		memory = new HashMap<Integer, Integer>();
		
		Scanner in = new Scanner(System.in);
		
		String[] instruction = in.nextLine().split(" ");
		opcode = instruction[0];

		getIn(instruction);
		in.close();
		
		int[] IFBuffer = InformationFetch(instruction, 0);
		
		System.out.print("IF/ID Buffer" + " : [ ");
		for (int i : IFBuffer) {
			System.out.print(i + " ");
		}
		System.out.println("]");
		
		
		
		int[] IDBuffer = InformationDecode(IFBuffer);
		
		System.out.print("ID/EX Buffer" + " : [");
		for (int i : IDBuffer) {
			System.out.print(i + " ");
		}
		System.out.println("]");
	
	
		int[] EXBuffer = InformationExecute(IDBuffer);
		
		System.out.print("EX/MEM Buffer" + " : [");
		for (int i : EXBuffer) {
			System.out.print(i + " ");
		}
		System.out.println("]");
	}
	
	private int[] InformationFetch(String[] instruction, int PC) throws Exception {
		
		PC += 4;
		
		int operand1;
		int operand2;
		int operand3;
		
		try {
			opcode = instruction[0];
			operand1 = Integer.parseInt(instruction[1]);
			operand2 = Integer.parseInt(instruction[2]);
			operand3 = Integer.parseInt(instruction[3]);
		} catch (Exception ex) {
			throw new Exception("Invalid Input");
		}
		
		if (opcode.equals("add")) {
			System.out.println("Semantics: $" + operand1 + " <- $" + operand2 + " + $" + operand3);
			return new int[]{ 0, operand2, operand3, operand1, 0, 32, PC };
		}
		else if (opcode.equals("sub")) {
			System.out.println("Semantics: $" + operand1 + " <- $" + operand2 + " - $" + operand3);
			return new int[]{ 0, operand2, operand3, operand1, 0, 34, PC };
		}
		else if (opcode.equals("lw")) {
			System.out.println("Semantics: $" + operand1 + " <- M[$" + operand3 + " + " + operand2 + "]");
			return new int[]{ 35, operand3, operand1, operand2, PC };
		}
		else if (opcode.equals("sw")) {
			System.out.println("Semantics: " +  "M[$" + operand3 + " + " + operand2 + "]" + " <- " + operand1);
			return new int[]{ 43, operand3, operand1, operand2, PC };
		}
		else if (opcode.equals("beq")) {
			System.out.println("Semantics: " +  "IF ($" + operand1 + " == $" + operand2 + ") THEN PC <- 4 + 4 * " + operand3);
			return new int[]{ 4, operand1, operand2, operand3, PC };
		}
		else {
			throw new Exception("Invalid Opcode");
		}
	}
	
	private int[] InformationDecode(int[] IFBuffer) throws Exception {
		
		int PC = -1;
		int destinationRegister = -1;
		if (IFBuffer[0] == 0) {
			destinationRegister = IFBuffer[3];
			PC = IFBuffer[6];
		}
		else {
			PC = IFBuffer[4];
		}
		
		int op = IFBuffer[0];
		String opcode = op + "";
		if (op == 0) {
			opcode += "-"+IFBuffer[5];
		}
		
		System.out.println("Opcode : " + opcode);
		System.out.println("Control Vector : [ Reg-Dst ALU-Op1 ALU-Op0 ALU-Src Branch Mem-Read Mem-Write Reg-Write Mem-to-Reg ]");
		
		if (op == 0) {
			System.out.println("Control Vector : [ 1 1 0 0 0 0 0 1 0 ]");
			RegDST = 1;
			ALUOp1 = 1;
			ALUOp0 = 0;
			ALUSrc = 0;
			Branch = 0;
			MemRead = 0;
			MemWrite = 0;
			RegWrite = 1;
			MemtoReg = 0;
			opcode += IFBuffer[5];
		}
		else if (op == 35) {
			System.out.println("Control Vector : [ 0 0 0 1 0 1 0 1 1 ]");
			RegDST = 0;
			ALUOp1 = 0;
			ALUOp0 = 0;
			ALUSrc = 1;
			Branch = 0;
			MemRead = 1;
			MemWrite = 0;
			RegWrite = 1;
			MemtoReg = 1;
		}
		else if (op == 43) { 
			System.out.println("Control Vector : [ x 0 0 1 0 0 1 0 x ]");
			RegDST = 0;
			ALUOp1 = 0;
			ALUOp0 = 0;
			ALUSrc = 1;
			Branch = 0;
			MemRead = 0;
			MemWrite = 1;
			RegWrite = 0;
			MemtoReg = 0;
		}
		else if (op == 4) {
			System.out.println("Control Vector : [ x 0 1 0 1 0 0 0 x ]");
			RegDST = 0;
			ALUOp1 = 0;
			ALUOp0 = 1;
			ALUSrc = 0;
			Branch = 1;
			MemRead = 0;
			MemWrite = 0;
			RegWrite = 0;
			MemtoReg = 0;
		}
		
		int readRegister1 = IFBuffer[1];
		int readRegister2 = IFBuffer[2];
		
		int readData1 = registers.get(readRegister1);
		int readData2 = registers.get(readRegister2);
		
		return new int[] { destinationRegister, -1, -1, readData2, readData1, PC };
	}
	
		private int[] InformationExecute(int[] IDBuffer) throws Exception {
			
			int readData2 = IDBuffer[3];
			int readData1 = IDBuffer[4];
			int aluResult = 0;
			int destination = IDBuffer[0];
			int mux12 = RegDST;
			int mux11 = ALUSrc;
			
			switch (opcode) 
			{
			case "add": 
				aluResult = readData2 + readData1;
				System.out.println("ALU Result: " + aluResult);
				if (mux12 == 1) destination = 100;
				break;
				
			case "sub":
				aluResult = readData2 - readData1;
				System.out.println("ALU Result: " + aluResult);
				break;
				
			}
			
			
			return new int[] {destination, readData2, aluResult, 0};
		}
		
		
		private void getIn(String[] in) throws Exception {
			try {
				Scanner s = new Scanner(System.in);
				
				if (opcode.equals("add") || opcode.equals("sub"))
				{
					for (int i = 2; i < in.length; i++)
					{
						System.out.println("Enter register value for R"+ in[i]+":");
						registers.put(Integer.parseInt(in[i]), Integer.parseInt(s.nextLine()));
					}
				}
				if (opcode.equals("lw") || opcode.equals("sw") || opcode.equals("beq")) 
				{
					for (int i = 1; i < in.length - 1; i++)
					{
						System.out.println("Enter register value for R"+ in[i]+":");
						registers.put(Integer.parseInt(in[i]), Integer.parseInt(s.nextLine()));
					}
					System.out.println("Enter value for offset: ");
					registers.put(Integer.parseInt(in[3]), Integer.parseInt(s.nextLine()));	
				}

			} catch (Exception ex) {
				throw new Exception("Invalid Input");
			}
	}
}