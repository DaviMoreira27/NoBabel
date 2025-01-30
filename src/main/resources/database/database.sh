#!/bin/bash

# Configurações do Postgres
POSTGRES_USER="pgdefault_user"
POSTGRES_PASSWORD="2706"
POSTGRES_PORT="5431"

# Configurações do Redis
REDIS_PASSWORD="700*//"
REDIS_PORT="6379"

# Iniciando o container do Postgres
echo "Iniciando o container do Postgres..."
podman run --name postgres-container \
  -e POSTGRES_USER=$POSTGRES_USER \
  -e POSTGRES_PASSWORD=$POSTGRES_PASSWORD \
  -e POSTGRES_HOST_AUTH_METHOD=trust \
  -p $POSTGRES_PORT:5432 \
  -d postgres:16

# Verifica se o Postgres foi iniciado com sucesso
if [ $? -eq 0 ]; then
  echo "Postgres iniciado com sucesso na porta $POSTGRES_PORT."
else
  echo "Falha ao iniciar o Postgres."
  exit 1
fi

# Iniciando o container do Redis
echo "Iniciando o container do Redis..."
podman run --name redis-container \
  -p $REDIS_PORT:6379 \
  -d redis:7 redis-server --requirepass $REDIS_PASSWORD

# Verifica se o Redis foi iniciado com sucesso
if [ $? -eq 0 ]; then
  echo "Redis iniciado com sucesso na porta $REDIS_PORT."
else
  echo "Falha ao iniciar o Redis."
  exit 1
fi

echo "Todos os containers foram iniciados com sucesso!"
