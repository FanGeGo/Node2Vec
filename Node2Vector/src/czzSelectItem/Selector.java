package czzSelectItem;

import java.util.Random;

/**
 ѡ��������һ����ֵ��ͬ����Ŀ���У�������ֵռ�����ı������ѡ��һ����Ŀ
 @author CZZ*/
public class Selector {
	
	/**
	 ����ĿȨֵ�б���ѡ��ĳ����Ŀ��š���Ȩֵ����һ��ϲ���һ��Ͱ����Ͱ���������һ���㣬�����������ͨ���Ǹ���ɲ�����*/
	public static int select(float[] itemWeights) {
		int ret = -1;
		int n = itemWeights.length;
		float sum = 0;
		for(int i = 0; i <n ; i++) {
			sum += itemWeights[i];
		}
		float result = 0;			//������
		if(sum != 0) {					//ֻҪ����ֵ�Ϳ��ԱȽϴ�С������ǡ����0
			Random random = new Random();
			result = random.nextFloat() * sum;			//[0, 1) * sum
			float tempSum = 0;			//��ʱ��
			for(ret = 0; ret < n ; ret++) {
				tempSum += itemWeights[ret];
				if(tempSum > result) break;				//��ΪnextFloat������ҿ����䣬���Բ�ȡ�Ⱥ�
			}
		}
		return ret;
	}
}
