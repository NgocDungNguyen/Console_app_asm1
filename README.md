 
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
            