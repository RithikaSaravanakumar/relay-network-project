const { ethers } = require("hardhat");

async function main() {
  const ReputationLedger = await ethers.getContractFactory("ReputationLedger");
  const contract = await ReputationLedger.deploy();
  await contract.deployed();

  console.log("===========================================");
  console.log("ReputationLedger deployed to:", contract.address);
  console.log("===========================================");
  console.log("");
  console.log("UPDATE your application.properties with:");
  console.log(`blockchain.reputationLedger.address=${contract.address}`);
  console.log("");
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });
