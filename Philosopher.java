import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Philosopher implements Runnable
{
	private boolean[] forks;
	private int number;
	private int weight;
	private int consumed;
	private State state;

	public Philosopher(int number, int weight, boolean[] forks)
	{
		this.forks = forks;
		this.number = number;
		this.weight = weight;
		this.consumed = 0;
		this.state = State.THINKING;
	}

	public void run()
	{
		while(Dinner.get_food() > 0)
		{
			Dinner.lock.lock();
			try {
				if(Dinner.get_food() > 0)
				{
					if(is_thinking()) change_state();
					System.out.println("Philosopher" + this.number+" is " + this.state);
					System.out.println("Philosopher " + this.number + " weight " + this.weight);
					System.out.println("Yaaaaam " + Dinner.get_food()) ;
					consume();
					System.out.println("Awww... One less " + Dinner.get_food());
				}		
			} finally {
	            Dinner.lock.unlock();
	            if(is_eating()) change_state();
	            System.out.println("Philosopher" + this.number+" is " + this.state);
	            //Aqui ele precisa esperar

	        }
		}

		System.out.println("Philosopher" + this.number+" consumed "+ this.consumed);
	}

	//Setters
	public void consume() 
	{ 
		if(Dinner.get_mode() == 'U') 
		{
			Dinner.set_food(Dinner.get_food() - 1);
			this.consumed += 1;
		}
		else 
		{
			Dinner.set_food(Dinner.get_food() - this.weight);
			this.consumed += this.weight;
		}
	}

	//Getters
	public int get_number() { return this.number; }
	public int get_weight() { return this.weight; }
	public int get_consumed() { return this.consumed; }

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
}