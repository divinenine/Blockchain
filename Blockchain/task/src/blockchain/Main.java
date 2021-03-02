package blockchain;

import blockchain.solution_stage4.Chatter;
import blockchain.solution_stage4.Message;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Main {


    static LinkedList<Block> blockchain = new LinkedList<>();
    static String zeroNumString;
    static File file;
    static int testMsgId;
   // static int poolSize = Runtime.getRuntime().availableProcessors();
    static ExecutorService executorService = Executors.newFixedThreadPool(5);
    static Queue<Message> pendingMessages = new ArrayDeque<>();
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        file = new File("C:\\Users\\Major\\IdeaProjects\\Blockchain\\Blockchain\\task\\src\\blockchain\\blockchain.bcn");
        try {
            boolean createdNew = file.createNewFile();
            if (createdNew) {
           //     System.out.println("The file was successfully created.");
            } else {
               blockchain = (LinkedList<Block>) SerializationUtils.deserialize(file.getAbsolutePath());
         //      System.out.println(blockchain);
          //      System.out.println("Reading from file...");
            }
        } catch (IOException | ClassNotFoundException e) {
        //    System.out.println("Cannot create the file: " + file.getPath());
        }

       // List<Future<String>> listFuture = new ArrayList<>();

        List<Callable<Block>> listMiners = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            Miner miner = new Miner();
            miner.setId(i);
            listMiners.add(miner);
        }


        for (int i = 0; i < 5; i++) {
            StringBuilder sb = new StringBuilder();
            Block resultBlock = executorService.invokeAny(listMiners);
            blockchain.add(resultBlock);
            sb.append(resultBlock.toString());

            sb.append("Block data: ").append("\n").append(blockchain.getLast().getData());

            long seconds = blockchain.getLast().getSeconds();
            sb.append("Block was generating for ").append(seconds).append(" seconds").append("\n");
            if (seconds < 10) {
                Block.increaseComplexity();
                sb.append("N was increased to ").append(Block.getComplexity()).append("\n");
            } else if (seconds > 10 && seconds < 60) {
                sb.append("N stays the same");
            } else if (seconds > 60) {
                Block.decreaseComplexity();
                sb.append("N was decreased to ").append(Block.getComplexity()).append("\n");
            }


            System.out.println(sb.toString());

        }
        executorService.shutdown();


    }

    public static void queueMessage(Message message) {
        synchronized (pendingMessages) {
            pendingMessages.add(message);
        }
    }



    public static boolean validateBlockchain() {
        boolean chainIsValid = true;
        int id = blockchain.getLast().getId();
        int counter = blockchain.size() - 1;
        while (true) {
            System.out.println(id);
            if (id == 1) {
                break;
            } else {
                String prevHash = blockchain.get(counter).getHashPrev();
                Block prevBlock = blockchain.get(counter - 1);
                String hash = prevBlock.getHash();
                if (!hash.equals(prevHash)) {
                    chainIsValid = false;
                    break;
                }
                id = prevBlock.getId();
                counter--;
            }

        }
        return chainIsValid;
    }


}
