import imp
import os
import sys

from flask import Flask
from flask_login import LoginManager
from bbs.top_ten import Top_ten

app = Flask(__name__)
app.config['SECRET_KEY'] = os.getenv('SECRET_KEY', 'dev')

# top_ten_maintainer = Top_ten()

from bbs import views, auth