# SocialBank

## Basic setup

In **application.properties** file located in `src/main/resources/` add the following variables:

```
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
flyway.url=
flyway.user=
flyway.password=
mail.host=
mail.port=
mail.from=
mail.password=
```

Where _url_ should look like `jdbc:postgresql://localhost:<PORT>/<DATABASE_NAME>?useSSL=false`
_username_ and _user_ are the username of the database and _password_ the belonging password.

### With Vagrant VM

Download and install [VirtualBox 5.2.8](https://www.virtualbox.org/)

Download and install [Vagrant 2.0.3](https://www.vagrantup.com/)

On IntelliJ IDEA install Vagrant plugin and restart IDE

To start Vagrant machine go to _Tools > Vagrant > Up_

**IMPORTANT:** Remember to turn off VM, using _vagrant halt_, before shutting down host machine

Vagrantfile comes with default user, password and database configurations. These are:

```
user: vagrant
pass: vagrant
db:   vagrant
```

To connect to it from IntelliJ IDEA go to _Database > New > Data Source > PostgreSQL_, put information on popup window and download missing driver

### With Docker

You must have Docker installed and Hyper-V enabled. Run following commands in order to build image and run a Docker container:

```
docker image build -t socialbank .
docker run --name socialbank -p 5432:5432 -d socialbank
```  

Dockerfile comes with default user, password and database configurations. These are:

```
user: socialbank
pass: socialbank
db:   socialbank
```

To connect to it from IntelliJ IDEA go to _Database > New > Data Source > PostgreSQL_, put information on popup window and download missing driver