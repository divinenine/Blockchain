package blockchain.mysolution;

import blockchain.solution_stage4.Message;
import blockchain.solution_stage4.MessageList;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Callable;

import static blockchain.mysolution.Main.*;
import static java.util.stream.Collectors.toList;

public class Miner implements Callable<Block> {

    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public Block call() throws Exception {
        Block block = null;
        String blockString = "";
        try {
        synchronized (pendingMessages) {
            queueMessage(new Message("Bot " + id, UUID.randomUUID().toString()));
            block = generateBlock(id);
            pendingMessages.clear();
        }

          //  Thread.sleep(5000);
        } catch (IOException  e) {
            e.printStackTrace();
        }
        return block;
    }

    public Block generateBlock(int id) throws IOException {
        StringBuilder blockString = new StringBuilder();
        int complexity = Block.getComplexity();
        StringBuilder sb = new StringBuilder();
        sb.append("0".repeat(Math.max(0, complexity)));
        Main.zeroNumString = sb.toString();
        Block block = new Block(id, pendingMessages.isEmpty()
                ? null
                : new MessageList(pendingMessages.stream().collect(toList())));

        if (!blockchain.isEmpty()) {
            block.id = blockchain.getLast().getId() + 1;
          //  System.out.println(blockchain.size());
        }

     //   blockString.append(blockchain.getLast());


        try {
            SerializationUtils.serialize(blockchain, Main.file.getAbsolutePath());
            //      System.out.println("File saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return block;

    }

}
