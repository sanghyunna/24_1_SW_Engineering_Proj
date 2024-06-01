from request import *
from util import *

import global_variables as gv


def projects_menu():
    clear_screen() # 임시 해제
    display_logo()
    
    print_login_status()
    print()
    
    print_projects_list()

    print("[1] Select Project")
    print("[2] Create Project")
    print("[0] Login / Logout / Register")
    print("[*] Exit")

    user_input = recieve_input()

    if user_input == "*": # Placeholder, "*" is exited in the recieve_input function
        exit()

    elif user_input == 0:
        login_menu()

    elif user_input == 1: # Select Project
        print("Select Project: ")
        project_index = recieve_input()
        if project_index < 1 or project_index > len(gv.PROJECTS_CACHE):
            gv.MSG = "Invalid project index"
            return
        gv.PROJECT_CACHE = gv.PROJECTS_CACHE[project_index - 1]
        project = gv.PROJECT_CACHE
        gv.MSG = f"Selected Project: {project['title']}"
        project_menu()

    elif user_input == 2: # Create Project
        print("Title: ")
        title = input()
        print("PL Names(split by commas): ")
        pl = input().split(", ")
        print("Dev Names(split by commas): ")
        dev = input().split(", ")
        print("Tester Names(split by commas): ")
        tester = input().split(", ")
        req = {
            "title": title,
            "token": gv.TOKEN
        }
        if len(pl) > 0:
            req["PLNameArray"] = pl
        if len(dev) > 0:
            req["DevNameArray"] = dev
        if len(tester) > 0:
            req["TesterNameArray"] = tester

        code, res = send_post_request("/project", req)

        if code == 201:
            gv.MSG = "Project created successfully"
            gv.PROJECTS_CACHE.append(res)
        elif code == 400:
            gv.MSG = "ERROR CODE : 400 Bad Request"
        elif code == 401:
            gv.MSG = "ERROR CODE : 401 Unauthorized"
        else:
            gv.MSG = f"ERROR CODE : {code} Failed to create project"
    
    else:
        gv.MSG = "Invalid input"
        projects_menu()

def project_menu():
    clear_screen()
    display_logo()
    print("[ Project ]")
    print(f"Selected Project: {gv.PROJECT_CACHE['title']}")
    print()


    print("[1] View Project Details")
    print("[2] Edit Project")
    print("[3] Manage Issues of the Project")
    print("[4] View Issue Statistics of this Project")

    print("[0] Back")
    print("[*] Exit")

    user_input = recieve_input()

    if user_input == 1: # [1] View Project Details
        code, project = send_get_request("/project/" + str(gv.PROJECT_CACHE["id"]))
        if code == 404:
            gv.MSG = "ERROR CODE : 404 Not Found"
            return
        gv.PROJECT_CACHE = project

        print_project(project)

        print()
        input("Press Enter to continue...")

        project_menu()

    elif user_input == 2: # [2] Edit Project
        project_id = gv.PROJECT_CACHE["id"]
        print("Title: ")
        title = input()
        print("PL Names: ")
        pl = input().split(", ")
        print("Dev Names: ")
        dev = input().split(", ")
        print("Tester Names: ")
        tester = input().split(", ")
        req = {
            "title": title,
            "token": gv.TOKEN
            }
        
        if len(pl) > 0:
            req["PLNameArray"] = pl
        if len(dev) > 0:
            req["DevNameArray"] = dev
        if len(tester) > 0:
            req["TesterNameArray"] = tester
        
        code, res = send_patch_request(f"/project/{str(project_id)}", req)
        if code == 200:
            gv.MSG = "Project edited successfully"
            gv.PROJECT_CACHE = res
            gv.PROJECTS_CACHE = []
        elif code == 401:
            gv.MSG = "ERROR CODE : 401 Unauthorized"
        elif code == 404:
            gv.MSG = "ERROR CODE : 404 Not Found"
        else:
            gv.MSG = f"ERROR CODE : {code} Failed to edit project"

    elif user_input == 3: # [3] Manage Issues of the Project
        issues_menu()

    elif user_input == 4: # [4] View Issue Statistics of this Project
        # /project/{projectId}/issue/stats
        project_id = gv.PROJECT_CACHE["id"]
        code, stats = send_get_request(f"/project/{project_id}/issue/stats")
        if code == 200:
            print_statistics(stats)
        elif code == 404:
            gv.MSG = "ERROR CODE : 404 Not Found"
        else:
            gv.MSG = f"ERROR CODE : {code} Failed to view statistics"

        input("Press Enter to continue...")

    elif user_input == 0: # [0] Back
        clear_screen()

def login_menu():
    clear_screen()
    display_logo()
    print("[ Login ]")
    print()

    print("[1] Login / Logout")
    print("[2] Register")
    print("[3] Edit User")
    user_input = recieve_input()
    print()

    if user_input == 2: # [2] Register
        register_menu()
        return
    
    if user_input == 3: # [3] Edit User
        if gv.TOKEN == None:
            gv.MSG = "You must be logged in to edit user"
            login_menu()
            return
        print("Name: ")
        name = input()
        print("Password: ")
        password = input()
        code, res = send_patch_request("/user", {
            "name": name,
            "password": password,
            "token": gv.TOKEN
            })
        if code == 200:
            gv.MSG = "User edited successfully, Logged out"
            gv.TOKEN = None
            gv.USERNAME = ""            

        elif code == 404:
            gv.MSG = "ERROR CODE : 404 Not Found"
        else:
            gv.MSG = f"ERROR CODE : {code} Failed to edit user"
        return
    

    if user_input != 1:
        gv.MSG = "Invalid input"
        login_menu()
        return

    if gv.TOKEN != None:
        gv.TOKEN = None
        gv.USERNAME = ""
        gv.MSG = "Logged out successfully"
        return

    print("Username: ")
    username = input()
    print("Password: ")
    password = input()

    code, response = send_post_request("/login", {
        "name": username,
        "password": password}
        )
    if code == 200:
        gv.TOKEN = response["token"]
        gv.USERNAME = response["name"]
        gv.MSG = "Login successful"
    elif code == 401:
        gv.MSG = "Invalid username or password"
    else:
        gv.MSG = f"ERROR CODE : {code} Failed to login"

def issues_menu():
    clear_screen()
    display_logo()
    print("[ Issues ]")
    print()

    project_id = gv.PROJECT_CACHE["id"]
    code, issues = send_get_request(f"/project/{project_id}/issue")
    if code == 200:
        gv.ISSUES_CACHE = issues
        print_issues_list(issues)
    elif code == 404:
        gv.MSG = "ERROR CODE : 404 Not Found"
        return
    else:
        gv.MSG = f"ERROR CODE : {code} Failed to view issues"
        return

    print("[1] Select Issue")
    print("[2] Create Issue")
    print("[3] Search for Issue")

    print("[0] Back")
    print("[*] Exit")

    user_input = recieve_input()

    if user_input == 0: # [0] Back
        clear_screen()
        projects_menu()

    elif user_input == 1: # [1] Select Issue
        print("Select Issue: ")
        issue_index = recieve_input()
        if issue_index < 1 or issue_index > len(gv.ISSUES_CACHE):
            gv.MSG = "Invalid issue index"
            issues_menu()
        gv.ISSUE_CACHE = gv.ISSUES_CACHE[issue_index - 1]
        issue = gv.ISSUE_CACHE
        gv.MSG = f"Selected Issue: {issue['title']}"
        issue_menu()

    elif user_input == 2: # [2] Create Issue
        print("Title: ")
        title = input()
        print("Due Date: ")
        dueDate = input()
        print("Content: ")
        content = input()
        print("assignees(split by commas): ")
        assigneeNameArray = input().split(", ")
        print("priority: ")
        priority = input()
        if priority not in ["", "BLOCKER", "CRITICAL", "MAJOR", "MINOR", "TRIVIAL"]:
            gv.MSG = "Invalid priority"
            return

        req = {
            "title": title,
            "dueDate": dueDate,
            "content": content,
            "token": gv.TOKEN,
        }
        if len(assigneeNameArray) > 0:
            req["assigneeNameArray"] = assigneeNameArray
        if priority != "":
            req["priority"] = priority

        code, res = send_post_request(f"/project/{project_id}/issue", req)
        if code == 201:
            gv.MSG = "Issue created successfully"
            gv.ISSUES_CACHE = []
        elif code == 400:
            gv.MSG = "ERROR CODE : 400 Bad Request"
        elif code == 401:
            gv.MSG = "ERROR CODE : 401 Unauthorized"
        else:
            gv.MSG = f"ERROR CODE : {code} Failed to create issue"

        issues_menu()

    elif user_input == 3: # [3] Search for Issue
        search_menu()
    
    elif user_input == 0: # [0] Back
        clear_screen()
        project_menu()

    elif user_input == "*": # Placeholder, "*" is exited in the recieve_input function
        exit()

    else:
        gv.MSG = "Invalid input"
        issues_menu()
        
def issue_menu():
    clear_screen()
    display_logo()
    print("[ Issue ]")
    print()

    issue_id = gv.ISSUE_CACHE["id"]
    code, issue = send_get_request(f"/issue/{issue_id}")
    if code == 404:
        gv.MSG = "ERROR CODE : 404 Not Found"
        return
    gv.ISSUE_CACHE = issue

    project_id = str(issue["projectId"])
    code, project = send_get_request(f"/project/{project_id}")
    if code == 404:
        gv.MSG = "ERROR CODE : 404 Not Found"
        return
    gv.PROJECT_CACHE = project

    print_issue(issue)
    print()

    print("[1] Edit Issue")
    print("[2] Update Issue Status")
    print("[3] Manage Comments of the Issue")
    print("[4] Recommend Assignee")

    print("[0] Back")
    print("[*] Exit")

    user_input = recieve_input()

    if user_input == 0: # [0] Back
        clear_screen()  
        issues_menu()

    elif user_input == 1: # [1] Edit Issue
        issue_id = gv.ISSUE_CACHE["id"]
        print("Title: ")
        title = input()
        print("Due Date: ")
        due_date = input()
        print("Content: ")
        content = input()
        print("Assignee(split by commas): ")
        assignee = input().split(", ")
        print("Priority: ")
        priority = input()
        if priority not in ["", "BLOCKER", "CRITICAL", "MAJOR", "MINOR", "TRIVIAL"]:
            gv.MSG = "Invalid priority"
            return
        code, res = send_patch_request(f"/issue/{issue_id}", {
            "title": title,
            "dueDate": due_date,
            "content": content,
            "assigneeNameArray": assignee,
            "priority": priority,
            "token": gv.TOKEN
            })
        if code == 200:
            gv.MSG = "Issue edited successfully"
            gv.ISSUE_CACHE = res
        elif code == 404:
            gv.MSG = "ERROR CODE : 404 Not Found"
        else:
            gv.MSG = f"ERROR CODE : {code} Failed to edit issue"
        
        issues_menu()

    elif user_input == 2: # [2] Update Issue Status
        issue_id = gv.ISSUE_CACHE["id"]
        print("Status: ")
        status = input()
        if status not in ["NEW", "ASSIGNED", "FIXED", "CLOSED", "REOPENED"]:
            gv.MSG = "Invalid status"
            return
        code, res = send_patch_request(f"/issue/{issue_id}/status", {
            "statusName": status,
            "token": gv.TOKEN
            })
        if code == 200:
            gv.MSG = "Issue status updated successfully"
            print(res)
            gv.ISSUE_CACHE = res
        elif code == 401:
            gv.MSG = "ERROR CODE : 401 Unauthorized"
        elif code == 404:
            gv.MSG = "ERROR CODE : 404 Not Found"
        else:
            gv.MSG = f"ERROR CODE : {code} Failed to update issue status"

        issue_menu()

    elif user_input == 3: # [3] Manage Comments of the Issue
        comments_menu()

    elif user_input == 4: # [4] Recommend Assignee
        issue_id = gv.ISSUE_CACHE["id"]
        code, res = send_get_request(f"/issue/{issue_id}/recommend")
        if code == 200:
            print("Recommended Assignee: ")
            if res == [] or res == None or res == "":
                print(" < No assignee recommended > ")
            else:
                print_array(res)
        elif code == 404:
            gv.MSG = "ERROR CODE : 404 Not Found"
        else:
            gv.MSG = f"ERROR CODE : {code} Failed to recommend assignee"

        input("Press Enter to continue...")

        issue_menu()

    elif user_input == "*": # Placeholder, "*" is exited in the recieve_input function
        exit()

    else:
        gv.MSG = "Invalid input"
        issue_menu()

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
    print("[*] Exit")

    user_input = recieve_input()

    if user_input == 1: # [1] View Users
        code, users = send_get_request("/user")
        if code == 200:
            for user in users:
                user.print_info()
        else:
            gv.MSG = f"ERROR CODE : {code} Failed to view users"

    elif user_input == 2: # [2] Create User
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
            gv.MSG = "ERROR CODE : 400 Bad Request"
        else:
            gv.MSG = f"ERROR CODE : {code} Failed to create user"

    elif user_input == 3: # [3] Edit User
        userID = input("Enter the ID of the user you want to edit: ")
        print("Name: ")
        name = input()
        print("Password: ")
        password = input()
        code, res = send_patch_request(f"/user/{userID}", {
            "name": name,
            "password": password,
            })
        if code == 200:
            gv.MSG = "User edited successfully"
        elif code == 404:
            gv.MSG = "ERROR CODE : 404 Not Found"
        else:
            gv.MSG = f"ERROR CODE : {code} Failed to edit user"

    elif user_input == 4: # [4] Delete User
        # Deletion not implemented yet
        pass

    elif user_input == 0: # [0] Back
        clear_screen()
        projects_menu()

def comments_menu():
    clear_screen()
    display_logo()
    print("[ Comments ]")
    print()

    # /issue/{issueId}/comment
    issue_id = gv.ISSUE_CACHE["id"]
    code, comments = send_get_request(f"/issue/{issue_id}/comment")
    if code == 200:
        print_comments_list(comments)
    elif code == 404:
        gv.MSG = "ERROR CODE : 404 Not Found"
        return
    else:
        gv.MSG = f"ERROR CODE : {code} Failed to view comments"
        return

    print("[1] Create Comment")
    print("[2] Edit Comment")

    print("[0] Back")
    print("[*] Exit")

    user_input = recieve_input()

    if user_input == 1: # Create Comment
        print("Content: ")
        content = input()
        code, res = send_post_request(f"/issue/{issue_id}/comment", {
            "content": content,
            "token": gv.TOKEN
            })
        if code == 201:
            gv.MSG = "Comment created successfully"
        elif code == 400:
            gv.MSG = "ERROR CODE : 400 Bad Request"
        elif code == 401:
            gv.MSG = "ERROR CODE : 401 Unauthorized"
        else:
            gv.MSG = f"ERROR CODE : {code} Failed to create comment"

        comments_menu()

    elif user_input == 2: # Edit Comment
        print("Select Comment: ")
        comment_index = recieve_input()
        if comment_index < 1 or comment_index > len(comments):
            gv.MSG = "Invalid comment index"
            comments_menu()
        comment_id = comments[comment_index - 1]["id"]
        print("Content: ")
        content = input()
        code, res = send_patch_request(f"/comment/{comment_id}", {
            "content": content,
            "token": gv.TOKEN
            })
        if code == 200:
            gv.MSG = "Comment edited successfully"
        elif code == 401:
            gv.MSG = "ERROR CODE : 401 Unauthorized"
        elif code == 404:
            gv.MSG = "ERROR CODE : 404 Not Found"
        else:
            gv.MSG = f"ERROR CODE : {code} Failed to edit comment"

        comments_menu()


    elif user_input == 0: # [0] Back
        clear_screen()

    elif user_input == "*": # Placeholder, "*" is exited in the recieve_input function
        exit()
    
    else:
        gv.MSG = "Invalid input"
        comments_menu()

def search_menu():
    clear_screen()
    display_logo()
    print("[ Search ]")
    print()

    print("Search keyword: ")
    keyword = input()
    project_id = gv.PROJECT_CACHE["id"]
    code, issues = send_get_request(f"/project/{project_id}/issue/search?keyword={keyword}")

    if code == 200:
        gv.ISSUES_CACHE = issues
        gv.MSG = f"Search results for \'{keyword}\'"
        if issues == [] or issues == None or issues == "":
            print("\n < No results found > \n")
        else:
            print_issues_list(issues)
    elif code == 404:
        gv.MSG = "ERROR CODE : 404 Not Found"
    else:
        gv.MSG = f"ERROR CODE : {code} Failed to search issues"

    print("[1] Select Issue")
    print("[0] Back")
    print("[*] Exit")

    user_input = recieve_input()

    if user_input == 0: # [0] Back
        clear_screen()
        issues_menu()

    elif user_input == 1: # [1] Select Issue
        print("Select Issue: ")
        issue_index = recieve_input()
        if issue_index < 1 or issue_index > len(gv.ISSUES_CACHE):
            gv.MSG = "Invalid issue index"
            search_menu()
        print(f"issue_index: {issue_index}")
        print(gv.ISSUES_CACHE)
        gv.ISSUE_CACHE = gv.ISSUES_CACHE[issue_index - 1]
        issue = gv.ISSUE_CACHE
        gv.MSG = f"Selected Issue: {issue['title']}"
        issue_menu()

    elif user_input == "*": # Placeholder, "*" is exited in the recieve_input function
        exit()
    
    else:
        gv.MSG = "Invalid input"
        search_menu()

def register_menu():
    clear_screen()
    display_logo()
    print("[ Register ]")
    print()

    print("Name: ")
    name = input()
    print("Password: ")
    password = input()

    code, response = send_post_request("/user", {
        "name": name,
        "password": password}
        )
    if code == 201:
        gv.MSG = "Register successful"
    elif code == 401:
        gv.MSG = "ERROR CODE : 401 Unauthorized"
    else:
        gv.MSG = f"ERROR CODE : {code} Failed to register"