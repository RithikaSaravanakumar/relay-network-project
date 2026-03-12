// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract ReputationLedger {

    struct Entry {
        string nodeId;
        uint256 reputation; // fixed-point scaled by 10000
        uint256 timestamp;
    }

    mapping(string => Entry) public ledger;

    event ReputationUpdated(string nodeId, uint256 reputation, uint256 timestamp);

    function updateReputation(string memory nodeId, uint256 reputation) public {
        ledger[nodeId] = Entry(nodeId, reputation, block.timestamp);
        emit ReputationUpdated(nodeId, reputation, block.timestamp);
    }

    function getReputation(string memory nodeId) public view returns (uint256) {
        return ledger[nodeId].reputation;
    }
}
