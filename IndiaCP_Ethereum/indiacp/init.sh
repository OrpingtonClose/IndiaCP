#!/bin/bash
set -u
set -e

echo "[*] Cleaning up log directories"
rm -rf qdata
mkdir -p qdata/logs

echo "[*] Configuring node issuer1"
mkdir -p qdata/issuer1/keystore
cp keys/key1 qdata/issuer1/keystore
cp keys/key2 qdata/issuer1/keystore
geth --datadir qdata/issuer1 init genesis.json  # initializing chain at each node's datadir.

echo "[*] Configuring node issuer2"
mkdir -p qdata/issuer2/keystore
cp keys/key3 qdata/issuer2/keystore
cp keys/key4 qdata/issuer2/keystore
cp keys/key9 qdata/issuer2/keystore
cp keys/key10 qdata/issuer2/keystore
cp keys/key11 qdata/issuer2/keystore
geth --datadir qdata/issuer2 init genesis.json

echo "[*] Configuring node investor1"
mkdir -p qdata/investor1/keystore
cp keys/key5 qdata/investor1/keystore
cp keys/key6 qdata/investor1/keystore
geth --datadir qdata/investor1 init genesis.json

echo "[*] Configuring node investor2"
mkdir -p qdata/investor2/keystore
cp keys/key7 qdata/investor2/keystore
cp keys/key8 qdata/investor2/keystore
geth --datadir qdata/investor2 init genesis.json

echo "[*] Configuring node nsdl"
mkdir -p qdata/nsdl/keystore
cp keys/key9 qdata/nsdl/keystore
cp keys/key10 qdata/nsdl/keystore
cp keys/key11 qdata/nsdl/keystore
geth --datadir qdata/nsdl init genesis.json
