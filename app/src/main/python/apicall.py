import requests
import datetime

def main(stock):
	# Yesterday date
	today = datetime.date.today()
	yest = today - datetime.timedelta(days = 1)
	yesterday = str(yest)

	API_KEY = 'HK307QGUCAWX4H4N'
	r = requests.get('https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=' +stock+ '&apikey=' + API_KEY)

	if (r.status_code == 200):
		result = r.json()

	return (result["Time Series (Daily)"][yesterday]["1. open"] + ',' + result["Time Series (Daily)"][yesterday]["2. high"] + ',' + result["Time Series (Daily)"][yesterday]["3. low"] + ',' + result["Time Series (Daily)"][yesterday]["4. close"] + ',' + result["Time Series (Daily)"][yesterday]["5. volume"])


def getPrice(date, stk):

	API_KEY = 'HK307QGUCAWX4H4N'
	r = requests.get('https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=' + stk + '&interval=60min&apikey=' + API_KEY)
	if (r.status_code == 200):
		result = r.json()

	i = 8
	csv_str = ""
	while i < 16:
		if (i<10):
			hour = str(i)
			dbl = float(result["Time Series (60min)"][date + " 0" +hour+":00:00"]["1. open"])
			open_str = str(dbl)
			csv_str += open_str + ","
		else:
			hour = str(i)
			dbl = float(result["Time Series (60min)"][date + " " +hour+":00:00"]["1. open"])
			open_str = str(dbl)
			csv_str += open_str + ","
		i += 1

	return (csv_str)


def getDaily(year, month, day, sk):
	lastDay = 0
	if (month == 1 or month == 3 or month == 5 or month == 7 or month == 8 or month == 10 or month == 12):
		lastDay = 31
	elif month == 2:
		lastDay = 28
	else:
		lastDay = 30

	print(lastDay)

	priceValue = ""
	prevVal = ""
	cnt = 0
	API_KEY = 'HK307QGUCAWX4H4N'
	r = requests.get('https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=' + sk + '&apikey=' + API_KEY)
	if (r.status_code == 200):
		result = r.json()
	start_date = datetime.date(year, month, 1)
	end_date = datetime.date(year, month, lastDay)
	delta = datetime.timedelta(days=1)

	while start_date <= end_date:
		if result["Time Series (Daily)"].get(str(start_date)):
			priceValue += result["Time Series (Daily)"].get(str(start_date)).get("4. close") + ","
			prevVal = result["Time Series (Daily)"].get(str(start_date)).get("4. close")
			cnt += 1
		else:
			if not (cnt == 0):
				priceValue += prevVal + ","
			else:
				tomorrow = start_date + datetime.timedelta(days=2)
				priceValue += result["Time Series (Daily)"].get(str(tomorrow)).get("4. close") + ","
				prevVal = result["Time Series (Daily)"].get(str(tomorrow)).get("4. close")

		start_date += delta

	return priceValue