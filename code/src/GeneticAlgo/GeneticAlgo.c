/*
 * GeneticAlgo.c
 *
 *  Created on: Sep 9, 2011
 *      Author: ulfnore
 */
#include "GeneticAlgo.h"



void standardEvolutionCycle()
{
	int i, indA, indB;
	double crossover;
	double mutate;
	char** temp;

	// allocate new population;
	temp = malloc(noIndividuals * sizeof(char*));

	if (selectionRoutine == &sortingSelection)
		sortByFitness();

	for(i = 0; i< noIndividuals; i++)
	{

		// select an individual
		indA = selectionRoutine();

		crossover = (double)rand()/(double)RAND_MAX;
		mutate = (double)rand()/(double)RAND_MAX;

		if(mutate<mutateProb) temp[i] = mutateFunction(indA);
		else if(crossover<crossoverProb)
		{
			//select another individual
			do{	indB = selectionRoutine(); }while(indA != indB);
			temp[i] = crossoverFunction(indA, indB);
		}
		else temp[i] = copy(indA);
	}

	// clear previous population:
	for(i = 0; i< noIndividuals; i++)
		free(population[i]);
	free(population);
	population = temp;

}

void computeFitness(){
	int i;
	totalFitness = 0.0;
	maxFitness = 0.0;
	for(i  = 0; i< noIndividuals; i++){
		fitnessFunction(i);
		totalFitness += fitness[i];
		if (fitness[i]>maxFitness) maxFitness = fitness[i];
	}
}

void run()
{
	int generation = 0;
	srand(time(0));
	do
	{
		computeFitness();

		if(maxFitness>=targetFitness)
		{
			printf("\nSearch successfully terminated after %d generations.\n", generation);
			break;
		}

		if(generation>=maxGenerations)
		{
			printf("\nSearch halted with no solution.\n");
			break;
		}

		evolutionCycle();


		generation++;
	}while(1);
}


