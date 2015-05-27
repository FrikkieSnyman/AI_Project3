import java.util.Random;
public class RandomGenerator{

	public static  Double randomDouble(Double min, Double max){
		Random r = new Random();
		return min + (max - min) * r.nextDouble();
	}

	public static int randomInteger(Integer min, Integer max){
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min; // inclusive
	}
}