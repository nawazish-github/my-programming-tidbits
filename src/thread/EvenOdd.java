package thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class EvenOdd {

	private static final int MAX = 10;
	private static int c;
	private static final ReentrantLock lock = new ReentrantLock();
	private static Condition isEven = lock.newCondition();
	private static Condition isOdd = lock.newCondition();
	
	public static void main(String[] args) {
		new Thread(()->{
			printEven();
		} ,"even-thread").start();
		
		new Thread(()->{
			printOdd();
		},"odd-thread").start();
	}
	
	public static void printEven(){
		while (c<=MAX) {
			try {
				lock.lock();
				if (c % 2 == 0) {
					System.out.println("num: " + c++ + " : "
							+ Thread.currentThread().getName());
					isOdd.signal();
				}
				isEven.await();
			} catch (InterruptedException ignore) {
			} finally {
				lock.unlock();
			}
		}
	}

	public static void printOdd(){
		while (c<=MAX) {
			try {
				lock.lock();
				if (c % 2 != 0) {
					System.out.println("num: " + c++ + " : "
							+ Thread.currentThread().getName());
					isEven.signal();
				}
				isOdd.await();
			} catch (InterruptedException ignore) {
			} finally {
				lock.unlock();
			}
		}
	}
}
