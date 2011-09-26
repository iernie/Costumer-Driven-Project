/*
 * Blotto.c
 *
 *  Created on: Sep 20, 2011
 *      Author: ulfnore
 */
#include "Blotto.h"



void initializeBlotto(int noIndivs, int initialIndividualSize, double totalSoldiers)
{
	int i,j;
	double* d;
	double soldiersLeft;

	primitiveSize = sizeof(double);
	noIndividuals = noIndivs;
	initIndividualSize = initialIndividualSize;

	population = malloc(sizeof(char*) * noIndividuals);
	fitness = malloc(sizeof(double) * noIndividuals);
	individualSize = malloc(sizeof(int) * noIndividuals);



	for(i= 0; i< noIndividuals; i++)
	{
		soldiersLeft = totalSoldiers;

		population[i] = malloc(initialIndividualSize*primitiveSize);
		individualSize[i] = initialIndividualSize;
		fitness[i] = 0.0;

		for(j = 0; j< individualSize[i]; j++)
		{
			d = (double*)(population[i])+j;
			if(soldiersLeft>0)
			{
				if (j == individualSize[i] - 1)
				{
					*d = soldiersLeft;
					soldiersLeft = 0.0;
				}
				else
				{
					*d = (double)(rand()%(int)soldiersLeft);
					soldiersLeft -= *d;
				}
			}
			else *d = 0.0;
		}

	}

}

void printBlotto()
{
	/* for printing a population of blotto individuals */

	int i = 0,j = 0;
	double *d;

	for(;i<noIndividuals; i++)
	{
		for(j = 0; j<individualSize[i]; j++){
			d = ((double*)(population[i]))+j;
			printf("%f ", *d);
		}
		printf(" %f\n", fitness[i]);
	}

}


void testBlotto(int indSize,
				int popnSize,
				double coProb,
				double mutProb,
				char* (*crossoverFn)(int,int),
				int (*selectionFn)(void))
{
	srand(time(0));
	double totalSoldiers = 100.0;
	scaleFactor = totalSoldiers/10;

	initializeBlotto(indSize,popnSize,totalSoldiers);

	mutateProb = mutProb;
	crossoverProb = coProb;
	maxGenerations = 100;
	targetFitness = popnSize;
	tournamentSize = popnSize/3;

	crossoverFunction = crossoverFn;
	selectionRoutine = selectionFn;
	fitnessFunction = globalBlottoFitnessFunction;
	mutateFunction = bitFlipMutate;
	evolutionCycle = standardEvolutionCycle;

	computeFitness();
	printf("Initial population:\n");
	printBlotto();
	run();
	printf("\n");
	printBlotto();
}

main()
{
	testBlotto(2,2,.9,.9,
			simpleSinglePointCrossover,
			tournamentSelection);
}
