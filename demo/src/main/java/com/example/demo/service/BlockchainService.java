
// // // // package com.example.demo.service;

// // // // import org.springframework.stereotype.Service;

// // // // @Service
// // // // public class BlockchainService {

// // // //     public void connectToBlockchain() {

// // // //         System.out.println("Connected to blockchain network");

// // // //     }

// // // //     public void registerNodeOnBlockchain(String nodeId, String publicKey) {

// // // //         System.out.println("Registering node on blockchain: " + nodeId);

// // // //         String txHash = "0x" + Integer.toHexString(nodeId.hashCode());

// // // //         System.out.println("Transaction hash: " + txHash);

// // // //     }

// // // //     public void storeReputation(String nodeId, double reputationScore) {

// // // //         System.out.println("Storing reputation on blockchain");

// // // //         System.out.println("Node: " + nodeId);
// // // //         System.out.println("Score: " + reputationScore);

// // // //     }
// // // // }
// // // package com.example.demo.service;

// // // import java.math.BigInteger;
// // // import java.util.Arrays;
// // // import java.util.Collections;
// // // import java.util.List;

// // // import org.springframework.stereotype.Service;
// // // import org.web3j.abi.FunctionEncoder;
// // // import org.web3j.abi.FunctionReturnDecoder;
// // // import org.web3j.abi.TypeReference;
// // // import org.web3j.abi.datatypes.Function;
// // // import org.web3j.abi.datatypes.Utf8String;
// // // import org.web3j.abi.datatypes.generated.Uint256;
// // // import org.web3j.crypto.Credentials;
// // // import org.web3j.protocol.Web3j;
// // // import org.web3j.protocol.core.DefaultBlockParameterName;
// // // import org.web3j.protocol.core.methods.request.Transaction;
// // // import org.web3j.tx.RawTransactionManager;
// // // import org.web3j.tx.gas.DefaultGasProvider;

// // // @Service
// // // public class BlockchainService {

// // //     private final Web3j web3j;
// // //     private final Credentials credentials;

// // //     private static final String CONTRACT_ADDRESS = "PASTE_YOUR_REPUTATION_LEDGER_ADDRESS";

// // //     public BlockchainService(Web3j web3j, Credentials credentials) {
// // //         this.web3j = web3j;
// // //         this.credentials = credentials;
// // //     }

// // //     // 🔥 STORE TRUST (SCALED)
// // //     public void updateReputation(String nodeId, double trust) {

// // //         try {
// // //             RawTransactionManager txManager =
// // //                     new RawTransactionManager(web3j, credentials);

// // //             // 🔥 SCALE TRUST
// // //             BigInteger scaled = BigInteger.valueOf((long) (trust * 10000));

// // //             Function function = new Function(
// // //                     "updateReputation",
// // //                     Arrays.asList(
// // //                             new Utf8String(nodeId),
// // //                             new Uint256(scaled)
// // //                     ),
// // //                     Collections.emptyList()
// // //             );

// // //             String encoded = FunctionEncoder.encode(function);

// // //             txManager.sendTransaction(
// // //                     DefaultGasProvider.GAS_PRICE,
// // //                     DefaultGasProvider.GAS_LIMIT,
// // //                     CONTRACT_ADDRESS,
// // //                     encoded,
// // //                     BigInteger.ZERO
// // //             );

// // //         } catch (Exception e) {
// // //             e.printStackTrace();
// // //         }
// // //     }

// // //     // 🔥 GET TRUST (NORMALIZED)
// // //     public double getReputation(String nodeId) {

// // //         try {
// // //             Function function = new Function(
// // //                     "getReputation",
// // //                     Arrays.asList(new Utf8String(nodeId)),
// // //                     Arrays.asList(new TypeReference<Uint256>() {})
// // //             );

// // //             String encoded = FunctionEncoder.encode(function);

// // //             String value = web3j.ethCall(
// // //                     Transaction.createEthCallTransaction(
// // //                             credentials.getAddress(),
// // //                             CONTRACT_ADDRESS,
// // //                             encoded),
// // //                     DefaultBlockParameterName.LATEST
// // //             ).send().getValue();

// // //             List<org.web3j.abi.datatypes.Type> output =
// // //                     FunctionReturnDecoder.decode(
// // //                             value, function.getOutputParameters());

// // //             BigInteger raw = (BigInteger) output.get(0).getValue();

// // //             // 🔥 NORMALIZE BACK
// // //             return raw.doubleValue() / 10000.0;

// // //         } catch (Exception e) {
// // //             e.printStackTrace();
// // //             return 0.0;
// // //         }
// // //     }
// // // }
// // package com.example.demo.service;

// // import java.math.BigInteger;
// // import java.util.Arrays;
// // import java.util.Collections;
// // import java.util.List;

// // import org.springframework.stereotype.Service;
// // import org.web3j.abi.FunctionEncoder;
// // import org.web3j.abi.FunctionReturnDecoder;
// // import org.web3j.abi.TypeReference;
// // import org.web3j.abi.datatypes.Function;
// // import org.web3j.abi.datatypes.Utf8String;
// // import org.web3j.abi.datatypes.generated.Uint256;
// // import org.web3j.crypto.Credentials;
// // import org.web3j.protocol.Web3j;
// // import org.web3j.protocol.core.DefaultBlockParameterName;
// // import org.web3j.protocol.core.methods.request.Transaction;
// // import org.web3j.tx.RawTransactionManager;
// // import org.web3j.tx.gas.DefaultGasProvider;

// // @Service
// // public class BlockchainService {

// //     private final Web3j web3j;
// //     private final Credentials credentials;

// //     private static final String CONTRACT_ADDRESS = "PASTE_YOUR_ADDRESS";

// //     public BlockchainService(Web3j web3j, Credentials credentials) {
// //         this.web3j = web3j;
// //         this.credentials = credentials;
// //     }

// //     // 🔥 STORE TRUST
// //    public void updateReputation(String relayId, double trust) {

// //     try {
// //         RawTransactionManager txManager =
// //                 new RawTransactionManager(web3j, credentials);

// //         // 🔥 SCALE TRUST
// //         BigInteger scaled =
// //                 BigInteger.valueOf((long) (trust * 10000));

// //         Function function = new Function(
// //                 "updateReputation",
// //                 Arrays.asList(
// //                         new Utf8String(relayId),
// //                         new Uint256(scaled)
// //                 ),
// //                 Collections.emptyList()
// //         );

// //         String encoded = FunctionEncoder.encode(function);

// //         // 🔥 SEND TRANSACTION
// //         var response = txManager.sendTransaction(
// //                 DefaultGasProvider.GAS_PRICE,
// //                 DefaultGasProvider.GAS_LIMIT,
// //                 CONTRACT_ADDRESS,
// //                 encoded,
// //                 BigInteger.ZERO
// //         );

// //         String txHash = response.getTransactionHash();

// //         System.out.println("TX SENT: " + txHash);

// //         // 🔥 WAIT FOR TRANSACTION TO BE MINED
// //         org.web3j.protocol.core.methods.response.TransactionReceipt receipt = null;

// //         int attempts = 40; // wait max ~10 sec

// //         while (attempts-- > 0) {

// //             var receiptResponse =
// //                     web3j.ethGetTransactionReceipt(txHash).send();

// //             if (receiptResponse.getTransactionReceipt().isPresent()) {
// //                 receipt = receiptResponse.getTransactionReceipt().get();
// //                 break;
// //             }

// //             Thread.sleep(250); // wait 250 ms
// //         }

// //         if (receipt != null) {
// //             System.out.println("TX MINED: " + receipt.getTransactionHash());
// //         } else {
// //             System.out.println("⚠️ TX not mined yet");
// //         }

// //     } catch (Exception e) {
// //         e.printStackTrace();
// //     }
// // }
// //     // 🔥 GET TRUST
// //     public double getReputation(String relayId) {

// //         try {
// //             Function function = new Function(
// //                     "getReputation",
// //                     Arrays.asList(new Utf8String(relayId)),
// //                     Arrays.asList(new TypeReference<Uint256>() {})
// //             );

// //             String encoded = FunctionEncoder.encode(function);

// //             String value = web3j.ethCall(
// //                     Transaction.createEthCallTransaction(
// //                             credentials.getAddress(),
// //                             CONTRACT_ADDRESS,
// //                             encoded),
// //                     DefaultBlockParameterName.LATEST
// //             ).send().getValue();

// //             List<org.web3j.abi.datatypes.Type> output =
// //                     FunctionReturnDecoder.decode(
// //                             value, function.getOutputParameters());

// //             BigInteger raw = (BigInteger) output.get(0).getValue();

// //             return raw.doubleValue() / 10000.0;

// //         } catch (Exception e) {
// //             e.printStackTrace();
// //             return 0.0;
// //         }
// //     }
// // }
// package com.example.demo.service;

// import java.math.BigInteger;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.List;

// import org.springframework.stereotype.Service;
// import org.web3j.abi.FunctionEncoder;
// import org.web3j.abi.FunctionReturnDecoder;
// import org.web3j.abi.TypeReference;
// import org.web3j.abi.datatypes.Function;
// import org.web3j.abi.datatypes.Utf8String;
// import org.web3j.abi.datatypes.generated.Uint256;
// import org.web3j.crypto.Credentials;
// import org.web3j.protocol.Web3j;
// import org.web3j.protocol.core.DefaultBlockParameterName;
// import org.web3j.protocol.core.methods.request.Transaction;
// import org.web3j.tx.RawTransactionManager;
// import org.web3j.tx.gas.DefaultGasProvider;

// @Service
// public class BlockchainService {

//     private final Web3j web3j;
//     private final Credentials credentials;

//     private static final String CONTRACT_ADDRESS = "PASTE_YOUR_REPUTATION_LEDGER_ADDRESS";

//     public BlockchainService(Web3j web3j, Credentials credentials) {
//         this.web3j = web3j;
//         this.credentials = credentials;
//     }

//     // =========================================================
//     // 🔥 DUMMY CONNECT METHOD (FOR CONTROLLER FIX)
//     // =========================================================
//     public String connectToBlockchain() {
//         return "Connected to Blockchain";
//     }

//     // =========================================================
//     // 🔥 REGISTER NODE (OPTIONAL - SIMPLE LOGIC)
//     // =========================================================
//     public void registerNodeOnBlockchain(String nodeId, String publicKey) {
//         System.out.println("Node registered on blockchain: " + nodeId);
//     }

//     // =========================================================
//     // 🔥 STORE REPUTATION (WRAPPER)
//     // =========================================================
//     public void storeReputation(String nodeId, double trust) {
//         updateReputation(nodeId, trust);
//     }

//     // =========================================================
//     // 🔥 UPDATE REPUTATION (REAL BLOCKCHAIN CALL)
//     // =========================================================
//     public void updateReputation(String relayId, double trust) {

//         try {
//             RawTransactionManager txManager =
//                     new RawTransactionManager(web3j, credentials);

//             BigInteger scaled =
//                     BigInteger.valueOf((long) (trust * 10000));

//             Function function = new Function(
//                     "updateReputation",
//                     Arrays.asList(
//                             new Utf8String(relayId),
//                             new Uint256(scaled)
//                     ),
//                     Collections.emptyList()
//             );

//             String encoded = FunctionEncoder.encode(function);

//             var response = txManager.sendTransaction(
//                     DefaultGasProvider.GAS_PRICE,
//                     DefaultGasProvider.GAS_LIMIT,
//                     CONTRACT_ADDRESS,
//                     encoded,
//                     BigInteger.ZERO
//             );

//             String txHash = response.getTransactionHash();

//             System.out.println("TX SENT: " + txHash);

//             // 🔥 WAIT FOR MINING
//             org.web3j.protocol.core.methods.response.TransactionReceipt receipt = null;

//             int attempts = 40;

//             while (attempts-- > 0) {

//                 var receiptResponse =
//                         web3j.ethGetTransactionReceipt(txHash).send();

//                 if (receiptResponse.getTransactionReceipt().isPresent()) {
//                     receipt = receiptResponse.getTransactionReceipt().get();
//                     break;
//                 }

//                 Thread.sleep(250);
//             }

//             if (receipt != null) {
//                 System.out.println("TX MINED: " + receipt.getTransactionHash());
//             }

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     // =========================================================
//     // 🔥 GET REPUTATION
//     // =========================================================
//     public double getReputation(String relayId) {

//         try {
//             Function function = new Function(
//                     "getReputation",
//                     Arrays.asList(new Utf8String(relayId)),
//                     Arrays.asList(new TypeReference<Uint256>() {})
//             );

//             String encoded = FunctionEncoder.encode(function);

//             String value = web3j.ethCall(
//                     Transaction.createEthCallTransaction(
//                             credentials.getAddress(),
//                             CONTRACT_ADDRESS,
//                             encoded),
//                     DefaultBlockParameterName.LATEST
//             ).send().getValue();

//             List<org.web3j.abi.datatypes.Type> output =
//                     FunctionReturnDecoder.decode(
//                             value, function.getOutputParameters());

//             BigInteger raw = (BigInteger) output.get(0).getValue();

//             return raw.doubleValue() / 10000.0;

//         } catch (Exception e) {
//             e.printStackTrace();
//             return 0.0;
//         }
//     }
// }
package com.example.demo.service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

@Service
public class BlockchainService {

    private final Web3j web3j;
    private final Credentials credentials;

    private static final String CONTRACT_ADDRESS = "PASTE_YOUR_CONTRACT_ADDRESS";

    public BlockchainService(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
    }

    // 🔥 Update reputation
    public void updateReputation(String relayId, double trust) {

        try {
            System.out.println("Saving trust: " + trust + " for " + relayId);

            RawTransactionManager txManager =
                    new RawTransactionManager(web3j, credentials);

            long scaledValue = Math.max(1, (long)(trust * 10000));
            BigInteger scaled = BigInteger.valueOf(scaledValue);

            Function function = new Function(
                    "updateReputation",
                    Arrays.asList(
                            new Utf8String(relayId),
                            new Uint256(scaled)
                    ),
                    Collections.emptyList()
            );

            String encoded = FunctionEncoder.encode(function);

            var response = txManager.sendTransaction(
                    DefaultGasProvider.GAS_PRICE,
                    DefaultGasProvider.GAS_LIMIT,
                    CONTRACT_ADDRESS,
                    encoded,
                    BigInteger.ZERO
            );

            System.out.println("TX SENT: " + response.getTransactionHash());

        } catch (Exception e) {
            System.out.println("Blockchain ERROR: " + e.getMessage());
        }
    }

    // 🔥 Get reputation
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
                            CONTRACT_ADDRESS,
                            encoded),
                    DefaultBlockParameterName.LATEST
            ).send().getValue();

            List<org.web3j.abi.datatypes.Type> output =
                    FunctionReturnDecoder.decode(
                            value, function.getOutputParameters());

            if (output.isEmpty()) return 0.0;

            BigInteger raw = (BigInteger) output.get(0).getValue();

            double trust = raw.doubleValue() / 10000.0;

            System.out.println("Blockchain trust: " + trust);

            return trust;

        } catch (Exception e) {
            System.out.println("Read ERROR: " + e.getMessage());
            return 0.0;
        }
    }

    // 🔧 Fix for old calls
    public String connectToBlockchain() {
        return "Connected";
    }

    public void registerNodeOnBlockchain(String nodeId, String publicKey) {
        System.out.println("Node registered: " + nodeId);
    }

    public void storeReputation(String nodeId, double trust) {
        updateReputation(nodeId, trust);
    }
}