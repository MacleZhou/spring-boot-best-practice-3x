package com.macle.study.blockchain;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {

    private List<Block> chain;

    public Blockchain() {
        chain = new ArrayList<>();
        // Add the genesis block
        chain.add(createGenesisBlock());
    }

    private Block createGenesisBlock() {
        return new Block(0, "Genesis Block", "0");
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public void addBlock(Block newBlock) {
        newBlock.setPreviousHash(getLatestBlock().getHash());
        newBlock.setHash(newBlock.calculateHash());
        chain.add(newBlock);
    }

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                return false;
            }
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }

    // Getters and setters

    @Override
    public String toString() {
        StringBuilder blockchainString = new StringBuilder();
        for (Block block : chain) {
            blockchainString.append(block.toString()).append("\n");
        }
        return blockchainString.toString();
    }
}
