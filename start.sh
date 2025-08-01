#!/bin/bash

# Notes App - Start Script
# This script helps you start the Notes App in different modes

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if Java is installed
check_java() {
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed. Please install Java 11 or higher."
        exit 1
    fi
    
    java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
    print_status "Java version: $java_version"
}

# Function to check if Maven is installed
check_maven() {
    if ! command -v mvn &> /dev/null; then
        print_error "Maven is not installed. Please install Maven 3.6 or higher."
        exit 1
    fi
    
    maven_version=$(mvn -version | head -n 1 | awk '{print $3}')
    print_status "Maven version: $maven_version"
}

# Function to start the application in development mode
start_dev() {
    print_status "Starting Notes App in development mode..."
    check_java
    check_maven
    
    print_status "Compiling the application..."
    mvn clean compile
    
    print_status "Starting Spring Boot application..."
    mvn spring-boot:run
}

# Function to start the application in production mode
start_prod() {
    print_status "Starting Notes App in production mode..."
    check_java
    check_maven
    
    print_status "Building the application..."
    mvn clean package -DskipTests
    
    print_status "Starting the application..."
    java -jar target/*.jar --spring.profiles.active=production
}

# Function to start with Docker
start_docker() {
    print_status "Starting Notes App with Docker..."
    
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed. Please install Docker."
        exit 1
    fi
    
    print_status "Building Docker image..."
    docker build -t notes-app .
    
    print_status "Starting Docker container..."
    docker run -d \
        --name notes-app \
        -p 8080:8080 \
        -v notes_data:/app/data \
        notes-app
    
    print_success "Notes App is running at http://localhost:8080"
    print_status "To stop the container: docker stop notes-app"
    print_status "To remove the container: docker rm notes-app"
}

# Function to start with Docker Compose
start_compose() {
    print_status "Starting Notes App with Docker Compose..."
    
    if ! command -v docker-compose &> /dev/null; then
        print_error "Docker Compose is not installed. Please install Docker Compose."
        exit 1
    fi
    
    print_status "Starting services..."
    docker-compose up -d
    
    print_success "Notes App is running at http://localhost:8080"
    print_status "To stop services: docker-compose down"
    print_status "To view logs: docker-compose logs -f"
}

# Function to run tests
run_tests() {
    print_status "Running tests..."
    check_maven
    
    mvn test
    
    print_success "Tests completed!"
}

# Function to clean the project
clean_project() {
    print_status "Cleaning the project..."
    check_maven
    
    mvn clean
    
    # Clean Docker resources if they exist
    if command -v docker &> /dev/null; then
        print_status "Cleaning Docker resources..."
        docker system prune -f
    fi
    
    print_success "Project cleaned!"
}

# Function to show help
show_help() {
    echo "Notes App - Start Script"
    echo ""
    echo "Usage: $0 [OPTION]"
    echo ""
    echo "Options:"
    echo "  dev       Start in development mode (default)"
    echo "  prod      Start in production mode"
    echo "  docker    Start with Docker"
    echo "  compose   Start with Docker Compose"
    echo "  test      Run tests"
    echo "  clean     Clean the project"
    echo "  help      Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 dev      # Start in development mode"
    echo "  $0 docker   # Start with Docker"
    echo "  $0 test     # Run tests"
}

# Main script logic
case "${1:-dev}" in
    "dev")
        start_dev
        ;;
    "prod")
        start_prod
        ;;
    "docker")
        start_docker
        ;;
    "compose")
        start_compose
        ;;
    "test")
        run_tests
        ;;
    "clean")
        clean_project
        ;;
    "help"|"-h"|"--help")
        show_help
        ;;
    *)
        print_error "Unknown option: $1"
        show_help
        exit 1
        ;;
esac
