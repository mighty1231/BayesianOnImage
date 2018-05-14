from skimage.measure import compare_ssim as ssim
import numpy as np
from scipy import misc

def compute_mse (img1, img2):
  ''' calculate mean squared error of two images. 0.0 means two images are equal.
  '''
  # for each r,g,b
  num_channels = img1.shape[2] # gray scale, rgb or rgba

  total_err = 0.0
  for i in range(num_channels):
    err = np.sum((img1[:,:,i].astype("float") - img2[:,:,i].astype("float"))**2)
    err /= float(img1.shape[0] * img2.shape[1])
    total_err += err

  total_err /= num_channels

  return total_err



def  compute_ssim (img1, img2):
  ''' calculate structrual similarity index of two images. 1.0 means two images are equal.
  '''
  num_channels = img1.shape[2] # gray scale, rgb or rgba  

  total_err = 0.0
  for i in range(num_channels):
    err = ssim(img1[:,:,i], img2[:,:,i])
    total_err += err

  total_err /= num_channels

  return total_err



if __name__  == '__main__':
  img1 = misc.imread('dog.jpg')
  img2 = misc.imread('dog_copy.jpg')
  print (img1.shape, img2.shape)

  m = compute_mse(img1, img2)
  s = compute_ssim(img1, img2)

  print (m)
  print (s)







