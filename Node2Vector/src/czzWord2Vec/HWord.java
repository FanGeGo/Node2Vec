package czzWord2Vec;

import java.util.ArrayList;

/**
 可以存储Huffman编码的词语*/
public class HWord<T> extends Word<T>{

	public ArrayList<Byte> code;
	
	/*================================方法 methods================================*/
	
	/**
	 空构造方法*/
	public HWord() {
		super();
		code = new ArrayList<Byte>();
	}
	
	/**
	 构造方法*/
	public HWord(T word) {
		super(word);
		code = new ArrayList<Byte>();
	}

}
