from typing import List
from selenium import webdriver
import requests
import json
import time
import datetime

driver = webdriver.Chrome(executable_path=r"D:\chromedriver\chromedriver.exe")

def processon():
    url = 'https://www.processon.com/login'
    driver.get(url=url)
    driver.find_element_by_id('login_email').send_keys('bacmive@sohu.com')
    driver.find_element_by_id('login_password').send_keys('tsj19960311')
    driver.find_element_by_id('signin_btn').click()
    cookies_list = driver.get_cookies()
    with open("cookies.json", "w") as fp:
        json.dump(cookies_list, fp)  
    

def appannie():
    login_site = "https://www.appannie.com/account/login"
    driver.get(url=login_site)
    driver.find_element_by_xpath('//*[@id="__next"]/div/div/div[1]/div/div[3]/form/div/div[1]/input').send_keys('huqj@rd.netease.com')
    time.sleep(2)
    driver.find_element_by_xpath('//*[@id="__next"]/div/div/div[1]/div/div[3]/form/div/div[2]/input').send_keys('Y0uda0Ad5~')
    time.sleep(2)
    driver.find_element_by_xpath('//*[@id="__next"]/div/div/div[1]/div/div[3]/form/div/button').click()
    time.sleep(2)
    driver.find_element_by_xpath("//div[@class='Icon__StaticIcon-lcgvp9-0 iFPPRx']")
    cookies_list = driver.get_cookies()
    cookie = json.dumps(cookies_list)
    body = json.dumps({
        "facets": [
            "product_id"
        ],
        "filters": {
            "product_id": {
                "equal": 20600000000888
            }
        },
        "fields": {
            "product_id": {
                "fields": [
                    "publisher_id",
                    "category_id",
                    "package_name"
                ],
                "category_id": {
                    "fields": [
                        "name"
                    ]
                },
                "publisher_id": {
                    "fields": [
                        "website",
                        "company_id"
                    ],
                    "company_id": {
                        "fields": [
                            "country_code",
                            "website"
                        ]
                    }
                }
            }
        }
    })
    headers = {
        'Cookie': cookie,
        'Content-Type': 'application/json' 
    }
    post_site = 'https://www.appannie.com/ajax/v2/query?query_identifier=app_details_about'
    response = requests.post(url=post_site, data=body, headers=headers)
    dict_obj = response.json()
    with open("resp.json", 'w') as rp:
        json.dump(dict_obj, fp=rp, ensure_ascii=False)
    # browser.get(url)


def update_test():
    url = 'https://www.processon.com/login'
    driver.get(url=url)
    driver.find_element_by_id('login_email').send_keys('bacmive@sohu.com')
    driver.find_element_by_id('login_password').send_keys('tsj19960311')
    driver.find_element_by_id('signin_btn').click()
    cookies_list = driver.get_cookies()
    with open("cookies1.json", "w") as fp:
        json.dump(cookies_list, fp)  
    time.sleep(5)
    driver.get(url='https://www.processon.com/login')
    driver.find_element_by_xpath('//input[@name="login_email"]').send_keys('bacmive@sohu.com')
    driver.find_element_by_xpath('//*[@id="login_password"]').send_keys('tsj19960311')
    driver.find_element_by_xpath('//*[@id="signin_btn"]').click()
    cookies_list = driver.get_cookies()
    with open("cookies2.json", "w") as fp:
        json.dump(cookies_list, fp)
    driver.close()

appannie()
driver.close()
# update_test()
# print(datetime.datetime.fromtimestamp( 1639022540 )  )
# print(datetime.datetime.fromtimestamp( 1639022546 )  )