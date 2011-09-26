/*
 * FitnessFunction.c
 *
 *  Created on: Sep 18, 2011
 *      Author: ulfnore
 */


#include "FitnessFunction.h"
double sumFitnessFunction(int ind_idx)
{
	/* Fitness function for one-max problem */
	int i;
	char* ch = population[ind_idx];
	fitness[ind_idx] = 0.0;
	for(i = 0; i< individualSize[ind_idx]; i++)
		if(ch[i] == 1) fitness[ind_idx] += 1.0;
	return fitness[ind_idx];
}

double globalBlottoFitnessFunction(int ind_idx)
{
	/*
	 * Computes the fitness of an individual as the number
	 * of battles won.
	 */
	int i=0,j=0;
	double* d;
	fitness[ind_idx] = 0.0;

	for(; i< noIndividuals && i != ind_idx; i++)
		for(d = population[i]; d<population[i]+noIndividuals; d++)
		{
			if (*d<*((double*)population[ind_idx])+j)
				fitness[ind_idx] += 1.0;
			j++;
		}
	return fitness[ind_idx];
}
