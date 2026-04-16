// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

contract ReputationLedger {

    mapping(string => uint256) public reputation;

    function updateReputation(string memory nodeId, uint256 value) public {
        reputation[nodeId] = value;
    }

    function getReputation(string memory nodeId) public view returns (uint256) {
        return reputation[nodeId];
    }
}
