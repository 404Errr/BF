package bf;

import java.util.ArrayList;
import java.util.List;

public class BF {
//	private static final String RAW = "++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.";
//	private static final String RAW = "";
	private static final String RAW = "+>+[<[>>+>+<<<-]>>>[<<<+>>>-]<<[>+>+<<-]>>[<<+>>-]<]";//"+>+ [< [>>+>+<<<-]>>>[<<<+>>>-]copy <<[>+>+<<-]>>[<<+>>-]add <]"

	private static final boolean PRINT_DATA = true;
	private static final boolean OUTPUT_NUMBER = false;
	
	private static final char LEFT = '<', RIGHT = '>';
	private static final char PLUS = '+', MINUS = '-';
	private static final char OUTPUT = '.', INPUT = ',';
	private static final char LOOP_OPEN = '[', LOOP_CLOSE = ']';
	private static final char[] LEGAL = {LEFT, RIGHT, PLUS, MINUS, OUTPUT, INPUT, LOOP_OPEN, LOOP_CLOSE};

	private static char[] code;
//	private static byte[] data;
	private static int[] data;
	private static int codePointer, pointer;

	public static void main(String[] args) {
		compile(RAW);
//		System.out.println(Arrays.toString(code));
		data = new int[30000];
//		data = new byte[30000];
		for (int i = 0;i<data.length;i++) {
			data[i] = 0;
		}
		run();
	}

	private static void checkValidBrackets(List<Character> brackets) {
		int pairCount = 0;
		for (int i = 0;i<brackets.size();i++) {
			if (brackets.get(i)==LOOP_OPEN) pairCount++;//is [
			else pairCount--;//is ]
		}
		try {
			while (brackets.size()>=2) {
				int j = 0;
				while (brackets.get(j)==LOOP_OPEN) j++;
				brackets.remove(j);
				brackets.remove(j-1);
			}
		}
		catch (Exception e) {}
		if (brackets.size()>0||pairCount!=0) {
			System.err.println("bracket error");
			System.exit(0);
		}
	}
	
	private static void compile(String rawCode) {
		List<Character> codeTemp = new ArrayList<>();
		for (int i = 0;i<rawCode.length();i++) {
			boolean valid = false;
			char command = rawCode.charAt(i);
			for (int j = 0;j<LEGAL.length;j++) {
				if (command==LEGAL[j]) {
					valid = true;
					break;
				}
			}
			if (valid) codeTemp.add(rawCode.charAt(i));
		}
		code = new char[codeTemp.size()];
		List<Character> brackets = new ArrayList<>(); 
		for (int i = 0;i<code.length;i++) {
			code[i] = codeTemp.get(i);
			if (codeTemp.get(i)==LOOP_OPEN||codeTemp.get(i)==LOOP_CLOSE) brackets.add(codeTemp.get(i));
		}
		checkValidBrackets(brackets);
	}

	private static void run() {
		while (codePointer<code.length) {
//			System.out.println((char) code[codePointer]+"\t"+codePointer);
			switch (code[codePointer]) {
			case LEFT:
				if (pointer>0) pointer--;
				break;
			case RIGHT:
				if (pointer<data.length-1) pointer++;
				break;
			case PLUS:
				data[pointer]++;
				break;
			case MINUS:
				data[pointer]--;
				break;
			case OUTPUT:
				if (OUTPUT_NUMBER) System.out.println(data[pointer]);
				else System.out.print((char) data[pointer]);
				break;
			case INPUT:
				//todo
				break;
			case LOOP_OPEN:
				if (data[pointer]==0) {
					int depth = 0;
					do {
						if (code[codePointer]!=LOOP_CLOSE) depth++;
						if (code[codePointer]!=LOOP_OPEN) depth--;
						codePointer++;
					} while (depth!=0);
				}
				break;
			case LOOP_CLOSE:
				if (data[pointer]!=0) {
					int depth = 0;
					do {
						if (code[codePointer]!=LOOP_CLOSE) depth++;
						if (code[codePointer]!=LOOP_OPEN) depth--;
						codePointer--;
					} while (depth!=0);
				}
				break;
			}
			if (PRINT_DATA) printData();
			codePointer++;
		}
	}	

	private static final int PRINT_LENGTH = 30;
	private static void printData() {
		StringBuilder str = new StringBuilder();
		for (int i = 0;i<PRINT_LENGTH;i++) {
			if (i==pointer) {
				str.append("[");
				if (data[i]<=15) str.append("0");
				str.append(Integer.toHexString(data[i]));
				continue;
			}
			if (i-1==pointer) str.append("]");
			else str.append(" ");
			if (data[i]<=15) str.append("0");
			str.append(Integer.toHexString(data[i]));
		}
		System.out.println(str.toString());
	}
}
