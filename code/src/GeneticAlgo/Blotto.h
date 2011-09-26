/*
 * Blotto.h
 *
 *  Created on: Sep 20, 2011
 *      Author: ulfnore
 */

#ifndef BLOTTO_H_
#define BLOTTO_H_

#include "GeneticAlgo.h"
#include "Operators.h"
#include "FitnessFunction.h"
#include <stdlib.h>
#include <stdio.h>

double scaleFactor;

void initialzeBlotto(int noIndivs, int initialIndividualSize, double totalSoldiers);
void printBlotto();
void testBlotto(int indSize,
				int popnSize,
				double coProb,
				double mutProb,
				char* (*crossoverFn)(int,int),
				int (*selectionFn)(void));

#endif /* BLOTTO_H_ */
