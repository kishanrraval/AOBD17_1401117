I = imread('sample1.jpg');
I = im2double(rgb2gray(I));

%normalizing image
 I = I - min(I(:));
 I = I/max(I(:));

%adding block noise (CORRUPTING)
p = [0.05 0.1 0.15 0.2 0.25 0.3 0.35];           %percentage corrupted values
for i = 1:length(p)                         %for each value of p
    tic
I_noisy = imnoise(I,'salt & pepper',p(i));          %adding block noise
[L1, S1] = RobustPCA(I_noisy);                      %recovering with rpca
er(i) = rms(rms(I-L1));                            %calculating error for rpca
toc
end
plot(p,er);                                  %percentage noise vs error
