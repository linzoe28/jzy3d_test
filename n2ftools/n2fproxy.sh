#!/bin/bash

TIMESTAMP=`date +%Y-%m-%d_%H-%M-%S`
TIMESTAMP=`realpath $TIMESTAMP`
mkdir $TIMESTAMP
mkdir $TIMESTAMP/.n2f
mkdir $TIMESTAMP/ap
mkdir $TIMESTAMP/ap/.n2f
# $1=Number of Subspace
# $2=Operating Frequency(MHz)
# $3=ap Data Directory
# $4=theta
# $5=phi
echo "initializing..." > $TIMESTAMP/.n2f/status
./cpscript $1 $2 $3 $TIMESTAMP
cp -r $3/ap $TIMESTAMP/
echo "RCS: Executing RCS_code.m..." > $TIMESTAMP/.n2f/status
octave-cli RCS_code.m $TIMESTAMP/ap/ $1 $4 $5
rm -r -f $TIMESTAMP/.n2f
