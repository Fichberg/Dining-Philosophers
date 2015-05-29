public class Philosopher implements Runnable
{
	private int number;
	private int weight;

	public Philosopher(int number, int weight)
	{
		this.number = number;
		this.weight = weight;
	}

	public void run()
	{
		System.out.println("Philosopher " + this.number + " weight " + this.weight);
	}


	public int get_number() { return this.number; }
	public int get_weight() { return this.weight; }
}