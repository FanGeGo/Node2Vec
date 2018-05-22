package czzMatrix;

/**
 * �׷����������ֵ����������*/
public class PowerMethod {

	private Matrix mat;
	
	/*================================���� methods================================*/
	
	public PowerMethod(Matrix m) {
		this.mat = m;
	}
	
	public Eigen maxEigen() {
		Eigen ret = null;
		if(this.mat.isSquare()) {
			ret = new Eigen();
			int row = mat.getRow();				//��������������Ҳ������������ά��
			Matrix eigVector = new Matrix(row, 1, 1);				//������
			Matrix x0 = mat.multiply(eigVector), x1;
			Matrix y;
			while(true) {
				y = PowerMethod.normalized(x0);					//��һ��
				x1 = mat.multiply(y);
				if(PowerMethod.subFNorm(x1, x0, 2) < 1e-4) {
					break;
				}
				x0 = y;
			}
			ret.eigenvalue = PowerMethod.maxComponent(x1);
			ret.eigenvector = y;
		}
		return ret;
	}
	
	/**
	 * ��һ����������һ��*/
	public static Matrix normalized(Matrix vec) {
		Matrix ret = null;
		if(vec.getRow() > 0 && vec.getColumn() == 1) {			//������
			ret = new Matrix();
			ret.copy(vec);
			float maxComponent = PowerMethod.maxComponent(vec);
			ret.multiply(1 / maxComponent);
		}
		return ret;
	}
	
	private static float maxComponent(Matrix vec) {
		float ret = 0;
		if(vec.getRow() > 0 && vec.getColumn() == 1) {			//������
			ret = vec.get(0, 0);
			float maxAbs = Math.abs(ret);				//����ֵ������
			float nowComponent, nowAbs;
			for(int i = 0; i < vec.getRow(); i++) {
				nowComponent = vec.get(i, 0);
				nowAbs = Math.abs(nowComponent);
				if(Math.abs(nowAbs) > maxAbs) {
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
}
