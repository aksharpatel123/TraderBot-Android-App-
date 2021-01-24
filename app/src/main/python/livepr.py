import bs4
import requests
from bs4 import BeautifulSoup

import pandas as pd
from yahoo_fin import stock_info
import io

def parsePrice(stk):
    r = requests.get(f'https://finance.yahoo.com/quote/{stk}?p={stk}')
    soup = BeautifulSoup(r.content,'html.parser')
    price = soup.find('div', class_='My(6px) Pos(r) smartphone_Mt(6px)').find('span').text

    return str(price)



def myfunc(stk):
    ret_str = ""
    #Extracting values from Alphavantage API
    API_KEY = 'VC3KLRZHTGWTLGW4'

    """
    Using AlphaVantage API to get the values of open close and low. Using the close values to find the mean and then store it in a string. 
    Lists are made to store the extracted data 
    """
    response = requests.get(f"https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY_EXTENDED&symbol={stk}&apikey={API_KEY}&datatype=csv&interval=30min&slice=year1month1").content
    responseData = pd.read_csv(io.StringIO(response.decode('utf-8')))

    #storing all the data into a list
    time = list(responseData['time'])
    close = list(responseData['close'])
    openStk = list(responseData['open'])
    low = list(responseData['low'])
    mean = sum(responseData['close']) / len(responseData['close'])

    r = requests.get(f'https://finance.yahoo.com/quote/{stk}?p={stk}')
    soup = BeautifulSoup(r.content,'html.parser')

    price = soup.find('div', class_='My(6px) Pos(r) smartphone_Mt(6px)').find('span').text

    for i in price:
        stats = soup.find('span', class_='Trsdu(0.3s) Fw(500) Pstart(10px) Fz(24px) C($positiveColor)').text

    parsedString = stats.split(' ')
    #print(parsedString[0])

    if '+' in parsedString[0]:
        ret_str += "Sell "
    elif '-' in parsedString[0]:
        ret_str += "Buy "


    ret_str += str(stats)
    return (ret_str)


