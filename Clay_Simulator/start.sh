#!/bin/bash

ports=( 4445 4446 4447 4448 4449 )

for i in "${ports[@]}"
do
	docker run -p $i:4445/udp -d -e "CLAY_BROADCAST=192.168.2.255" -e "CLAY_BROADCAST_PORT=4445" clay
done

#docker run -p 4445:4445/udp -t -i -e "CLAY_BROADCAST=192.168.2.255" -e "CLAY_BROADCAST_PORT=4445" clay

#docker run -p 4445:4445/udp -t -i clay bash
