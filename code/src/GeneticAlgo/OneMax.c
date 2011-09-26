/*
 * OneMax.c
 *
 *  Created on: Sep 20, 2011
 *      Author: ulfnore
 */

#include "GeneticAlgo.h"

void initializeOneMax(int noIndivs, int initialIndividualSize)
{
	int i,j;
	// Set parameters
	primitiveSize = 1;
	noIndividuals = noIndivs;
	initIndividualSize = initialIndividualSize;

	// Allocate memory
	population = malloc(sizeof(char*) * noIndividuals);
	fitness = malloc(sizeof(double) * noIndividuals);
	individualSize = malloc(sizeof(int) * noIndividuals);

	for(i= 0; i< noIndividuals; i++)
	{
		population[i] = malloc(initialIndividualSize);
		for (j = 0; j< initialIndividualSize; j++)
			population[i][j] = rand()%2;
		fitness[i] = 0.0;
		individualSize[i] = initialIndividualSize;
	}
}

void oneMaxPrint()
{
	/* Print the population as a character stream */
	int i,j;
	for (i  = 0; i< noIndividuals; i++)
	{
		for(j = 0; j< individualSize[i] * primitiveSize; j++)
			printf("%d", population[i][j] == 1 ? 1 : 0);
		printf(": %f\n", fitness[i]);

	}
}

void testOneMax(int indSize,
		int popnSize,
		double coProb,
		double mutProb,
		char* (*crossoverFn)(int,int),
		int (*selectionFn)(void))
{
	srand(time(0));
	initializeOneMax(indSize,popnSize);
	mutateProb = mutProb;
	crossoverProb = coProb;
	maxGenerations = 100;
	targetFitness = popnSize;
	tournamentSize = popnSize/3;

	crossoverFunction = crossoverFn;
	selectionRoutine = selectionFn;
	fitnessFunction = &sumFitnessFunction;
	mutateFunction = &bitFlipMutate;
	evolutionCycle = &standardEvolutionCycle;

	computeFitness();
	printf("Initial population:\n");
	oneMaxPrint();
	run();
	printf("\n");
	oneMaxPrint();
}


//main(){
//	testOneMax(10,10,.9,.2,
//			simpleSinglePointCrossover,
//			tournamentSelection);
//}
