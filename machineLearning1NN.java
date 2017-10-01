package mobileKNN;

public class machineLearning1NN {
	public static void Main(String[] args){
		//这是两个方法
		//主要功能是实现输入rssi和trainset之后，挑选出最符合条件的点
		//所有数据都是用数组存的，trainset是三维数组，xy是坐标，每个坐标对应由5个ap信号组成数组，即x*y*5的三维数组。
		
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
