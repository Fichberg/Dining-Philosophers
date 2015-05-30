import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor
{
	public static final Lock food_lock = new ReentrantLock(true);
	private static final Lock forks_lock = new ReentrantLock(true);
	private static int food;
	private static boolean[] forks;

	Monitor(int food, boolean[] forks)
	{
		this.food = food;
		this.forks = forks;
	}

	public static void set_food(int food_quantity) { food = food_quantity; }
	public static int get_food() { return food; }
}