import global_variables as gv
import requests


def send_get_request(endpoint):
    obj = None
    try:
        obj = requests.get(gv.URL + endpoint)
        content = None
        try:
            content = obj.json()
        except:
            content = obj.text

        return obj.status_code, content
    except:
        gv.MSG = "Failed to connect to server"
        return 0, []

def send_post_request(endpoint, data):
    obj = None
    try:
        obj = requests.post(gv.URL + endpoint, json=data)
        content = None
        try:
            content = obj.json()
        except:
            content = obj.text
            
        return obj.status_code, content
    except:
        gv.MSG = "Failed to connect to server"
        return 0, []
    

def send_patch_request(endpoint, data):
    obj = None
    try:
        obj = requests.patch(gv.URL + endpoint, json=data)
        content = None
        try:
            content = obj.json()
        except:
            content = obj.text
            
        return obj.status_code, content
    except:
        gv.MSG = "Failed to connect to server"
        return 0, []