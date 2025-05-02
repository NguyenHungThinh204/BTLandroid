#include <bits/stdc++.h>
using namespace std;
int main(){
	int a[100], n;
	cout<<"N = ";
	cin>>n;
	for(int i=0; i<n; i++){
		cout<<"a["<<i<<"] = ";
		cin>>a[i];
	}
	cout<<"Day vua nhap la: ";
	for(int i=0; i<n; i++){
		cout<<a[i]<<" ";
	}
	for(int i=0; i<n-1; i++)
	{
		for(int j=i+1; j<n; j++){
			if(a[i]>a[j]){
				int doi = a[i];
				a[i] = a[j];
				a[j] = doi;
			}
		}
	}
	cout<<"\nDay sau khi doi: ";
	for(int i=0; i<n; i++)
		cout<<a[i]<<" ";
	int x;
	cout<<"\nGia tri can xoa: ";
	cin>>x;
	int i=0, m=n;
	while(i<m)
		if(a[i]==x){
			for(int j=i; j<m-1; j++)
				a[j]=a[j+1];
			m--;
		}
		else
			i++;
	
	cout<<"\nDay sau khi xoa: ";
	for(int i=0; i<m; i++)
		cout<<a[i]<<" ";
	int k, z;
	cout<<"\nVi tri can them: ";cin>>k;
	cout<<"\nNhap gia tri: ";cin>>z;
	for(int i=n; i>=k; i--){
		a[i]=a[i-1];
	}
	a[k]=z;
	n++;
	cout<<"\nDay sau khi chen: ";
	for(int i=0; i<n; i++)
		cout<<a[i]<<" ";
	return 0;
		
}
