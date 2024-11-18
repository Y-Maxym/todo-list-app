# Todo List API

A simple RESTful API to manage a todo list application built using Spring Boot, Hibernate, and MySQL. This API allows users to create, read, update, and delete todo items. Each todo item consists of a title and a description. The items are persisted in a MySQL database using Hibernate.

## Features

- **Create** a new todo item with a title and description.
- **Read** all todo items or fetch a specific todo item by ID.
- **Update** an existing todo item.
- **Delete** a todo item by its ID.

## Tech Stack

- **Spring Boot**: The main framework for building the RESTful API.
- **Hibernate**: For object-relational mapping (ORM) to interact with the MySQL database.
- **MySQL**: A relational database to persist the todo items.
- **Gradle**: Dependency management and build tool.

## Prerequisites

- Java 17 or higher
- MySQL server running locally or remotely
- Gradle for dependency management and building the project

## Setup and Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/Y-Maxym/todo-list-api.git
   cd todo-list-api
    ```
2. **Configure MySQL**
   - Create a database in MySQL. You can name it todo_list or any other name of your choice.
   - Update the application.yaml file to match your MySQL configuration:
       ```
       spring.datasource.url=jdbc:mysql://localhost:3306/todo_list
       spring.datasource.username=root
       spring.datasource.password=root
       spring.jpa.hibernate.ddl-auto=update
       spring.jpa.show-sql=true
       spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
      ```
    Replace localhost, root, and root with your actual MySQL database host, username, and password.

3. **Install Dependencies**

   Use Gradle to install all the required dependencies:
    ```bash
   ./gradlew build
   ```
4. **Run the Application**
     ```bash
    ./gradlew bootRun
    ```

Short feedback:
1. Was it easy to complete the task using AI?
   - The task was relatively easy except for some configuration issues.
2. How long did task take you to complete?
   - About 3 hours.
3. Was the code ready to run after generation? What did you have to change to make it usable?
   - The main code that provided the chat was fully working. The only problems were in the configurations
4. Which challenges did you face during completion of the task?
   - The only difficulties were in writing the configurations and that took the most time
5. Which specific prompts you learned as a good practice to complete the task?
   - We need to give clearer and clearer tasks considering the details