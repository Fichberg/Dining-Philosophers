import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

public class Philosopher implements Runnable
{
	private int number;
	private int weight;
	private int consumed;
	private State state;

	public Philosopher(int number, int weight)
	{
		this.number = number;
		this.weight = weight;
		this.consumed = 0;
		this.state = State.THINKING;
	}

	public void run()
	{
		while(Monitor.get_food() > 0)
		{
			think();
			//TODO: Monitor.get_fork
			eat();
			//TODO: Monitor.put.fork
		}

		System.out.println("Philosopher" + this.number+" consumed "+ this.consumed);
	}

	//Getters
	/**********************************************************************/
	public int get_number() { return this.number; }
	public int get_weight() { return this.weight; }
	public int get_consumed() { return this.consumed; }
	/**********************************************************************/
	//Other methods
	//Changes the philosopher's state
	private void change_state()
	{
		if(this.state == State.THINKING) this.state = State.EATING;
		else this.state = State.THINKING;
	}

	//Check if philosopher is thinking
	private boolean is_thinking() { return this.state == State.THINKING; }

	//Check if philosopher is eating
	private boolean is_eating() { return this.state == State.EATING; }

	//Consumes the food from the dinner
	private void eat() 
	{ 
		Monitor.food_lock.lock();
		try 
		{
			if(Monitor.get_food() > 0)
			{
				if(is_thinking()) change_state();
				if(Dinner.get_mode() == 'U') 
				{
					Monitor.set_food(Monitor.get_food() - 1);
					this.consumed += 1;
				}
				else 
				{
					Monitor.set_food(Monitor.get_food() - this.weight);
					this.consumed += this.weight;
				}
			}
		}
		finally {
            Monitor.food_lock.unlock();
            if(is_eating()) change_state();
	    }
	}

	//The philosopher is focused in his thoughts for the next t seconds.
	private void focus(int t) throws InterruptedException
	{
		Thread.sleep(t);
	}

	//The philosopher starts to think
	private void think()
	{
		try {
			int max = Dinner.get_philosophers() * 50, min = Dinner.get_philosophers(); //5s maximum and 0.5s minimum time
			Random rand = new Random();
			focus(rand.nextInt((max - min) + 1) + min);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}