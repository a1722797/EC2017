package ttp.Optimisation;

import ttp.TTPDynamicItems;
import ttp.TTPInstance;
import ttp.TTPSolution;
import ttp.Utils.DeepCopy;

import java.util.ArrayList;

/**
 * Created by yanshuo on 17/5/6.
 */
public class OptimisationofDynamicItems extends Optimisation {
    private static boolean dynamicMode = true;
    private static int benchmarkIndex = 1;              //choose 1 or 2 for dynamic benchmark for ex2

    public static TTPSolution hillClimber(TTPInstance instance, int[] tour,
                                          int mode,
                                          int durationWithoutImprovement, int maxRuntime) {

        ttp.Utils.Utils.startTiming();

        TTPSolution s = null;
        boolean debugPrint = !true;

        int[] packingPlan = new int[instance.numberOfItems];

        TTPDynamicItems dItems = null;
        int[] dItemsSet = new int[instance.numberOfItems];
        if(dynamicMode){
            dItems = new TTPDynamicItems(instance,benchmarkIndex);
        }

        boolean improvement = true;
        double bestObjective = Double.NEGATIVE_INFINITY;

        long startingTimeForRuntimeLimit = System.currentTimeMillis()-200;

        int i = 0;
        int counter = 0;
        while(counter<durationWithoutImprovement && i < 100000) {

            if (i%10==0 /*do the time check just every 10 iterations, as it is time consuming*/
                    && (System.currentTimeMillis()-startingTimeForRuntimeLimit)>=maxRuntime)
                break;


            //every 50 generations the status of each items changes with the probability 5/(length of item set)
            if (dynamicMode && (i+1) % 50 == 0) {
                //for(int j = 0; j < newPackingPlans.length; j++) {
                dItemsSet = changeItemStatus(dItems.dynamicItemStatus.get(i/50), dItemsSet);
                packingPlan = checkifAvailable(packingPlan,dItemsSet);
                //}
            }
            if (i % 100 == 0) {
            	  //              System.out.println(" i=" + i + "(" + counter + ") bestObjective=" + bestObjective);
            	            	System.out.println(i +" "+ bestObjective);
            }
            if (debugPrint) {
                System.out.println(" i="+i+"("+counter+") bestObjective="+bestObjective);
            }
            int[] newPackingPlan = (int[])DeepCopy.copy(packingPlan);

            boolean flippedToZero = false;

            switch (mode) {
                case 5:
                    // flip one bit
                    int position = (int)(Math.random()*newPackingPlan.length);
//                    newPackingPlan[position] = Math.abs(newPackingPlan[position]-1);
                    if (newPackingPlan[position] == 1) {
                        newPackingPlan[position] = 0;
                        // investigation: was at least one item flipped to zero during an improvement?
//                                flippedToZero = true;
                    } else {
                        newPackingPlan[position] = 1;
                    }
                    break;
                case 6:
                    // flip with probability 1/n
                    for (int j=0; j<packingPlan.length; j++) {
                        if (Math.random()<1d/packingPlan.length)
                            if (newPackingPlan[j] == 1) {
                                newPackingPlan[j] = 0;
                                // investigation: was at least one item flipped to zero during an improvement?
//                                flippedToZero = true;
                            } else {
                                newPackingPlan[j] = 1;
                            }
                    }
                    break;
            }



//            ttp.Utils.Utils.startTiming();
            TTPSolution newSolution = new TTPSolution(tour, newPackingPlan);
            instance.evaluate(newSolution);
//            System.out.println(ttp.Utils.Utils.stopTiming());


            /* replacement condition:
             *   objective value has to be at least as good AND
             *   the knapsack cannot be overloaded
             */
            if (newSolution.ob >= bestObjective && newSolution.wend >=0 ) {

                // for the stopping criterion: check if there was an actual improvement
                if (newSolution.ob > bestObjective && newSolution.wend >=0) {
                    improvement = true;
                    counter = 0;
                }

                packingPlan = newPackingPlan;
                s = newSolution;
                bestObjective = newSolution.ob;

            } else {
                improvement = false;
                counter ++;
            }

            i++;

        }

        long duration = ttp.Utils.Utils.stopTiming();
        s.computationTime = duration;
        return s;
    }





    public static TTPSolution greedyHillClimber(TTPInstance instance, int[] tour,
                                                int durationWithoutImprovement, int maxRuntime) {
        ttp.Utils.Utils.startTiming();

        TTPSolution s = null;
        boolean debugPrint = !true;
        int[] packingPlan = new int[instance.numberOfItems];


        TTPDynamicItems dItems = null;
        int[] dItemsSet = new int[instance.numberOfItems];
        if(dynamicMode){
            dItems = new TTPDynamicItems(instance,benchmarkIndex);
        }

        boolean improvement = true;
        double bestObjective = Double.NEGATIVE_INFINITY;

        long startingTimeForRuntimeLimit = System.currentTimeMillis()-200;

        int i = 0;
        int counter = 0;
        while(counter<durationWithoutImprovement && i <100000) {

            if (i % 10 == 0 /*do the time check just every 10 iterations, as it is time consuming*/
                    && (System.currentTimeMillis() - startingTimeForRuntimeLimit) >= maxRuntime)
                break;

            if (debugPrint) {
                System.out.println(" i=" + i + "(" + counter + ") bestObjective=" + bestObjective);
            }
            if (i % 100 == 0) {
  //              System.out.println(" i=" + i + "(" + counter + ") bestObjective=" + bestObjective);
            	System.out.println(i +" "+ bestObjective);
            }

            //every 50 generations the status of each items changes with the probability 5/(length of item set)
            if (dynamicMode && (i+1) % 50 == 0) {
                //for(int j = 0; j < newPackingPlans.length; j++) {
                dItemsSet = changeItemStatus(dItems.dynamicItemStatus.get(i/50), dItemsSet);
                packingPlan = checkifAvailable(packingPlan,dItemsSet);
                //}
            }


            int[][] newPackingPlans = null;
            int num = instance.numberOfItems / 4000;
            if(num  > 5){
                newPackingPlans = new int[5][instance.numberOfItems];
            }else{
                newPackingPlans = new int[1][instance.numberOfItems];
            }



            for(int j=0; j < newPackingPlans.length; j++){
                newPackingPlans[j] = (int[]) DeepCopy.copy(packingPlan);
                newPackingPlans[j] = flipN(newPackingPlans[j], dItemsSet);  //flip one bit if it is available
            }



            int[] bestPlan = new int[instance.numberOfItems];
            double bValuse = Double.NEGATIVE_INFINITY;
            for(int j = 0; j < newPackingPlans.length; j++){
                TTPSolution newSolution = new TTPSolution(tour, newPackingPlans[j]);
                instance.evaluate(newSolution);
                if(newSolution.ob > bValuse){
                    bValuse = newSolution.ob;
                    bestPlan = (int[]) DeepCopy.copy(newPackingPlans[j]);
                }

            }


            TTPSolution newSolution = new TTPSolution(tour, bestPlan);
            instance.evaluate(newSolution);



            /* replacement condition:
             *   objective value has to be at least as good AND
             *   the knapsack cannot be overloaded
             */

            if (newSolution.ob >= bestObjective && newSolution.wend >=0 ) {

                // for the stopping criterion: check if there was an actual improvement
                if (newSolution.ob > bestObjective && newSolution.wend >=0) {
                    improvement = true;
                    counter = 0;
                }
                else {
                    improvement = false;
                }

                packingPlan = bestPlan;
                s = newSolution;
                bestObjective = newSolution.ob;

            } else {
                improvement = false;
                counter ++;
            }
            i++;
        }
        long duration = ttp.Utils.Utils.stopTiming();
        s.computationTime = duration;

        return s;
    }

    //every 50 generations the status of each items changes with the probability 5/(length of item set)
    private static int[] changeItemStatus(ArrayList list, int[] availableSet){
        for(int i = 0; i < list.size(); i++) {
            if (availableSet[(int) list.get(i)] == 0) {
                availableSet[(int) list.get(i)] = -1;
                //packplan[i] = 0;
            } else {
                availableSet[(int) list.get(i)] = 0;
                //packplan[i] = -1;
            }
        }
        return availableSet;
    }

    private static int[] flipN(int[] packingplan, int[] availableSet){
        int numberofFlip = 1;
        if(packingplan.length > 20000){
            numberofFlip = packingplan.length /20000;
        }
        for(int i = 0; i < numberofFlip; i++){
            boolean signal = true;      //only flip if that bit is aviable
            while(signal) {
                int position = (int) (Math.random() * packingplan.length);
                if(availableSet[position] != -1) {
                    if (packingplan[position] == 1) {
                        packingplan[position] = 0;

                    } else if (packingplan[position] == 0) {
                        packingplan[position] = 1;
                    }
                    signal = false;
                }
            }
        }
        return packingplan;
    }

    private static int[] checkifAvailable(int[] packingplan, int[] availableSet){
        for(int i = 0; i < availableSet.length; i++){
            if(availableSet[i] == -1){
                packingplan[0] = 0;
            }
        }
        return packingplan;
    }
}
