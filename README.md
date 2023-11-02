# Recipe Management System API

## Frameworks and Language Used
- Framework: Spring Boot
- Language: Java
- Database: MySQL
- Deployment: AWS

## Data Flow
### Controller
- `UserController`: Manages user-related actions (sign-up, sign-in, etc.).
- `RecipeController`: Handles recipe-related actions (create, update, retrieve, delete, like, comment, etc.).

### Services
- `UserService`: Implements user-related logic, including user registration, sign-in, sign-out, and user-related actions.

### Repository
- `IUserRepo`: Manages user data in the database.
- `IRecipeRepo`: Handles recipe data storage.

### Other Dependencies
- `AuthenticationService`: Manages user authentication and token generation.
- `RecipeService`: Manages recipe-related logic and actions.
- `CommentService`: Handles comments on recipes.
- `LikesService`: Manages likes on recipes.
- `FollowService`: Manages user following relationships.

## Data Structure Used in Project
- An ArrayList in Java is a dynamic array-based data structure that provides a more flexible and convenient way of working with arrays. Unlike traditional arrays, ArrayLists can grow or shrink in size dynamically, making it a versatile choice for managing collections of data.

## Project Summary
The Recipe Management System API simplifies the management of culinary recipes. It offers user registration, recipe creation and editing, and various ways to retrieve and interact with recipes. Users can follow chefs, like and comment on recipes, and explore trending tags. This Java-based API, powered by Spring Boot and hosted on AWS, revolutionizes how food enthusiasts manage and share their culinary creations.
