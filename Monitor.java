import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor
{
	//True -> fork is available; False -> fork is unavailable
	private boolean[] forks;

	private final Lock[] lock;
	private final Condition[] available;
	private final Condition[] unavailable;

	Monitor(boolean[] forks)
	{
		this.forks = forks;
		
		lock = new Lock[forks.length];
		available = new Condition[forks.length];
		unavailable = new Condition[forks.length];

		for(int i = 0; i < forks.length; i++) 
		{
			lock[i] = new ReentrantLock(true);
			available[i] = lock[i].newCondition();
			unavailable[i] = lock[i].newCondition();
		}
	}

	//Attempts to get fork i and fork (i + 1) % forks.length, where 0 <= i < forks.length
	public void get_forks(int fork) throws InterruptedException
	{
		lock[fork].lock();
		lock[(fork + 1) % forks.length].lock();

		try {
       
        while (forks[fork] == false) wait(available[fork]);
        forks[fork] = false;

        while (forks[(fork + 1) % forks.length] == false) wait(available[fork]);
        forks[(fork + 1) % forks.length] = false;

        signal(unavailable[fork]);
        signal(unavailable[(fork + 1) % forks.length]);

        } finally {
            lock[fork].unlock();
            lock[(fork + 1) % forks.length].unlock();
        }
	}

	//Put down the forks
	public void put_forks(int fork) throws InterruptedException
	{
		lock[fork].lock();
		lock[(fork + 1) % forks.length].lock();

		try {
       
        while (forks[fork] == true) wait(unavailable[fork]);
        forks[fork] = true;

        while (forks[(fork + 1) % forks.length] == true) wait(unavailable[fork]);
        forks[(fork + 1) % forks.length] = true;

        signal(available[fork]);
        signal(available[(fork + 1) % forks.length]);

        } finally {
            lock[fork].unlock();
            lock[(fork + 1) % forks.length].unlock();
        }
	}


	//Aliases
	private void wait(Condition cv) throws InterruptedException { cv.await(); }
	private void signal(Condition cv) throws InterruptedException { cv.signal(); }
}