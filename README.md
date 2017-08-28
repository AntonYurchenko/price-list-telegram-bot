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
mkdir ~/botCod/

# go to work directory
cd ~/botCod/

# download code
wget https://github.com/AntonYurchenko/price-list-telegram-bot/archive/<release version>.tar.gz
```

### Compiling
When you downloaded package with last version of the code you need extract it.
I will show example for linux Fedora 26 Server.
```bash
# go to work directory
cd ~/botCod/

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