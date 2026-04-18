package com.example.demo.service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.RawTransactionManager;


@Service
public class BlockchainService {

    private final Web3j web3j;
    private final Credentials credentials;

    // Use property value or fallback if not specified in configuration
    @Value("${blockchain.reputationLedger.address:0xf8e81D47203A594245E36C48e151709F0C19fBe8}")
    private String contractAddress;

    @Value("${blockchain.nodeRegistry.address:0xDA0bab807633f07f013f94DD0E6A4F96F8742B53}")
    private String nodeRegistryAddress;

    public BlockchainService(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
    }

    // =========================================================
    // 🔥 UPDATE REPUTATION
    // =========================================================
    public void updateReputation(String relayId, double trust) {
        try {
            System.out.println("Saving trust: " + trust + " for " + relayId);
            System.out.println("Contract address: " + contractAddress);
            System.out.println("Sender address: " + credentials.getAddress());

            // Fetch the dynamic Chain ID from Ganache (prevents 1337 vs 5777 issues)
            long chainId = web3j.ethChainId().send().getChainId().longValue();

            // Use the correct constructor to avoid EIP-155 replay protection errors
            RawTransactionManager txManager = new RawTransactionManager(web3j, credentials, chainId);

            // ✅ SCALE TRUST properly by 10000
            long scaledValue = (long)(trust * 10000);
            BigInteger scaled = BigInteger.valueOf(Math.max(0, scaledValue)); // Ensure it's not negative

            Function function = new Function(
                    "updateReputation",
                    Arrays.asList(
                            new Utf8String(relayId),
                            new Uint256(scaled)
                    ),
                    Collections.emptyList()
            );

            String encoded = FunctionEncoder.encode(function);

            // ✅ Safe Custom Gas Limits to prevent "exceeds block gas limit" errors!
            BigInteger gasPrice = BigInteger.valueOf(20_000_000_000L); // 20 gwei
            BigInteger gasLimit = BigInteger.valueOf(200000); // safe for Ganache

            EthSendTransaction response = txManager.sendTransaction(
                    gasPrice,
                    gasLimit,
                    contractAddress,
                    encoded,
                    BigInteger.ZERO
            );

            // 🔥 ERROR HANDLING
            if (response == null) {
                System.out.println("❌ TX FAILED: response is null");
                return;
            }

            if (response.getError() != null) {
                System.out.println("❌ TX ERROR: " + response.getError().getMessage());
                return;
            }

            if (response.getTransactionHash() == null) {
                System.out.println("❌ TX FAILED: hash is null");
                return;
            }

            System.out.println("✅ TX HASH: " + response.getTransactionHash());

        } catch (Exception e) {
            System.out.println("❌ Blockchain ERROR: " + e.getMessage());
        }
    }

    // =========================================================
    // 🔥 GET REPUTATION
    // =========================================================
    public double getReputation(String relayId) {
        try {
            System.out.println("Reading trust for: " + relayId);

            Function function = new Function(
                    "getReputation",
                    Arrays.asList(new Utf8String(relayId)),
                    Arrays.asList(new TypeReference<Uint256>() {})
            );

            String encoded = FunctionEncoder.encode(function);

            String value = web3j.ethCall(
                    Transaction.createEthCallTransaction(
                            credentials.getAddress(),
                            contractAddress,
                            encoded),
                    DefaultBlockParameterName.LATEST
            ).send().getValue();

            System.out.println("Raw blockchain response: " + value);

            // Handle empty/failed contract interaction
            if (value == null || value.equals("0x")) {
                System.out.println("⚠️ Empty blockchain response");
                return 0.0;
            }

            List<org.web3j.abi.datatypes.Type> output =
                    FunctionReturnDecoder.decode(
                            value, function.getOutputParameters());

            if (output == null || output.isEmpty()) {
                System.out.println("⚠️ Decoding failed - no output");
                return 0.0;
            }

            BigInteger raw = (BigInteger) output.get(0).getValue();

            System.out.println("Raw reputation value: " + raw);

            // ✅ DECODE BACK using same 10000 factor
            double trust = raw.doubleValue() / 10000.0;

            System.out.println("✅ Blockchain trust: " + trust);

            return trust;

        } catch (Exception e) {
            System.out.println("❌ Read ERROR: " + e.getMessage());
            e.printStackTrace();
            return 0.0;
        }
    }

    public String connectToBlockchain() {
        return "Connected to Blockchain";
    }

    public void registerNodeOnBlockchain(String nodeId, String publicKey) {
        try {
            System.out.println("Registering node: " + nodeId + " with key: " + publicKey);
            
            long chainId = web3j.ethChainId().send().getChainId().longValue();
            RawTransactionManager txManager = new RawTransactionManager(web3j, credentials, chainId);

            Function function = new Function(
                    "registerNode",
                    Arrays.asList(
                            new Utf8String(nodeId),
                            new Utf8String(publicKey)
                    ),
                    Collections.emptyList()
            );

            String encoded = FunctionEncoder.encode(function);
            BigInteger gasPrice = BigInteger.valueOf(20_000_000_000L); // 20 gwei
            BigInteger gasLimit = BigInteger.valueOf(300000); 

            EthSendTransaction response = txManager.sendTransaction(
                    gasPrice,
                    gasLimit,
                    nodeRegistryAddress,
                    encoded,
                    BigInteger.ZERO
            );

            if (response != null && response.getTransactionHash() != null) {
                System.out.println("✅ NODE REGISTERED TX HASH: " + response.getTransactionHash());
            } else if (response != null && response.getError() != null) {
                System.out.println("❌ TX ERROR: " + response.getError().getMessage());
            }
        } catch (Exception e) {
            System.out.println("❌ Node Registration ERROR: " + e.getMessage());
        }
    }

    public void storeReputation(String nodeId, double trust) {
        updateReputation(nodeId, trust);
    }
}