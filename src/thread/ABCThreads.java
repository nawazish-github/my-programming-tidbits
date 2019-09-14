package thread;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ABCThreads {
    static ReentrantLock lock = new ReentrantLock();
    static ArrayList<Character> letters = new ArrayList<>();
    static {
        letters.add('C');
        letters.add('B');
        letters.add('A');
    }

    public static void main(String[] args) {
        ABCThreads abcThreads = new ABCThreads();

        Condition conditionA = lock.newCondition();
        Condition conditionB = lock.newCondition();
        Condition conditionC = lock.newCondition();

        Thread threadA = new Thread(()->{
            lock.lock();
            try{
                while (true) {
                    if(letters.size() == 3){
                        System.out.println(Thread.currentThread().getName()+": "+abcThreads.getLetter());
                        conditionB.signal();
                        return;
                    }else{
                        conditionA.await();
                    }
                }
            }catch(Exception e){}
            finally {
                lock.unlock();
            }
        }, "Thread-A");


        Thread threadB = new Thread(()->{
            lock.lock();
            try{
                while (true) {
                    if(letters.size() ==2){
                        System.out.println(Thread.currentThread().getName()+": "+abcThreads.getLetter());
                        conditionC.signal();
                        return;
                    }else{
                        conditionB.await();
                    }
                }
            }catch(Exception e){}
            finally {
                lock.unlock();
            }
        }, "Thread-B");

        Thread threadC = new Thread(()->{
            lock.lock();
            try{
                while (true) {
                    if(letters.size() == 1){
                        System.out.println(Thread.currentThread().getName()+": "+abcThreads.getLetter());
                        return;
                    }else{
                        conditionC.await();
                    }
                }
            }catch(Exception e){}
            finally {
                lock.unlock();
            }
        }, "Thread-C");


        threadC.start();
        threadB.start();
        threadA.start();
    }

    private char getLetter(){
        return letters.remove(letters.size()-1);
    }
}
