package czzWord2Vec;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;

import czzVector.CVector;
import czzVector.IVector;

/**
 * word2vec��һ�ִ���һ�������д����ϵ�ķ�ʽ����һ�ִ���ġ�Ƕ�롱��ʽ��������One-hot��ʾת��Ϊ�ֲ�ʽDistributed��ʾ����
 * ���������Ա�������һ�ָ����ĳ�����������Ĳ��������Ǵ�������һЩ���ص����ʣ��������ͨ�������ж�������������Ӧ�Ĵʵľ���
 * @author CZZ*/
public class Word2Vec<T> {

	/**
	 ģ�����ͣ�skip-gramģ�ͣ�����CBOWģ��*/
	public enum ModelType {Skip_gram, CBOW};
	
	/**
	 �ֲ�������ֵHierarchical Softmax�����߸�����Negative Sampling*/
	public enum TrainMethod {HS, NS, BOTH};
	
	/**
	 * ģ������*/
	private ModelType modelType;
	
	/**
	 * ѵ����ʽ*/
	private TrainMethod trainMethod;
	
	/**
	 ������ά�ȣ�Ҳ�����������ز����Ԫ����*/
	private int dimensions;

	/**
	 �����Ĵ��ڴ�Сc��windows������ǰ���е��ʵ�����£����ȡ�õ�ǰ��ǰ���WindowSize�����ʣ�Ҳ�������2*WindowSize�����ʣ�
	 ��������Ӿ��������ǣ�ǰ��ֱ�ѡ��[1,WindowSize]�������������ĴʵĴ������˵�����ױ�ѡ��*/
	private int windowSize;
	
	/**
	 * ����������*/
	private int negative;
	
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
	private float startLearnRate;
	
	/**
	 ��ǰѧϰ��*/
	private float learnRate;
	
	/**
	 * ���£�ÿ��һ�䣬ÿ�䶼��һ�������*/
	private Passage<T> passags;
	
	private ExpTable expTable;
	
	/**
	 * �ʵ�*/
	private Vocabulary<T> vocabulary;
	
	/**
	 * ģ�Ͳ�����Ҳ����ѵ������ÿ���ʵ�����*/
	private IVector[] _models;					
	
	/**
	 * ���������м�ڵ�Ĳ�����������*/
	private IVector[] _huffmanTheta;			//x * ��
	
	/**
	 * ������������������*/
	private IVector[] _negTheta;			//x * ��
	
	/**
	 * �Ƿ񾭹���ʼ��*/
	private boolean initialized;
	
	/*================================���� methods================================*/
	
	/*
	/**
	 * �չ��췽��*
	public Word2Vec() {
		vocabulary = new Vocabulary<T>();
		setParam(ModelType.CBOW, TrainMethod.BOTH, 5, 200, 5, 0.05f, 0, 1, 1);
	}*/
	
	/**
	 * ���췽��
	 * @param mt ģ������
	 * @param tm ѵ����ʽ
	 * @param dimensions Ƕ�������ά��
	 * @param windowSize ���ڴ�С
	 * @param learnRate ѧϰ��
	 * @param minWordCount ��ʹ�Ƶ
	 * @param iteratorNumber ȫ��������������
	 * @param threadNumber �����߳���
	 * */
	public Word2Vec(ModelType modelType, TrainMethod trainMethod, int negative, int dimensions, int windowSize, float learnRate, int minWordCount, int iteratorNumber, int threadNumber) {
		expTable = new ExpTable();
		initialized = false;
		setParam(modelType, trainMethod, negative, dimensions, windowSize, learnRate, minWordCount, iteratorNumber, threadNumber);
	}
	
	private boolean setParam(ModelType modelType, TrainMethod trainMethod, int negative, int dimensions, int windowSize, float learnRate, int minWordCount, int iteratorNumber, int threadNumber) {
		this.modelType = modelType;
		this.trainMethod = trainMethod;
		this.negative = negative;
		this.dimensions = dimensions;
		this.windowSize = windowSize;
		this.startLearnRate = learnRate;
		this.minWordCount = minWordCount;
		this.iteratorNumber = iteratorNumber;
		this.threadNumber = threadNumber;
		return true;
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

	public float getLearnRate() {
		return learnRate;
	}

	public void setLearnRate(int learnRate) {
		this.learnRate = learnRate;
	}
	
	public void init(ArrayList<T[]> words) {
		this.vocabulary = new Vocabulary<T>();
		this.vocabulary.loadVocabulary(words);
		this.vocabulary.sortVocabulary();			//�ʵ��еĴ��ﰴ�մ�Ƶ��С��������
		this.passags = new Passage<T>();
		this.passags.loadSentences(words);
		this.vocabulary.frequencyFilter(minWordCount);				//���˵�Ƶ��
		if(this.trainMethod == TrainMethod.BOTH || this.trainMethod == TrainMethod.HS) {
			this.vocabulary.getHuffmanCode();			//�����������������ÿ���ʵı���
		}
		
		initModels();				//��ʼ������
		this.learnRate = this.startLearnRate;
		initialized = true;
	}
	
	/**
	 * ��������������v*/
	public void randomSetVector(IVector v) {
		if(v != null) {
			float[] va = v.getVector();
			if(va != null) {
				Random rand = new Random();
				for(int i = 0; i <va.length; i++) {
					//va[i] = (float) rand.nextDouble();
					va[i] = ((rand.nextFloat() - 0.5f) / this.dimensions);
				}
			}
		}
		
	}
	
	/**
	 * ��ʼ����������*/
	public void initModels() {
		_models = new IVector[this.vocabulary.getVocabularyLength()];
		if(this.trainMethod == TrainMethod.HS || this.trainMethod == TrainMethod.BOTH)
			_huffmanTheta = new IVector[this.vocabulary.getVocabularyLength()];
		if(this.trainMethod == TrainMethod.NS || this.trainMethod == TrainMethod.BOTH)
			_negTheta = new IVector[this.vocabulary.getVocabularyLength()];
		for(int i = 0; i < this.vocabulary.getVocabularyLength(); i++) {
			_models[i] = new CVector(this.dimensions);
			randomSetVector(_models[i]);						//��������
			if(this.trainMethod == TrainMethod.HS || this.trainMethod == TrainMethod.BOTH)
				_huffmanTheta[i] = new CVector(this.dimensions);
			if(this.trainMethod == TrainMethod.NS || this.trainMethod == TrainMethod.BOTH)
				_negTheta[i] = new CVector(this.dimensions);
		}
	}
	
	/**
	 * ��ѵ���õ��ļ���ȡ����*/
	public boolean loadModels() {
		//TODO
		return false;
	}
	
	/**
	 * ��ʼѵ��*/
	public void startTrainning() {
		if(this.initialized) {				//������ʼ��
			int localIteratorNumber = 1;			//��ǰ��������
			int c;									//[1, windowSize]��������ڴ�С��ʹ����֮���ϵ��Ϊ����
			Random rand = new Random();
			HWord<T> word;
			T contextWord;							//������
			int contextIndex;					//�����ĵ�������
			int thetaIndex;						//����������
			IVector e;
			float f, g;
			for(localIteratorNumber = 1; localIteratorNumber <= this.iteratorNumber; localIteratorNumber++) { //��������
				for(int sentenceIndex = 0; sentenceIndex < this.passags.getSentenceCount(); sentenceIndex++) {
					T[] sentence = this.passags.getSentence(sentenceIndex);
					for(int sentence_position = 0; sentence_position < sentence.length; sentence_position++) {
						c = Math.max((rand.nextInt() + 11) % this.windowSize, 1);		//[1, windowSize]��������ڴ�С
						word = this.vocabulary.getWord(sentence[sentence_position]);
						this.learnRate *= 0.999f;							//��Сѧϰ��
						if (this.learnRate < this.startLearnRate * 0.0001f) this.learnRate = this.startLearnRate * 0.0001f;
						if(this.modelType == ModelType.CBOW) {
							//TODO
						}//if(this.modelType == ModelType.CBOW)
						else if(this.modelType == ModelType.Skip_gram) {
							for (int i = c; i < this.windowSize * 2 + 1 - c; i++) {			//������ǰ�ʵ�������
								if (i != this.windowSize)					//�ǵ�ǰ�ʵ������ģ������ǵ�ǰ��
								{
									c =	sentence_position -	this.windowSize + i;
									if (c <	0) continue;
									if (c >= sentence.length) continue;			//for (int i = c; i < this.windowSize * 2 + 1 - c; i++)
									contextWord = sentence[c];
									contextIndex = this.vocabulary.getWordIndex(contextWord);
									if(contextIndex == -1) continue;			//for (int i = c; i < this.windowSize * 2 + 1 - c; i++)
									e = new CVector(this.dimensions);		//e=0
									// HIERARCHICAL	SOFTMAX
									if (this.trainMethod == TrainMethod.HS || this.trainMethod == TrainMethod.BOTH)
										for	(int l = 0;	l <	word.code.size(); l++)		//��huffman·���ֲ�SoftMax
										{
											thetaIndex = word.point.get(l);
											// Propagate hidden -> output
											f = this._models[contextIndex].multiply(this._huffmanTheta[thetaIndex]);	//f = x * theta;
											if (f <= -this.expTable.getMaxX()) continue;
											else if (f >= this.expTable.getMaxX()) continue;
											else f = expTable.getSigmoid(f);								//sigmoid����
											// 'g' is the	gradient multiplied	by the learning	rate
											g = (1 - word.code.get(l) - f) * this.learnRate;				//ƫ������ѧϰ��
											// Propagate errors output ->	hidden
											e.add(this._huffmanTheta[thetaIndex].new_Multi(g));				//e += g * theta
											// Learn weights hidden -> output
											this._huffmanTheta[thetaIndex].add(this._models[contextIndex].new_Multi(g));
										}
									// NEGATIVE	SAMPLING
									if (negative > 0) {
										//TODO
									}
									// Learn weights input -> hidden
									this._models[contextIndex].add(e);
								}//if (i != this.windowSize)
							}//for (int i = c; i < this.windowSize * 2 + 1 - c; i++)	
						}//if(this.modelType == ModelType.Skip_gram)
					}//for(int sentence_position = 0; sentence_position < this.passags.getSentence(sentenceIndex); sentence_position++) {
				}//for(int sentenceIndex = 0; sentenceIndex < this.passags.getSentenceCount(); sentenceIndex++)
			}//for(localIteratorNumber = 1; localIteratorNumber <= this.iteratorNumber; localIteratorNumber++)
		}//if(this.initialized)
	}
	
	
	public IVector[] getModels() {
		return this._models;
	}
	
	/**
	 * ������ļ�file
	 * @param file �ᱻд����ļ�·��
	 * @throws IOException */
	public void outputFile(String file) throws IOException {
		File f = new File(file);
        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		String str;
        out.write(this.vocabulary.getVocabularyLength() + " " + this.dimensions + "\n");
        for(int i = 0; i < this._models.length; i++) {
        	out.write((i + 1) + " ");
        	for(int j = 0; j < this._models[i].getVector().length; j++) {
        		if(j != 0) out.write(" ");
        		str = _models[i].getVector()[j] + "";
        		out.write(str);
        	}
        	if(i != this._models.length - 1) out.write("\n");
        }
        out.flush();
        out.close();
	}
}
