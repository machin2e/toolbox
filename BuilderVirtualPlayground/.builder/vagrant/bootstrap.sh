#!/bin/bash

nohup python /vagrant/builder.py start server &
nohup python /vagrant/builder.py start discovery &
