package blockchain;

import java.util.Date;

public class Recursive {


    public static void main(String[] args) {
        BlockChain blockChain = new BlockChain();
        blockChain.print();
        for (int i = 0; i < 4; i++) {
            BlockChain nextBlock = blockChain.generateBlock();
            nextBlock.print();
            blockChain = nextBlock;
        }
    }

}

class BlockChain {

    private int id;
    private String prevHash;
    private BlockChain prevBlock;
    long timeStamp = new Date().getTime();

    BlockChain() {
        prevHash = String.valueOf("0");
        id = 1;
    }

    private BlockChain (String hash, int id, BlockChain block) {
        this.prevHash = hash;
        this.id = id + 1;
        this.prevBlock = block;
    }

    String generateHash() {
     return    StringUtil.applySha256(prevHash + id + timeStamp);
    }

    BlockChain generateBlock() {
        String currentHash = generateHash();
        return new BlockChain(currentHash, this.id, this);
    }

    void print() {
        String output = "Block:" +
                "\nId: " + id +
                "\nTimestamp: " + timeStamp +
                "\nHash of the previous block:\n" + prevHash +
                "\nHash of the block:\n" + generateHash() +
                "\n";
        System.out.println(output);


    }

    public BlockChain getPrevBlock() {
        return prevBlock;
    }

    static boolean validate(BlockChain lastBlock) {
        boolean chainIsValid = true;
        int id = lastBlock.id;
        while (true) {
            System.out.println(id);
            if (id == 1) {
                break;
            } else {
                String prevHash = lastBlock.prevHash;
                BlockChain prevBlock = lastBlock.getPrevBlock();
                String hash = prevBlock.generateHash();
                if (!hash.equals(prevHash)) {
                    chainIsValid = false;
                    break;
                }
                id = prevBlock.id;
                lastBlock = prevBlock;
            }

        }
        return chainIsValid;



    }




}


