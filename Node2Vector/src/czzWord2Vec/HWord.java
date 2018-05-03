package czzWord2Vec;

import java.util.ArrayList;

/**
 ���Դ洢Huffman����Ĵ���*/
public class HWord<T> extends Word<T>{

	public ArrayList<Byte> code;
	
	/*================================���� methods================================*/
	
	/**
	 �չ��췽��*/
	public HWord() {
		super();
		code = new ArrayList<Byte>();
	}
	
	/**
	 ���췽��*/
	public HWord(T word) {
		super(word);
		code = new ArrayList<Byte>();
	}

}
