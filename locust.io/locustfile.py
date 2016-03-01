import time
from locust import HttpLocust, TaskSet, task
from pyquery import PyQuery

login = "admin@mailinator.com"
password = "qwerty"

class WebsiteTasks(TaskSet):

    def index(self, step_name):
        r = self.client.get("/", name=step_name)

    def login(self):
        global login,password
        r = self.client.post("/login", data={
            "email": login,
            "password": password,
        }, name="[LOGIN]")

    def filter_uri(self, params):
        x = dict(year="", month="", user="", status="", kind="")
        x.update(params)
        return"/?year={year}&month={month}&user={user}&status={status}&kind={kind}".format(**x)

    def timeoffs_filter(self, filter_name, **kwargs):
        self.client.get(self.filter_uri(kwargs), name=filter_name)

    @task
    def admin_workflow(self):
        self.index("[INDEX]")
        self.login()
        self.timeoffs_filter("[FILTER_YEAR_2015]", year=2015)
        self.timeoffs_filter("[FILTER_MONTH_JAN]", year=2015, month=1)
        self.timeoffs_filter("[FILTER_MONTH_FEB]", year=2015, month=2)
        self.timeoffs_filter("[FILTER_MONTH_APR]", year=2015, month=4)
        self.timeoffs_filter("[FILTER_USER_JOHNDOE]", year=2015, month=4, user=2)
        self.timeoffs_filter("[FILTER_STATUS_NEW]", year=2015, month=4, user=2, status="new")
        self.timeoffs_filter("[FILTER_KIND_PAID]", year=2015, month=4, user=2, status="new", kind="paid")
        self.index("[RETURN_HOME]")

class WebsiteUser(HttpLocust):
    task_set = WebsiteTasks
    host = "http://labour.local"
    min_wait = 5000
    max_wait = 15000
