package com.gbce.sssm.coreObjectModel.util;



/**
 * The logic/code in this class was copied from the rosettacode.org site
 *            (https://rosettacode.org/wiki/Nth_root#Java)
 */



public class Maths {

    public static double nthRoot(int    n,
                                 double value) {

        return nthroot(n, value, .001);
    }



    public static double nthroot(int    n,
                                 double value,
                                 double precision) {

        double rootValue;

        if (value < 0)
            rootValue = -1;             // we handle only real positive numbers
        else if (value == 0)
            rootValue = 0;
        else {

            double prevRootValue = value;

            rootValue = value / n;  // starting "guessed" value...

            while (Math.abs(rootValue - prevRootValue) > precision) {

                prevRootValue = rootValue;
                rootValue = ((n - 1.0) * rootValue + value / java.lang.Math.pow(rootValue, n - 1.0)) / n;
            }
        }

        return rootValue;
    }
}

