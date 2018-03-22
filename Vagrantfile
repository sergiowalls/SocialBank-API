Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/xenial64"
  config.ssh.insert_key = false
  config.vm.network "forwarded_port", guest: 5432, host: 5432, host_ip: "127.0.0.1"
  config.vm.provision "shell", inline: <<-SHELL
    sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt/ $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list'
    wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add -
    echo "Updating system"
    apt-get update
    apt-get dist-upgrade -y
    echo "Installing postgres"
    apt-get install -y postgresql-client-9.4 postgresql-9.4
    echo "Configuring and restarting PostgreSQL"
    echo 'listen_addresses = '"'"'*'"'" >> /etc/postgresql/9.4/main/postgresql.conf
    echo 'host    all             all             10.0.2.0/24            md5' >> /etc/postgresql/9.4/main/pg_hba.conf
    systemctl restart postgresql
    echo "Creating vagrant user and database"
    echo "CREATE ROLE vagrant CREATEDB CREATEROLE CREATEUSER LOGIN UNENCRYPTED PASSWORD 'vagrant'" | sudo -u postgres psql -a -f -
    echo "CREATE DATABASE vagrant OWNER vagrant" | sudo -u postgres psql -a -f -
    echo "Preparing for packaging"
  SHELL
end
