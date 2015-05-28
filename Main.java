import java.util.Scanner;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
			System.out.println("Enter the percentage size of the trainingset:");
			Double percentage = scanner.nextDouble();
			System.out.println("Number of trials:");
			Integer n = scanner.nextInt();
			for (int i =0 ; i < n; ++i){
				Trainer trainer = new Trainer(learningrate,momentum,epoch,percentage,26,hiddenNodes,2,args[0]);
				trainer.train(i);
			}
			generateAverageFile(learningrate,momentum,epoch,percentage,hiddenNodes);
		} else {
			System.out.println("Incorrect number of arguments. Use java main [pathToTrainingFolder]");
		}
	}

	private static void generateAverageFile(Double learningrate, Double momentum, Integer epoch, Double percentage, Integer hiddenNodes){
		System.out.println("Caclculating averages...");
		int n = 0;
		double[] taTotal = null;
		double[] gaTotal = null;
	  	File dir = new File("files/");
	  	File[] directoryListing = dir.listFiles();
	  	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");
		Calendar cal = Calendar.getInstance();
	  	PrintWriter writer = null;
		try{
			writer = new PrintWriter("averages/"+learningrate+" " +momentum+ " "+ epoch + " " + percentage + " " + hiddenNodes+" "+dateFormat.format(cal.getTime())+".csv","UTF-8");
			writer.println("Epoch,Training Accuracy,Generalization Accuracy");
		} catch (Exception e){
			e.printStackTrace();
		}

	  	if (directoryListing != null) {
	  		n = directoryListing.length;
	  		taTotal = new double[epoch];
	  		gaTotal = new double[epoch];
	    	for (File child : directoryListing) {
	      		List<String> lines = readFile(child.toString());
	      		for (int i = 1; i < lines.size(); ++i){
	      			String[] split = lines.get(i).split(",");
	      			taTotal[i-1] += Double.parseDouble(split[1]);
	      			gaTotal[i-1] += Double.parseDouble(split[2]);
	      		}
	      		child.delete();
	    	}
	  	}
	  	for (int i = 0; i < epoch; ++i){
	  		writer.println(i+","+taTotal[i]/n+","+gaTotal[i]/n);
	  	}
	  	writer.close();
	  	System.out.println("Done!");
	}


	private static List<String> readFile(String path){
		List<String> lines = null;
		try{
			lines = Files.readAllLines(Paths.get(path), Charset.defaultCharset());
		} catch(IOException e){
			e.printStackTrace();
		}
		return lines;
	}
}