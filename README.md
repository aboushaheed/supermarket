  **Wellcome to the supermarket CLI**
  
  ![enter image description here](https://i.ibb.co/HxQLrLs/super.png)
 
 
  ![enter image description here](https://i.ibb.co/71792Yn/super.png)
   
 **Requierement**
 
 - Installing the Lombok IDE Plugin
    - in IntelliJ IDEA
    
        1) Go to File > Settings > Plugins
        2) Click on Browse repositories...
        3) Search for Lombok Plugin
        4) Click on Install plugin
        5) Restart IntelliJ IDEA

    - in Eclipse
    check this [tutorial](https://howtodoinjava.com/automation/lombok-eclipse-installation-examples/)
    
 --------------------------------
 
  **Just use de supermarket CLI**
  
  > init stock one time
  
  `Supermarket:>init`
  
  ![enter image description here](https://i.ibb.co/GvmRhRv/super.png)  
  
  > Example : add 5 apples to the basket
  
  `Supermarket:>shop -n apple -q 5`
  
  ![enter image description here](https://i.ibb.co/VThZQBs/super.png)
  
  > Example : create strawberry as product
  
  `Supermarket:>create-product -n strawberry -q 60 -p 0.89 `
  
  > Example : give an offer to strawberry
  
  `Supermarket:>update-product-offer -n strawberry -f "Buy Five Get Two For Free" `

  > Example : create any type of offer
  
    `Supermarket:>craete-offer -n "Buy Five Get Two For Free" -qb 5 -qf 2`
    
  > Show all offers 
  
   `Supermarket:>offers`
  
---------
  **All commandes**

             Built-In Commands
                     clear: Clear the shell screen.
                     exit, quit: Exit the shell.
                     help: Display help about available commands.
                     history: Display or save the history of previously run commands
                     script: Read and execute commands from a file.
                     stacktrace: Display the full stacktrace of the last error.
             
             Cli Basket Service
                     checkout: checkout all basket
                     shop: shop a product to the current basket ,[ shop -n|--name PRODUCT_NAME -q|--quantity QUANTITY ]
             
             Cli Create Service
                     create-offer: Create an offer,[ create-offer -n||--name OFFER_NAME  -qb QUANTITY_TO_BUY  -qf QUANTITY_FOR_FREE ]
                     create-product: Create a product,[ create-product -n||--name PRODUCT_NAME -q||--quantity QUANTITY  -p||--price PRICE ]
             
             Cli Printer Service
                     basket: show current basket
                     message: show a message to screen ,[ message -m|--message MESSAGE_TEXT ]
                     offers: show current offers
                     stock: show current stock
             
             Cli Stock Service
                     init: init stock (one shot option)
             
             Cli Update Service
                     update-offer: Update the offer for a given product,[ update-offer -n||--name OFFER_NAME -qb QUANTITY_TO_BUY  -qf QUANTITY_FOR_FREE  , use quote fo long name ]
                     update-product-offer: Update the offer for a given product,[ update-product-offer -n||--name PRODUCT_NAME -f||--offer OFFER_NAME , use quote fo long name ]
                     update-product-price: Update the price for a given product,[ update-product-price -n||--name PRODUCT_NAME -p||--price PRICE ]
                     update-product-stock: Update a stock for a given product,[ update-product-stock -n||--name PRODUCT_NAME -q||--quantity QUANTITY ]
