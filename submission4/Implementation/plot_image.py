from numpy import *
from numpy.linalg import *
import matplotlib.pyplot as plt

iteration = '250'
W = load('generator_mnist_'+iteration+'_W.npy')
Xn_M = load('generator_mnist_'+iteration+'_Xn_M.npy')
W_ppca = load('ppca_model_W.npy')
Xn_M_ppca = load('ppca_model_Xn_M.npy')

W_ppca = W_ppca[0,:,:]
Xn_M_ppca = Xn_M_ppca[0,:,:]
ppca = W_ppca.dot(inv(W_ppca.T.dot(W_ppca))).dot(Xn_M_ppca)

W = W[0,:,:,0]
Xn_M = Xn_M[0,:,:,0]
rd = W.dot(inv(W.T.dot(W))).dot(Xn_M)
rd = W.dot(inv(W.T.dot(W))).dot(Xn_M)
fig = plt.figure()
plt.imshow(rd, cmap=plt.cm.Greys_r)
plt.show()
fig.savefig(iteration+'.png', dpi=fig.dpi)
print norm(ppca-rd)
