package messurments;

import java.util.*;

public class Inserts {
    private static int log2(int d) {
        return (int) (Math.log(d)/Math.log(2));
    }

    public static void main(String[] args) {
        int num = 5;
        int types = 3;
        long[][] avlTimes = new long[num][types];
        long[][] bstTimes = new long[num][types];
        long[][] avlTimesAVG = new long[num][types];
        long[][] bstTimesAVG = new long[num][types];
        long t0;
        long t1;
        BST[][] bsts = new BST[num][types];
        AVLTree[][] avls = new AVLTree[num][types];
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < types; j++) {
                bsts[i][j] = new BST();
                avls[i][j] = new AVLTree();
            }
        }
        BST bst;
        AVLTree avl;
        int k1;
        int m;
        int s;
        int l;
        int k2;
        int k3;
        int ind;
        Random rand = new Random();
        int n;
        for (int i = 1; i <= num ; i++) {
            n = 1000*i;
            for (int j = 0; j < n; j++) {
                k1 = j+1;
                ind = 0;
                bst = bsts[i-1][ind];
                avl = avls[i-1][ind];
                t0 = System.nanoTime();
                bst.insert(k1,true);
                t1 = System.nanoTime();
                bstTimes[i-1][ind] += t1-t0;
                t0 = System.nanoTime();
                avl.insert(k1,true);
                t1 = System.nanoTime();
                avlTimes[i-1][ind] += t1-t0;

                l = (int) Math.pow(2, log2(j+1));
                s = (int) Math.pow(2, log2(n) + 1);
                m = s/(2*l);
                k2 = m + 2*m*((j+1)%l);
                ind = 1;
                bst = bsts[i-1][ind];
                avl = avls[i-1][ind];
                t0 = System.nanoTime();
                bst.insert(k2,true);
                t1 = System.nanoTime();
                bstTimes[i-1][ind] += t1-t0;
                t0 = System.nanoTime();
                avl.insert(k2,true);
                t1 = System.nanoTime();
                avlTimes[i-1][ind] += t1-t0;

                k3 = rand.nextInt();
                ind = 2;
                bst = bsts[i-1][ind];
                avl = avls[i-1][ind];
                t0 = System.nanoTime();
                bst.insert(k3,true);
                t1 = System.nanoTime();
                bstTimes[i-1][ind] += t1-t0;
                t0 = System.nanoTime();
                avl.insert(k3,true);
                t1 = System.nanoTime();
                avlTimes[i-1][ind] += t1-t0;
            }
            for (int j = 0; j < types; j++) {
                avlTimesAVG[i-1][j] = avlTimes[i-1][j] / n;
                bstTimesAVG[i-1][j] = bstTimes[i-1][j] / n;
            }
        }
        System.out.println(Arrays.deepToString(avlTimesAVG));
        System.out.println(Arrays.deepToString(bstTimesAVG));
    }
}
