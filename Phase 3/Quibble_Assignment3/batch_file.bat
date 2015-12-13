@echo off
	REM Christopher Thomas, 10066835
	REM Nicholas Smith, 10098522


	REM This is the directory in which we keep all our test inputs
set "dir1=C:\Users\Nicholas Smith\Desktop\Alienware Files\Nicholas\Queens\3rd Year 1st Semester\CISC 327\cisc327dir"


	REM Outer for loop to go through all folders, organized by which function they are testing (add, create, delete, etc.)
	REM Inner for loop for each test case folder (t1, t2, t3 etc.)
	REM Takes each text input file and uses it as the standard input for our Java Quibble program
	REM After input is read, it takes program output and stores it in a text file
FOR %caseFolder in ("%dir1%\*") DO for %testFolder in ("%dir1%\%caseFolder\*in.txt") DO java MainClass cisc327dir/%testFolderin.txt output/%testFolderout.txt < cisc327dir/%testFolderin.txt > output/%testFolderoutTest.txt

	REM Note: we have generated expected text output files, and will manually check this script's outputs against them