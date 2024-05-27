class User:
    def __init__(self, name, id):
        self.id = id
        self.name = name
    def print_info(self):
        print(f"Name: {self.name}, ID: {self.id}")

class Project:
    def __init__(self, id, title, projectOwner):
        if type(projectOwner) != User:
            raise Exception("projectOwner is not a User object")
        self.id = id
        self.title = title
        self.projectOwner = projectOwner
        self.pls = []
        self.dev = []
        self.testers = []
        self.createDate = ""
        self.updateDate = ""
    
    def add_pl(self, pls):
        for pl in pls:
            if type(pl) != User:
                raise Exception("pl is not a User object")
        self.pls = pls # pls is a list of User objects

    def add_dev(self, devs):
        for dev in devs:
            if type(dev) != User:
                raise Exception("dev is not a User object")
        self.dev = devs # devs is a list of User objects

    def add_tester(self, testers):
        for tester in testers:
            if type(tester) != User:
                raise Exception("tester is not a User object")
        self.testers = testers # testers is a list of User objects

    def set_create_date(self, create_date):
        if type(create_date) != User:
            raise Exception("create_date is not a String")
        self.createDate = create_date

    def set_update_date(self, update_date):
        if type(update_date) != User:
            raise Exception("update_date is not a String")
        self.updateDate = update_date

    def print_info(self):
        print(f"ID: {self.id}, Title: {self.title}, Project Owner: {self.projectOwner.name}")
        print("PLs:")
        for pl in self.pls:
            print(f"  {pl.name}")
        print("Developers:")
        for dev in self.dev:
            print(f"  {dev.name}")
        print("Testers:")
        for tester in self.testers:
            print(f"  {tester.name}")
        print(f"Create Date: {self.createDate}, Update Date: {self.updateDate}\n")

class Issue:
    def __init__(self, id, title, createDate, updateDate, dueDate, content, status, priority):
        self.id = id
        self.title = title
        self.createDate = createDate
        self.updateDate = updateDate
        self.dueDate = dueDate
        self.content = content
        self.status = status
        self.priority = priority
        self.reporter = []
        self.assignee = []
        self.fixer = []
    
    def add_reporter(self, reporter):
        if type(reporter) != User:
            raise Exception("reporter is not a User object")
        self.reporter.append(reporter)

    def add_assignee(self, assignee):
        if type(assignee) != User:
            raise Exception("assignee is not a User object")
        self.assignee.append(assignee)
        
    def add_fixer(self, fixer):
        if type(fixer) != User:
            raise Exception("fixer is not a User object")
        self.fixer.append(fixer)

    def print_info(self):
        print(f"ID: {self.id}, Title: {self.title}, Create Date: {self.createDate}, Update Date: {self.updateDate}, Due Date: {self.dueDate}, Content: {self.content}, Status: {self.status}, Priority: {self.priority}")
        print("Reporter:")
        for reporter in self.reporter:
            print(f"  {reporter.name}")
        print("Assignee:")
        for assignee in self.assignee:
            print(f"  {assignee.name}")
        print("Fixer:")
        for fixer in self.fixer:
            print(f"  {fixer.name}")
    
class Comment:
    def __init__(self, issue_id, content):
        self.id = issue_id
        self.content = content

    def print_info(self):
        print(f"Issue ID: {self.id}, Content: {self.content}")