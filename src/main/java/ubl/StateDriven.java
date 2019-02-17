package ubl;

public class StateDriven {

    int M=40;
    int param;
    float max;
    float P[][];
    int count[][];
    float mem=0;
    StateDriven(String para)
    {
        P=new float[M][M];
        count= new int [M][M];
        max=100;
        mem=max/M;
    }
    void train(int a[])
/* This function identifies and combutes Probability matrix P which is a transition matrix from one state to another */
    {
        int r=0,c=0,i=0;
        int sum[]=new int[M];
        for(i=1;i<a.length;i++)
        {
            r=(int) (a[i-1]/(mem));
            c=(int) (a[i]/(mem));
            r=r>=40?39:r;
            c=c>=40?39:c;
            count[r][c]++;
            sum[r]++;
        }
        c=(int) (a[a.length-1]/(mem));
        c=c>=40?39:c;
        count[c][c]++;
        sum[c]++;
        for(i=0;i<M;i++)
        {
            if(sum[i]==0)
                for(int j=0;j<M;j++)
                    P[i][j]=0;
            else
            {
                for(int j=0;j<M;j++)
                {
                    P[i][j]=(float)count[i][j]/(float)sum[i];
                }
            }
        }
    }
    
    int predict(int time,int ini)
/*This function predicts the state of system from current to the state after time 'time' using Chapman-Kolgorov formula. */
    {
        ini=(int) (ini/mem);
        double res;
        int max;
        double pre[][]=new double[M][M];
        for(int j=0;j<M;j++)
            {
                for(int k=0;k<M;k++)
                {
                    for(int l=0;l<M;l++)
                    {
                        pre[j][k]+=P[j][l]*P[l][k];
                    }
                }
            }
//for computing time-2 matrix multiplication we call matrixmul which is a special function function just to compute matrix multiplication
        for(int i=0;i<time-2;i++)
        {
            pre=matrixmul(pre);
        }

        max=-1;
        res=pre[ini][0];
        for(int i=1;i<M;i++)
        {
            if(res<pre[ini][i])
            {
                res=pre[ini][i];
                max=i;
            }
        }
        if(max<=0)
            return Math.round(ini*mem);
        else
            return Math.round(max*mem);
    }
    
    double [][] matrixmul(double pre[][])
    {
        double c[][]=new double[M][M];
      for(int j=0;j<M;j++)
            {
                for(int k=0;k<M;k++)
                {
                    for(int l=0;l<M;l++)
                    {
                        c[j][k]+=pre[j][l]*P[l][k];
                    }
                }
            }
        
       return c;
    }
}

