package com.et.bc.model;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.6.0.
 */
public class Lottery extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060008054600160a060020a0319163317905561051f806100326000396000f3fe6080604052600436106100615763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663481c6a7581146100665780635d495aea146100975780638b5b9ccc146100ae578063e97dcb6214610113575b600080fd5b34801561007257600080fd5b5061007b61011b565b60408051600160a060020a039092168252519081900360200190f35b3480156100a357600080fd5b506100ac61012a565b005b3480156100ba57600080fd5b506100c361023a565b60408051602080825283518183015283519192839290830191858101910280838360005b838110156100ff5781810151838201526020016100e7565b505050509050019250505060405180910390f35b6100ac610317565b600054600160a060020a031681565b600054600160a060020a031633146101a357604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820181905260248201527f4f6e6c79204d616e616765722063616e207069636b207468652077696e6e6572604482015290519081900360640190fd5b6001546000906101b16103dc565b8115156101ba57fe5b06905060006001828154811015156101ce57fe5b6000918252602082200154604051600160a060020a03909116925030918391833180156108fc0292909190818181858888f19350505050158015610216573d6000803e3d6000fd5b50604080516000815260208101918290525161023491600191610450565b50505050565b600054606090600160a060020a031633146102b657604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820181905260248201527f4f6e6c79204d616e616765722063616e207069636b207468652077696e6e6572604482015290519081900360640190fd5b600180548060200260200160405190810160405280929190818152602001828054801561030c57602002820191906000526020600020905b8154600160a060020a031681526001909101906020018083116102ee575b505050505090505b90565b662386f26fc10000341161038c57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601d60248201527f4d7573742068617665206174206c6561737420302e3031206574686572000000604482015290519081900360640190fd5b6001805480820182556000919091527fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf601805473ffffffffffffffffffffffffffffffffffffffff191633179055565b60004442600160405160200180848152602001838152602001828054801561042d57602002820191906000526020600020905b8154600160a060020a0316815260019091019060200180831161040f575b505060408051601f19818403018152919052805160209091012094505050505090565b8280548282559060005260206000209081019282156104b2579160200282015b828111156104b2578251825473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03909116178255602090920191600190910190610470565b506104be9291506104c2565b5090565b61031491905b808211156104be57805473ffffffffffffffffffffffffffffffffffffffff191681556001016104c856fea165627a7a7230582055bd529f290c2d8726d80485d1329183ac4fdaae166ff9a71e635598f1acec780029";

    public static final String FUNC_MANAGER = "manager";

    public static final String FUNC_PICKWINNER = "pickWinner";

    public static final String FUNC_GETPLAYERS = "getPlayers";

    public static final String FUNC_ENTER = "enter";

    @Deprecated
    protected Lottery(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Lottery(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Lottery(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Lottery(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<String> manager() {
        final Function function = new Function(FUNC_MANAGER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> pickWinner() {
        final Function function = new Function(
                FUNC_PICKWINNER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<List> getPlayers() {
        final Function function = new Function(FUNC_GETPLAYERS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteCall<TransactionReceipt> enter(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_ENTER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public static RemoteCall<Lottery> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Lottery.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<Lottery> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Lottery.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Lottery> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Lottery.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Lottery> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Lottery.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static Lottery load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Lottery(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Lottery load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Lottery(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Lottery load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Lottery(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Lottery load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Lottery(contractAddress, web3j, transactionManager, contractGasProvider);
    }
}
