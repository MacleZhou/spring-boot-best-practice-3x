package com.macle.study.blockchain;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blockchain")
public class BlockchainController {

    private Blockchain blockchain;

    public BlockchainController() {
        blockchain = new Blockchain();
    }

    @PostMapping("/addBlock")
    public String addBlock(@RequestBody String data) {
        Block newBlock = new Block(blockchain.getLatestBlock().getIndex() + 1, data, blockchain.getLatestBlock().getHash());
        blockchain.addBlock(newBlock);
        return "Block added: " + newBlock.toString();
    }

    @GetMapping("/getBlockchain")
    public String getBlockchain() {
        return blockchain.toString();
    }

    @GetMapping("/isValid")
    public String isValid() {
        return "Blockchain is valid: " + blockchain.isChainValid();
    }
}