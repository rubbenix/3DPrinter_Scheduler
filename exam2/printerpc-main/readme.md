# PrinterPC interface

Interface that runs on a PC connected to multiple printers.
Has the ability to add a print to be printed which returns a unique ID.
Can start and stop the printing process and display current prints and completed prints.

There is a mock created that simulates the activity. 
Feature not included in this implementation is registering a printer as ready. 
The implementation also does not track specific printers, just that it has a limit of 24 printers.

Tests were written which use dependency injection to make sure the mock works as intended.
There is also a simple testerapp that allows checking the thread starts and stops properly.

It is best to include this project as a module in your own code.

Dependencies:

Jackson fasterxml.jackson.core.databind
Junit 