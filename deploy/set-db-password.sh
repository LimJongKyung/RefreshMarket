#!/usr/bin/env bash
set -euo pipefail

read -rsp "REFRESHMARKET database password: " db_password
printf '\n'
read -rsp "Confirm database password: " db_password_confirm
printf '\n'

if [[ "$db_password" != "$db_password_confirm" ]]; then
    echo "Passwords do not match." >&2
    exit 1
fi

sudo sed -i '/^DB_PASSWORD=/d' /opt/refreshmarket/refreshmarket.env
printf 'DB_PASSWORD=%s\n' "$db_password" | sudo tee -a /opt/refreshmarket/refreshmarket.env >/dev/null
unset db_password db_password_confirm

sudo chmod 600 /opt/refreshmarket/refreshmarket.env
sudo systemctl enable --now refreshmarket
sudo systemctl status refreshmarket --no-pager
