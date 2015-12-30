#!/bin/bash

echo -n "Building."
cp ../clay.py .
docker build -t clay .
rm ./clay.py
echo "Done."
