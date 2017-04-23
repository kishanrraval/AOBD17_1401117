clear all
load mnist
Ic = images(:,1:4000);                       %column images
L = labels(1:4000);
I_ones = Ic(:,4);
for i = 5:4000
    if L(i) == 1
        I_ones = [I_ones,Ic(:,i)];
    end
end
I = reshape(I_ones(:,2),28,28);
[W,M,Xn,recoveredData] = ppca_em(I,8);
Ig(:,:,1) = csvread('150.csv');
Ig(:,:,2) = csvread('250.csv');
Ig(:,:,3) = csvread('350.csv');
Ig(:,:,4) = csvread('450.csv');
Ig(:,:,5) = csvread('500.csv');   
%%
M_Xn = M*Xn;
x(:,:,1) = csvread('150_M_Xn.csv');
x(:,:,2) = csvread('250_M_Xn.csv');
x(:,:,3) = csvread('350_M_Xn.csv');
x(:,:,4) = csvread('450_M_Xn.csv');
x(:,:,5) = csvread('500_M_Xn.csv');
%%
w(:,:,1) = csvread('150_W.csv');
w(:,:,2) = csvread('250_W.csv');
w(:,:,3) = csvread('350_W.csv');
w(:,:,4) = csvread('450_W.csv');
w(:,:,5) = csvread('500_W.csv');
%%
for i = 1:5
    N(i) = rms(rms(M_Xn - x(:,:,i)));
    Nw(i) = rms(rms(W - w(:,:,i)));
    Nm(i) = rms(rms(recoveredData - Ig(:,:,i)));
end  
%%
n = [150,250,350,450,500];
figure,
plot(n,N),title('Representation of PCs by GAN'),xlabel('Number of iterations'),ylabel('RMS error');