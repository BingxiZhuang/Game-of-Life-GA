package edu.neu.coe.info6205.life.base;
import io.jenetics.*;
import io.jenetics.engine.*;
import io.jenetics.util.Factory;
import io.jenetics.util.IntRange;
import io.jenetics.util.MSeq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class JeneticsTest {

    private static int run;
    private static int eval(int[][] cells) {

        List<Point> points=new ArrayList<>();
       for(int i=0;i<cells.length;i++){
           for(int j=0;j<cells.length;j++){
               if(cells[i][j]==1) points.add(new Point(j,i));
           }
       }


        Game game=new Game();
        final Game.Behavior generations=game.run(0L, points);

        run++;
        return (int)generations.generation;
//        return gt.getChromosome()
//                .as(BitChromosome.class)
//                .bitCount();
    }

    public static void main(String[] args) {
        // 1.) Define the genotype (factory) suitable
        //     for the problem.
//        Factory<Genotype<IntegerGene>> gtf =
//                Genotype.of(IntegerChromosome.of(0,1,100));

        // 3.) Create the execution environment.
        Engine<IntegerGene, Integer> engine = Engine
                .builder(JeneticsTest::eval, Codecs.ofMatrix( IntRange.of(0,1),10,10))
                .populationSize(1000)
                .survivorsSelector(new TournamentSelector<>(10))
                .offspringSelector(new RouletteWheelSelector<>())
                .alterers(new Crossover<IntegerGene, Integer>(0.5D) {
                    @Override
                    protected int crossover(MSeq<IntegerGene> mSeq, MSeq<IntegerGene> mSeq1) {
                        return 0;
                    }
                })
                .alterers(new Mutator<>(0.1))
                .optimize(Optimize.MAXIMUM)
                .build();

        // 4.) Start the execution (evolution) and
        //     collect the result.
        Phenotype<IntegerGene, Integer> result = engine.stream()
                //.limit(Limits.bySteadyFitness(7))
                .limit(100)
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println("RUN:     "+run);

        System.out.println("The best result:\n" + result);
    }
}
