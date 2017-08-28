# Bot for sending price lists through telegram messenger
## Build
For building the bot your need to perform next simple steps:
1. Install Java 8
2. Install scala build tools SBT
3. Download last version. You can do it [here](https://github.com/AntonYurchenko/price-list-telegram-bot/releases)
4. Compile code and create universal package.

### Installation Java 8
I will show example for linux Fedora 26 Server. 
You need to open terminal and execute this simple commands: 
```bash
# enable root mode
su
# after input root password

# download java 8
wget -c --header "Cookie: oraclelicense=accept-securebackup-cookie" \
 http://download.oracle.com/otn-pub/java/jdk/8u144-b01/090f390dda5b47b9b721c7dfaa008135/jdk-8u144-linux-x64.rpm

# install java 8
dnf install ./jdk-8u144-linux-x64.rpm
```
### Installation SBT
If java has been installed successfully then we can install SBT 
You need to open terminal and execute this simple commands:
```bash
# enable root mode
su
# after input root password

# download sbt
wget https://github.com/sbt/sbt/releases/download/v0.13.15/sbt-0.13.15.tgz

# extract sbt
tar -xzvf sbt-0.13.15.tgz -C /opt/

# install sbt
ln -s /opt/sbt/bin/sbt /usr/local/bin/sbt
```

### Downloading last version code
You can download last version of code from browser [here](https://github.com/AntonYurchenko/price-list-telegram-bot/releases)
or from command line. If you want to use command line then you need to open terminal and execute this simple commands: 
```bash
# create work directory
mkdir ~/botCode/

# go to work directory
cd ~/botCode/

# download code
wget https://github.com/AntonYurchenko/price-list-telegram-bot/archive/<release version>.tar.gz
```

### Compiling
When you downloaded package with last version of the code you need extract it.
You need to open terminal and execute this simple commands:
```bash
# go to work directory
cd ~/botCode/

# extract code
tar -xzvf <release version>.tar.gz

# go to code directory
cd price-list-telegram-bot-<release version>/

# compiling of code
sbt compile
# waiting for the successful completion

# creating universal package
sbt universal:packageBin
# waiting for the successful completion
```
Universal package ```price-list-telegram-bot.zip``` will be at ```target/universal```.
The package contains all necessary for installation bot on server.

## Installation on server
If you completed compilation then you have universal package ```price-list-telegram-bot.zip```. 
Right now I show you how install the bot on server. You need perform next simple steps:
1. Unpack the bot
2. Create system user and make it owner of bot home directory
3. Change configuration for your purposes
4. Install bot as service 

### Unpacking
You need to unpack ```price-list-telegram-bot.zip``` to directory ```/opt/``` on server.
You need to open terminal and execute this simple commands:
```bash
# go to directory with universal package
cd ~/botCode/price-list-telegram-bot-<release version>/target/universal

# enable root mode
su
# after input root password

# extract bot
unzip price-list-telegram-bot.zip -d /opt/
```

### User owner
Right now we will create system user. Home directory for the user will be work directory of the bot.
You need to open terminal and execute this simple commands:
```bash
# enable root mode
su
# after input root password

# add system user
useradd --system \
 --home /opt/price-list-telegram-bot \
 --shell /sbin/nologin \
 telBot

# add execution mode for start script
chmod +x /opt/price-list-telegram-bot/bin/price-list-telegram-bot

# login as user telBot
su telBot

# create temp directory
mkdir /opt/price-list-telegram-bot/tmp
```

### Configuration
It is very important step. Go to directory ```/opt/price-list-telegram-bot/conf```. 
In this directory is two files ```application.conf``` and ```logging.properties```.
Use any text editor for changing configuration in the files.

#### Description of fields application.conf
* ```telegram.bot.user.name``` - name of the bot specified during registration
* ```telegram.bot.token``` - token string received during registration
* ```messages.start.header``` - new user will see this header of message
* ```messages.contacts.header``` - header of contact item
* ```messages.commands.header``` - header of commands item
* ```messages.contacts.list``` - list of contacts
* ```messages.bad.command``` - if bot does not understand command of user then he send the message
* ```messages.price.not.available``` - the bot send the message if he cannot read actual price list
* ```messages.admin.welcome``` - if user will input true admin password then he will see the message
* ```messages.success.status``` - bot send the message if he successfully completed admin command 
* ```commands.list``` - list of commands
* ```price.save.dir``` - here will be saved price lists (there is the directory in work directory of the bot: /opt/price-list-telegram-bot/price)
* ```tmp.dir``` - temp directory (we already created it: /opt/price-list-telegram-bot/tmp )
* ```admin.password``` - password for enable admin mode
* ```admin.auto.disable``` - count of minutes before time out of admin mode (set 5 for example)

#### Description of logging.properties
By default logger write log only to ```System.in```. You can add opportunity of writing log to file.
For this you need to uncomment necessary lines. For more information you can see documentation at slf4j-jdk14 logger

### Install bot as service
If you want use the bot as service then you can use prepared file ```price-list-telegram-bot.service```.
Check path to work directory of the bot at field ```ExecStart``` after ```/usr/bin/bash```.
If the path is true then you need execute this simple command:
```bash
# enable root mode
su
# after input root password

# create link for price-list-telegram-bot.service
ln -s /opt/price-list-telegram-bot/bin/price-list-telegram-bot.service \
 /usr/lib/systemd/system/price-list-telegram-bot.service

# reloading of system daemons
systemctl daemon-reload

# enable service
systemctl enable price-list-telegram-bot

# start daemon
systemctl start price-list-telegram-bot
```
 