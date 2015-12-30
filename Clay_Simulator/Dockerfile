FROM ubuntu:trusty

COPY ./clay.py /usr/bin/clay.py

RUN apt-get -y update
RUN apt-get install -y python

CMD echo "Clay"
CMD /sbin/ip route|awk '/default/ { print $3 }'

# Report the environment variables set up during startup.
CMD echo $CLAY_BROADCAST
CMD echo $CLAY_BROADCAST_PORT

# Manually send a UDP packet.
#CMD echo -n "hello" | nc -4u -q1 $CLAY_BROADCAST $CLAY_BROADCAST_PORT

CMD python /usr/bin/clay.py start
#CMD python /usr/bin/clay.py start -b $CLAY_BROADCAST
