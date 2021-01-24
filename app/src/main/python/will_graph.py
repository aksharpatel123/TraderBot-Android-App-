
import pandas as pd
import matplotlib.pyplot as plt


def dict_to_df(dataForAllDays):

    df = pd.DataFrame.transpose(pd.DataFrame.from_dict(dataForAllDays)) # transpose function swaps col and row
    df = df.apply(pd.to_numeric) # cast str to numeric
    df = df.iloc[::-1] # rearrange order of df by dates

    return df


def graph(df, company):

    df['4. close'].plot()
    plt.title(company)
    plt.xlabel('date')
    plt.ylabel('price (USD)')
    plt.xticks(rotation = 70)

    # return plt.show() #display chart in a new window
    return plt.savefig(company+'.png', bbox_inches = 'tight')



import requests
from alpha_vantage.timeseries import TimeSeries
import json


def main():

    # get API key and company
    API_KEY = 'VC3KLRZHTGWTLGW4'
    
    company = 'AAPL' # Google
    

    # get data
    r = requests.get('https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol='+company+'&apikey='+API_KEY)
    if (r.status_code == 200):
        result = r.json()
        dataForAllDays = result['Time Series (Daily)']

    # call graph function to graph
    return graph(dict_to_df(dataForAllDays), company)

    