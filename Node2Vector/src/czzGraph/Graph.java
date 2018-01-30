package czzGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
ͼ 
@author CZZ*/
public class Graph {
	
	/**
	 ��ʼ�������id���*/
	private int random;
	
	/**
	 �Ƿ�Ϊ����ͼ*/
	private boolean _isDirected;
	
	/**
	  �Ƿ�Ϊ��Ȩͼ*/
	private boolean _isWeighted;
	
	/**
	  �ڵ㼯��*/
	private HashMap<Integer, Node> _nodeList;

	/**
	 �߼��ϣ������ڽӱ���ʽ������NodeΪ���*/
	private HashMap<Integer, HashMap<Integer, Edge>> _edgeList;
	
	/*================================���� methods================================*/

	/**
	 ͼ��Ĭ�Ϲ��캯��*/
	public Graph() {
		_nodeList = new HashMap<Integer, Node>();
		_edgeList = new HashMap<Integer, HashMap<Integer, Edge>>();
		Random rand = new Random(65535);
		random = rand.nextInt();
		this._isDirected = false;
		this._isWeighted = false;
	}
	
	/**
	 ͼ�Ĺ��캯��
	 @param isDirected �Ƿ�Ϊ����ͼ
	 @param isWeighted �Ƿ�Ϊ��Ȩͼ*/
	public Graph(boolean isDirected, boolean isWeighted) {
		_nodeList = new HashMap<Integer, Node>();
		_edgeList = new HashMap<Integer, HashMap<Integer, Edge>>();
		Random rand = new Random(65535);
		random = rand.nextInt();
		this._isDirected = isDirected;
		this._isWeighted = isWeighted;
	}
	
	public boolean isEmpty() {
		return (this._nodeList.size() == 0 && this._edgeList.size() == 0);
	}
	
	/**
	 @param id �ڵ��id
	 @return �ڵ������*/
	public Node getNode(int id) {
		return this._nodeList.get(id);
	}
	
	/**
	 @param id1 ���id
	 @param id2 �յ�id
	 @return �ߵ�����*/
	public Edge getEdge(int id1, int id2) {
		Edge ret = null;
		HashMap<Integer, Edge> edgeMap = this._edgeList.get(id1);
		if(edgeMap != null) {
			ret = edgeMap.get(id2);
		}
		return(ret);
	}
	
	/**
	  �Ƿ�Ϊ����ͼ*/
	public boolean isDirected() {
		return _isDirected;
	}
	
	/**
	  �Ƿ�Ϊ��Ȩͼ*/
	public boolean isWeighted() {
		return _isWeighted;
	}
	
	/**
	  ���һ���ڵ㣨ID�����֣�*/
	public boolean addNode(int id, String name) {
		boolean notexist = false;
		if(_nodeList.get(id) == null) {
			_nodeList.put(id, new Node(id, name));
			notexist = true;
		}
		return notexist;
	}
	
	/**
	  ���һ���ڵ㣨id��*/
	public boolean addNode(int id) {
		return addNode(id, null);
	}
	
	/**
	 * ���һ���ڵ�(�������id,��������)��
	 @return ���سɹ���ȡ��id������idʧ�ܷ���-1*/
	public int addNode(String name) {
		int newId = -1;			//�ڵ����id����Ϊ����
		int i = 0;
		boolean flag = true;
		for(i = 0; i < 10; i++){		//�������id,����10��
			newId = random;
			if(addNode(newId, name)) {		//��ȡ�ɹ�
				random++;
				flag = false;
				break;					//׼������id
			}
			Random rand = new Random(65535);		//�������ѡ����һ��id
			random = rand.nextInt();
		}
		if(flag) {
			newId = -1;				//10�γ���ʧ�ܣ�δ����������ʵ�id������-1
		}
		return newId;
	}
	
	/**
	 * ���һ���ڵ�(�������id,����ҲΪ��)��
	 @return ���سɹ���ȡ��id������idʧ�ܷ���-1*/
	public int addNode() {
		return addNode(null);
	}
	
	/**�����Ѿ����ڵ������ڵ�
	 * @param id1 �ڵ�1��id
	 * @param id2 �ڵ�2��id
	 * @return �����ɹ�����true
	 */
	public boolean addEdge(int id1, int id2, int weight) {
		Node v1 = this._nodeList.get(id1);
		Node v2 = this._nodeList.get(id2);
		return addEdge(v1, v2, weight);
	}
	
	/**�����Ѿ����ڵ������ڵ�
	 * @param v1 �ڵ�1
	 * @param v2 �ڵ�2
	 * @return �����ɹ�����true
	 */
	public boolean addEdge(Node v1, Node v2, int weight) {
		boolean ret = false;
		if(v1 != null && v2!= null) {
			HashMap<Integer, Edge> eV1 = this._edgeList.get(v1.getId());
			if(eV1 == null) {
				eV1 = new HashMap<Integer, Edge>();			//���û�з���ռ䣬���ֳ�����
				this._edgeList.put(v1.getId(), eV1);
			}
			int id2 = v2.getId();
			if(eV1.get(id2) == null) {						//�����ظ�����
				Edge newEdge = new Edge(v1, v2, weight);
				eV1.put(id2, new Edge(v1, v2, weight));			//�����µı�
				v1.arriveAt(newEdge);					//v1��¼��һ���±�
				v2.comeFrom(v1);						//v2��¼v1���Ե���v2
				ret = true;
			}
		}
		return ret;
	}
	
	/**
	 ��ͼ��ɾ���ڵ㣨id��
	 @return ɾ��ǰ�ڵ�Ķ�*/
	public int removeNode(int id) {
		int ret = -1;
		Node v = this._nodeList.get(id);
		if(v != null) {
			ret = v.getDegree();
			HashMap<Integer, Edge> outEdgeList = this._edgeList.get(id);
			if(outEdgeList != null) {
				outEdgeList.clear();			//1.ɾ���ɴ˽ڵ�����ı�
			}
			this._edgeList.remove(id);
			
			HashMap<Integer, Node> inNodeList = v.getInNodeList();
			Set<Integer> inNodeKeySet = inNodeList.keySet();		//id����
			Iterator<Integer> iter = inNodeKeySet.iterator();		//������
			HashMap<Integer, Edge> v1ev2Set = null;			//��������������˽ڵ�ıߵļ���
			int v1id = -1;									//��ʱ���v1��id
			while(iter.hasNext()) {
				v1id = iter.next();
				v1ev2Set =_edgeList.get(v1id);
				v1ev2Set.remove(id);							//2.ɾ�����Ե���˽ڵ�ı�
				if(v1ev2Set.size() == 0) {
					v1ev2Set.clear();
					_edgeList.remove(v1id);
				}
			}
			v.beforeDeleteSelf();					//�Ͽ������ڵ���˽ڵ��йصļ�¼
			this._nodeList.remove(id);
		}
		return ret;
	}
	
	/**
	 ��ͼ��ɾ���ڵ�v
	 @return ɾ��ǰ�ڵ�Ķ�*/
	public int removeNode(Node v) {
		int id = v.getId();
		return removeNode(id);
	}
	
	/**
	 ɾ����v1��v2�ı�
	 @param v1 �ߵ����
	 @param v2 �ߵ��յ�
	 @return ɾ�������trueɾ���ɹ�*/
	public boolean removeEdge(Node v1, Node v2) {
		boolean ret = false;
		if(v1 != null && v2 != null) {
			HashMap<Integer, Edge> v1Set = this._edgeList.get(v1.getId());
			if(v1Set!=null) {
				if(v1Set.get(v2.getId()) != null) {
					v1Set.remove(v2.getId());
					v1.removeEdge(v1, v2);
					v2.removeEdge(v1, v2);
					ret = true;
				}
			}
		}
		return ret;
	}
	
	/**
	 ɾ��ͼ�еı�
	 @param id1 ����id
	 @param id2 �յ��id
	 @return ɾ�������trueɾ���ɹ�*/
	public boolean removeEdge(int id1, int id2) {
		Node v1 = this._nodeList.get(id1);
		Node v2 = this._nodeList.get(id2);
		return removeEdge(v1, v2);
	}
	
	/**
	 ɾ��ͼ�еı�
	 @param id1 ����id
	 @param id2 �յ��id
	 @return ɾ�������trueɾ���ɹ�*/
	public boolean removeEdge(Edge vv) {
		boolean ret = false;
		if(vv != null) {
			Node v1 = vv.getV1();
			Node v2 = vv.getV2();
			ret = removeEdge(v1, v2);
		}
		return ret;
	}
	
	/**
	 ���ͼ*/
	public void clear() {
		if(!this.isEmpty()) {
			/*
			Set<Integer> kSet = this._nodeList.keySet();		//id����
			Iterator<Integer> iter = kSet.iterator();		//������
			while(iter.hasNext()) {
				this.removeNode(iter.next());
			}
			this._edgeList.clear();
			*/
			Set<Integer> edgeKeySet = this._edgeList.keySet();		//id����
			Iterator<Integer> iter = edgeKeySet.iterator();		//������
			while(iter.hasNext()) {
				this._edgeList.get(iter.next()).clear();
			}
			this._edgeList.clear();
			this._nodeList.clear();
		}
	}
	
	/**
	 ��EdgeList�ļ�����һ��ͼ
	 @param file �ļ�·��
	 @param isDirected �Ƿ�Ϊ����ͼ
	 @param isWeighted �Ƿ�Ϊ��Ȩͼ
	 @return ���سɹ���ʧ��*/
	public boolean loadGraphFromEdgeListFile(String file, boolean isDirected, boolean isWeighted) {
		boolean ret = false;			//����ֵ	
		if(this.isEmpty()) {
			try {
	            File fp = new File(file);
	            BufferedReader bufread;
	            String read;
	            String[] numstr = null;
	            int id1 = -1, id2 = -1, weight;
	            Integer iWeight = null;
	            bufread = new BufferedReader(new FileReader(fp));
	            while ((read = bufread.readLine()) != null) {  //��ȡһ��
	            	numstr = read.split(" ");
	            	weight = 0;
	            	id1 = Integer.parseInt(numstr[0]);
	            	id2 = Integer.parseInt(numstr[1]);
	            	if(numstr.length > 3) {
	            		weight = Integer.parseInt(numstr[2]);			//��Ȩͼ���������û��Ȩֵ��weight = 0;
	            	}
	            	if(isDirected) {		//��Ȩͼ
	            		iWeight = weight;
	            	}
	            	else {
	            		iWeight = 1;
	            	}
	            	if(this.getNode(id1) == null) this.addNode(id1); 
	            	if(this.getNode(id2) == null) this.addNode(id2);
	            	if(this.getEdge(id1, id2) == null) this.addEdge(id1, id2, iWeight);
	            	if(isWeighted) {
	            		if(this.getEdge(id2, id1) == null) this.addEdge(id2, id1, iWeight);
	            	} 
	            }  
	            bufread.close();
	            this._isDirected = isDirected;
	            this._isWeighted = isWeighted;
	            ret = true;					//���سɹ�
	        } catch (FileNotFoundException ex) {  
	            ex.printStackTrace();  
	        } catch (IOException ex) {  
	            ex.printStackTrace();  
	        }
		}
		return ret;
	}
	
	public StringBuffer toMatrixString() {
		StringBuffer str = new StringBuffer();
		int n = this._nodeList.size();
		int i, j;
		ArrayList<Integer> idList = new ArrayList<Integer>();
		Set<Integer> nodeKeySet = this._nodeList.keySet();		//id����
		Iterator<Integer> iter = nodeKeySet.iterator();		//������
		int tempid = -1;									//��ʱ���v1��id
		while(iter.hasNext()) {
			tempid = iter.next();
			idList.add(tempid);
		}
		HashMap<Integer, Edge> v1Edges = null;
		for(i = -1; i < n; i++) {
			if(i >= 0) v1Edges = this._edgeList.get(idList.get(i));
			for(j = -1; j < n; j++) {
				if(i == -1) {							//�е�ͷ����һ�У�
					if(j == -1) str.append(' ');
					else str.append(idList.get(j));
					str.append("\t");
				}
				else {
					if(j == -1) str.append(idList.get(i));		//�е�ͷ����һ�У�
					else {
						boolean flag1 = true;
						if(v1Edges != null) {			//��ͷ�Ľڵ���ڿɴ�ڵ�
							Edge e = v1Edges.get(idList.get(j));			
							if(e != null) {					//�ýڵ�ɴ�
								if(this.isWeighted()) {			//��Ȩͼ
									str.append(e.weight());
								}
								else {							//����Ȩͼ
									str.append(1);
								}
								flag1 = false;
							}
						}
						if(flag1) {							//�в����ڻ��߽ڵ㲻�ɴ�
							if(this.isWeighted()) {				//��Ȩͼ
								str.append('i');					//����
							}
							else {								//����Ȩͼ
								str.append('0');
							}
						}
					}
					str.append("\t");
				}
			}
			if(i != n - 1) str.append('\n');
		}
		return str;
	}
}
