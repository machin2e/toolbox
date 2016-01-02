FROM ubuntu:trusty

COPY ./clay.py /usr/bin/clay.py

RUN apt-get -y update
RUN apt-get install -y python

CMD echo "Clay"
CMD /sbin/ip route|awk '/default/ { print $3 }

# Report the environment variables set up during startup.
CMD echo $DISCOVERY_BROADCAST_ADDRESS
CMD echo $DISCOVERY_BROADCAST_PORT
CMD echo $BROADCAST_PORT

# Manually send a UDP packet.
#CMD echo -n "hello" | nc -4u -q1 $CLAY_BROADCAST $CLAY_BROADCAST_PORT

# Start Clay
#CMD python /usr/bin/clay.py start -h $DISCOVERY_BROADCAST_ADDRESS -p $DISCOVERY_BROADCAST_PORT,$BROADCAST_PORT
CMD python /usr/bin/clay.py start -h $DISCOVERY_BROADCAST_ADDRESS -p 4445,$BROADCAST_PORT
