#ECHO off

	REM this is the directory where we will be keeping all of the files
set "dir1=C:\Users\Nicholas Smith\Desktop\Alienware Files\Nicholas\Queens\3rd Year 1st Semester\CISC 327\A6\dailyRun"

	REM for loop to loop through each run of the front end during a given day
for %%i in ("%dir1%\*.txt") DO
	
	REM opening MainClass.java (Front End)
	REM we have one text files per login session, routed to the standard input. 
	java MainClass session%%~in.txt < %%i > nul

	REM This line merges all of the daily transaction files into dailyTransactions.txt
copy /b /y dailyTransactions%%~i\*.txt dailyTransactions.txt > nul


	REM BackEnd.java does not take any parameters and does not need the standard input/ouput streams
	REM we are able to simply run BackEnd.java with no other arguments
	REM We are specificing that BackEnd.java  will be in the same directory as the text input files
	REM namely, dailyTransactions.txt and masterEvents.txt)
java BackEnd 