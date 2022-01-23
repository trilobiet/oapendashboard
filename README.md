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
sudo systemctl start your-service.service  
- **Stop service:**  
sudo systemctl start your-service.service    
- **Check service status:**  
sudo systemctl status example.service  
- **Enable service on reboot:**  
sudo systemctl enable example.service  
- **Disable service reboot:**  
sudo systemctl disable example.service  


That's all folks.