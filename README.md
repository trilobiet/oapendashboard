# OAPEN IRUS-UK Dashboard

Serves a REST API to harvested data from IRUS-UK, along with a nice website full of grids and graphics.

## How to use
Install as a service (check the port 8080/8081).

## Caching

Cache is written at ${java.io.tmpdir}/ehcache/oapen-dashboard.

## Installing

Run mvn install -Pprod to choose the `production` profile.

Copy `irus-dashboard.service` to `/etc/systemd/system`. 

Finally run `sudo systemctl daemon-reload` to activate the service.


- **Start service:**  
sudo systemctl start irus-dashboard.service
- **Stop service:**  
sudo systemctl stop irus-dashboard.service    
- **Check service status:**  
sudo systemctl status irus-dashboard.service
- **Enable service on reboot:**  
sudo systemctl enable irus-dashboard.service
- **Disable service reboot:**  
sudo systemctl disable irus-dashboard.service


That's all folks.
