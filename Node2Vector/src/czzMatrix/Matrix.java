package czzMatrix;

import java.util.LinkedList;

public class Matrix {
	
	/**
	 * �����ά����*/
	private float [][] matrix;
	
	/**
	 * ����*/
	private int row;
	
	/**
	 * ����*/
	private int column;
	
	/*================================���� methods================================*/
	
	/**
	 * �չ��췽��*/
	public Matrix(){
		this.row = 0;
		this.column = 0;
		this.matrix = null;
	}
	
	/**
	 * �޳�ʼ����ֵ���췽��
	 * @param row ����
	 * @param column ����*/
	public Matrix(int row, int column){
		this.row = row;
		this.column = column;
		this.matrix = new float[row][];
		for(int i = 0; i < row; i++) {
			this.matrix[i] = new float[column];
		}
	}
	
	/**
	 * ���췽��
	 * @param row ����
	 * @param column ����
	 * @param num ��ʼ����ֵ*/
	public Matrix(int row, int column, float num){
		this.row = row;
		this.column = column;
		this.matrix = new float[row][];
		for(int i = 0; i < row; i++) {
			this.matrix[i] = new float[column];
			for(int j = 0; j < column; j++) {
				this.matrix[i][j] = num;
			}
		}
	}
	
	/**
	 * ���ݶ�ά����װ�ش˾��󣬾���δ��ʼ��������ͬ��״����װ��
	 * @param mat ��ά����*/
	public boolean load(float[][] mat) {
		boolean ret = false;
		int r = mat.length, c;
		if(r > 0) {
			c = mat[0].length;
			if(c > 0) {
				if(this.matrix == null) {					//����δ��ʼ��
					this.row = r;
					this.column = c;
					this.matrix = new float[this.row][];
					for(int i = 0; i < this.row; i++) {
						this.matrix[i] = new float[this.column];
					}
				}
				if(this.row == r && this.column == c) {			//����ͬ��
					for(int i = 0; i < this.row; i++) {
						for(int j = 0; j < this.column; j++) {
							this.matrix[i][j] = mat[i][j];
						}
					}
				}
			}
		}
		return ret;
	}
	
	/**
	 * @return ��������*/
	public int getRow() {
		return this.row;
	}
	
	/**
	 * @return ��������*/
	public int getColumn() {
		return this.column;
	}
	
	/**
	 * @return ����*/
	public float[][] getMatrix() {
		return this.matrix;
	}
	
	/**
	 * ��þ����е�ֵ,���д�0��ʼ
	 * @param row ��row�� 0 1 2
	 * @param column ��column�� 0 1 2
	 * @return ������Ӧλ�õ�ֵ�����߱�ʾ�����ڵ�null*/
	public Float get(int row, int column) {
		Float ret = null; 
		if(row >= 0 && column >=0 && row < this.row && column < this.column) {
			ret = matrix[row][column];
		}
		return ret;
	}
	
	/**
	 * ���þ����е�ֵ,���д�0��ʼ
	 * @param row ��row�� 0 1 2
	 * @param column ��column�� 0 1 2
	 * @param f ���õ���*/
	public void set(int row, int column, float f) {
		if(row >= 0 && column >=0 && row < this.row && column < this.column) {
			matrix[row][column] = f;
		}
	}
	
	/**
	 * ����˷�a * b
	 * @param a ����a
	 * @param b ����b
	 * @return ����a * b*/
	public static Matrix multiply(Matrix a, Matrix b) {
		int ar, ac, br, bc;
		ar = a.getRow();
		ac = a.getColumn();
		br = b.getRow();
		bc = b.getColumn();
		int i, j, k;
		Matrix ret = null;
		if(ar > 0 && ac == br && br > 0 && bc > 0) {
			ret = new Matrix(ar, bc, 0);
			for(i = 0; i < ar; i++) {
				for(j = 0; j < bc; j++) {
					ret.getMatrix()[i][j] = 0;
					for(k = 0; k < ac; k++) {
						ret.getMatrix()[i][j] += a.get(i, k) * b.get(k, j);
					}
				}
			}
		}
		return ret;
	}
	
	/**
	 * ����˷���this * a
	 * @return �������*/
	public Matrix multiply(Matrix m) {
		return Matrix.multiply(this, m);
	}
	
	/**
	 * �������ˣ�ÿ��λ�ó�f
	 * @param f һ��������*/
	public void multiply(float f) {
		for(int i = 0; i < this.row; i++) {
			for(int j = 0; j < this.column; j++) {
				matrix[i][j] *= f;
			}
		}
	}
	
	/**
	 * ��ȡ��ǰ�����ת�þ���
	 * @return ��ǰ�����ת�þ���*/
	public Matrix transposition() {
		Matrix ret = null;
		if(this.row > 0 && this.column > 0) {
			ret = new Matrix(this.column, this.row);
			for(int i = 0; i < this.row; i++) {
				for(int j = 0; j < this.column; j++) {
					ret.set(j, i, this.get(i, j));
				}
			}
		}
		return ret;
	}
	
	/**
	 * �˾��������˾���ͬ�εľ���m
	 * @param m ��˾���ͬ�ξ���m*/
	public void add(Matrix m) {
		if(this.row == m.getRow() && this.column == m.getColumn()) {
			for(int i = 0; i < this.row; i++) {
				for(int j = 0; j < this.column; j++) {
					this.matrix[i][j] += m.get(i, j);
				}
			}
		}
	}
	
	/**
	 * ������m����r * c�Σ��¾����൱��r��c�и�m*/
	public Matrix repeat(Matrix m, int row, int column) {
		Matrix ret = null;
		int mr = m.getRow();
		int mc = m.getColumn();
		if(mr > 0 && mc > 0 && row > 0 && column > 0) {
			ret = new Matrix(row * mr, column * mc);
			for(int a = 0; a < mr; a++) {
				for(int b = 0; b < mc; b++) {
					for(int c = 0; c < row; c++) {
						for(int d = 0; d < column; d++) {
							ret.set(c * mc + a, d * mr + b, m.get(a, b));
						}
					}
				}
			}
		}
		return ret;
	}
	
	/**
	 * �����Ϊ������*/
	public void negative() {
		for(int i = 0; i < this.row; i++) {
			for(int j = 0; j < this.column; j++) {
				this.matrix[i][j] = -this.matrix[i][j];
			}
		}
	}
	
	/**
	 * ������е�ƽ����
	 * @return 1��c�о���c��������������ÿ��������һ�е�ƽ����*/
	public Matrix means() {
		Matrix ret = null;
		if(this.row > 0 && this.column > 0) {
			ret = new Matrix(1, this.column, 0);
			for(int i = 0; i < this.row; i++) {
				for(int j= 0; j < this.column; j++) {
					ret.matrix[1][j] += this.matrix[i][j];
				}
			}
			ret.multiply(1.0f / this.row);
		}
		return ret;
	}
	
	/**������Ӿ���
	 * @param r0 ����ʼ��������
	 * @param r1 �н�����������
	 * @param c0 ����ʼ��������
	 * @param c1 �н�����������
	 * @return ��r0��r1����c0��c1�ģ��Ӿ���
	 * */
	public Matrix subMatrix(int r0, int r1, int c0, int c1) {
		Matrix ret = null;
		if(r0 <= r1 && r1 < this.row && c0 <= c1 && c1 < this.column) {
			ret = new Matrix(r1 - r0 + 1, c1 - c0 + 1);
			for(int i = r0; i <= r1; i++) {
				for(int j= c0; j <= c1; j++) {
					ret.matrix[i][j] += this.matrix[r0 + i][c0 + j];
				}
			}
		}
		return ret;
	}
	
	/**
	 * n-1�׵�ȥ��i��j�еľ��󣨴�������ʽ��
	 * @param i ׼��ȥ���ĵ�i��
	 * @param j ׼��ȥ���ĵ�j��
	 * @return n-1�׵��Ӿ���*/
	public Matrix subIJ(int i, int j) {
		Matrix ret = null;
		if(i >= 0 && i < this.row && j >= 0 && j < this.column) {
			ret = new Matrix(this.row - 1, this.column - 1);
			int r, c;
			for(int a = 0; a < this.row; a++) {
				if(a == i) continue;
				r = a;
				if(a > i) r--; 
				for(int b = 0; b < this.column; b++) {
					if(b == j) continue;
					c = b;
					if(b > j) c--;
					ret.matrix[r][c] += this.matrix[a][b];
				}
			}
		}
		return ret;
	}
	
	/**
	 * ���������ʽ*/
	public Float determinant() {
		Float ret =  null;
		if(this.row == this.column && this.row > 0) {
			LinkedList<Matrix> matrixQueue = new LinkedList<Matrix>();				//������ľ�����У��ö��д���ݹ�
			LinkedList<Float> coefficientQueue = new LinkedList<Float>();				//ϵ������
			Matrix mat;
			int n;
			matrixQueue.add(this);
			coefficientQueue.add(1f);
			ret = 0f;
			while(matrixQueue.size() > 0) {
				mat = matrixQueue.peek();			//��ǰջ��Ԫ��
				n = mat.getRow();
				if(n == 1) {
					ret += coefficientQueue.poll() * mat.get(0, 0);
				}
				else if(n == 2) {
					ret += coefficientQueue.poll() * (mat.get(0, 0) * mat.get(1, 1) - mat.get(0, 1) * mat.get(1, 0));
				}
				else if(n > 2) {
					int symbol = 1;
					for(int i = 0; i < n; i++) {
						matrixQueue.addLast(mat.subIJ(0, i));				//����һ��չ��
						coefficientQueue.addLast(symbol * coefficientQueue.peek() * mat.get(0, i));
						symbol = -symbol;
					}
					coefficientQueue.poll();
				}
				matrixQueue.poll();
			}
		}
		return ret;
	}
	
	/**
	 * ����İ������*/
	public Matrix adjoint() {
		//TODO
		return null;
	}
	
	/**
	 * ����������*/
	public Matrix inverse() {
		//TODO
		return null;
	}
	
	/**
	 * Э�������*/
	public static Matrix cov(Matrix m) {
		//TODO
		return null;
	}
}
