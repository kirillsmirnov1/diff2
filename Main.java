import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.linear.GaussianSolver;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.matrix.functor.MatrixFunction;
import org.la4j.operation.MatrixOperation;
import org.la4j.vector.dense.BasicVector;
import org.la4j.vector.functor.VectorFunction;

import java.util.function.BiFunction;
import java.util.function.DoubleFunction;
import java.util.function.IntFunction;

public class Main {
    //example num 15
    private static final double LEFT = 0.0;
//    private final double RIGHT = 0.5;
    private static final double RIGHT = 2.0 * Math.PI;
    private static final int N = 6;
    private static double h;

    public static void main(String[] args) {
        //Main main = new Main();
        h = (RIGHT - LEFT) / N;
        System.out.println(h);
        System.out.println();

        System.out.println("MID RECT");
        Vector rectVec = quadMethod(rectMatrInit);
        System.out.println(rectVec);
        System.out.println();

        System.out.println("TRAPEZIUM");
        Vector trapezVec = quadMethod(trapeziumMatrInit);
        System.out.println(trapezVec);
        System.out.println();

        System.out.println("SIMPSON");
        Vector simpsonVec = quadMethod(simpsonMatrInit);
        System.out.println(simpsonVec);
        System.out.println();

        System.out.println("THREE-EIGHTS");
        Vector threeVec = quadMethod(threeEightsMatrInit);
        System.out.println(threeVec);
        System.out.println();

        //print result vector from answer
        Vector resVec = new BasicVector(N + 1);
        for(int i = 0; i < resVec.length(); i++){
            resVec.set(i, resultFunc.apply(initialX.apply(i)));
        }
        System.out.println("result vector is");
        System.out.println(resVec);

    }

    private static DoubleFunction<Double> resultFunc = (i) ->{
        return Math.cos(2 * i);
    };

    static private DoubleFunction<Double> fX = (x) ->{
//        return 1.0 + (Math.cos(x / 2 - 1.0) * 1.0 / x);
        return Math.cos(2 * x);
    };

    private static IntFunction<Double> initialX = (x) ->
            LEFT + x * h;

    static private BiFunction<Double, Double, Double> ker = (x, t) ->
//            Math.sin(x * t);
//            x * t * t;
        Math.sin(x) * Math.cos(t);

    static private double elemVal(int i, int j, double hMultiplier){
        return  - h * hMultiplier * ker.apply(fX.apply(i), fX.apply(j));
    }

    private double elemRectVal(int i, int j, double hMultiplier){
        //todo
        return  - h * hMultiplier * ker.apply(fX.apply(i), fX.apply(j));
    }

    static private MatrixFunction trapeziumMatrInit = (i, j, d) ->{
        double val = elemVal(i, j, 1.0/2);
        if(j == 0 || j == N)
            val /= 2.0;
        if(i == j) {
            val += 1.0;
        }
        return val;
    };

    static private MatrixFunction simpsonMatrInit = (i, j , d) ->{
        double val = elemVal(i, j, 1.0/3);
        if(j != 0 && j != N){
            if (j % 2 == 0)
                val *= 2;
            else if (j % 2 != 0)
                val *= 4;
        }
        if(i == j)
            val += 1.0;
        return val;
    };

    static private MatrixFunction rectMatrInit = (i, j , d) ->{
        double val = elemVal(i, j, 2d);
        //todo get f(x) from the middle of h
//        val *= 2;
        if(i == j)
            val += 1.0;
        return val;
    };

    //fixme
    static private MatrixFunction threeEightsMatrInit = (i, j , d) ->{
        double val = elemVal(i, j, 3.0/8);
        if(j != 0 && j != N){
            if(j % 3 == 0)
                val *= 2;
            else
                val *= 3;
        }
        return val;
    };

    static private Vector quadMethod(MatrixFunction mf){
        Matrix resMatr = new Basic2DMatrix(N + 1, N + 1);
        resMatr.setAll(0d);
        //free members creation
        resMatr.update(mf);
        Vector freeMembers = createFreeMembersColumn();
        GaussianSolver gaussianSolver = new GaussianSolver(resMatr);
        System.out.println(resMatr);
        try{
            return gaussianSolver.solve(freeMembers);
        }catch (IllegalArgumentException e){
            System.out.println("determinant is " + resMatr.determinant());
            freeMembers.setAll(0d);
            return freeMembers;
        }
    }

    static private Vector createFreeMembersColumn(){
        Vector vec = new BasicVector(N + 1);
        for(int i = 0; i < vec.length(); i++){
            vec.set(i, fX.apply(initialX.apply(i)));
        }
        return vec;
    }

}
