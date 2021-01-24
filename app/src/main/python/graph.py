# First import all libraries
# The script will convert image to byte string and return
# then java code will use this tring to extract image

import numpy as np
import cv2
import matplotlib.pyplot as plt
from PIL import Image
import io
import base64

def main(X,Y):
	fig = plt.figure()

	x = X.split(",")
	y = Y.split(",")

	# suppose we pass stinr 1,2,3 from app edittext...in string
	# format...then it convert it to ['1','2','3']
	# But we need [1,2,3] in int format to plot.

	x_data = []
	y_data = []

	for i in x:
		x_data.append(float(i))
	for i in y:
		y_data.append(float(i))

	ay = fig.add_subplot(1,1,1)
	ay.plot(x_data, y_data)

	fig.canvas.draw() # now we use this canvas to convert data to numpy array...

	img = np.fromstring(fig.canvas.tostring_rgb(),dtype=np.uint8,sep='')
	img = img.reshape(fig.canvas.get_width_height()[::-1]+(3,)) # reshape data
	img = cv2.cvtColor(img,cv2.COLOR_RGB2BGR)

	# Now it is converted to cv2 image...
	# now I will convert this to PIL image and then finally byte string.. and 
	# then we return this byte string to our java code..

	pil_im = Image.fromarray(img)
	buff = io.BytesIO()
	pil_im.save(buff,format="PNG")
	img_str = base64.b64encode(buff.getvalue())

	return ""+str(img_str,'utf-8')


