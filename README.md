# SocialBank test

## Database setup

In **application.properties** file located in `src/main/resources/` add the following variables:

```
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
```

Where _url_ should look like `jdbc:postgresql://localhost:<PORT>/<DATABASE_NAME>?useSSL=false`
_username_ is the username of the database and _password_ the belonging password.

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
