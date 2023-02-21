# deloma-sepa-xml 

## A lightweight java toolkit library to work with sepa or related banking tasks.



# Documentation 

## Important notes: 
- create JAXB classes from given xsd file by using following command: 

`xjc -p com.package.subpackage.targetedPackageName -d drive:\path\to\xsdfile.xsd`

or just simply by right clicking on the xsd file and then clicking Generate 
  >> JAXB Classes 
   >> Select the Project 
   >> Select a package / give a name >> click next 
   >> Here Trear input as XML schema must be selected, == recommended checkboxes are then `Verbose` and `Use Strict validation` == 
 see following screenshot: 
![Generate JAXB classes in Eclipse example](https://user-images.githubusercontent.com/26557468/219416739-c0664896-e4a9-41e2-afd8-d4709b225238.png)



## Currently supported formats:
- *CAMT*: 
  - camt.052.001.02
  - camt.052.001.08

- *PAIN*: 
  - pain.008.003.02
  - pain.008.001.02
