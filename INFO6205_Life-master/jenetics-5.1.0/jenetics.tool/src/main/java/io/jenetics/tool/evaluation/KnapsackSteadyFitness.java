/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */
package io.jenetics.tool.evaluation;

import static java.lang.Math.log10;
import static java.lang.Math.max;
import static java.lang.Math.pow;
import static io.jenetics.tool.evaluation.engines.KNAPSACK;

import java.util.function.Supplier;
import java.util.stream.IntStream;

import io.jenetics.BitGene;
import io.jenetics.engine.Limits;
import io.jenetics.tool.trial.Params;
import io.jenetics.tool.trial.TrialMeter;
import io.jenetics.util.ISeq;

import io.jenetics.xml.stream.Reader;
import io.jenetics.xml.stream.Writer;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 4.0
 * @since 3.4
 */
public class KnapsackSteadyFitness {

	private static final double GEN_BASE = pow(10, log10(100)/20.0);
	private static final Params<Integer> PARAMS = Params.of(
		"Generations",
		IntStream.rangeClosed(1, 50)
			.map(i -> max((int)pow(GEN_BASE, i), i))
			.boxed()
			.collect(ISeq.toISeq())
	);

	private static final Supplier<TrialMeter<Integer>>
	TRIAL_METER = () -> TrialMeter.of(
		"Steady fitness",
		"Create steady fitness performance measures",
		PARAMS,
		"Generation",
		"Fitness",
		"Runtime"
	);

	public static void main(final String[] args) throws InterruptedException {
		final Runner<Integer, BitGene, Double> runner = Runner.of(
			fitness -> KNAPSACK,
			Limits::bySteadyFitness,
			TRIAL_METER,
			Writer.text().map(Object::toString),
			Reader.text().map(Integer::parseInt),
			args
		);

		runner.start();
		runner.join();
	}

}
