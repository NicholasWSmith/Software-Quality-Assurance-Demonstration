#ECHO off


	REM The for loop to go through 5 runs of the daily script. 
for %%i IN (1 2 3 4 5) DO

	REM This calls the dailyScript.bat script and sends the standard output of each dailyScript to NUL
	call dailyScript.bat dayScript~%%i > NUL

	REM This merges the different transaction files into one cumulative dailyTransactions.txt file
	copy /b /y dayScript~%%i\eventTransactionFile dailyTransactions.txt> NUL
