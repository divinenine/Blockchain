package blockchain.mysolution;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static blockchain.mysolution.Main.blockchain;
import static blockchain.mysolution.Main.zeroNumString;

public class Block implements Serializable {

   // static LinkedList<Block> blocks = new LinkedList<>();
    public int id = 1;
    private long timeStamp;
   private String hash;
   private String hashPrev;
   private int magicNumber;
   private long seconds;
   private static int complexity = 0;
   private int minerId;


    private Serializable data;
    public Serializable getData() {
        return data;
    }

    public Block(int id, Serializable data) {
       if (blockchain.isEmpty()) {
            hashPrev = "0";
            timeStamp = new Date().getTime();
            hash = generateHash();
           // message = "no messages";
      //      blockchain.add(this);
           this.data = null;

        } else {
            //this.id = blockchain.getLast().id + 1;
            hashPrev = blockchain.getLast().hash;
            timeStamp = new Date().getTime();
            hash = generateHash();
           this.data = data;
      //     blockchain.add(this);
        }

       minerId = id;

    }

    public int getId() {
        return id;
    }

    public String generateHash() {
        long start = System.nanoTime();
        Random magicNumberGen = new Random();
        magicNumber = magicNumberGen.nextInt(Integer.MAX_VALUE);
        String hash = StringUtil.applySha256(magicNumber + hashPrev + String.valueOf(id)
                + String.valueOf(timeStamp));
        while (!hash.startsWith(zeroNumString)) {
            magicNumber = magicNumberGen.nextInt(Integer.MAX_VALUE);
            hash = StringUtil.applySha256(magicNumber + hashPrev + String.valueOf(id)
                    + String.valueOf(timeStamp));
      //     System.out.println("Magic: " + magicNumber);
      //      System.out.println("Gen Hash: " + hash);
        }
        long stop = System.nanoTime();
        long elapsedTime = stop - start;
     //   double seconds = (double) (stop - start) / 1_000_000_000.0;
       seconds = TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);



        return hash;
    }

    public String getHash() {
        return hash;
    }

    public long getSeconds() {
        return seconds;
    }

    public String getHashPrev() {
        return hashPrev;
    }


    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        String block = "Block:\n" +
                "Created by miner #" + minerId + "\n" +
                "Id: " + String.valueOf(id) + "\n" +
                "Timestamp:" + String.valueOf(timeStamp) + "\n" +
                "Magic number:" + magicNumber + "\n" +
                "Hash of the previous block: " + "\n" + hashPrev + "\n" +
                "Hash of the block:" + "\n" + hash + "\n";


        sb.append(block);



        return sb.toString();


    }

    public static int getComplexity() {
        return complexity;
    }

    public static void increaseComplexity() {
        complexity++;
    }
    public static void decreaseComplexity() {
        complexity--;
    }

    public void increaseId() {this.id = blockchain.getLast().getId() + 1; }
}
