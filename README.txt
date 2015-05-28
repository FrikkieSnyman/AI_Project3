To compile:
	Open a terminal, and navigate to code, then type
	javac *.java -d build/

To run:
	Navigate to build/ and type

	Linux:
	java Main ../trainingFiles/

	Windows
	java Main ..\trainingFiles\

When running the program, you'll be prompted for some inputs. Be sure to provide input as stated below:

	Learning rate: 		Positive Double value
	Momentum: 			Positive Double value
	Max Epochs:			Positive non-zero integer value
	Hidden nodes:		Positive non-zero integer value
	Trainingset prec:	Positive Double value between 0.0 and 1.0
	Trials:				Positive non-zero integer


Within the build/ directory, there are two other directories, namely
	averages/
	files/

These directories are used to determine the averages over the number of trials as specified.

The averages are stored in a csv file in the averages directory.