#!/bin/sh

mv $1 $1.bak
sort $1.bak > $1
