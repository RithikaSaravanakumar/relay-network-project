// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract NodeRegistry {
    struct NodeData {
        string nodeId;
        string publicKey;
        uint256 timestamp;
    }

    mapping(string => NodeData) public nodes;

    event NodeRegistered(string nodeId, string publicKey, uint256 timestamp);

    function registerNode(string memory nodeId, string memory publicKey) public {
        nodes[nodeId] = NodeData(nodeId, publicKey, block.timestamp);
        emit NodeRegistered(nodeId, publicKey, block.timestamp);
    }

    function getNode(string memory nodeId) public view returns (string memory, string memory, uint256) {
        NodeData memory d = nodes[nodeId];
        return (d.nodeId, d.publicKey, d.timestamp);
    }
}
