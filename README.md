# CartCrafters 🛒 - EECS 4413 Project

This project represents the collaborative work of **Lev Kropp** and **Kyle Schwartz**.

## Overview 🌐

CartCrafters is a project developed as part of the EECS 4413 course. Our primary goal is to apply the practical concepts learned throughout the course. More details about the project's functionality can be found in the project documentation.

## Get Started 🏁

By default, the backend is not externally accessibly. To change this, add `-p 8080:8080` to docker run or uncomment the appropriate line in the compose.

### Docker CLI

```
docker run -d -p 3000:3000 ghcr.io/ksmarty/cartcrafters:main
```

### Docker Compose

Also available in [`docker-compose.yml`](docker-compose.yml)

```yaml
version: "3.3"
services:
  cartcrafters:
    container_name: cartcrafters
    image: "ghcr.io/ksmarty/cartcrafters:main"
    ports:
      - "80:3000"
    #   Uncomment to access backend externally
    #   - "8080:8080"
    restart: unless-stopped
```

## Installation 🛠️

### Prerequisites

Make sure the following are installed on your local development machine:

- Java Development Kit (JDK)

### Setup

1. Clone this repository to your local machine using `https://github.com/ksmarty/CartCrafters.git`.
2. Import the project into your preferred IDE.
3. Install the recommended extensions for your IDE.
4. Run the Dev configuration.
5. Navigate to `http://localhost:8080/` or `http://localhost:8080/eecs4413-project/` in your web browser to access the application.

## Contribution 🤝

We welcome contributions! Whether you're creating new features, fixing bugs, improving documentation, or translating, your input is appreciated.

## License 📜

This project is licensed under the MIT License - see the LICENSE.md file for more details.

## Authors 👥

- Lev Kropp
- Kyle Schwartz

## Acknowledgments 🙏

We extend our gratitude to our course instructor and fellow classmates for their valuable feedback and insights, contributing to the success of this project.
