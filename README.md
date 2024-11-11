 
# Rental Property Management System

This is a Java-based system for managing rental properties, tenants, and rental agreements.

## Features

- Manage tenants, hosts, and properties
- Create, update, and delete rental agreements
- View and sort rental agreements
- Save and load data from files

## Setup

1. Clone the repository
2. Ensure you have Java 11 and Maven installed
3. Run `mvn clean install` to build the project

## Running the Application

Run the `ConsoleUI` class to start the application:

```
java -cp target/rental-property-management-system-1.0-SNAPSHOT.jar com.rentalsystem.ui.ConsoleUI
```

## Project Structure

- `src/main/java/com/rentalsystem/`
  - `model/`: Contains entity classes
  - `manager/`: Contains the RentalManager interface and implementation
  - `util/`: Contains utility classes for file handling and date operations
  - `ui/`: Contains the ConsoleUI class for user interaction

## Testing

Run `mvn test` to execute the unit tests.

## Contributing

Please read CONTRIBUTING.md for details on our code of conduct, and the process for submitting pull requests.

## License

This project is licensed under the MIT License - see the LICENSE.md file for details.



Project Structure:

```
rental-property-management-system/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── rentalsystem/
│                   ├── manager/
│                   │   ├── RentalManager.java
│                   │   ├── RentalManagerImpl.java
│                   │   ├── TenantManager.java
│                   │   ├── TenantManagerImpl.java
│                   │   ├── HostManager.java
│                   │   ├── HostManagerImpl.java
│                   │   ├── PropertyManager.java
│                   │   └── PropertyManagerImpl.java
│                   ├── model/
│                   │   ├── Person.java
│                   │   ├── Tenant.java
│                   │   ├── Host.java
│                   │   ├── Property.java
│                   │   ├── ResidentialProperty.java
│                   │   ├── CommercialProperty.java
│                   │   ├── RentalAgreement.java
│                   │   └── Payment.java
│                   ├── util/
│                   │   ├── FileHandler.java
│                   │   ├── DateUtil.java
│                   │   └── InputValidator.java
│                   └── ui/
│                       ├── ConsoleUI.java
│                       └── MenuBuilder.java
└── resources/
    ├── tenants.txt
    ├── hosts.txt
    ├── properties.txt
    └── rental_agreements.txt
```

1. Model Classes:

a. Person.java:
- Base class for Tenant and Host
- Attributes: id, fullName, dateOfBirth, contactInformation
- Methods: getters and setters for all attributes

b. Tenant.java (extends Person):
- Additional attributes: List<RentalAgreement> rentalAgreements, List<Payment> paymentTransactions
- Methods:
  - addRentalAgreement(RentalAgreement agreement)
  - addPaymentTransaction(Payment payment)
  - getters for rentalAgreements and paymentTransactions

c. Host.java (extends Person):
- Additional attributes: List<Property> managedProperties, List<String> cooperatingOwners, List<RentalAgreement> rentalAgreements
- Methods:
  - addManagedProperty(Property property)
  - addCooperatingOwner(String owner)
  - addRentalAgreement(RentalAgreement agreement)
  - getters for managedProperties, cooperatingOwners, and rentalAgreements

d. Property.java:
- Attributes: id, address, price, status (enum: AVAILABLE, RENTED, UNDER_MAINTENANCE), owner, List<Host> hosts
- Methods:
  - addHost(Host host)
  - getters and setters for all attributes

e. ResidentialProperty.java (extends Property):
- Additional attributes: numberOfBedrooms, hasGarden, isPetFriendly
- Methods: getters and setters for additional attributes

f. CommercialProperty.java (extends Property):
- Additional attributes: businessType, parkingSpaces, squareFootage
- Methods: getters and setters for additional attributes

g. RentalAgreement.java:
- Attributes: id, mainTenant, List<Tenant> subTenants, property, period (enum: DAILY, WEEKLY, FORTNIGHTLY, MONTHLY), contractDate, rentingFee, status (enum: NEW, ACTIVE, COMPLETED)
- Methods:
  - addSubTenant(Tenant subTenant)
  - getters and setters for all attributes

h. Payment.java:
- Attributes: id, amount, paymentDate, paymentMethod, tenant, rentalAgreement
- Methods: getters and setters for all attributes

2. Manager Interfaces and Implementations:

a. RentalManager.java and RentalManagerImpl.java:
- Methods:
  - addRentalAgreement(RentalAgreement agreement)
  - updateRentalAgreement(RentalAgreement agreement)
  - deleteRentalAgreement(String agreementId)
  - getRentalAgreement(String agreementId)
  - getAllRentalAgreements()
  - getSortedRentalAgreements(String sortBy)
  - saveToFile()
  - loadFromFile()

b. TenantManager.java and TenantManagerImpl.java:
- Methods:
  - addTenant(Tenant tenant)
  - updateTenant(Tenant tenant)
  - deleteTenant(String tenantId)
  - getTenant(String tenantId)
  - getAllTenants()
  - saveToFile()
  - loadFromFile()

c. HostManager.java and HostManagerImpl.java:
- Methods:
  - addHost(Host host)
  - updateHost(Host host)
  - deleteHost(String hostId)
  - getHost(String hostId)
  - getAllHosts()
  - saveToFile()
  - loadFromFile()

d. PropertyManager.java and PropertyManagerImpl.java:
- Methods:
  - addProperty(Property property)
  - updateProperty(Property property)
  - deleteProperty(String propertyId)
  - getProperty(String propertyId)
  - getAllProperties()
  - saveToFile()
  - loadFromFile()

3. Utility Classes:

a. FileHandler.java:
- Methods for loading and saving data to/from text files:
  - loadTenants(), saveTenants(List<Tenant> tenants)
  - loadHosts(), saveHosts(List<Host> hosts)
  - loadProperties(), saveProperties(List<Property> properties)
  - loadRentalAgreements(), saveRentalAgreements(List<RentalAgreement> agreements)
  - saveAllData(List<Tenant> tenants, List<Host> hosts, List<Property> properties, List<RentalAgreement> agreements)

b. DateUtil.java:
- Methods:
  - parseDate(String dateString): Converts a string to a Date object
  - formatDate(Date date): Converts a Date object to a string

c. InputValidator.java:
- Methods:
  - getIntInput(Scanner scanner, String prompt, int min, int max): Gets an integer input within a specified range
  - getStringInput(Scanner scanner, String prompt): Gets a string input
  - getDoubleInput(Scanner scanner, String prompt): Gets a double input

4. UI Classes:

a. ConsoleUI.java:
- Main class for user interaction
- Methods:
  - start(): Initializes the application and displays the main menu
  - displayMainMenu(): Shows the main menu options
  - manageRentalAgreements(), manageTenants(), manageHosts(), manageProperties(): Sub-menus for managing different entities
  - addRentalAgreement(), updateRentalAgreement(), deleteRentalAgreement(), viewRentalAgreement(), viewAllRentalAgreements(): Methods for managing rental agreements
  - addTenant(), updateTenant(), deleteTenant(), viewTenant(), viewAllTenants(): Methods for managing tenants
  - addHost(), updateHost(), deleteHost(), viewHost(), viewAllHosts(): Methods for managing hosts
  - addProperty(), updateProperty(), deleteProperty(), viewProperty(), viewAllProperties(): Methods for managing properties
  - generateReports(): Generates various reports
  - loadAllData(), saveAllData(): Loads and saves all data to/from files

b. MenuBuilder.java:
- Utility class for building menus
- Methods:
  - addOption(String option): Adds an option to the menu
  - clear(): Clears all options
  - display(String title): Displays the menu with a title

How to Use the System:

1. Run the ConsoleUI class to start the application.
2. The main menu will be displayed with options to manage rental agreements, tenants, hosts, properties, generate reports, or exit.
3. Select an option by entering the corresponding number.
4. For each management option (rental agreements, tenants, hosts, properties), you can add, update, delete, view individual items, or view all items.
5. When adding or updating items, you'll be prompted to enter the necessary information.
6. The system automatically saves changes to the corresponding text files after each operation.
7. You can generate reports to view summaries of tenants, hosts, properties, and rental agreements.
8. When you exit the application, all data is saved to the text files in the resources folder.

To use specific functions:

1. Adding an item (e.g., addTenant()):
   - Select the "Add Tenant" option from the Manage Tenants menu.
   - Enter the required information when prompted (ID, full name, date of birth, contact information).
   - The new tenant will be added to the system and saved to the tenants.txt file.

2. Updating an item (e.g., updateProperty()):
   - Select the "Update Property" option from the Manage Properties menu.
   - Enter the ID of the property you want to update.
   - Enter new information for the fields you want to change, or press enter to keep the current values.
   - The updated property information will be saved to the properties.txt file.

3. Deleting an item (e.g., deleteHost()):
   - Select the "Delete Host" option from the Manage Hosts menu.
   - Enter the ID of the host you want to delete.
   - The host will be removed from the system and the hosts.txt file will be updated.

4. Viewing items (e.g., viewAllRentalAgreements()):
   - Select the "View All Rental Agreements" option from the Manage Rental Agreements menu.
   - The system will display a list of all rental agreements stored in the system.

5. Generating reports:
   - Select the "Generate Reports" option from the main menu.
   - Choose the type of report you want to generate (Tenant, Host, Property, or Rental Agreement).
   - The system will display a summary of the selected entity type.













# Rental Property Management System Report

## 1. Application Description

The Rental Property Management System is a Java-based console application designed to manage rental properties, tenants, hosts, and rental agreements. This system provides a comprehensive solution for property managers to efficiently handle various aspects of rental property management.

Key features of the application include:

1. Management of tenants, hosts, properties, and rental agreements
2. Data persistence using text files
3. Report generation for various entities
4. User-friendly console interface

The application follows object-oriented design principles and implements a layered architecture, separating concerns between data models, business logic, and user interface.

### 1.1 UML Class Diagram

[Insert UML Class Diagram here]

The UML class diagram illustrates the relationships between the main classes in the system. Key components include:

- Model classes (Person, Tenant, Host, Property, RentalAgreement, Payment)
- Manager interfaces and implementations
- Utility classes (FileHandler, DateUtil, InputValidator)
- UI classes (ConsoleUI, MenuBuilder)

### 1.2 Design Patterns and Principles

The application implements several design patterns and principles:

1. **Inheritance**: The Person class serves as a base class for Tenant and Host, promoting code reuse.
2. **Interface Segregation**: Separate manager interfaces (TenantManager, HostManager, etc.) are used to define specific responsibilities.
3. **Dependency Injection**: Manager implementations are injected into the ConsoleUI, allowing for easier testing and future modifications.
4. **Single Responsibility Principle**: Each class has a single, well-defined responsibility.
5. **Open/Closed Principle**: The system is designed to be easily extendable, e.g., adding new property types or report types.

## 2. Application Flow

The application flow can be described as follows:

1. The user starts the application by running the ConsoleUI class.
2. The system loads existing data from text files.
3. The main menu is displayed, offering options to manage different entities or generate reports.
4. Based on the user's selection, the system navigates to the appropriate sub-menu or performs the requested action.
5. For management operations (add, update, delete, view), the system interacts with the corresponding manager class.
6. After each operation, data is automatically saved to the text files.
7. The user can generate reports to view summaries of different entities.
8. When the user chooses to exit, all data is saved, and the application terminates.

[Insert Application Flow Diagram here]

## 3. API List

The application provides several APIs through its manager interfaces:

### 3.1 RentalManager

- `addRentalAgreement(RentalAgreement agreement)`: Adds a new rental agreement to the system.
- `updateRentalAgreement(RentalAgreement agreement)`: Updates an existing rental agreement.
- `deleteRentalAgreement(String agreementId)`: Removes a rental agreement from the system.
- `getRentalAgreement(String agreementId)`: Retrieves a specific rental agreement.
- `getAllRentalAgreements()`: Returns a list of all rental agreements.
- `getSortedRentalAgreements(String sortBy)`: Returns a sorted list of rental agreements.

### 3.2 TenantManager

- `addTenant(Tenant tenant)`: Adds a new tenant to the system.
- `updateTenant(Tenant tenant)`: Updates an existing tenant's information.
- `deleteTenant(String tenantId)`: Removes a tenant from the system.
- `getTenant(String tenantId)`: Retrieves a specific tenant.
- `getAllTenants()`: Returns a list of all tenants.

### 3.3 HostManager

- `addHost(Host host)`: Adds a new host to the system.
- `updateHost(Host host)`: Updates an existing host's information.
- `deleteHost(String hostId)`: Removes a host from the system.
- `getHost(String hostId)`: Retrieves a specific host.
- `getAllHosts()`: Returns a list of all hosts.

### 3.4 PropertyManager

- `addProperty(Property property)`: Adds a new property to the system.
- `updateProperty(Property property)`: Updates an existing property's information.
- `deleteProperty(String propertyId)`: Removes a property from the system.
- `getProperty(String propertyId)`: Retrieves a specific property.
- `getAllProperties()`: Returns a list of all properties.

### 3.5 FileHandler

- `loadTenants()`, `saveTenants(List<Tenant> tenants)`: Load and save tenant data.
- `loadHosts()`, `saveHosts(List<Host> hosts)`: Load and save host data.
- `loadProperties()`, `saveProperties(List<Property> properties)`: Load and save property data.
- `loadRentalAgreements()`, `saveRentalAgreements(List<RentalAgreement> agreements)`: Load and save rental agreement data.
- `saveAllData(...)`: Saves all data to their respective files.

## 4. Drawbacks and Future Work

### 4.1 Drawbacks

1. **Limited Scalability**: The current implementation uses text files for data storage, which may not be suitable for large-scale applications with high concurrency.

2. **Lack of Authentication**: The system does not include user authentication or authorization, which may be necessary for a production environment.

3. **Limited Reporting**: The current reporting functionality is basic and may not meet the needs of all users.

4. **Console-based Interface**: While functional, a console-based interface may not be as user-friendly or efficient as a graphical user interface (GUI) for some users.

5. **Limited Error Handling**: The current implementation could benefit from more robust error handling and validation.

### 4.2 Future Work

1. **Database Integration**: Implement a database solution (e.g., MySQL, PostgreSQL) for improved data management and scalability.

2. **User Authentication**: Add a user authentication system with different roles (e.g., admin, property manager, tenant) and corresponding access levels.

3. **Enhanced Reporting**: Develop more comprehensive reporting features, including financial reports, occupancy rates, and maintenance schedules.

4. **GUI Development**: Create a graphical user interface using a framework like JavaFX or Swing for improved user experience.

5. **API Development**: Develop a RESTful API to allow integration with other systems or the creation of mobile applications.

6. **Payment Integration**: Implement a payment system to handle rent collection and financial transactions.

7. **Maintenance Management**: Add features to track and manage property maintenance requests and schedules.

8. **Document Management**: Implement a system to store and manage important documents (e.g., leases, inspection reports).

9. **Notifications**: Add an automated notification system for important events (e.g., rent due, lease expiration).

10. **Data Analytics**: Implement data analysis features to provide insights on property performance, tenant behavior, and market trends.

By addressing these drawbacks and implementing the proposed future work, the Rental Property Management System can evolve into a more robust, scalable, and feature-rich solution for property management needs.