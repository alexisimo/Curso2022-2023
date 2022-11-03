import os

HELIO_SERVER = os.environ.get("HELIO_SERVER", "http://localhost:9000/sparql")
APP_HOST = os.environ.get("APP_HOST", "127.0.0.1")
APP_PORT = os.environ.get("APP_IP", "8080")