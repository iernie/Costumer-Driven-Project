/*
 * Operators.c
 *
 *  Created on: Sep 18, 2011
 *      Author: ulfnore
 */

#include "Operators.h"
#include "Blotto.h"


/**********************/
/* SELECTION OPERATORS*/

int rouletteSelection(){
	int rnd = rand()/RAND_MAX * totalFitness;
	int idx = 0;
	while(rnd>=0 && idx<noIndividuals)
		rnd -= fitness[idx++];
	return idx;
}

int compare(void* a, void* b)
{
	return fitness[(size_t)a]-fitness[(size_t)b];
}

void sortByFitness()
{
	qsort(population,noIndividuals,sizeof(char*),&compare);
	qsort(individualSize,noIndividuals,sizeof(int),&compare);
	qsort(fitness,noIndividuals,sizeof(double),&compare);
}

int tournamentSelection(){
	int indA,indB,i;
	indA=rand()%noIndividuals;
	for(i = 0; i< tournamentSize; i++){
		do indB = rand()%noIndividuals; while(indB == indA);
		if(fitness[indB]>fitness[indA]) indA = indB;
	}
	return indA;
}

int sortingSelection()
{
	int i = 0;
	for(; i<noIndividuals;i++){
		fitness[i] = noIndividuals-i;
		totalFitness+=fitness[i];
	}
	int rnd = rand()%noIndividuals;
	i= 0;
	while(rnd>=0)
		rnd-=fitness[i++];
	return i;
}


/************************/
/* CROSSOVER & MUTATION */

char* randomCrossover(int indA, int indB){
	char* new = malloc(individualSize[indA]*primitiveSize);
	int i;
	for(i = 0; i< individualSize[indA]*primitiveSize; i++)
		new[i] = rand()/RAND_MAX>.5 ? population[indA][i] : population[indB][i];
	return new;
}

char* simpleSinglePointCrossover(int indA, int indB)
{
	int cp = rand()%individualSize[indA]*primitiveSize, i;
	char* new = malloc(individualSize[indA]*primitiveSize);
	for(i = 0; i< cp; i++)
		new[i] = population[indA][i];
	for(;i<individualSize[indA]; i++)
		new[i] = population[indB][i];
	return new;
}

char* bitFlipMutate(int idx)
{
	char* new = malloc(individualSize[idx]*primitiveSize);
	memcpy(new, population[idx],individualSize[idx]*primitiveSize);

	int i = rand()%individualSize[idx];
	if (new[i] == 1) new[i] = 0; else new[i] = 1;

	return new;
}

char* copy(int idx)
{
	char* new = malloc(individualSize[idx]*primitiveSize);
	memcpy(new,population[idx],individualSize[idx]*primitiveSize);
	return new;
}


// Operators assuming double representation
char* sumCrossover(int indA, int indB)
{
	// crossover by averaging doubles
	char* new = malloc(primitiveSize * individualSize[indA]);
	int i;
	double *d1, *d2;

	for(i = 0; i< individualSize[indA]; i++)
	{
		d1 = ((double*)population[indA])+i;
		d1 = ((double*)population[indB])+i;
		*(((double*)new)+i) = *d1+*d2;
	}

	return new;
}

char* gaussNoiseMutate(int idx)
{
	char* new = malloc(primitiveSize * individualSize[idx]);
	int i;
	double d;


	for(i  = 0; i< individualSize[idx]; i++)
	{
		d = *((double*)population[idx]+i);
		*(((double*)new)+i) = d+boxMillerRand()*scaleFactor;
	}

	return new;
}


//Aux
double boxMillerRand(){
	double r1, r2;
	r1 = rand()/(double)RAND_MAX;
	r2 = rand()/(double)RAND_MAX;
	return sqrt(-2.0 * r1) * cos(2.0* PI * r2);
}
