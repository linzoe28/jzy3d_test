num_sub=0
num_col=0
num_theta=0
num_phi=0
#data_dir='/home/n2f/Desktop/Missle.nas/ap/'
data_dir=''
if length(argv()) < 4
	data_dir=input('Please enter the data directory with trailing /: ', "s");
	num_sub=input('Please enter the number of subspaces: ');
	num_theta=input('Please enter the theta value of the maximum scattering angle: ');
	num_phi=input('Please enter the phi value of the maximum scattering angle: ');
else
	data_dir=argv(){1}
	num_sub=str2num(argv(){2});
	num_theta=str2num(argv(){3});
	num_phi=str2num(argv(){4});
endif

status_dir=[]
cmd=["grep -n \"^", num2str(num_theta), " ", num2str(num_phi), "\" ", data_dir, "0.ap | cut -f1 -d:"]
[status, cmdout]=system(cmd);
num_col=str2num(cmdout);
printf("%d", num_col);
printf("data dir=%s, num_sub = %d, num_col= %d, num_theta=%d, num_phi=%d\n", data_dir, num_sub, num_col, num_theta, num_col);

data=[];
new=[];
E_the_re_all1=0;
E_the_im_all1=0;
E_phi_re_all1=0;
E_phi_im_all1=0;
for i=0:num_sub-1
cmd=["echo \"RCS: ", num2str(i), "/", num2str(num_sub-1), "\" > ", data_dir, ".n2f/status"];
system(cmd);
filename = [data_dir,num2str(i),'.ap'];
if !isfile(filename)
	continue;
end
#printf("filename=%s\n", filename);
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
RCS_the=(abs(complex(E_the_re,E_the_im))).^2*(4*pi);
RCS_phi=(abs(complex(E_phi_re,E_phi_im))).^2*(4*pi);
RCS_total=RCS_the+RCS_phi;
new=[new;RCS_total];
end
#csvwrite('RCS_total.csv',new);
RCS_the1=(abs(complex(E_the_re_all1,E_the_im_all1))).^2*(4*pi);
RCS_phi1=(abs(complex(E_phi_re_all1,E_phi_im_all1))).^2*(4*pi);
RCS_total_1=RCS_the1+RCS_phi1;
new=[new;RCS_total_1];
csvwrite([data_dir, 'RCS_total.csv'],new);
fprintf("finish\n");
