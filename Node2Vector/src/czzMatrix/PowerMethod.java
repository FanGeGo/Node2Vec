package czzMatrix;

/**
 * �׷����������ֵ����������*/
public class PowerMethod {

	private Matrix mat;
	
	/*================================���� methods================================*/
	
	public PowerMethod(Matrix m) {
		this.mat = m;
	}
	
	/**
	 * ����m�ģ�����ֵ���������ֵ���Ӧ����������*/
	public Eigen maxEigen() {
		Eigen ret = null;
		if(this.mat.isSquare()) {
			ret = new Eigen();
			int row = mat.getRow();				//��������������Ҳ������������ά��
			Matrix eigVector = new Matrix(row, 1, 1);				//���У�����.��ʼ��
			Matrix x0 = mat.multiply(eigVector);
			Matrix x1;
			Matrix y = PowerMethod.divideMax(x0);					//��һ��;
			while(true) {
				x1 = mat.multiply(y);
				if(PowerMethod.subFNorm(x1, x0, 2) < 1e-5) {
					break;
				}
				x0 = x1;
				y = PowerMethod.divideMax(x1);					//��һ��
			}
			ret.eigenvalue = PowerMethod.maxComponent(x1);
			ret.eigenvector = PowerMethod.normalized(y);
		}
		return ret;
	}
	
	/**
	 * ��һ��������������ֵ���ķ���*/
	public static Matrix divideMax(Matrix vec) {
		Matrix ret = null;
		if(vec.getRow() > 0 && vec.getColumn() == 1) {			//������
			ret = new Matrix();
			ret.copy(vec);
			float maxComponent = PowerMethod.maxComponent(vec);
			ret.multiply(1 / maxComponent);
		}
		return ret;
	}
	
	/**
	 * ��һ��������������ֵ���ķ���*/
	public static Matrix normalized(Matrix vec) {
		Matrix ret = null;
		if(vec.getRow() > 0 && vec.getColumn() == 1) {			//������
			ret = new Matrix();
			ret.copy(vec);
			ret.multiply(1 / PowerMethod.getLength(ret));
		}
		return ret;
	}
	
	/**
	 * �����������ķ���*/
	private static float maxComponent(Matrix vec) {
		float ret = 0;
		if(vec.getRow() > 0 && vec.getColumn() == 1) {			//������
			ret = vec.get(0, 0);
			float maxAbs = Math.abs(ret);				//����ֵ������
			float nowComponent, nowAbs;
			for(int i = 1; i < vec.getRow(); i++) {
				nowComponent = vec.get(i, 0);
				nowAbs = Math.abs(nowComponent);
				if(nowAbs > maxAbs) {
					maxAbs = nowAbs;
					ret = nowComponent;
				}
			}
		}
		return ret;
	}
	
	/**
	 * �����������������F����*/
	private static float subFNorm(Matrix vec1, Matrix vec2, float f) {
		float ret = -1;
		if(vec1.getRow() > 0 && vec1.getColumn() == 1 && vec1.getRow() == vec2.getRow() && vec2.getColumn() == 1) {	
			ret = 0;
			for(int i = 0; i < vec1.getRow(); i++) {
				ret += Math.pow(Math.abs(vec1.get(i, 0) - vec2.get(i, 0)), f);
			}
			ret = (float) Math.pow(ret, 1 / f);
		}
		return ret;
	}
	
	/**
	 * ��������ģ�����ȡ�2-����*/
	public static float getLength(Matrix vec) {
		float ret = 0;
		if(vec.getRow() > 0 && vec.getColumn() == 1) {	
			for(int i = 0; i < vec.getRow(); i++) {
				ret += Math.pow(vec.get(i, 0), 2);
			}
			ret = (float) Math.sqrt(ret);
		}
		return ret;
	}
}
