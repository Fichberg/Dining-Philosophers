import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor
{
	//True -> fork is available; False -> fork is unavailable
	private boolean[] forks;

	private int total_forks;
	private int remaining;

	private final Lock[] lock;
	private final Condition[] available;
	private final Condition more_forks;

	Monitor(boolean[] forks)
	{
		this.forks = forks;

		remaining = forks.length;
		total_forks = forks.length;

		lock = new Lock[total_forks + 1];
		available = new Condition[total_forks];

		for(int i = 0; i < total_forks; i++)
		{
			lock[i] = new ReentrantLock(true);
			available[i] = lock[i].newCondition();		
		}

		lock[total_forks] = new ReentrantLock(true);
		more_forks = lock[total_forks].newCondition();
	}

	//Attempts to get fork i and fork (i + 1) % forks.length, where 0 <= i < forks.length
	public void get_forks(int fork, int food) throws InterruptedException
	{
		lock[total_forks].lock();
		try {
			while(remaining <= 1) wait(more_forks);
			remaining--;
		} finally {
			lock[total_forks].unlock();
		}

		//Get left fork
		lock[fork].lock();
		try {
	        if(food > 0) while (forks[fork] == false) wait(available[fork]);
	        forks[fork] = false;
        } finally {
            lock[fork].unlock();
        }

        //Get right fork
        lock[(fork + 1) % forks.length].lock();
		try {
	        if(food > 0) while (forks[(fork + 1) % forks.length] == false) wait(available[(fork + 1) % forks.length]);
			
	        lock[total_forks].lock();
			try {
				while(remaining == 0) wait(more_forks);
				remaining--;
			} finally {
				lock[total_forks].unlock();
			}

	        forks[(fork + 1) % forks.length] = false;
        } finally {
            lock[(fork + 1) % forks.length].unlock();
        }
	}

	//Put down the forks
	public void put_forks(int fork, int food) throws InterruptedException
	{	
        lock[total_forks].lock();
		try {
			remaining++;
			signal(more_forks);
		} finally {
			lock[total_forks].unlock();
		}

		//Put left fork
		lock[fork].lock();
		try {

        	forks[fork] = true;
        	signal(available[fork]);

        } finally {
            lock[fork].unlock();
        }


		lock[total_forks].lock();
		try {
			remaining++;
			signal(more_forks);
		} finally {
			lock[total_forks].unlock();
		}
		
		//Put right fork
		lock[(fork + 1) % forks.length].lock();
		try {
        	forks[(fork + 1) % forks.length] = true;
        	signal(available[(fork + 1) % forks.length]);

        } finally {
            lock[(fork + 1) % forks.length].unlock();
        }


	}

	//Aliases
	public void wait(Condition cv) throws InterruptedException { cv.await(); }
	public void signal(Condition cv) throws InterruptedException { cv.signal(); }
}