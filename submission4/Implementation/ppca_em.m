function [W,M,Xn,recoveredData] = ppca_em(t,k)

    itr = 10;
    dataSize = size(t); 
    height = dataSize(1);   %extracting height
    width = dataSize(2);    %extracting width


    sum = zeros(height,1);
    for i=1:height
       for j=1:width
          if ~isnan(t(i,j)) 
             sum(i,1) = sum(i,1) + t(i,j); 
          end
       end
    end
    meanData = sum./width;

    % subtracting mean
    for i=1:height
       for j=1:width
          if ~isnan(t(i,j)) 
             t(i,j) = t(i,j) - meanData(i,1); 
          else
             t(i,j) = 0;
          end
       end
    end

    %initializing data
    W = randn(height,k);
    sigma_square = randn(1,1);

    for i=1:itr
        %initializing values
        Xn = zeros(k,width); 
        Xn_Xn_T = zeros(k,k); 

        invM = inv(W'*W + sigma_square*eye(k,k)); 
        
        for i=1:width
           Xn(:,i) = (invM)*W'*t(:,i); 
            % Expected XnXn'
           Xn_Xn_T = Xn_Xn_T + (sigma_square*(invM) + Xn(:,i)*(Xn(:,i)')); 
        end

        temp = zeros(height,k);
        for i=1:width
            temp = temp + t(:,i)*(Xn(:,i)');
        end

        %new W
        W = (temp)*inv(Xn_Xn_T); 

        tempSum = 0;
        for i=1:width
            temp2 = sigma_square*(invM) + Xn(:,i)*(Xn(:,i)');
            tempSum = tempSum + (norm(t(:,1))^2 - 2*(Xn(:,i)')*(W')*(t(:,i)) + trace(temp2*(W')*W));
        end    
        sigma_square = tempSum/(width*height);
    end
    M = W'*W + sigma_square*eye(k,k);
    Xn = (inv(W'*W + sigma_square*eye(k,k)))*W'*t; 
    
    recoveredData = W * inv(W' * W) * M * Xn;
   % recoveredData = recoveredData + (meanData)*ones(1,size(recoveredData,2));
end