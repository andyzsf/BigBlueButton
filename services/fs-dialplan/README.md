fs-dialplan
=========

Proxies FS XML_CURL requests to BBB Dialplan

INSTALLATION
============

## Install Node (0.10.26)

```
1. sudo apt-get install build-essential
2. wget http://nodejs.org/dist/v0.10.26/node-v0.10.26.tar.gz
3. tar zxvf node-v0.10.26.tar.gz
4. cd node-v0.10.26
5. ./configure
6. make
7. sudo make install
```

## Install CoffeeScript (1.7.1)

```
sudo npm install -g coffee-script
```

## Install fs-dialplan

```
1. Create /usr/local/bigbluebutton dir
2. Copy bigbluebutton/services/fs-dialplan
3. cd /usr/local/bigbluebutton/fs-dialplan
4. npm install
6. Create /var/log/bigbluebutton/fs-dialplan.log file
```

STARTING
========

```
1. Install pm2 (https://github.com/Unitech/pm2) 
   sudo npm install pm2 -g
2. pm2 start index.coffee --name fs-dialplan

2. Create a startup script (NOTE: You might need to reboot. see: https://github.com/Unitech/pm2#startup-script)
    sudo pm2 startup ubuntu
```

## Managing

```
# List the processes
pm2 list

# Stop the process
pm2 stop fs-dialplan

# Restart
pm2 restart fs-dialplan

# Remove the process managed by pm2
pm2 delete fs-dialplan

```


## Monitoring

```
Calling http://<ip>:3004/
```


When it returns the ff, the service is UP.

```
<?xml version="1.0" encoding="utf-8" standalone="no"?>  
<document type="freeswitch/xml">
  <section name="result" description="Reject Call">
    <result status="Invalid XML CURL URL"/>
  </section>
</document>
```

