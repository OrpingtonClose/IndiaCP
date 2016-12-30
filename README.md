Introduction
============

Commercial Paper in India is a new addition to short-term instruments in
Indian Money market since 1990 onward. The introduction of Commercial
paper as the short-term monetary instrument was the beginning of a
reform in Indian Money market on the background of trend of
Liberalization which began in the world economy during 1985 to 1990. A
commercial paper in India is the monetary instrument issued in the form
of promissory note. It acts as the debt instrument to be used by large
corporate companies for borrowing short-term monetary funds in the money
market.

Commercial Paper Market
-----------------------

The introduction of commercial paper as debt instrument has promoted
commercial paper market as one of the components of Indian money market.
In this commercial paper market, the issuers of commercial paper create
supply while the subscribers to commercial paper create demand for these
papers. The interaction between supply and demand for commercial papers
promotes the commercial paper market.

Issuers of Commercial Paper
---------------------------

The issuers of Commercial papers in Indian money market are broadly
classified into:

-   Leasing and Finance Companies

-   Manufacturing companies

-   [Financial
    > Institutions](https://en.wikipedia.org/wiki/Financial_Institution)

During the decade of 2000-01 to 2010-11, Leasing and finance companies
had the average share of 70% of total issue of Commercial papers; while
Manufacturing companies and Financial institutions had the average share
of 15% each.

Our implementation
------------------

Our application is a demonstration of how a commercial paper trading
network might be implemented on Blockchain which is agnostic to the
underlying DL infrastructure.

Distributed Ledger
==================

What is a DL
------------

A distributed ledger is a digital record of who-owns-what. But unlike
traditional database technology, there is no central administrator of
the ledger, nor is there a central data store.

Instead, the ledger is replicated among many different nodes in a
peer-to-peer network, and a consensus algorithm ensures that each node’s
copy of the ledger is identical to every other node’s copy, which is why
we can refer to the set of copies as a single shared ledger.

Asset owners must use cryptographic signature to debit their account and
credit another’s, so a distributed ledger is unforgeable.

India CP Implementation
-----------------------

We have provided 2 choices of DLT's

-   R3 Corda

-   Ethereum Quorum.

### R3 Corda

Corda is a distributed ledger platform designed from the ground up to
record, manage and synchronise financial agreements between regulated
financial institutions. It is heavily inspired by and captures the
benefits of blockchain systems, without the design choices that make
blockchains inappropriate for many banking scenarios.

-   Link to the R3 Corda site: http://www.r3cev.com/

-   Corda’s key features include:

-   Corda has no unnecessary global sharing of data: only those parties
    with a legitimate need to know can see the data within an agreement

-   Corda choreographs workflow between firms without a central
    controller

-   Corda achieves consensus between firms at the level of individual
    deals, not the level of the system

-   Corda’s design directly enables regulatory and supervisory observer
    nodes

-   Corda transactions are validated by parties to the transaction
    rather than a broader pool of unrelated validators

-   Corda supports a variety of consensus mechanisms

-   Corda records an explicit link between human-language legal prose
    documents and smart contract code

-   Corda is built on industry-standard tools

-   Corda has no native cryptocurrency

### Ethereum Quorum

Quorum is an Ethereum-based distributed ledger protocol with
transaction/contract privacy and a new consensus mechanism.

Key enhancements:

-   Quorum Chain - a new consensus model based on majority voting

-   Constellation - a peer-to-peer encrypted message exchange

-   Peer Security - node/peer permissioning using smart contracts

Source location
===============

The source is located in the Github repository.

Azure Cloud Setup
=================

Infrastructure
--------------

### Software required

-   MobaXterm/Putty to SSH the machine on Azure

### Machine’s and IP’s

-   Ubuntu machine hosting the DL’s and Integration layer:
    **52.172.24.128**

-   UI Website: <http://finwizui.azurewebsites.net/>

### Credentials

Ubuntu admin

-   IP: **52.172.24.128**

-   Username : indiacp

-   Password : \*\*\*\*\*\*\*\* (connect with Nilav)

UI credentials

Link: <http://finwizui.azurewebsites.net/>

1.  Issuer

    -   Issuer1

    -   Username: issuer1

    -   Password: issuer1@12345

2.  Investor

    -   Investor1

    -   Username: investor1

    -   Password: investor1@12345

    <!-- -->

    -   Investor2

    -   Username: investor2

    -   Password: investor2@12345

### Corda Build Setup

1.  The build for the Corda DL is outlined at the VSTS online link

> IndiaCP\_CordaBuild:
>
> <https://finwiz.visualstudio.com/IndiaCP/_build/index?context=Mine&path=%5C&definitionId=3&_a=completed>

1.  Fire of a build using the Queue new build option

    ![](media/image1.png){width="4.680555555555555in"
    height="1.4981299212598425in"}

### Ethereum

SSH to the Azure Ubuntu Box : **52.172.24.128**

\*\*\*\*\*\*\*\*\*The entire code is copied to the folder
**eth-indiacp** in the home directory.\*\*\*\*\*\*\*\*

This can be done through the IndiaCP\_EthBuild build definition on the
visual studio team project. The build and run steps need to be automated
still.

![](media/image2.png){width="6.268055555555556in"
height="1.1118055555555555in"}

### DL Integration Layer 

1.  SSH to the Azure Ubuntu Box : **52.172.24.128**

2.  Execute the following steps in order

    -   cd IndiaCP

    -   sudo git pull

    -   cd IndiaCP\_DL\_Integration

    -   mvn clean package

    -   mvn exec:java

3.  This will start the Integration layer service on the port 8181

4.  Check the following link to confirm that you are able to access the
    same - <http://52.172.42.128:8181/indiacp/application.wadl>

### UI Layer

Build the UI Layer

![](media/image3.png){width="4.215277777777778in"
height="1.6574365704286964in"}

Once the build is completed, it will automatically get released and you
should be able to check the url <http://finwizui.azurewebsites.net/> .

DL Agnostic Integration Layer
=============================

The What
--------

The How
-------

The Setup
---------

User Interface
==============

The What
--------

The How
-------

The Setup
---------

Infrastructure Setup
====================

Cloud platform
--------------

Node Setup
----------

The Setup
---------

Swagger API specification
=========================

The Swagger specification for the DL Layer is in the GitHub source
folder **Swagger Specification.**

API Commands 
=============

-   CP Program

-   CP Issue

-   Add IPA Verification Doc

-   Add ISIN for CP Program

-   Add ISIN generation Doc for CP Program


