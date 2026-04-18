// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

contract NodeRegistry {

    struct NodeIdentity {
        string nodeId;
        string publicKey;
        uint256 registeredAt;
        bool isActive;
    }

    mapping(string => NodeIdentity) public registry;

    event NodeRegistered(string nodeId, string publicKey, uint256 timestamp);

    function registerNode(string memory nodeId, string memory publicKey) public {
        require(!registry[nodeId].isActive, "Node is already registered");

        registry[nodeId] = NodeIdentity({
            nodeId: nodeId,
            publicKey: publicKey,
            registeredAt: block.timestamp,
            isActive: true
        });

        emit NodeRegistered(nodeId, publicKey, block.timestamp);
    }

    function getNodeIdentity(string memory nodeId) public view returns (string memory, string memory, uint256, bool) {
        NodeIdentity memory node = registry[nodeId];
        return (node.nodeId, node.publicKey, node.registeredAt, node.isActive);
    }
}
