#!/bin/bash
rm *.o
gcc -c main.c Message_Queue.c Message.c
gcc *.o -o test
./test
