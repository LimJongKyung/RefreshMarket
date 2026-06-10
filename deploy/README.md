# OCI VM deployment

These commands assume an Oracle Linux VM and the default `opc` user.

## Install the runtime

```bash
sudo dnf install -y java-17-openjdk-devel git nginx
sudo mkdir -p /opt/refreshmarket/app /opt/refreshmarket/wallet
sudo chown -R opc:opc /opt/refreshmarket
```

## Copy the application and Wallet

Clone the repository into `/opt/refreshmarket/app`. Extract the instance Wallet
into `/opt/refreshmarket/wallet`, then restrict its permissions:

```bash
chmod 700 /opt/refreshmarket/wallet
chmod 600 /opt/refreshmarket/wallet/*
```

Create `/opt/refreshmarket/refreshmarket.env` from
`deploy/refreshmarket.env.example` and replace all placeholder values.

## Build and start

```bash
cd /opt/refreshmarket/app
chmod +x mvnw
./mvnw clean package -DskipTests
sudo cp deploy/refreshmarket.service /etc/systemd/system/
sudo systemctl daemon-reload
sudo systemctl enable --now refreshmarket
```

## Publish through Nginx

```bash
sudo cp deploy/nginx-refreshmarket.conf /etc/nginx/conf.d/refreshmarket.conf
sudo setsebool -P httpd_can_network_connect 1
sudo systemctl enable --now nginx
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --reload
```

Also add an OCI ingress rule for TCP port 80 to the VM subnet security list or
network security group. The application should then be available at the VM's
public IP address.

## HTTPS with sslip.io

The included Caddy configuration publishes the application at:

```text
https://refreshmarket.168-107-2-100.sslip.io
```

TCP ports 80 and 443 must be allowed by both the OCI security list and the
Oracle Linux firewall. Caddy obtains and renews the TLS certificate
automatically.

## Logs

```bash
sudo journalctl -u refreshmarket -f
```
