package com.example.demo.service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

@Service
public class BlockchainService {

    private final Web3j web3j;
    private final Credentials credentials;
    private final String nodeRegistryAddress;
    private final String reputationLedgerAddress;
    private final BigInteger gasPrice = BigInteger.valueOf(20_000_000_000L);
    private final BigInteger gasLimit = BigInteger.valueOf(4_300_000);

    public BlockchainService(@Value("${blockchain.rpc.url}") String rpcUrl,
                             @Value("${blockchain.private.key}") String privateKey,
                             @Value("${blockchain.nodeRegistry.address}") String nodeRegistryAddress,
                             @Value("${blockchain.reputationLedger.address}") String reputationLedgerAddress) {
        this.web3j = Web3j.build(new HttpService(rpcUrl));
        this.credentials = Credentials.create(privateKey);
        this.nodeRegistryAddress = nodeRegistryAddress;
        this.reputationLedgerAddress = reputationLedgerAddress;
    }

    public String registerNodeOnBlockchain(String nodeId, String publicKey) throws Exception {
        Function function = new Function(
                "registerNode",
                Arrays.asList(new Utf8String(nodeId), new Utf8String(publicKey)),
                Collections.emptyList()
        );
        return sendTransaction(function, nodeRegistryAddress);
    }

    public String updateReputationOnBlockchain(String nodeId, double reputationScore) throws Exception {
        // store score as uint256 with 4 decimal fixed-point (score * 10000)
        long scaled = Math.round(reputationScore * 10000.0);
        Function function = new Function(
                "updateReputation",
                Arrays.asList(new Utf8String(nodeId), new Uint256(BigInteger.valueOf(scaled))),
                Collections.emptyList()
        );
        return sendTransaction(function, reputationLedgerAddress);
    }

    public double getReputationFromBlockchain(String nodeId) throws IOException {
        Function function = new Function(
                "getReputation",
                Arrays.asList(new Utf8String(nodeId)),
                Arrays.asList(new TypeReference<Uint256>() {})
        );

        String encoded = FunctionEncoder.encode(function);
        Transaction ethCallTransaction = Transaction.createEthCallTransaction(credentials.getAddress(), reputationLedgerAddress, encoded);
        EthCall response = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).send();
        String value = response.getValue();
        if (value == null || value.equals("0x")) return 0.0;
        // decode
        java.util.List<org.web3j.abi.datatypes.Type> decoded = FunctionReturnDecoder.decode(value, function.getOutputParameters());
        if (decoded.isEmpty()) return 0.0;
        Uint256 v = (Uint256) decoded.get(0);
        long scaled = v.getValue().longValue();
        return scaled / 10000.0;
    }

    private String sendTransaction(Function function, String contractAddress) throws Exception {
        String encoded = FunctionEncoder.encode(function);

        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.PENDING).send();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        RawTransaction rawTx = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contractAddress, encoded);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTx, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
        if (ethSendTransaction.hasError()) {
            throw new RuntimeException("Transaction error: " + ethSendTransaction.getError().getMessage());
        }
        return ethSendTransaction.getTransactionHash();
    }

}
