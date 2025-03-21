#Variables
DOCKER_COMPOSE = docker compose

#Colors
GREEN = \033[0;32m
NC = \033[0m

#Commands
run:
	@echo "$(GREEN)Starting the application...$(NC)"
	$(DOCKER_COMPOSE) up -d --build

stop:
	@echo "$(RED)Stopping the application...$(NC)"
	$(DOCKER_COMPOSE) down

setup:
	@if [ ! -f .env ]; then \
		echo "$(GREEN)Creating .env file from .env.example...$(NC)"; \
		cp .env.example .env; \
	else \
		echo "$(GREEN).env file already exists.$(NC)"; \
	fi

help:
	@echo "$(GREEN)Usage: make <command>$(NC)"
	@echo ""
	@echo "Commands:"
	@echo "  run       - Start the application"
	@echo "  stop      - Stop the application"
	@echo "  setup     - Set up the environment (create .env file if missing)"
	@echo "  help      - Display this help message"