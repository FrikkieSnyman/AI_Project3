import java.util.Scanner;

public class Main{
	
	public static void main(String[] args) {
		Trainer trainer = new Trainer(0.1,0.1,100,0.8,26,27,2,"../trainingFiles/");
		trainer.train();
	}
}