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

	Monitor(boolean[] forks)
	{
		this.forks = forks;
		
		lock = new ReentrantLock(true);
		available = lock.newCondition();
	}

	//Attempts to get fork i and fork (i + 1) % forks.length, where 0 <= i < forks.length
	public void get_forks(int fork, int food) throws InterruptedException
	{
		lock.lock();

		try {
       
        if(food > 0) while (forks[fork] == false || forks[(fork + 1) % forks.length] == false) wait(available);
        forks[fork] = false;
        forks[(fork + 1) % forks.length] = false;

        } finally {
            lock.unlock();
        }
	}

	//Put down the forks
	public void put_forks(int fork, int food) throws InterruptedException
	{	
		lock.lock();

		try {

        forks[fork] = true;
        forks[(fork + 1) % forks.length] = true;

        signal(available);

        } finally {
            lock.unlock();
        }	
	}

	public void release_all() throws InterruptedException
	{ 
		 lock.lock();
		 for(int i = 0; i < forks.length; i++) forks[i] = true;
		 signal_all(available); Thread.sleep(100);
		 lock.unlock();
	}

	//Aliases
	public void wait(Condition cv) throws InterruptedException { cv.await(); }
	public void signal(Condition cv) throws InterruptedException { cv.signal(); }
	public void signal_all(Condition cv) throws InterruptedException { cv.signalAll(); }
}