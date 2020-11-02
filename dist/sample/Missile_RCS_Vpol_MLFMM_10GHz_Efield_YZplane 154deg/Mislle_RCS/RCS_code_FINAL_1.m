num_sub=input('Please enter the number of subspaces: ');
num_col=input('Please enter the maximum scattering angle(column): ');
data={};
new={};
for i=0:num_sub-1
filename = ['C:\中科院測試檔案\missile_RCS\E field\Missile_RCS_Vpol_MLFMM_10GHz_Efield_YZplane 154deg_1000\ap\',num2str(i),'.ap'];
num = load(filename);
data = [data;num];
E_the_re = num(num_col,3);
E_the_im = num(num_col,4);
E_phi_re = num(num_col,5);
E_phi_im = num(num_col,6);
RCS_the=(abs(complex(E_the_re,E_the_im)))^2*(4*pi);
RCS_phi=(abs(complex(E_phi_re,E_phi_im)))^2*(4*pi);
RCS_total=RCS_the+RCS_phi;
new=[new;RCS_total];
end
csvwrite('RCS_total.csv',new);
fprintf("finish");