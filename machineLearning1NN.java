package mobileKNN;

public class machineLearning1NN {
	public static void Main(String[] args){
		//������������
		//��Ҫ������ʵ������rssi��trainset֮����ѡ������������ĵ�
		//�������ݶ����������ģ�trainset����ά���飬xy�����꣬ÿ�������Ӧ��5��ap�ź�������飬��x*y*5����ά���顣
		
	}
	
	public static int[] getCoordinate(int[][][] trainset, int[] rssiDetected, int xbound, int ybound){
		int minManhattanDistance = getManhattanDistance(trainset[0][0], rssiDetected);
		for(int i = 0; i < xbound; i++){
			for(int j = 0; j < ybound; j++){
				int tempManhattanDistanc = getManhattanDistance(trainset[i][j], rssiDetected);
				if(minManhattanDistance > tempManhattanDistanc){
					minManhattanDistance = tempManhattanDistanc;
				}
			}
		}
		int[] coordinate = {0,0};
		for(int i = 0; i < xbound; i++){
			for(int j = 0; j < ybound; j++){
				if(minManhattanDistance == getManhattanDistance(trainset[i][j], rssiDetected)){
					coordinate[0] = i;
					coordinate[1] = j;
					return coordinate;
				}
			}
		}
		return coordinate;
		
	}
	
	
	
	public static int getManhattanDistance(int[] coordinate, int[] predict){
		int result = 0;
		for (int i = 0; i < coordinate.length; i++){
			result = result + Math.abs(coordinate[i] - predict[i]);
		}
		return result;
	}
}
