package czzWord2Vec;

/**
 * word2vec��һ�ִ���һ�������д����ϵ�ķ�ʽ����һ�ִ���ġ�Ƕ�롱��ʽ��������One-hot��ʾת��Ϊ�ֲ�ʽDistributed��ʾ����
 * ���������Ա�������һ�ָ����ĳ�����������Ĳ��������Ǵ�������һЩ���ص����ʣ��������ͨ�������ж�������������Ӧ�Ĵʵľ���
 * @author CZZ*/
public class Word2Vec {

	/**
	 ģ�����ͣ�skip-gramģ�ͣ�����CBOWģ��*/
	public enum ModelType {Skip_gram, CBOW};
	
	/**
	 �ֲ�������ֵHierarchical Softmax�����߸�����Negative Sampling*/
	public enum TrainMethod {HS, NS};
	
	/**
	 ������ά�ȣ�Ҳ�����������ز����Ԫ����*/
	private int dimensions;

	/**
	 �����Ĵ��ڴ�Сc��windows������ǰ���е��ʵ�����£����ȡ�õ�ǰ��ǰ���WindowSize�����ʣ�Ҳ�������2*WindowSize�����ʣ�
	 ��������Ӿ��������ǣ�ǰ��ֱ�ѡ��[1,WindowSize]�������������ĴʵĴ������˵�����ױ�ѡ��*/
	private int windowSize;
	
	/**
	 ��������*/
	private int iteratorNumber;
	
	/**
	 ��ʹ�Ƶ���������Ƶ�ʵĵ��ʲ��ᱻ����ʵ�vocabulary*/
	private int minWordCount;
	
	/**
	 ͬʱ���е��߳���*/
	private int threadNumber;
	
	/**
	 ����һ����ʼ��ѧϰ��*/
	private int learnRate;
	
	/*================================���� methods================================*/
	
	public Word2Vec() {

	}
	
	/**
	 ��ȡά�ȴ�С*/
	public int getDimensions() {
		return dimensions;
	}

	/**
	 ��ȡ���ڴ�С*/
	public int getWindowSize() {
		return windowSize;
	}

	public int getIteratorNumber() {
		return iteratorNumber;
	}

	public int getThreadNumber() {
		return threadNumber;
	}

	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}

	public int getMinWordCount() {
		return minWordCount;
	}

	public int getLearnRate() {
		return learnRate;
	}

	public void setLearnRate(int learnRate) {
		this.learnRate = learnRate;
	}
}
