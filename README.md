# deloma-sepa-xml 

A lightweight java toolkit library to work with sepa or related banking tasks to generate SEPA direct debit collector XML files in PAIN format or parse SEPA bank account statement XML files in CAMT format.


# Documentation 

## Minimum Java Version Requirement

|Version|min Java Version|JAXB generation|
|:---:|:---:|:---:|
|[2.0](https://github.com/deloma-de/deloma-sepa-xml/releases/tag/2.0)|11|maven plugin|
|[1.1](https://github.com/deloma-de/deloma-sepa-xml/releases/tag/1.1)|8|eclipse plugin|
|[1.0](https://github.com/deloma-de/deloma-sepa-xml/releases/tag/1.0)|8|eclipse plugin|

## Supported SEPA XML formats

|Format|min Version|
|:---:|:---:|
|camt.052.001.02|1.0|
|camt.052.001.08|1.0|
|pain.008.003.02|1.0|
|pain.008.001.02|1.0|
|pain.008.001.08|1.1|

## Migration Guide

### 1.1 to 2.0

- javax dependencies removed for new java 11 (jakarta) versions
- functions that return collections / arrays are renamed to plural form like `getRpt()` to `getRpts()`

## Test files

In the code folder `src/test/resources` are some test files for CAMT parsing which are used in the included unit tests. 

For test bank account data like IBAN check out this website [www.ibanvalidieren.de](https://ibanvalidieren.de/beispiele.html).