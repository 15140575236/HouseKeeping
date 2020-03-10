cd /home/oimdev
mkdir OIMSimpData
cd /home/oimdev/OIMSimpData
cp /home/oimdev/OIMSimpData.tar ./
tar -xvf OIMSimpData.tar
chmod 755 db2.sh
##nohup /home/oimdev/OIMSimpData/db2.sh &
ps -eaf | grep db2.sh
