import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

public class Philosopher implements Runnable
{
	private int number;
	private int weight;
	private int consumed;
	private int meals;
	private State state;
	private Monitor monitor;

	public Philosopher(int number, int weight, Monitor monitor)
	{
		this.number = number;
		this.weight = weight;
		this.consumed = 0;
		this.meals = 0;
		this.state = State.THINKING;
		this.monitor = monitor;
	}

	public void run()
	{
		while(Dinner.have_food())
		{
			try{
				think();
				monitor.get_forks(number - 1, Dinner.get_food());
				eat();
				monitor.put_forks(number - 1, Dinner.get_food());
			}
			catch (InterruptedException e) {
				System.out.println(e);
			}
		}
		
		if(Dinner.get_mode() == 'U') System.out.println("Philosopher" + this.number+" consumed "+ this.consumed + " ("+meals+" meals).");
		else /*mode == P*/ System.out.println("Philosopher" + this.number+" (weight: "+ this.weight +") consumed "+ this.consumed + " ("+meals+" meals).");
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
		Dinner.food_lock.lock();
		try 
		{
			if(Dinner.get_food() > 0)
			{
				if(is_thinking()) change_state();
				degustate();

				if(Dinner.get_mode() == 'U') 
				{
					Dinner.set_food(Dinner.get_food() - 1);
					this.consumed += 1;
				}
				else 
				{
					if(Dinner.get_food() >= this.weight)
					{
						Dinner.set_food(Dinner.get_food() - this.weight);
						this.consumed += this.weight;
					}
					else
					{
						this.consumed += Dinner.get_food();
						Dinner.set_food(0);
					}
				}
				meals++;
			}
		}
		finally {
            Dinner.food_lock.unlock();
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
			int max = Dinner.get_philosophers() * 100, min = Dinner.get_philosophers(); //4000 -> 4s
			System.out.println(Dinner.get_chronometer().get_time() + "Philosopher #" + this.number + " is "+state+". Hmmmm... So focused...");
			Random rand = new Random();
			focus(rand.nextInt((max - min) + 1) + min);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private void degustate()
	{
		try {
			System.out.println(Dinner.get_chronometer().get_time() + "Philosopher #" + this.number + " is "+state+". Ohhh, so tasty... delicious! Splendid!");
			digest();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	private void digest() throws InterruptedException
	{
		Thread.sleep(50); //Philosophers degustate and then digest for a fixed amount of 0.05s. They eat REALLY fast!
	}
}