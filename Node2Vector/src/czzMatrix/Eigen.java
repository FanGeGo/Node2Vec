package czzMatrix;

/**
 * ��������ֵ�����������Ľṹ��������������
 * @author CZZ*/
public class Eigen {

	/**
	 * ����ֵ*/
	public float eigenvalue;
	
	/**
	 * ��������*/
	public Matrix eigenvector;
	
	/*================================���� methods================================*/
	
	/**
	 * �չ��췽��*/
	public Eigen() {
		this.eigenvalue = 0;
		this.eigenvector = null;
	}
	
	/**
	 * ���췽��*/
	public Eigen(float eigenvalue, Matrix eigenvector) {
		this.eigenvalue = 0;
		this.eigenvector = null;
	}
}
