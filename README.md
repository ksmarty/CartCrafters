# CartCrafters 🛒 - EECS 4413 Project

This project represents the collaborative work of **Lev Kropp** and **Kyle Schwartz**.

## Overview 🌐

CartCrafters is a project developed as part of the EECS 4413 course. Our primary goal is to apply the practical concepts learned throughout the course. More details about the project's functionality can be found in the project documentation.

## Get Started 🏁

### Docker CLI

```
docker run -d -p 3000:3000 -p 8080:8080 ghcr.io/ksmarty/cartcrafters:main
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
      - "3000:3000"
      - "8080:8080"
    # - "host:container"
    restart: unless-stopped
```

## API

[View Here](https://studio-ws.apicur.io/sharing/803e7642-744f-434d-8077-3155542a53dc)

## Database UML

[View Live](https://www.plantuml.com/plantuml/uml/bLDDJ-im4BpdL_ZbrAVt8fLAvP1JGpyWQcchgd2jJ6p0Kcm7UoDKjVntujXLZHKLkEtER6PdlCf80rPGypBw0w-KKrILc8aeeW8Ox57yeAdICS8QrQP2XmYXVssTanJbootghUT_6p198jJY0dEQeiTiCxxBDhr1tv3FJ6bOVY66BL7YRfnopCepUDLlqGfh_IuLyUI7ruR0X2YgjOVR3vY-hQc65DsiLejfjcoXZ70cpj37EUnQIL8NOFDMhd6HNTkLsYOEl1Ag2yKgO59uuInNOA0lKkt3r4yA2nA2bM958p-PZlFxRD78_nyO-At6hUlMZXzYfSZfPDAKBe1jke_cuJE3sza91hCtsquR12JWCkEo5X1w2NVlSg3SCx1ZflTTc84k9uOCh0Jlxj0WFKBuEsTZhDnT2rCvDwR-oPE4Ibv48-4IUfhT0Ittwtnck_txZiSaaOVJNLCKp-Pnv3gVSuRSW5jnQSINTo_a6DjuJ9VsfOaH5QJcvIS0)

![Database UML](https://www.plantuml.com/plantuml/svg/bLDDJ-im4BpdL_ZbrAVt8fLAvP1JGpyWQcchgd2jJ6p0Kcm7UoDKjVntujXLZHKLkEtER6PdlCf80rPGypBw0w-KKrILc8aeeW8Ox57yeAdICS8QrQP2XmYXVssTanJbootghUT_6p198jJY0dEQeiTiCxxBDhr1tv3FJ6bOVY66BL7YRfnopCepUDLlqGfh_IuLyUI7ruR0X2YgjOVR3vY-hQc65DsiLejfjcoXZ70cpj37EUnQIL8NOFDMhd6HNTkLsYOEl1Ag2yKgO59uuInNOA0lKkt3r4yA2nA2bM958p-PZlFxRD78_nyO-At6hUlMZXzYfSZfPDAKBe1jke_cuJE3sza91hCtsquR12JWCkEo5X1w2NVlSg3SCx1ZflTTc84k9uOCh0Jlxj0WFKBuEsTZhDnT2rCvDwR-oPE4Ibv48-4IUfhT0Ittwtnck_txZiSaaOVJNLCKp-Pnv3gVSuRSW5jnQSINTo_a6DjuJ9VsfOaH5QJcvIS0)

## Installation 🛠️

### Prerequisites

Make sure the following are installed on your local development machine:

- Java Development Kit 17 ([Temurin](https://adoptium.net/temurin/releases/))
- [Maven 3.9.4](https://maven.apache.org/install.html) or later
- [Node.js 16.14](https://nodejs.org/) or later

### Setup

1. Clone this repository to your local machine using `https://github.com/ksmarty/CartCrafters.git`.
2. Open `/backend` with your preferred IDE. We use IntelliJ IDEA Ultimate.
3. Install the dependencies with `mvn clean install`.
4. If using IntelliJ IDEA Ultimate, install the recommended extensions for your IDE & Run the `Dev` configuration.
5. Navigate to `http://localhost:8080/` to access the backend API.
6. Open `/frontend` with your preferred IDE. We use VSCode.
7. Install the dependencies with `npm i` and run the frontend with `next dev`.
8. Navigate to `http://localhost:3000/` to access the frontend.

## Building Docker Images

We use [nektos/act](https://github.com/nektos/act) to run our pipelines locally for testing.

Run `act -s GITHUB_TOKEN=[YOUR_TOKEN]` with your github token as outlined [here](https://github.com/nektos/act#github_token).

## Contribution 🤝

We welcome contributions! Whether you're creating new features, fixing bugs, improving documentation, or translating, your input is appreciated.

## License 📜

This project is licensed under the MIT License - see the LICENSE.md file for more details.

## Authors 👥

- Lev Kropp
- Kyle Schwartz

## Credits 🙏

T-Shirt Photo by <a href="https://unsplash.com/@anomaly?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Anomaly</a> on <a href="https://unsplash.com/photos/WWesmHEgXDs?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Unsplash</a>

Backpack Photo by <a href="https://unsplash.com/@jibarox?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Luis Quintero</a> on <a href="https://unsplash.com/photos/8TSqJoI-NVs?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Unsplash</a>

Smart Phone Photo by <a href="https://unsplash.com/@mehrshadr?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Mehrshad Rajabi</a> on <a href="https://unsplash.com/photos/cLrcbfSwBxU?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Unsplash</a>

Jeans Photo by <a href="https://unsplash.com/@socialcut?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">S O C I A L . C U T</a> on <a href="https://unsplash.com/photos/02NEVE2fKQw?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Unsplash</a>

Bluetooth Speaker Photo by <a href="https://unsplash.com/@habibdadkhah?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Habib Dadkhah</a> on <a href="https://unsplash.com/photos/zxvnrxl5OXc?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText">Unsplash</a>
