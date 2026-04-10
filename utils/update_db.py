import os 
from dotenv import load_dotenv


def update_db():
    load_dotenv()

    db_password = os.getenv("DB_PASSWORD")
    db_user = os.getenv("DB_USER")
    db_name = os.getenv("DB_NAME")
    db_host = os.getenv("DB_HOST")
    db_port = os.getenv("DB_PORT")

    command = f'mysql -u {db_user} -p{db_password} -h {db_host} -P {db_port} {db_name} < ./utils/update_db.sql'
    
    os.system(command)