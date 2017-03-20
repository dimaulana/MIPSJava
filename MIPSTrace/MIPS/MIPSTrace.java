import java.util.Arrays;
import java.util.Collections;
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
		while (true) {	
			System.out.println("Enter an instruction to begin the trace or 1 to exit");
			
			Scanner in = new Scanner(System.in);
			String instruction = in.nextLine();

			if (instruction.equals("1")) {
				return;
			}
			
			try {		
				mips.startTrace(instruction);	
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	String opcode;
	int op;
	int PC;
	int RegDST, ALUOp1, ALUOp0, ALUSrc, Branch, MemRead, MemWrite, RegWrite, MemtoReg;
	
	
	private Map<Integer, Integer> registers;
	private Map<Integer, Integer> memory;
	
	
	/*
	 * Runs through a trace of one instruction 
	 */
	public void startTrace(String input) throws Exception {
		
		String[] instruction = input.split(" ");
		
		registers = new HashMap<Integer, Integer>();
		memory = new HashMap<Integer, Integer>();

		opcode = instruction[0];
		getIn(instruction);
		

		
		
		System.out.println("*************************************");
		System.out.println("PC is currently: " +PC);
		System.out.println("");
		
		System.out.println("Registers");
		for(Integer key : registers.keySet()) {
			   System.out.println("R"+key+ " value is " +registers.get(key)+ " ");
			  			 }
		
		
		System.out.println();
		System.out.println("Memory Address");
		for (Integer key : memory.keySet()) {
			System.out.println("M"+ key +" value is " + memory.get(key));
		}
		
		
		System.out.println();
		
		System.out.println("*************************************");
		
		int[] IFBuffer = InformationFetch(instruction, 0);
		System.out.println("*************************************");

		System.out.println("IF STAGE");
		System.out.println();
		System.out.print("IF/ID Buffer" + " : [ ");
		for (int i : IFBuffer) {
			System.out.print(i + " ");
		}
		System.out.println("]");
		System.out.println("*************************************");

		System.out.println("ID STAGE");
		System.out.println();
		int[] IDBuffer = InformationDecode(IFBuffer);
		
		System.out.print("ID/EX Buffer" + " : [");
		for (int i : IDBuffer) {
			System.out.print(i + " ");
		}
		System.out.println("]");
	
	
		System.out.println();
		System.out.println("*************************************");

		
		System.out.println("EX STAGE");
		System.out.println();
		
		int[] EXBuffer = InformationExecute(IDBuffer);
		
		System.out.print("EX/MEM Buffer" + " : [");
		for (int i : EXBuffer) {
			System.out.print(i + " ");
		}
		System.out.println("]");
		
		
		System.out.println();
		System.out.println("*************************************");

		
		System.out.println("MEM/WB STAGE");
		System.out.println();
		int[] MEMBuffer = MemoryStage(EXBuffer);
		
		System.out.print("MEM/WB Buffer" + " : [");
		for (int i : MEMBuffer) {
			System.out.print(i + " ");
		}
		System.out.println("]");
		
		WriteBackStage(MEMBuffer);

		System.out.println("*************************************");
	}
	
	/*
	 * Information Fetch Stage
	 * Fetches relevant data from the user
	 * Returns IF/ID Buffer
	 */
	
	private int[] InformationFetch(String[] instruction, int PC) throws Exception {
		
		
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
			System.out.println("Semantics: R" + operand1 + " <- R" + operand2 + " + R" + operand3);
			return new int[]{ 0, operand2, operand3, operand1, 0, 32, PC };
		}
		else if (opcode.equals("sub")) {
			System.out.println("Semantics: R" + operand1 + " <- R" + operand2 + " - R" + operand3);
			return new int[]{ 0, operand2, operand3, operand1, 0, 34, PC };
		}
		else if (opcode.equals("lw")) {
			System.out.println("Semantics: R" + operand1 + " <- M[R" + operand3 + " + " + operand2 + "]");
			return new int[]{ 35, operand3, operand1, operand2, PC };
		}
		else if (opcode.equals("sw")) {
			System.out.println("Semantics: " +  "M[R" + operand3 + " + " + operand2 + "]" + " <- R" + operand1);
			return new int[]{ 43, operand3, operand1, operand2, PC };
		}
		else if (opcode.equals("beq")) {
			System.out.println("Semantics: " +  "IF (R" + operand1 + " == R" + operand2 + ") THEN PC <- 4 + 4 * " + operand3);
			return new int[]{ 4, operand1, operand2, operand3, PC };
		}
		else {
			throw new Exception("Invalid Opcode");
		}
	}
	
	
	/*
	 *  Information Decode Stage
	 * 	Decodes the data
	 *  Sets the control vector
	 *  Returns the ID/EX Buffer
	 */
	private int[] InformationDecode(int[] IFBuffer) throws Exception {
		
		
		int IR2016 = 0;
		int IR1511 = 0;
		int signExtended = 0;
		
		
		if (IFBuffer[0] == 0) 	
			{
			PC = IFBuffer[6];
			IR2016 = IFBuffer[2];
			IR1511 = IFBuffer[3];
			}
		
		if (IFBuffer[0] == 35 || IFBuffer[0] == 43 || IFBuffer[0] == 4) {
			PC = IFBuffer[4];
			signExtended = IFBuffer[3];
			IR2016 = IFBuffer[2];
			IR1511 = 0;
			}
		

		
		int op = IFBuffer[0];
		String opcode = op + "";
		if (op == 0) {
			opcode += ""+IFBuffer[5];
		}
		System.out.println("Opcode : " + opcode);
		
		if (op == 0) {
			printControlVector(new int[] { 1, 1, 0, 0, 0, 0, 0, 1, 0});
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
			printControlVector(new int[] { 0, 0, 0, 1, 0, 1, 0, 1, 1});
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
			printControlVector(new int[] { -1, 0, 0, 1, 0, 0, 1, 0, -1});
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
			printControlVector(new int[] { -1, 0, 1, 0, 1, 0, 0, 0, -1});
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
		
		return new int[] { IR1511, IR2016, signExtended, readData2, readData1, PC };
	}
	
	/*
	 * Execute Stage
	 * Performs all calculations
	 * Returns EX/WB Buffer
	 */
	
		private int[] InformationExecute(int[] IDBuffer) throws Exception {
			
			int readData2 = IDBuffer[3];
			int readData1 = IDBuffer[4];
			int zero = 0;
			int address = IDBuffer[5];
			int aluResult = 0;
			int destination = IDBuffer[0];
			int mux12 = RegDST;
			int mux11 = ALUSrc;
			
			
			if (mux12 == 0) destination = IDBuffer[1]; 	
			address += (IDBuffer[2]*4);
			
			
			switch (opcode) 
			{
			case "add": 
				aluResult = readData1 + readData2;
				break;
				
			case "sub":
				aluResult = readData1 - readData2;
				break;
			
			case "lw": 
				aluResult = readData1 + IDBuffer[2];
				
				break;
				
			case "sw":
				aluResult = readData1 + IDBuffer[2];
				break;
				
			case "beq":
				if (readData2 == readData1) zero = 1;
				aluResult = readData1 - readData2;
				break;
			}
			
			
			System.out.println("Adder Result: " + address);
			System.out.println("ALU Result: " + aluResult);
			System.out.println("Zero: " + zero);
			System.out.println("Mux11: " + mux11);
			System.out.println("Mux12: " + mux12);

			return new int[] {destination, readData2, aluResult, zero, address};
		}
		
		/*
		 * Memory Stage
		 * Branches when required
		 * Loads data from memory when required
		 * Returns MEM/WB Buffer
		 */
		
		private int[] MemoryStage(int[] EXBuffer) throws Exception {
		
			int destination = EXBuffer[0];

			int zero = EXBuffer[3];
			int address = EXBuffer[4];
			int readData2 = EXBuffer[1];
			int readData = EXBuffer[2];

			if (MemWrite == 1) {
				System.out.println("M[" + readData+"] <- R"+destination);
			}
			else if (MemRead == 1) {
				System.out.println("LOAD DATA <- M[" + readData+"]");
			}
			
			
			if (zero == 1 && Branch == 1) {
				System.out.println("Branch successful go to " + address);
				PC = address;
				System.out.println("PC is now: " + PC );
			}

			
			return new int[] { destination, readData2, readData };
		}
		
		/*
		 * WriteBack Stage
		 * Writes relevant data to the destination register if relevant
		 */
		private void WriteBackStage(int[] MEMBuffer) {
			int readData = MEMBuffer[2];
			int readData2 = MEMBuffer[1];
			String writeData;;
			
			if (MemtoReg == 1) {
				writeData = "M["+readData2+"]";
			}
			else {
				readData = MEMBuffer[2];
			}
			
			if (MemWrite == 1) {
				writeData = Integer.toString(readData2);
			}
			
			System.out.println("Mux14: "+ MemtoReg);
			int writeRegister = MEMBuffer[0];
			if (RegWrite == 1)
				System.out.println("R"+ writeRegister +" <- " + readData);

		}
		
		/*
		 * Method to get all relevant data from the user
		 */
		private void getIn(String[] in) throws Exception {
			try {
				Scanner s = new Scanner(System.in);
				PC += 4;

				if (opcode.equals("add") || opcode.equals("sub"))
				{
					for (int i = 2; i < in.length; i++)
					{
						System.out.println("Enter register value for R"+ in[i]+":");
						registers.put(Integer.parseInt(in[i]), Integer.parseInt(s.nextLine()));
					}
				}
				
				if (opcode.equals("lw") || opcode.equals("sw")) 
				{
					for (int i = 1; i < in.length - 2; i++)
					{
						System.out.println("Enter register value for R"+ in[i]+":");
						registers.put(Integer.parseInt(in[i]), Integer.parseInt(s.nextLine()));
					}	
					System.out.println("Enter register value for R" + in[3]+":");
					registers.put(Integer.parseInt(in[3]), Integer.parseInt(s.nextLine()));

				}
				
				if (opcode.equals("beq")) 
					{
						for (int i = 1; i < in.length - 1; i++)
						{
							System.out.println("Enter register value for R"+ in[i]+":");
							registers.put(Integer.parseInt(in[i]), Integer.parseInt(s.nextLine()));
						}	
					}				
				getMemory();
				
			} catch (Exception ex) {
				throw new Exception("Invalid Input");
			}
		}
			
		private void getMemory() throws Exception {
				try {
					Scanner s = new Scanner(System.in);
					
					System.out.println("Enter read address and data as <address> <value>: ");
					memory.put(s.nextInt(), s.nextInt());
					
					System.out.println("Enter write address and data as <address> <value>: ");
					memory.put(s.nextInt(), s.nextInt());
										
				} catch (Exception ex) {
					throw new Exception("Invalid Input");
				}
		}
		
		private void printControlVector(int[] vector) {
			System.out.println("Control Vector:");
			System.out.println("-----------------------------------------------------------------------------------------");
			System.out.println("| RegDST | ALUOp1 | ALUOp0 | ALUSrc | Branch | MemRead | MemWrite | RegWrite | MemtoReg |");
			System.out.println("-----------------------------------------------------------------------------------------");
			System.out.println("|   "+vector[0]+"    |    "+vector[1]+"   |    "+vector[2]+"   |    "+vector[3]+"   |    "
					+vector[4]+"   |    "+vector[5]+"    |     "+vector[6]+"    |     "+vector[7]+"    |     "+vector[8]+"    |");
			System.out.println("-----------------------------------------------------------------------------------------");
		}
}