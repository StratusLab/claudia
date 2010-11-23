#!/bin/sh

ls -l $1/opt/claudia/lib/ > compare.txt
archive_list=compare.txt
for archive_file in `cat $archive_list`
do
if [ -f $2/opt/claudia/lib/$archive_file ]; then
echo "[$archive_file] existe en [$1] y [$2] , movidos a claudia-lib"
#mv $2/opt/claudia/lib/$archive_file stratuslab-claudia-lib/opt/claudia/lib
mv $1/opt/claudia/lib/$archive_file stratuslab-claudia-lib/opt/claudia/lib
fi
done
rm compare.txt
