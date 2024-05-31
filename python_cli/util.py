import datetime
import os
from request import *

import global_variables as gv

def clear_all_cache():
    gv.PROJECTS_CACHE = []
    gv.PROJECT_CACHE = None
    gv.ISSUES_CACHE = []
    gv.ISSUE_CACHE = None

def curr_time():
    return datetime.now().strftime("%Y-%m-%dT%H:%M:%S")

def recieve_input():
    user_input = input("INPUT: ")
    if user_input == "*":
        exit()
    int_input = None
    try:
        int_input = int(user_input)
    except:
        return gv.INVALID_INPUT
    return int_input

def print_login_status():
    if gv.TOKEN == None:
        print("You are currently not logged in.")
    else:
        print(f"You are currently logged in as \"{gv.USERNAME}\"")
        print(f"Token: {gv.TOKEN}")

def display_logo():
    print("┌─────────────────────────────┐")
    print("|  Issue Management Software  |")
    print("└─────────────────────────────┘")
    print(f"Sys Msg: << {gv.MSG} >>")
    if gv.MSG != "No Message":
        gv.MSG = "No Message"
    print("------------------------------")

def clear_screen():
    if os.name == "nt":
        os.system('cls')
    else:
        os.system('clear')

def print_array(arr):
    if type(arr) != list:
        print(arr)
        return
    
    if len(arr) == 0:
        print("None")
        return

    for i in range(len(arr)):
        if i != len(arr) - 1:
            print(arr[i], end=", ")
        else:
            print(arr[i])
            
def print_projects_list():
    code, projects = send_get_request("/project")
    if code != 200:
        print("Server unavailable")
        exit()
    gv.PROJECTS_CACHE = projects
    count = 1
    print("Projects List :")
    for project in projects:
        print(f"{count}) {project['title']} (Owner: {project['projectOwner']})")
        count += 1

    print()

def print_issues_list(issues):
    count = 1
    for issue in issues:
        print(f"{count}) ", end=" ")
        count += 1
        print(f"Title: {issue['title']}", end=", ")
        print(f"Priority: {issue['priority']}", end=", ")
        print(f"Status: {issue['status']}")
    print()

def print_comments_list(comments):
    count = 1
    for comment in comments:
        print(f"{count}) ", end=" ")
        count += 1
        print(f"Content: {comment['content']}", end=", ")
        print(f"Owner: {comment['commentOwner']}", end=", ")
        print(f"Created: {comment['createDate']}")
    print()

def print_statistics(stats):
    print("Statistics:")
    print(f"Today's Issues: {stats['todayCount']}")
    print(f"Monthly Issues: {stats['monthlyCount']}")
    print("Status Count:")
    for status, count in stats['statusCount'].items():
        print(f"\t{status}: {count}")
    print("Priority Count:")
    for priority, count in stats['priorityCount'].items():
        print(f"\t{priority}: {count}")
    print()