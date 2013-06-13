
Introduction
------------

This document will explain how to use the verifier for the Verificatum mix-net (VMN).
The Verificatum is an open source mix-net server utilized in the "Wombat" electronic voting system.
This project is an implementation of a third-party verifier, testing the correctness of Verificatum.

Written by: Itay Vallach, Sofia Zax, Daniel Shoshan and Tagel Shaked
Reviewed by: Prof. Amnon Ta-Shma, Mr. Tomer Hasid

Tel-Aviv university, fall 2013.

Installing the Verifier
-----------------------

Before installing Verifier you must have apache ant and git installed.

Instructions on how to build and run on Linux
>  git clone git://github.com/tagel/Verifier.git
> cd Verifier
> ant
> cd dist
> chmod a+x verifier.sh
You can now run the verifier.sh command

Instructions for windows
1. clone the repository from git://github.com/tagel/Verifier.git
2. open cmd and go to the directory where you cloned the repository
3. Run ant
4. cd dist
You can now run the verifier.bat command

Working with the verifier
-------------------------

The verifier can be invoked from the command line using a basic options described here:

Windows users - instead of "verifier" please use "verifier.bat" (for example verifier.bat -compat)
Linux users - instead of "verifier" please use "verifier.sh" (for example verifier.sh -compat)

1. verifier -compat 
	outputs a space separated list of all versions of Verificatum for which the verifier is compatible

2. verifier -mix protInfo.xml directory
	verifying a proof of mixing 

3. verifier -shuffle protInfo.xml directory
	verifying a proof of shuffling

4. verifier -decrypt protInfo.xml directory
	verifying a proof of decryption

Commands 2-4 can be used with the following optional flags, described in the implementation paper (attached):

-auxsid
-width
-nopos
-noposc
-noccpos 
-nodec 

protInfo - Path to protocol info file manufactured by Verificatum.
directory- Path to default directory which contains needed information.

for all commands, you can use the "-v" option to turn on verbose output.


Verifier's output
-----------------
The output of the verifier will be "True", if the verification process has succeeded and "False" otherwise.
The verifier will notify if any errors or exception occurring during verification.

If the "-v" option is chosen, the verifier will print logging messages during the verification process.