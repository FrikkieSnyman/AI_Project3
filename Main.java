import java.util.Scanner;

public class Main{
	
	public static void main(String[] args) {
		if (args.length == 1 ){
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter the learningrate:");
			Double learningrate = scanner.nextDouble();
			System.out.println("Enter the momentum:");
			Double momentum = scanner.nextDouble();
			System.out.println("Enter the maximum number of epochs:");
			Integer epoch = scanner.nextInt();
			System.out.println("Enter the number of hidden nodes");
			Integer hiddenNodes = scanner.nextInt();
			Trainer trainer = new Trainer(learningrate,momentum,epoch,0.8,26,100,2,args[0]);
			trainer.train();
		} else {
			System.out.println("Incorrect number of arguments. Use java main [pathToTrainingFolder]");
		}
	}
}