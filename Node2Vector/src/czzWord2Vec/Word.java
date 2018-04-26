package czzWord2Vec;

/**
 �����ת����Ƕ�룬embedding��Ϊ�����Ĵ���*/
public class Word<T>{
	
	/**
	 �ʣ����ʹ�õ�����ǡ������*/
	public T word;
	
	/**
	 ��ͳ�ƵĴ�Ƶ��Ҳ�����Դ�Ϊ���ݽ���Huffman��*/
	public int wordFrequency;
	
	/*================================���� methods================================*/
	
	/**
	 ���췽��*/
	public Word(T word) {
		this.word = word;
		wordFrequency = 1;
	}
}
