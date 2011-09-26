/*
 * OneMax.h
 *
 *  Created on: Sep 20, 2011
 *      Author: ulfnore
 */

#ifndef ONEMAX_H_
#define ONEMAX_H_

void initializeOneMax(int noIndivs, int initialIndividualSize);
void testOneMax(int indSize,
				int popnSize,
				double coProb,
				double mutProb,
				char* (*crossoverFn)(int,int),
				int (*selectionFn)(void));

#endif /* ONEMAX_H_ */
