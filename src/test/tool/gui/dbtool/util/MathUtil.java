package test.tool.gui.dbtool.util;

public class MathUtil {

	public static int max(int... arr) {
		if(arr == null || arr.length == 0){
			throw new RuntimeException("入参为null，或者待比较数组中元素个数为0！");
		}
		if(arr.length == 1){
			return arr[0];
		}
		int max = arr[0];
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
			}
		}
		return max;
	}
}
