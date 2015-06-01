import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor
{
	private static final Lock forks_lock = new ReentrantLock(true);
	private static boolean[] forks;

	Monitor(boolean[] forks)
	{
		this.forks = forks;
	}

}