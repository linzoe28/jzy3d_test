num_sub=input('Please enter the number of subspaces: ');
#num_deg=numer of total Incident angles
d=input('Please enter delta of angle:');
data=[];
new=[];
num_deg=((180/d)+1)*((360/d)+1);
E_the_re_all1=0;
E_the_im_all1=0;
E_phi_re_all1=0;
E_phi_im_all1=0;
for p=0:num_deg-1
    data=[];
    new=[];
    a=fix(p/[(180/d)+1]);
    b=mod(p,[(180/d)+1]);
    num_col=b*d*361+a*d+1;
    E_the_re = 0;
    E_the_im = 0;
    E_phi_re = 0;
    E_phi_im = 0;
    E_the_re1 = 0;
    E_the_im1 = 0;
    E_phi_re1 = 0;
    E_phi_im1 = 0;
    E_the_re_all1=0;
    E_the_im_all1=0;
    E_phi_re_all1=0;
    E_phi_im_all1=0;
    RCS_the=0;
    RCS_phi=0;
    RCS_total=0;
    for i=0:num_sub-1
        filename = ['/home/n2f/Desktop/Triangle.n2f/apMulti/ang',num2str(p),'/',num2str(i),'.ap'];
        num = load(filename);
        data = [data;num];
        E_the_re = num(num_col,3);
        E_the_im = num(num_col,4);
        E_phi_re = num(num_col,5);
        E_phi_im = num(num_col,6);
        E_the_re1 = num(num_col,3);
        E_the_im1 = num(num_col,4);
        E_phi_re1 = num(num_col,5);
        E_phi_im1 = num(num_col,6);
        E_the_re_all1=E_the_re1+E_the_re_all1;
        E_the_im_all1=E_the_im1+E_the_im_all1;
        E_phi_re_all1=E_phi_re1+E_phi_re_all1;
        E_phi_im_all1=E_phi_im1+E_phi_im_all1;
        RCS_the=(abs(complex(E_the_re,E_the_im)))^2*(4*pi);
        RCS_phi=(abs(complex(E_phi_re,E_phi_im)))^2*(4*pi);
        RCS_total=RCS_the+RCS_phi;       
        RCS_total=10*log10(RCS_total);%to dBsm
        new=[new;RCS_total];
    end
    Theta=b*d;
    Phi=a*d;
    filename1=[num2str(Theta),' ', num2str(Phi),'.csv'];
    csvwrite(filename1,new);
    RCS_the1=(abs(complex(E_the_re_all1,E_the_im_all1)))^2*(4*pi);
    RCS_phi1=(abs(complex(E_phi_re_all1,E_phi_im_all1)))^2*(4*pi);
    RCS_total_1=RCS_the1+RCS_phi1;
    RCS_total_1=10*log10(RCS_total_1);% to dBsm
    new=[new;RCS_total_1];
    csvwrite(filename1,new);
    
end
fprintf("finish");
