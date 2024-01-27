import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;

class Multiple extends Thread {
    private static AtomicInteger counter = new AtomicInteger(1);
    private static AtomicInteger tot = new AtomicInteger(0);
    private static AtomicLong sum = new AtomicLong(0);
    private static Long[] maxTen = new Long[10];


    @Override
    public void run()
    {
        long j = 0;
        while (counter.get() < 1e8)
        {
            j = counter.getAndIncrement();
            if (isPrime(j))
            {
                tot.getAndIncrement();
                sum.getAndAdd(j);
                
                // array keeps track of the 10 largest prime numbers
                for (int i = 9; i >= 0; i--)
                {
                    if (maxTen[i] == null)
                    {
                        maxTen[i] = j;
                    }
                    else if (j > maxTen[i])
                    {
                        long temp = maxTen[i];
                        maxTen[i] = j;
                        j = temp;
                    }
                }
            }
        }
    }

    public static AtomicInteger getCounter() { return counter; }
    public static AtomicInteger getTot() { return tot; }
    public static AtomicLong getSum() { return sum; }
    public static Long[] getMaxTen() { return maxTen; }

    // checks if a number if even by seeing if it has any multiples other than 1 or itself, but only up to the square root multiple
    public boolean isPrime(long n)
    {
        if (n <= 1)
        {
            return false;
        }

        if (n == 2)
        {
            return true;
        }

        int sqrtNum = (int)Math.sqrt(n);
        for (int i = 2; i <= sqrtNum; i++)
        {
            if (n % i == 0)
            {
                return false;
            }
        }
        return true;
    }

}

public class Assignment1
{
    public static void main(String[] args)
    {
        Multiple[] threads = new Multiple[8];
        long startT = System.currentTimeMillis();

        // spawn 8 threads and start, join them
        for (int i = 0; i < 8; i++) {
            threads[i] = new Multiple();
        }

        for (int i = 0; i < 8; i++) {
            threads[i].start();
        }

        for (Multiple thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endT = System.currentTimeMillis();

        try {
            File outputFile = new File("primes.txt");
            FileWriter writer = new FileWriter(outputFile);
            writer.append("" + (endT - startT));
            writer.append(" " + Multiple.getTot());
            writer.append(" " + Multiple.getSum());
            writer.append("\n");
            Long[] max = Multiple.getMaxTen();
            writer.append("" + max[0]);
            writer.append(" " + max[1]);
            writer.append(" " + max[2]);
            writer.append(" " + max[3]);
            writer.append(" " + max[4]);
            writer.append(" " + max[5]);
            writer.append(" " + max[6]);
            writer.append(" " + max[7]);
            writer.append(" " + max[8]);
            writer.append(" " + max[9]);
            writer.close(); 
        }
        catch (Exception e) {
            e.getStackTrace();
        }
    }
}