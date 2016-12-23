#!/bin/bash
set -u
set -e
NETID=87234
BOOTNODE_KEYHEX=77bd02ffa26e3fb8f324bda24ae588066f1873d95680104de5bc2db9e7b2e510
BOOTNODE_ENODE=enode://6433e8fb82c4638a8a6d499d40eb7d8158883219600bfd49acb968e3a37ccced04c964fa87b3a78a2da1b71dc1b90275f4d055720bb67fad4a118a56925125dc@[127.0.0.1]:33445

GLOBAL_ARGS="--bootnodes $BOOTNODE_ENODE --networkid $NETID --rpc --rpcaddr 0.0.0.0 --rpccorsdomain 49.248.29.163 --rpcapi admin,db,eth,debug,miner,net,shh,txpool,personal,web3,quorum"

echo "[*] Starting Constellation nodes"
nohup constellation-node tm1.conf 2>> qdata/logs/constellation1.log &
sleep 1
nohup constellation-node tm2.conf 2>> qdata/logs/constellation2.log &
nohup constellation-node tm3.conf 2>> qdata/logs/constellation3.log &
nohup constellation-node tm4.conf 2>> qdata/logs/constellation4.log &
nohup constellation-node tm5.conf 2>> qdata/logs/constellation5.log &


echo "[*] Starting bootnode"
nohup bootnode --nodekeyhex "$BOOTNODE_KEYHEX" --addr="127.0.0.1:33446" 2>>qdata/logs/bootnode.log &
echo "wait for bootnode to start..."
sleep 6


echo "[*] Starting node issuer1"
PRIVATE_CONFIG=tm1.conf nohup geth --datadir qdata/issuer1 $GLOBAL_ARGS --rpcport 32000 --port 31000 --unlock 0 --voteaccount "0x8a19fcbf7150d5ede014fd706ff246176a2c85fa" --votepassword "" --password passwords.txt 2>>qdata/logs/1.log &

echo "[*] Starting node issuer2"
PRIVATE_CONFIG=tm2.conf nohup geth --datadir qdata/issuer2 $GLOBAL_ARGS --rpcport 32001 --port 31001 --voteaccount "0x4eccf8ee8dc91ad5b3562255791bf06ca12ae1f3" --votepassword ""  --password passwords.txt 2>>qdata/logs/2.log &

echo "[*] Starting node investor1"
PRIVATE_CONFIG=tm3.conf nohup geth --datadir qdata/investor1 $GLOBAL_ARGS --rpcport 32002 --port 31002 --voteaccount "0x60422cbf0a1585ab2aa341e8d1f1f47361e5de7a" --votepassword ""  --password passwords.txt 2>>qdata/logs/3.log &

echo "[*] Starting node investor2"
PRIVATE_CONFIG=tm4.conf nohup geth --datadir qdata/investor2 $GLOBAL_ARGS --rpcport 32003 --port 31003 --voteaccount "0x1efa9fa1bb1430309dbfda51949e4d4f3c4135d3" --votepassword ""  --password passwords.txt 2>>qdata/logs/4.log &

echo "[*] Starting node nsdl"
PRIVATE_CONFIG=tm5.conf nohup geth --datadir qdata/nsdl $GLOBAL_ARGS --rpcport 32004 --port 31004 --voteaccount "0x65b70b9055dcca77ffb730df2591b78c01bcbd9d" --votepassword "" --blockmakeraccount "0x2351c97c88f3d630d421c6c04d17686b12b24e71" --blockmakerpassword "" --singleblockmaker --minblocktime 2 --maxblocktime 5 --password passwords.txt 2>>qdata/logs/5.log &

sleep 10
echo "[*] Sending first transaction"
PRIVATE_CONFIG=tm1.conf geth --exec 'loadScript("script1.js")' attach ipc:qdata/issuer1/geth.ipc

echo "All nodes configured. See 'qdata/logs' for logs, and run e.g. 'geth attach qdata/issuer1/geth.ipc' to attach to the first Geth node"


