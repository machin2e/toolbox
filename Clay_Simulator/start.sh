#!/bin/bash

# Simulation parameters
UNIT_COUNT=2

# Get IP address (on Mac)
DISCOVERY_BROADCAST_ADDRESS=`ifconfig en0 | grep 'broadcast' | cut -d\  -f6 | awk '{print $1}'`
DISCOVERY_BROADCAST_PORT=4445
BROADCAST_PORT=4446

echo "Starting Clay simluation environment."
echo "Simulating $UNIT_COUNT units."

# Generate UDP port numbers for containers
pi_start=445
pi_end=$(($pi_start + $UNIT_COUNT - 1))
DISCOVERY_BROADCAST_PORTS=( `seq -s ' ' $pi_start 1 $pi_end` )

po_start=545
po_end=$(($po_start + $UNIT_COUNT - 1))
BROADCAST_PORTS=( `seq -s ' ' $po_start 1 $po_end` )

# Parameterize and start the units sequentially
for i in $(seq 0 1 $(($UNIT_COUNT - 1)))
do
	echo -n ""
	docker run -p ${DISCOVERY_BROADCAST_PORTS[i]}:$DISCOVERY_BROADCAST_PORT/udp -p ${BROADCAST_PORTS[i]}:$BROADCAST_PORT/udp -d -e "DISCOVERY_BROADCAST_ADDRESS=$DISCOVERY_BROADCAST_ADDRESS" -e "DISCOVERY_BROADCAST_PORT=${DISCOVERY_BROADCAST_PORTS[i]}" -e "BROADCAST_PORT=${BROADCAST_PORTS[i]}" clay
done
