#!/bin/bash

 sed -i  "s/Barricada\\/reservoir/StratusLabs\\/git\\/claudia/g" $1 

 sed -i  "s/Claudia\\/DebPackages/StratusLabs\\/Deb/g" $1 

 sed -i  "s/reservoir/stratuslab/g" $1  
