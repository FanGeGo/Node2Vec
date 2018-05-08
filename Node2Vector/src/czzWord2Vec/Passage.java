package czzWord2Vec;

import java.util.ArrayList;

/**
 * ����
 * @author CZZ*/
public class Passage<T> {

	/**
	 * ����*/
	private ArrayList<T[]> _sentences;
	
	/**
	 * ��ȡָ��*/
	private int pointer;
	
	/*================================���� methods================================*/
	
	/**
	 * ���췽��*/
	public Passage() {
		_sentences = new ArrayList<T[]>();
		pointer = -1;
	}
	
	/**
	 * �Ӷ�ά������ؾ���
	 * @param ���ʶ�ά����*/
	public void loadSentences(T[][] words) {
		_sentences.clear();					//���
		pointer = -1;
		for(int i = 0; i < words.length; i++) {
			_sentences.add(words[i]);
		}
		if(_sentences.size() > 0) pointer = 0;
	}
	
	/**
	 * ��ArrayLisT���ؾ���
	 * @param �����б�*/
	public void loadSentences(ArrayList<T[]> words) {
		_sentences.clear();					//���
		pointer = -1;
		_sentences.addAll(words);
		if(_sentences.size() > 0) pointer = 0;
	}
	
	/**
	 * @return ��������*/
	public int getSentenceCount() {
		return _sentences.size();
	}
	
	/**
	 * @return ��������*/
	public ArrayList<T[]> getSentences() {
		return _sentences;
	}
	
	/**
	 * �ı���ӡ�ָ��"��λ��
	 * @param index ���õ�λ��
	 * @return ���óɹ�*/
	public boolean seek(int index) {
		boolean ret = false;
		if(index >= 0 && index < _sentences.size()) {
			this.pointer = index;
			ret = true;
		}
		return ret;
	}
	
	/**
	 * @return ��һ������*/
	public T[] getNextSentence() {
		T[] ret = null;
		if(pointer >= 0) {
			if(pointer >= _sentences.size()) pointer = 0;			//��β����
			ret = _sentences.get(pointer++);
		}
		return ret;
	}
	
	/**
	 * ����ָ����ŵľ���
	 * @param index ����������
	 * @return ��һ������*/
	public T[] getSentence(int index) {
		T[] ret = null;
		if(index >= 0 && index < _sentences.size()) ret = _sentences.get(index);
		return ret;
	}
}
