import datetime
import os
import requests
import global_variables as gv

def curr_time():
    return datetime.now().strftime("%Y-%m-%dT%H:%M:%S")

def recieve_input():
    return int(input("INPUT: "))

def print_login_status():
    if gv.TOKEN == "":
        print("You are currently not logged in.")
    else:
        print("You are currently logged in.")

def display_logo():
    print("┌─────────────────────────────┐")
    print("|  Issue Management Software  |")
    print("└─────────────────────────────┘")
    if gv.MSG != "":
        print(f" << {gv.MSG} >>")
        gv.MSG = ""

def clear_screen():
    if os.name == "nt":
        os.system('cls')
    else:
        os.system('clear')

def main_menu():
    clear_screen()
    display_logo()
    
    print_login_status()
    print()

    print("[1] Login / Logout")
    print("[2] Projects")
    print("[3] Issues")
    print("[4] Users")
    print("[5] Comments")

    print("[0] Exit")

    input = recieve_input()

    if input == 1:
        login_menu()
    elif input == 2:
        projects_menu()
    elif input == 3:
        issues_menu()
    elif input == 4:
        users_menu()
    elif input == 5:
        comments_menu()
    elif input == 0:
        exit()
    else:
        print("Invalid input")

def login_menu():
    clear_screen()
    display_logo()
    print("[ Login ]")
    print()

    print("Username: ")
    username = input()
    print("Password: ")
    password = input()

    # Not implemented yet
    code, response = send_post_request("/login", {
        "name": username,
        "password": password}
        )

    # if response code is 401
    if code == 200:
        gv.TOKEN = response["token"]
        gv.MSG = "Login successful"
    elif code == 401:
        gv.MSG = "Invalid username or password"
    else:
        gv.MSG = f"ERROR CODE : {code}\nFailed to login"

    
def projects_menu():
    clear_screen()
    display_logo()
    print("[ Projects ]")
    print()

    print("[1] View Projects")
    print("[2] Create Project")
    print("[3] Edit Project")
    print("[4] Delete Project")

    print("[0] Back")

    input = recieve_input()

    if input == 1: # [1] View Projects
        projects = send_get_request("/project")
        for project in projects:
            project.print_info()

    elif input == 2: # [2] Create Project
        print("Title: ")
        title = input()
        print("PL Names: ")
        pls = input().split(", ")
        print("Dev Names: ")
        devs = input().split(", ")
        print("Tester Names: ")
        testers = input().split(", ")
        code, res = send_post_request("/project", {
            "title": title,
            "pls": pls,
            "devs": devs,
            "testers": testers,
            "token": gv.TOKEN
            })
        if code == 201:
            gv.MSG = "Project created successfully"
        else:
            gv.MSG = f"ERROR CODE : {code}\nFailed to create project"

    elif input == 3: # [3] Edit Project
        print("Project ID: ")
        project_id = input()
        print("Title: ")
        title = input()
        print("PL Names: ")
        pls = input().split(", ")
        print("Dev Names: ")
        devs = input().split(", ")
        print("Tester Names: ")
        testers = input().split(", ")
        code, res = send_patch_request("/project/" + project_id, {
            "title": title,
            "pls": pls,
            "devs": devs,
            "testers": testers,
            "token": gv.TOKEN
            })
        if code == 200:
            gv.MSG = "Project edited successfully"
        elif code == 404:
            gv.MSG = "Project not found"
        else:
            gv.MSG = f"ERROR CODE : {code}\nFailed to edit project"

    elif input == 4: # [4] Delete Project
        # Deletion not implemented yet
        pass

    elif input == 0: # [0] Back
        clear_screen()
        main_menu()

def issues_menu(): # 이것부터 마저 하기
    clear_screen()
    display_logo()
    print("[ Issues ]")
    print()

    print("[1] View Issues")
    print("[2] Create Issue")
    print("[3] Edit Issue")
    print("[4] Delete Issue")

    print("[0] Back")

    input = recieve_input()

    if input == 1: # [1] View Issues
        issues = send_get_request("/issue")
        for issue in issues:
            issue.print_info()

    elif input == 2: # [2] Create Issue
        print("Project ID: ")
        project_id = input()
        print("Title: ")
        title = input()
        print("Content: ")
        content = input()
        print("Priority: ")
        priority = input()
        print("Due Date: ")
        due_date = input()
        code, res = send_post_request("/issue", {
            "project_id": project_id,
            "title": title,
            "content": content,
            "priority": priority,
            "due_date": due_date,
            "token": gv.TOKEN
            })
        if code == 201:
            gv.MSG = "Issue created successfully"
        elif code == 404:
            gv.MSG = "Project not found"
        else:
            gv.MSG = f"ERROR CODE : {code}\nFailed to create issue"

    elif input == 3: # [3] Edit Issue
        print("Issue ID: ")
        issue_id = input()
        print("Title: ")
        title = input()
        print("Content: ")
        content = input()
        print("Priority: ")
        priority = input()
        print("Due Date: ")
        due_date = input()
        code, res = send_patch_request("/issue/" + issue_id, {
            "title": title,
            "content": content,
            "priority": priority,
            "due_date": due_date,
            "token": gv.TOKEN
            })
        if code == 200:
            gv.MSG = "Issue edited successfully"
        elif code == 404:
            gv.MSG = "Issue not found"
        else:
            gv.MSG = f"ERROR CODE : {code}\nFailed to edit issue"

    elif input == 4: # [4] Delete Issue
        # Deletion not implemented yet
        pass

    elif input == 0: # [0] Back
        clear_screen()
        main_menu()

def users_menu():
    clear_screen()
    display_logo()
    print("[ Users ]")
    print()

    print("[1] View Users")
    print("[2] Create User")
    print("[3] Edit User")
    print("[4] Delete User")

    print("[0] Back")

    input = recieve_input()

    if input == 1: # [1] View Users
        code, users = send_get_request("/user")
        if code == 200:
            for user in users:
                user.print_info()
        else:
            gv.MSG = f"ERROR CODE : {code}\nFailed to view users"

    elif input == 2: # [2] Create User
        print("Name: ")
        name = input()
        print("Password: ")
        password = input()
        code, res = send_post_request("/user", {
            "name": name,
            "password": password,
            })
        if code == 201:
            gv.MSG = "User created successfully"
        elif code == 400:
            gv.MSG = "ERROR CODE : 400\nBad Request"
        else:
            gv.MSG = f"ERROR CODE : {code}\nFailed to create user"

    elif input == 3: # [3] Edit User
        userID = input("Enter the ID of the user you want to edit: ")
        print("Name: ")
        name = input()
        print("Password: ")
        password = input()
        code, res = send_patch_request("/user/" + userID, {
            "name": name,
            "password": password,
            })
        if code == 200:
            gv.MSG = "User edited successfully"
        elif code == 404:
            gv.MSG = "User not found"
        else:
            gv.MSG = f"ERROR CODE : {code}\nFailed to edit user"

    elif input == 4: # [4] Delete User
        # Deletion not implemented yet
        pass

    elif input == 0: # [0] Back
        clear_screen()
        main_menu()

def comments_menu():
    clear_screen()
    display_logo()
    print("[ Comments ]")
    print()

    print("[1] View Comments")
    print("[2] Create Comment")
    print("[3] Edit Comment")
    print("[4] Delete Comment")

    print("[0] Back")

    input = recieve_input()

    if input == 1: # [1] View Comments
        comments = send_get_request("/comment")
        for comment in comments:
            comment.print_info()

    elif input == 2: # [2] Create Comment
        print("Issue ID: ")
        issue_id = input()
        print("Content: ")
        content = input()
        code, res = send_post_request("/comment", {
            "issue_id": issue_id,
            "content": content,
            "token": gv.TOKEN
            })
        if code == 201:
            gv.MSG = "Comment created successfully"
        elif code == 404:
            gv.MSG = "Issue not found"
        else:
            gv.MSG = f"ERROR CODE : {code}\nFailed to create comment"

    elif input == 3: # [3] Edit Comment
        print("Comment ID: ")
        comment_id = input()
        print("Content: ")
        content = input()
        code, res = send_patch_request("/comment/" + comment_id, {
            "content": content,
            "token": gv.TOKEN
            })
        if code == 200:
            gv.MSG = "Comment edited successfully"
        elif code == 404:
            gv.MSG = "Comment not found"
        else:
            gv.MSG = f"ERROR CODE : {code}\nFailed to edit comment"

    elif input == 4: # [4] Delete Comment
        # Deletion not implemented yet
        pass

    elif input == 0: # [0] Back
        clear_screen()
        main_menu()

def send_get_request(endpoint):
    obj = None
    try:
        obj = requests.get(gv.URL + endpoint)
        return obj.status_code, obj.json()
    except:
        gv.MSG = "Failed to connect to server"
        main_menu()
        return 0, []

def send_post_request(endpoint, data):
    obj = None
    try:
        obj = requests.post(gv.URL + endpoint, json=data)
        return obj.status_code, obj.json()
    except:
        gv.MSG = "Failed to connect to server"
        main_menu()
        return 0, []

def send_patch_request(endpoint, data):
    obj = None
    try:
        obj = requests.patch(gv.URL + endpoint, json=data)
        return obj.status_code, obj.json()
    except:
        gv.MSG = "Failed to connect to server"
        main_menu()
        return 0, []