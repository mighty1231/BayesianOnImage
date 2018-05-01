import pymc3 as pm
from theano.compile.ops import as_op
import theano.tensor as tt
from numpy import array, empty, concatenate, int64

from PIL import Image, ImageDraw
import random

# Perceptual hash

# example: divide a image into 16(4x4) sectors and calculate average color in each sector.
def hashmethod_example(image):
    assert Image.isImageType(image)
    w, h = image.width, image.height

    ret = array([], dtype=int64)
    for j in range(4):
        for i in range(4):
            rgb = array([0, 0, 0])

            for y in range((h//4)*j,(h//4)*(j+1)):
                for x in range((w//4)*i,(w//4)*(i+1)):
                    rgb += array(image.getpixel((x, y)))
            
            ret = concatenate((ret, rgb))
    return ret

def main(imagePath, hashmethod = hashmethod_example, num_rect = 1):
    image = Image.open(imagePath)
    hashobj = hashmethod(image)

    @as_op(itypes=[tt.lvector, tt.lvector, tt.lvector], otypes=[tt.lvector])
    def evaluate(xpositions, ypositions, colors):
        im = Image.new('RGB', (image.width, image.height))

        draw = ImageDraw.Draw(im)
        for i in range(num_rect):
            x1, x2 = xpositions[i*2:i*2+2]
            y1, y2 = ypositions[i*2:i*2+2]
            r, g, b = colors[i*3:i*3+3]
            draw.rectangle([(x1, y1), (x2, y2)], fill=(r, g, b))
        del draw

        return hashmethod(im)

    with pm.Model() as model:

        # Priors
        xpositions = pm.DiscreteUniform('xpositions', lower=0, upper=image.width-1, shape=2*num_rect)
        ypositions = pm.DiscreteUniform('ypositions', lower=0, upper=image.height-1, shape=2*num_rect)
        colors = pm.DiscreteUniform('colors', lower=0, upper=255, shape=3*num_rect)

        hashval = evaluate(xpositions, ypositions, colors)

        # Data likelihood
        hashobj_obs = pm.Poisson('objective', hashval, observed=hashobj)

        step = pm.Metropolis([xpositions, ypositions])
        step2 = pm.Metropolis([colors])

        # # Initial values for stochastic nodes
        start = {
            'xpositions': [random.randrange(image.width) for _ in range(2*num_rect)],
            'ypositions': [random.randrange(image.height) for _ in range(2*num_rect)],
            'colors': [random.randrange(256) for _ in range(3*num_rect)]
        }

        tr = pm.sample(200, tune=100, start=start, step=[step, step2], cores=2)
        pm.traceplot(tr)

    import matplotlib.pyplot as plt
    plt.show()

main("sample1.png")
