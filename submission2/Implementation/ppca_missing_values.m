I = imread('sample1.jpg');
I = im2double(rgb2gray(I));

%normalizing image
I = I - min(I(:));
I = I/max(I(:));
 
n = size(I);
I_missing = I;                      %initializing
p = [0.05 0.1 0.15 0.2 0.25 0.3 0.35];        %percentage missing values
for k = 1:length(p)                     %for each value of p
    for i = 1:p(k)*(n(1)*n(2))          %generating p*(number of pixels)
         m = randi(n(1)*n(2));          %number of random positions
         I_missing(m) = NaN;            %to put missing values to
    end
    [R] = em_ppca(I_missing, 30);           %recovering by ppca method
    epm(k) = rms(rms(I-R));
end
plot(p,epm);                              %percentage missing values vs error