#
# http://docs.pymc.io/notebooks/getting_started
# A Motivating Example: Linear Regression
#   
# y ~ normal(mu, sigma*sigma)
# mu ~ a + b1 * x1 + b2 * x2
#
# a ~ normal(0, 100)
# bi ~ normal(0, 100)
# sigma ~ abs(normal(0, 1))
#

import numpy as np

def GenerateSamples(size = 10):
	# Following X1, X2, Y is generated randomly
	#  X1 ~ normal(0, 1)
	#  X2 ~ normal(0, 0.04)
	#  Y ~ normal(1 + 1 * X1 +  2.5 * X2, 1)

	alpha, sigma = 1, 1
	beta = [1, 2.5]

	X1 = np.random.randn(size)
	X2 = np.random.randn(size) * 0.2

	Y = alpha + beta[0]*X1 + beta[1]*X2 + np.random.randn(size)*sigma

	return X1, X2, Y

X1, X2, Y = GenerateSamples(100)

# 
# Now, start to infer a, b1, b2 given X1, X2, Y
#

import pymc3 as pm

print('Running on PYMC3 v{}'.format(pm.__version__))

basic_model = pm.Model()

import math

with basic_model:

	# Priors for unknown model parameters
	alpha = pm.Normal('alpha', mu=0, sd=10)
	beta = pm.Normal('beta', mu=0, sd=10, shape=2)
	sigma = pm.HalfNormal('sigma', sd=1)

	# Expected value of outcome
	mu = alpha + beta[0]*X1 + beta[1]*X2

	# Likelihood (sampling distribution) of observations
	Y_obs = pm.Normal('Y_obs', mu=mu, sd=sigma, observed=Y)

	from scipy import optimize

	# obtain starting values via MAP
	start = pm.find_MAP(fmin=optimize.fmin_powell)

	# instantiate sampler
	step = pm.Slice()

	# draw 5000 posterior samples
	trace = pm.sample(5000, step=step, start=start)

	pm.traceplot(trace)

print(pm.summary(trace))

import matplotlib.pyplot as plt
plt.show()
