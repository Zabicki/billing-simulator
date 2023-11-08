# Use the official Cassandra image from Docker Hub as the base image
FROM cassandra:latest

# Copy the ready-probe.sh script from the local directory into the container's root directory
COPY ready-probe.sh /

# Give execution permissions to the ready-probe.sh script
RUN chmod 777 /ready-probe.sh

# The default command already exists in the base image, so no need to repeat it here
