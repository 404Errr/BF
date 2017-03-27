package bf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BF {

//	private static final String RAW = "++++++++[->+<]>.";
//	private static final String RAW = "[[][]][][[][[]]]";
	private static final String RAW = "[[]]]]";

	private static final char LEFT = '<';
	private static final char RIGHT = '>';
	private static final char PLUS = '+';
	private static final char MINUS = '-';
	private static final char OUTPUT = '.';
	private static final char INPUT = ',';
	private static final char LOOP_OPEN = '[';
	private static final char LOOP_CLOSE = ']';
	private static final char[] LEGAL = {LEFT, RIGHT, PLUS, MINUS, OUTPUT, INPUT, LOOP_OPEN, LOOP_CLOSE};// everything above

	private static char[] code;
	private static int[] data;
	private static int codePointer, pointer;

	public static void main(String[] args) {
		compile(RAW);
//		System.out.println(Arrays.toString(code));
		data = new int[30000];
		for (int i = 0;i<data.length;i++) {
			data[i] = 0;
		}
//		run();
	}

	static void compile(String rawCode) {
		List<Character> codeTemp = new ArrayList<>();
		int pairCount = 0;
		System.out.println(pairCount);
		for (int i = 0;i<rawCode.length();i++) {
			boolean valid = false;
			char command = rawCode.charAt(i);
			for (int j = 0;j<LEGAL.length;j++) {
				if (command==LEGAL[j]) {
					valid = true;
					break;
				}
			}
			if (valid) {
				if (command==LOOP_OPEN) pairCount++;
				if (command==LOOP_CLOSE) pairCount--;
				if (command==LOOP_OPEN||command==LOOP_CLOSE) System.out.println(pairCount+"\t"+(char) command);
				if (pairCount<0) System.err.println("bracket error <0");
				codeTemp.add(rawCode.charAt(i));
			}
		}
		if (pairCount!=0) {
			System.err.println("bracket error !0");
		}
		code = new char[codeTemp.size()];
		for (int i = 0;i<code.length;i++) code[i] = codeTemp.get(i);
	}

	static void run() {
		while (codePointer<code.length) {
			System.out.println((char) code[codePointer]+"\t"+codePointer);
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
			codePointer++;
		}
	}

	static void loopOpen() {
		
	}
	
	static void loopClose() {
		
	}
	
	static void right() {
		if (pointer<data.length-1) pointer++;
	}

	static void left() {
		if (pointer>0) pointer--;
	}

	static void plus() {
		data[pointer]++;
	}

	static void minus() {
		data[pointer]--;
	}

	static void output() {
		System.out.print((char) data[pointer]);
	}

	static void input() {//TODO

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