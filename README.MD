## Contact information
- Name: Carlos
- Last name: Laffitte
- Email: cnlaffitte@gmail.com

## Project Name
- Tenpo

## Project description
- This project exposes two apis, the first one ("/v1/tenpo") expects two numbers received by the body, adds them and calls an external service to apply a percentage to it. After this operation, it is saved in a Postgres database.
  The second API ("/v1/tenpo/history") gets all the entries that the database has previously saved. Pages must be sent to obtain the result.

## Prerequisites
- SSH access to a VPS.
- Superuser (root) privileges or a user with sudo privileges.
- This `README.md` is designed for an Ubuntu based VPS. If you are using another distribution, the commands and packages may vary. Be sure to adapt the instructions according to your operating system.

### Update system, update dependencies, add docker repository, key GPG and install docker.
- sudo apt update && sudo apt upgrade -y ()
- sudo apt install apt-transport-https ca-certificates curl software-properties-common
- curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
- sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
- sudo apt update
- sudo apt install docker-ce
- sudo systemctl enable docker
- sudo systemctl start docker

### Install docker compose
- sudo curl -L "https://github.com/docker/compose/releases/download/latest/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
- sudo chmod +x /usr/local/bin/docker-compose

### Install git, clone the repository and build and start services
- sudo apt install git -y
- git clone https://github.com/charlyla/tenpo.git
- cd tenpo
- docker-compose build
- docker-compose up -d

### Validations - Versions and servers up
- docker --version
- docker-compose --version
- docker-compose ps

## Start Docker
### This project contains the docker-compose.yml
- Make sure you have Docker and Docker Compose installed:
- cd /path/to/tenpo/
- docker-compose up
- docker-compose ps (Check the status of services)

