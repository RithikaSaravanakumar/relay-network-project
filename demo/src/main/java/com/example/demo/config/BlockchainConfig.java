// // // package com.example.demo.config;

// // // import org.springframework.beans.factory.annotation.Value;
// // // import org.springframework.context.annotation.Bean;
// // // import org.springframework.context.annotation.Configuration;

// // // import org.web3j.crypto.Credentials;
// // // import org.web3j.protocol.Web3j;
// // // import org.web3j.protocol.http.HttpService;
// // // import org.web3j.tx.RawTransactionManager;
// // // import org.web3j.tx.TransactionManager;
// // // import org.web3j.tx.gas.DefaultGasProvider;

// // // /**
// // //  * Configuration for connecting to an Ethereum node (Ganache) via Web3j.
// // //  */
// // // @Configuration
// // // public class BlockchainConfig {

// // //     @Value("${ethereum.rpc.url:http://127.0.0.1:7545}")
// // //     private String ethereumRpcUrl;

// // //     @Value("${blockchain.private.key:}")
// // //     private String privateKey;

// // //     @Bean
// // //     public Web3j web3j() {
// // //         return Web3j.build(new HttpService(ethereumRpcUrl));
// // //     }

// // //     @Bean
// // //     public Credentials credentials() {
// // //         if (privateKey == null || privateKey.isBlank() || privateKey.startsWith("REPLACE")) {
// // //             // Return empty credentials to avoid NPEs; most calls will fail until configured correctly.
// // //             return Credentials.create("0x0000000000000000000000000000000000000000000000000000000000000000");
// // //         }
// // //         return Credentials.create(privateKey);
// // //     }

// // //     @Bean
// // //     public TransactionManager transactionManager(Web3j web3j, Credentials credentials) {
// // //         return new RawTransactionManager(web3j, credentials);
// // //     }

// // //     @Bean
// // //     public DefaultGasProvider defaultGasProvider() {
// // //         return new DefaultGasProvider();
// // //     }
// // // }
// // package com.example.demo.config;

// // import org.springframework.context.annotation.Bean;
// // import org.springframework.context.annotation.Configuration;
// // import org.web3j.crypto.Credentials;
// // import org.web3j.protocol.Web3j;
// // import org.web3j.protocol.http.HttpService;

// // @Configuration
// // public class BlockchainConfig {

// //     @Bean
// //     public Web3j web3j(){
// //         return Web3j.build(new HttpService("http://127.0.0.1:7545"));
// //     }

// //     @Bean
// //     public Credentials credentials(){
// //         return Credentials.create("YOUR_PRIVATE_KEY");
// //     }
// // }
// package com.example.demo.config;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.web3j.crypto.Credentials;
// import org.web3j.protocol.Web3j;
// import org.web3j.protocol.http.HttpService;

// @Configuration
// public class BlockchainConfig {

//     @Value("${blockchain.rpc.url}")
//     private String rpcUrl;

//     @Value("${blockchain.private.key}")
//     private String privateKey;

//     @Bean
//     public Web3j web3j() {
//         return Web3j.build(new HttpService(rpcUrl));
//     }

//     @Bean
//     public Credentials credentials() {
//         return Credentials.create(0xe9719fbbfbaae48e26166bab9d99242afb61ff1ff87c8203b66ce3ec702b27aa);
//     }
// }
package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class BlockchainConfig {

    @Value("${blockchain.rpc.url}")
    private String rpcUrl;

    @Value("${blockchain.private.key}")
    private String privateKey;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(rpcUrl));
    }

    @Bean
    public Credentials credentials() {
        return Credentials.create(privateKey); // ✅ FIXED
    }
}