import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor
{
	//True -> fork is available; False -> fork is unavailable
	private boolean[] forks;

	private final Lock lock;
	private final Condition available;
	private final Condition unavailable;

	Monitor(boolean[] forks)
	{
		this.forks = forks;
		
		lock = new ReentrantLock(true);
		available = lock.newCondition();
		unavailable = lock.newCondition();
	}

	//Attempts to get fork i and fork (i + 1) % forks.length, where 0 <= i < forks.length
	public void get_forks(int fork, int food) throws InterruptedException
	{
		if(food > 0) {
			lock.lock();

			try {
	       
	        while (forks[fork] == false && forks[(fork + 1) % forks.length] == false) wait(available);
	        forks[fork] = false;
	        forks[(fork + 1) % forks.length] = false;

	        signal(unavailable);
	        
	        } finally {
	            lock.unlock();
	        }
		}
	}

	//Put down the forks
	public void put_forks(int fork, int food) throws InterruptedException
	{
		if(food > 0){
			lock.lock();

			try {
	       
	        while (forks[fork] == true && forks[(fork + 1) % forks.length] == true) wait(unavailable);
	        forks[fork] = true;
	        forks[(fork + 1) % forks.length] = true;

	        signal(available);

	        } finally {
	            lock.unlock();
	        }
    	}
	}

	//Aliases
	public void wait(Condition cv) throws InterruptedException { cv.await(); }
	public void signal(Condition cv) throws InterruptedException { cv.signal(); }
}