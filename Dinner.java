import java.util.ArrayList;

public class Dinner
{
	private static ArrayList<Philosopher> philosopher;
	private static int philosophers;
	private static int food;
	private static char mode;

	public static void main(String[] args)
	{
		InputReader data = new InputReader(args);

		set_philosopher(data.get_philosopher());
		set_philosophers(data.get_philosophers());
		set_food(data.get_food());
		set_mode(data.get_mode());
/*


		for(int i = 1; i < 10; i++)
		{	
			Philosopher p = new Philosopher(i);
			philosopher.add(p);
			
		}
*/
		for (Philosopher p : philosopher) 
		{
			(new Thread(p)).start();
//			System.out.println("Philosopher "+p+" number " +p.get_number());
		}
	}

	//Setters
	/*****************************************************/
	private static void set_philosopher(ArrayList<Philosopher> philosopher_list)
	{
		philosopher = philosopher_list;
	}

	private static void set_philosophers(int total_philosophers)
	{
		philosophers = total_philosophers;
	}

	private static void set_food(int food_quantity)
	{ 
		food = food_quantity;
	}

	private static void set_mode(char selected_mode)
	{
		mode = selected_mode;
	}
}