package czzWord2Vec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 �ʵ䣬1.ͳ�������еĵ��ʣ�2.�ʵ����˵�Ƶ�ʣ�3.ͳ�ƴ�Ƶ��4.������Ҫ������ƵHuffman��*/
public class Vocabulary<T> implements IVocabulary{

	/**
	 �ʵ�*/
	private ArrayList<HWord<T> > _vocabulary;
	
	/**
	 �ʵ��дʵ����������Լ�¼�ʵ����Ƿ����ĳ�����ʣ�Ҳ���Լ�¼��������ڴʵ��еĸ�λ�ñ��*/
	private HashMap<T, Integer> _wordIndex;
	
	/**
	 �Ƚ�����
	 * @param <E> ���ʵ�����*/
	class WordFrequencyComparer<E> implements Comparator<Word<E>>  {

		public int compare(Word<E> o1, Word<E> o2) {
			int ret = o1.wordFrequency - o2.wordFrequency;
			if(ret > 0) ret = 1;
			else if(ret <0) ret = -1;
			return ret;
		}
	}
	
	private boolean _isSorted;
	
	/*================================���� methods================================*/
	
	public Vocabulary() {
		_vocabulary = new ArrayList<HWord<T> >();
		_wordIndex = new HashMap<T, Integer>();
		_isSorted = false;
	}
	
	public boolean isSorted() {
		return _isSorted;
	}

	/**
	 ��ȡ�ʵ䳤��*/
	public int getVocabularySize() {
		return _vocabulary.size();
	}
	
	/**
	 ���½����ʵ������*/
	private void reIndex() {
		_wordIndex.clear();
		for(int i = 0; i < _vocabulary.size(); i++) {
			_wordIndex.put(_vocabulary.get(i).word, i);			//��������
		}
	}
	
	/**
	 �ʵ����Ƿ���ڴ���word*/
	public boolean hasWord(T word) {
		boolean ret = false;
		if(_wordIndex.containsKey(word)) ret = true;
		return ret;
	}
	
	public HWord<T> getWord(T word) {
		HWord<T> ret = null;
		if(hasWord(word)) ret = _vocabulary.get(_wordIndex.get(word));
		return ret;
	}
	
	/**
	 ������װ�شʵ䣬���Ҽ����Ƶ*/
	public void loadVocabulary(T[][] words) {
		int i, j;
		for(i = 0; i < words.length; i++) {
			for(j = 0; j < words[i].length; j++) {
				addWord(words[i][j]);
			}
		}
		_isSorted = false;
	}
	
	/**
	 ��ʵ���װ�ش���*/
	public boolean addWord(T word) {
		boolean ret = false;
		if(_wordIndex.containsKey(word)) {
			_vocabulary.get(_wordIndex.get(word)).wordFrequency++;		//��Ƶ+1
		}
		else {
			_vocabulary.add(new HWord<T>(word));					//����ʵ�
			_wordIndex.put(word, _vocabulary.size() - 1);		//�������
		}
		_isSorted = false;					//��Ϊδ����״̬
		return ret;
	}
	
	public boolean removeWord(T word) {
		boolean ret = false;
		if(hasWord(word)) {
			int index = _wordIndex.get(word);
			_wordIndex.remove(word);
			_vocabulary.remove(index);
			reIndex();			//��Ҫ���½�������
			ret = true;
		}
		//_isSorted = false;			//���ı�����ʽ
		return ret;
	}
	
	/**
	 ���ݴʵ��д�Ƶ���򣬲����ؽ�����*/
	public void sortVocabulary() {
		_vocabulary.sort(new WordFrequencyComparer<T>());
		reIndex();
		_isSorted = true;
	}

	/**
	 ��ôʵ���ÿ�������Huffman����*/
	public boolean getHuffmanCode() {
		boolean ret = false;
		if(this._vocabulary.size() < 2) return ret;		//ֻ��һ���ڵ㣬������
		if(_isSorted) {
			int i, j;		//i����Ҷ�ӽڵ㣬j�����м�ڵ�
			HWord<T> min1, min2, tempmin;			//��С�������ڵ������
			int min1n, min2n;				//��С�������ڵ������
			HWord<T> parentNode;
			ArrayList<HWord<T> > nodeList = new ArrayList<HWord<T> >();				//Huffman���м�ڵ㣬n-1��
			HashMap<Integer, Integer> parentMap = new HashMap<Integer, Integer>();		//��¼ĳ���ڵ�ĸ��ڵ�
			int vLength = _vocabulary.size();						//�ʵ䳤��
			min1 = _vocabulary.get(0);
			min2 = _vocabulary.get(1);
			min1.code.add(new Byte((byte) 0));			//��С�����Һ��ӣ����0
			min2.code.add(new Byte((byte) 1));			//��С�������ӣ����1
			parentNode = new HWord<T>();
			parentNode.wordFrequency = min1.wordFrequency + min2.wordFrequency;		//���ڵ�=���Һ���ֵ�ĺ�
			nodeList.add(parentNode);
			parentMap.put(0, vLength);			//0��1�ĸ��ڵ㶼��nodeList��0��Ԫ��
			parentMap.put(1, vLength);
			i = 2;											//Ҷ�ӽڵ�ָ��λ��
			j = 0;											//�м�ڵ�ָ��λ��
			int k;
			for(k = 1; k < vLength - 1; k++) {
				tempmin = nodeList.get(j);
				if (i < vLength && (min1 = _vocabulary.get(i)).wordFrequency < tempmin.wordFrequency) { //�Ƚ�Ҷ�ӽڵ����м�ڵ�
					min1n = i;
					i++;
				}
				else {		
					min1 = tempmin;
					min1n = vLength + j;
					j++;
				}
				tempmin = nodeList.get(j);			//Ѱ�Ҵ���С�ڵ�Ĺ���
				if (i < vLength && (min2 = _vocabulary.get(i)).wordFrequency < tempmin.wordFrequency) { //�Ƚ�Ҷ�ӽڵ����м�ڵ�
					min2n = i;
				    i++;
				}
				else {		
					min2 = tempmin;
					min2n = vLength + j;
					j++;
				}
				min1.code.add(new Byte((byte) 0));			//��С�����Һ��ӣ����0
				min2.code.add(new Byte((byte) 1));			//��С�������ӣ����1
				parentNode = new HWord<T>();
				parentNode.wordFrequency = min1.wordFrequency + min2.wordFrequency;		//���ڵ�=���Һ���ֵ�ĺ�
				nodeList.add(parentNode);
				parentMap.put(min1n, vLength + k);			//min1��min2�ĸ��ڵ㶼��nodeList��0��Ԫ��
				parentMap.put(min2n, vLength + k);			//Ҳ����j����vLength���߼��ϰ�Ҷ�ӽڵ����м�ڵ������һ��ͳһ����
			}
			HWord<T> leafNode;			//Ҷ�ӽڵ�
			int parentIndex = -1;			//˫�׽ڵ�������
			for(k = 0; k < vLength; k++) {				//����Ҷ�ӽڵ㣬��Ҷ�ӽڵ����
				leafNode = _vocabulary.get(k);			//��ǰҶ�ӽڵ�
				parentIndex = parentMap.get(k);			//��ǰҶ�ӽڵ�ĸ��ڵ�
				while(parentIndex != vLength * 2 - 2) {
					parentNode = nodeList.get(parentIndex - vLength);		//��ȡ���ڵ�
					leafNode.code.add(0, parentNode.code.get(0));	//���ڵ�;
					parentIndex = parentMap.get(parentIndex);			//���ڵ�ĸ��ڵ�
				}
			}
			nodeList.clear();
			parentMap.clear();
			ret = true;
		}
		
		return ret;
	}
	
}
