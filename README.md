# Welcome to the Group 04 Resto App!
## Overview
The Group 04 Resto App currently allows users to place and edit tables in a restaurant, as well as display the restaurant's menu. The application is built using model-based programming, with code generated from Umple UML files.

## Installation
To run, clone the repository into an Apache Maven project.  

## Usage Instructions
### Tables
The Table Display grid starts a (0,0).   
Hover over icons for more information on what they can do. Click on an icon to activate the feature.  
  
Note that a table with a table number `x` that has been removed cannot be re-added. In other words, if `Table 1` is removed, the app will no longer allow a user to create a new `Table 1`. The previous `Table 1`'s data will however remain stored forever.

### Menu
Menu items are displayed in the Menu tab.  

### Persistence
All data is loaded from and saved to the `menu.resto` file.
