# SSSM
super simple stock market

# Assumptions
As the application requirements were unclear on some points and there was no possibility to discuss these with the requirements author, various assumptions have been made to allow the design and implementation of the code module to proceed.

## Formula
* dividend yield:: "¿ Dividend" has been assumed to mean fixed dividend
* p/e ratio:: "Dividend" has been assumed to mean last dividend
* geometric mean:: it has been assumed that all volume weighted stock price values, including those equalling zero, are to be included in the calculation
* geometric mean:: the requirements do not specifically nominate the time period for trade selection, so the time period for the volume weighted stock price calculation (the previous 5 minutes) has been assumed/applied

## Code
* as the requirements document gives example stocks listed in pennies (the smallest currency unit), it was assumed that all price values would also be in pennies, allowing these values to be passed as integers
* assumptions in the implemented code are identified in 'TODO' comments
* as it does not appear as a requirement, it is assumed that no input validation is expected
  * the exception to this assumption is null values, which are checked as part of the "must be production quality" requirement
