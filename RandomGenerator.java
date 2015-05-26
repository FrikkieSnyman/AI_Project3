import java.util.Random;
public class RandomGenerator{

	public static  Double randomDouble(Double min, Double max){
		Random r = new Random();
		return min + (max - min) * r.nextDouble();
	}
}