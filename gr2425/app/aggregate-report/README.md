
# AGGREGATE-REPORT #

This module allows for an easy overview over the testcoverage of the project.

To open up the test-coverage table, follow these instructions:

First open up a terminal and enter `mvn clean verify` this will clean any previous maven build files and then search for any tests and run them. 

After running this command, the folder "target" should appear under app/aggregate-report/target. Enter this folder and look for a folder called "site". The folderpath should be: app/aggregate-report/target/site/jacoco-aggregate. Then look for a file called "index.html", right click this file and click "Reveal in File Explorer". This should open your file explorer, then double-click the index.html file. This opens your browser, displaying the testcoverage of all modules.