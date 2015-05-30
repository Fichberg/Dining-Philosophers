import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class InputReader
{
	private ArrayList<Philosopher> philosopher = new ArrayList<Philosopher>();
	private int philosophers;
	private int food;
	private char mode;

	public InputReader(String[] args)
	{
		if(!correct_number_of_arguments(args.length)) use();
		if(!good_file(args[0])) bad_file(args[0]);
		if(!good_food_value(args[1])) bad_food_value(args[1]);
		if(!good_mode_value(args[2])) bad_mode_value(args[2]);
	}

	//Methods
	//Getters
	/***********************************************************************************************/
	public ArrayList<Philosopher> get_philosopher() { return this.philosopher; }
	public int get_philosophers() { return this.philosophers; }
	public int get_food() { return this.food; }
	public char get_mode() { return this.mode; }	
	/***********************************************************************************************/
	//Input-read related methods

	//Creates a new philosopher object and add it to the philosopher list
	private void create_philosopher(int number, int weight, boolean[] forks)
	{
		Philosopher p = new Philosopher(number, weight, forks);
		(this.philosopher).add(p);
	}

	//Check if the number of arguments is correct
	private boolean correct_number_of_arguments(int length) { return length == 3; }
	
	//See if the file is ok
	private boolean good_file(String filename)
	{
		String fname = System.getProperty("user.dir") + "/inputs/" + filename;
		File f = new File(fname);
		
		if(f.exists() && !f.isDirectory() && good_extension(filename))
		{
			try(BufferedReader buffr = new BufferedReader(new FileReader(fname)))
			{
				int i = 1;
				String line;

				while ((line = buffr.readLine()) != null) 
				{
					if(i == 1) 
					{
						if(!is_integer(line)) return false;
						this.philosophers = Integer.parseInt(line);
					}
					if(i == 2)
					{
						boolean[] forks;
						String[] weight = line.split(" ");
						
						/*Allocates the forks and initializes. Forks and the food are the resources in the philosophers' problem*/
						forks = new boolean[this.philosophers];
						for(int j = 0; j < this.philosophers; j++) forks[j] = true;
						
						for(int j = 0; j < this.philosophers; j++)
						{
							if(!is_integer(weight[j])) return false;
							create_philosopher(j + 1, Integer.parseInt(weight[j]), forks);
						}
						break;
					}
					i++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	//Checks if third argument is ok
	private boolean good_mode_value(String mode)
	{
		if(mode.equals("u") || mode.equals("U") || mode.equals("p") || mode.equals("P"))
		{
			this.mode = Character.toUpperCase(mode.charAt(0));
			return true;
		}
		return false;
	}

	//Checks if second argument is ok
	private boolean good_food_value(String food)
	{
		if(!is_integer(food)) return false;
		this.food = Integer.parseInt(food);
		return true;
	}

	//Must be integer number
	private boolean is_integer(String str)
	{
		try { 
	        Integer.parseInt(str); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}

	//Checks for .txt extension
	private boolean good_extension(String filename)
	{
		if(filename.contains(".")) 
		{
			String extension = filename.substring(filename.lastIndexOf("."), filename.length());
			return extension.equals(".txt");
		}
		else return false;
	}

	//Prints how to use program
	private void use()
	{
		System.out.print("Incorrect number of arguments. Use:\n\n\tjava  Dinner  <filename.txt>  R  U|P\n\nWhere:\n");
		System.out.print("filename contains the information about the philosophers (total number of philosophers and the weight of each of them. All integers).\n");
		System.out.print("R is an integer representing the total quantity of food.\n");
		System.out.print("U or P is the mode.\n1. P: food distribution is proportional to philosophers' weight.\n");
		System.out.print("2. U: uniform distribution of food between the philosophers.\n");
		System.exit(0);
	}

	//Prints the possible file problems that might have occured
	private void bad_file(String filename)
	{
		System.out.print("Bad file format. One of the following possilibities may have occured:\n");
		System.out.print("1. Couldn't find file: " + System.getProperty("user.dir") + "/inputs/" + filename + "\n");
		System.out.print("2. Bad format. Expecting .txt\n");
		System.out.print("3. Bad syntax. Expected the following:\n\n\tN\n\tw1 w2 w3 ... wN\n\nWhere:\n");
		System.out.print("N > 2 is an integer representing the number of philosophers to join the dinner.\n");
		System.out.print("wI, 1 <= I <= N is a list of the philosophers' weight. All wI are integers.\n");
		System.out.print("BE CAREFUL WITH THE SPACES!\n");
		System.exit(0);
	}

	//Prints that the program got a bad value for second argument
	private void bad_food_value(String food)
	{
		System.out.println("Got a bad value for second argument. Found the non-integer value \"" + food + "\".");
		System.exit(0);
	}

	//Prints that the program got a bad value for third argument
	private void bad_mode_value(String mode)
	{
		System.out.println("Got a bad value for third argument. Found the string value \"" + mode + "\" and was expecting [u|U|p|P].");
		System.exit(0);
	}
}