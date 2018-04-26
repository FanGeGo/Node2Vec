package czzWord2Vec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 �ʵ䣬1.ͳ�������еĵ��ʣ�2.�ʵ����˵�Ƶ�ʣ�3.ͳ�ƴ�Ƶ��4.������Ҫ������ƵHuffman��*/
public class Vocabulary<T> implements IVocabulary{

	/**
	 �ʵ�*/
	private ArrayList<Word<T> > _vocabulary;
	
	/**
	 �ʵ��дʵ����������Լ�¼�ʵ����Ƿ����ĳ�����ʣ�Ҳ���Լ�¼��������ڴʵ��еĸ�λ�ñ��*/
	private HashMap<T, Integer> _wordIndex;
	
	/**
	 �Ƚ�����*/
	class WordFrequencyComparer implements Comparator<Word>  {

		public int compare(Word o1, Word o2) {
			int ret = o1.wordFrequency - o2.wordFrequency;
			if(ret > 0) ret = 1;
			else if(ret <0) ret = -1;
			return ret;
		}
	}
	
	/*================================���� methods================================*/
	
	/**
	 ��ȡ�ʵ䳤��*/
	public int getVocabularySize() {
		return _vocabulary.size();
	}
	
	/**
	 ������װ���ֵ䣬���Ҽ����Ƶ*/
	public void loadVocabulary(T[][] words) {
		int i, j;
		for(i = 0; i < words.length; i++) {
			for(j = 0; j < words[i].length; j++) {
				addWordToVocabulary(words[i][j]);
			}
		}
	}
	
	public boolean addWordToVocabulary(T word) {
		boolean ret = false;
		if(_wordIndex.containsKey(word)) {
			_vocabulary.get(_wordIndex.get(word)).wordFrequency++;		//��Ƶ+1
		}
		else {
			_vocabulary.add(new Word<T>(word));					//����ʵ�
			_wordIndex.put(word, _vocabulary.size() - 1);		//�������
		}
		return ret;
	}
	
	public void sortVocabulary() {
		_vocabulary.sort(new WordFrequencyComparer());
		_wordIndex.clear();
		int i;
		for(i = 0; i < _vocabulary.size(); i++) {
			_wordIndex.put(_vocabulary.get(i).word, i);			//��������
		}
	}

}
