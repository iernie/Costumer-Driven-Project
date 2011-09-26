/*
 * Operators.c
 *
 *  Created on: Sep 18, 2011
 *      Author: ulfnore
 */

#ifndef OPERATORS
#define OPERATORS

#define PI 3.141596535


#include <stdlib.h>
#include <math.h>
#include "GeneticAlgo.h"




/* SELECTION OPERATORS */
/* Returning an index to the population array*/
int rouletteSelection();
int compare(void* a, void* b);
void sortByFitness();
int tournamentSelection();
int sortingSelection();


/************************/
/* CROSSOVER & MUTATION */
/* Returning a new individual as a char array */
char* randomCrossover(int indA, int indB);
char* simpleSinglePointCrossover(int indA, int indB);
char* bitFlipMutate(int idx);
char* copy(int idx);

// Operators assuming double representation
char* sumCrossover(int indA, int indB);
char* gaussNoiseMutate(int idx);

// Aux
double boxMillerRand();

#endif
