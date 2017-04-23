# Implementation of GAN using PPCA-em algorithm
----
>*  gan_ppca_w.py	  --> GAN Model which generates W which are the coefficints of principal components
>*  gan_ppca_MXn.py	  --> GAN Model which generates Xn*M which is used for generating image
>*  FactorAnalysis.py --> Generates the PC and M*Xn from MNIST 
>*  index_batch_random.npy --> Random number array which are used to feed the index to generator
>*  PPCA.py --> em-ppca algorithm, (reference: Tipping, Michael E., and Christopher M. Bishop. "Probabilistic principal component analysis." Journal of the Royal Statistical Society: Series B (Statistical Methodology) 61.3 (1999): 611-622.)
>*  plot_image.py	  --> plotting generated images
>*  ppca_em.m  --> PPCA algorithm in MATLAB
>*  ppca_mnist_sub.m --> Takes a sample image from the mnist dataset and compares it with the generated samples in csv format


**Results After every 50 iterations**
![](https://lh3.googleusercontent.com/-_8fUlbh5TGA/WP0FWufwanI/AAAAAAAAAFg/jckj8cyN1o8I5Kzr-Ud5wWjAe5LgRmwAgCLcB/s0/ppca_gan_sub.PNG "ppca_gan_sub.PNG")

----------------
**Comparing PCs with original PC of 1**
![](https://lh3.googleusercontent.com/-ZZ7JKmGSRII/WP0F2D_gjiI/AAAAAAAAAFs/E07SM7qLnywTNJJqJ_F0gTEkadlfP6G_ACLcB/s0/rms_compare.PNG "rms_compare.PNG")