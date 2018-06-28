#!/bin/bash

function  wp() {
  OUTPUT=`waypoints "$@"`
  if [ $? -eq 2 ]
    then cd "$OUTPUT"
    else echo "$OUTPUT"
  fi
}
