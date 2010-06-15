Bill Payee List Grammar
========================

This project contains a grammar template and all associated Java code
for a simple bill payee list grammar.

Installation
------------

To use this project with NuGram Server, unzip the NuGram Server
archive `nugram-server.zip` and copy the directories `bin`, `conf`,
`lib`, and `webapp` to the directory containing this README file.

Configuration 
-------------

In `webapp/WEB-INF/web.xml`, make sure to set the following:

* The `com.nuecho.application.grammarserver.license-directory` context parameter must be set to the name of the directory containing the NuGram Server license.

* the `com.nuecho.application.grammarserver.context-initializers` initialization parameter for the `/grammars` servlet must be set to

    billpayees.abnf=com.nuecho.samples.grammars.BillPayeeList


Running the demo
----------------

Start NuGram Server by issuing the following command in the shell:

    % sh bin/server.sh

In another shell, test the grammar by issuing the following command:

    % curl localhost:8765/webapp/grammars/billpayees.abnf?symbol=AAPL&symbol=T

to get the resulting grammar in ABNF. If you prefer to generate the
grammar in GrXML, just replace the extension by `grxml`:

    % curl localhost:8765/webapp/grammars/billpayees.grxml?symbol=AAPL&symbol=T


Eclipse project
---------------

This directory contains an Eclipse project that can be used to test
the grammar with NuGram IDE.