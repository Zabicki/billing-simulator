#!/bin/bash

# Create network if it doesn't exist
docker network create cassandra || echo "Network 'cassandra' already exists"

# Start first node
docker run \
    --network cassandra \
    --name cassandra1 \
    --memory=8g \
    --volume /home/chris/Work/IdeaProjects/billing-simulator/docker/cassandra/data/cass1:/var/lib/cassandra \
    --volume /home/chris/Work/IdeaProjects/billing-simulator/docker/cassandra/etc/cass1:/etc/cassandra \
    -p 7000:7000 \
    -p 9042:9042 \
    -d cassandra:4.0.6

# Wait for 80 seconds
echo "Waiting for 80 seconds before starting 2nd node..."
sleep 80

# Start the second node
docker run \
    --network cassandra \
    --name cassandra2 \
    --memory=8g \
    -e CASSANDRA_SEEDS=172.20.0.2 \
    --volume /home/chris/Work/IdeaProjects/billing-simulator/docker/cassandra/data/cass2:/var/lib/cassandra \
    --volume /home/chris/Work/IdeaProjects/billing-simulator/docker/cassandra/etc/cass2:/etc/cassandra \
    -d cassandra:4.0.6

# Wait for 80 seconds
echo "Waiting for 80 seconds before starting 3rd node..."
sleep 80

# Start the third node
docker run \
    --network cassandra \
    --name cassandra3 \
    --memory=4g \
    --volume /home/chris/Work/IdeaProjects/billing-simulator/docker/cassandra/data/cass3:/var/lib/cassandra \
    --volume /home/chris/Work/IdeaProjects/billing-simulator/docker/cassandra/etc/cass3:/etc/cassandra \
    -e CASSANDRA_SEEDS=172.19.0.2 \
    -d cassandra:4.0.6

echo "All Cassandra nodes started!"