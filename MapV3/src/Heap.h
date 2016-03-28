/*
 * Heap.h
 *
 *  Created on: Mar 25, 2016
 *      Author: john
 */

#ifndef HEAP_H_
#define HEAP_H_
#include "PathPane.h"
#include <math.h>
#include <iostream>

class Heap {
public:
	Heap(int maxHepSize);
	virtual ~Heap();
	PathPane poll();
	PathPane peek();
	void add(PathPane node);
	int getSize();
	void printHeapF();
private:
	void sortUp(PathPane node);
	PathPane sortDown();
	void swap(int parent, int child);
	int size;
	PathPane** nodes;

};

#endif /* HEAP_H_ */
