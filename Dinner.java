import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Dinner
{
	final static Lock lock = new ReentrantLock(true);
	private static ArrayList<Philosopher> philosopher;
	private static int philosophers;
	private static int food;
	private static char mode;

	public static void main(String[] args)
	{
		boolean[] forks; 
		InputReader data = new InputReader(args);

		/*Initializes variables*/
		set_philosopher(data.get_philosopher());
		set_philosophers(data.get_philosophers());
		set_food(data.get_food());
		set_mode(data.get_mode());

		//Create threads
		for (Philosopher p : philosopher) (new Thread(p)).start();
	}

	//Setters
	/*****************************************************/
	public static void set_philosopher(ArrayList<Philosopher> philosopher_list) { philosopher = philosopher_list; }
	public static void set_philosophers(int total_philosophers) { philosophers = total_philosophers; }
	public static void set_food(int food_quantity) { food = food_quantity; }
	public static void set_mode(char selected_mode) { mode = selected_mode; }
	//Getters
	/*****************************************************/
	public static int get_food() { return food; }
	public static char get_mode() { return mode; }
}