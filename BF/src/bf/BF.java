package bf;

import java.util.ArrayList;
import java.util.List;

public class BF {
	private static final String RAW = "++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.";
//	private static final String RAW = "";
	
	private static final boolean PRINT_DATA = false;
	private static final boolean OUTPUT_NUMBER = false;
	
	private static final char LEFT = '<', RIGHT = '>';
	private static final char PLUS = '+', MINUS = '-';
	private static final char OUTPUT = '.', INPUT = ',';
	private static final char LOOP_OPEN = '[', LOOP_CLOSE = ']';
	private static final char[] LEGAL = {LEFT, RIGHT, PLUS, MINUS, OUTPUT, INPUT, LOOP_OPEN, LOOP_CLOSE};

	private static char[] code;
	private static byte[] data;
	private static int codePointer, pointer;

	public static void main(String[] args) {
		compile(RAW);
//		System.out.println(Arrays.toString(code));
		data = new byte[30000];
		for (int i = 0;i<data.length;i++) {
			data[i] = 0;
		}
		run();
	}

	private static void checkValidBrackets(List<Character> brackets) {
		int pairCount = 0;
//		System.out.println(pairCount);
		for (int i = 0;i<brackets.size();i++) {
			if (brackets.get(i)==LOOP_OPEN) pairCount++;//is [
			else pairCount--;//is ]
//			System.out.println(pairCount+"\t"+(char) brackets.get(i));
		}
		if (pairCount!=0) {
			System.err.println("bracket error");
			System.exit(0);
		}
//		if (brackets.size()%2!=0) {
//			System.err.println("bracket error");
//			System.exit(0);
//		}
		try {
			while (brackets.size()>=2) {
				int j = 0;
				while (brackets.get(j)==LOOP_OPEN) j++;
				brackets.remove(j);
				brackets.remove(j-1);
			}
		}
		catch (Exception e) {}
		if (brackets.size()>0) {
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
				left();
				break;
			case RIGHT:
				right();
				break;
			case PLUS:
				plus();
				break;
			case MINUS:
				minus();
				break;
			case OUTPUT:
				output();
				break;
			case INPUT:
				input();
				break;
			case LOOP_OPEN:
				loopOpen();
				break;
			case LOOP_CLOSE:
				loopClose();
				break;
			}
			if (PRINT_DATA) printData();
			codePointer++;
		}
	}

	private static void loopOpen() {
//		if (data[pointer]==0) {
//			while (code[codePointer]!=LOOP_CLOSE) {
//				codePointer++;
//			}
//		}
		if (data[pointer]==0) {
			int depth = 0;
			do {
				if (code[codePointer]!=LOOP_CLOSE) depth++;
				if (code[codePointer]!=LOOP_OPEN) depth--;
				codePointer++;
			} while (depth!=0);
		}
	}
	
	private static void loopClose() {
//		if (data[pointer]!=0) {
//			while (code[codePointer]!=LOOP_OPEN) {
//				codePointer--;
//			}
//		}
		if (data[pointer]!=0) {
			int depth = 0;
			do {
				if (code[codePointer]!=LOOP_CLOSE) depth++;
				if (code[codePointer]!=LOOP_OPEN) depth--;
				codePointer--;
			} while (depth!=0);
		}
	}
	
	private static void right() {
		if (pointer<data.length-1) pointer++;
	}

	private static void left() {
		if (pointer>0) pointer--;
	}

	private static void plus() {
		data[pointer]++;
	}

	private static void minus() {
		data[pointer]--;
	}

	private static void output() {
		if (OUTPUT_NUMBER) System.out.println(data[pointer]);
		else System.out.print((char) data[pointer]);
	}

	private static void input() {//TODO

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
		System.out.println((char) code[codePointer]+"\t"+str.toString());
	}
}
/*
> right
< left
+ add 1
- subtract 1
. output
, input
[ loop start (end when current is 0)
] loop end
*/
