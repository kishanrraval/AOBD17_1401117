I = imread('6.jpg');
I = im2double(rgb2gray(I));

%normalizing image
 I = I - min(I(:));
 I = I/max(I(:));

%adding block noise (CORRUPTING)
p = [0.05 0.1 0.15 0.2 0.25 0.3 0.35];           %percentage corrupted values
for i = 1:length(p)                         %for each value of p
    tic
I_noisy = imnoise(I,'salt & pepper',p(i));          %adding block noise
R1 = em_ppca(I_noisy, 30);                  %recovering with ppca-em
ep(i) = rms(rms(I-R1));                            %calculating error
toc
end
plot(p,ep);                                  %percentage noise vs error
