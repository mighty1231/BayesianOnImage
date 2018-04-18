# A toy example based on disaster_model_theano_op.py
# Some modification in order to step forward to our project
#
# Guess a starting number and an ending number of given arithmetic sequence
# Answer : start=10, end=50

import pymc3 as pm
from theano.compile.ops import as_op
import theano.tensor as tt
import numpy as np

objective = np.array([10, 20, 30, 40, 50], dtype=np.int64)

# Types used in Theano.tensor are explained at here
# http://deeplearning.net/software/theano/library/tensor/basic.html
@as_op(itypes=[tt.lscalar, tt.lscalar], otypes=[tt.lvector])
def obj_(start, end):
    out = np.empty(5, dtype=np.int64)
    for i in range(5):
        out[i] = start + ((end-start)*i)//4 # Arithmetic sequence
    return out

with pm.Model() as model:

    # Priors
    startpoint = pm.DiscreteUniform('startpoint', lower=0, upper=1000)
    endpoint = pm.DiscreteUniform('endpoint', lower=0, upper=1000)

    obj = obj_(startpoint, endpoint)

    # Data likelihood
    objective_obs = pm.Poisson('objective_obs', obj, observed=objective)

    step = pm.Metropolis([startpoint, endpoint])

    # Initial values for stochastic nodes
    start = {'startpoint': 200, 'endpoint': 100}

    tr = pm.sample(1000, tune=500, start=start, step=[step], cores=2)
    pm.traceplot(tr)

import matplotlib.pyplot as plt
plt.show()
