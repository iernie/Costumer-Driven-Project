/*
 * GeneticAlgo.h
 *
 *  Created on: Sep 18, 2011
 *      Author: ulfnore
 */

#ifndef GENETICALGO_H_
#define GENETICALGO_H_


#include <stdlib.h>
#include <stdio.h>

#include "Operators.h"
#include "FitnessFunction.h"


// population
char** population;
char** elite;
int* individualSize;
double* fitness;

// Algorithm Parameters
int elitism;
double mutateProb;
double crossoverProb;
int tournamentSize;

// Population Parameters
int primitiveSize; // if using floats as primitives: sizeof(float)
int noIndividuals;
int initIndividualSize; // number of primitives per individual

// Statistics
double targetFitness;
double totalFitness;
int maxFitness;
int maxGenerations;


// Operators defined as global variables (function ptrs)
double (*fitnessFunction)(int);
char* (*crossoverFunction)(int,int);
char* (*mutateFunction)(int);
void (*evolutionCycle)(void);
int (*selectionRoutine)(void);


void print();
void standardEvolutionCycle();
void computeFitness();
void run();


#endif /* GENETICALGO_H_ */
