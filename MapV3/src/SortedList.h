/*
 * SortedList.h
 *
 *  Created on: Mar 26, 2016
 *      Author: john
 */

#ifndef SORTEDLIST_H_
#define SORTEDLIST_H_
#include "PathPane.h"
#include <iostream>
#include <math.h>

/*
 * To make the look upp even quicker in lower memory cost of the heap store chechup values directly in the maps
 * Gridpanes
 * And have a revision value like a how many time used path has been used so that
 * we don't have to oerwrite the old values from the previous finder.
 *
 * */
class SortedList {
public:
	SortedList(int maxSize);
	virtual ~SortedList();

	int getSize();
	void add(PathPane node);
	int exists(PathPane node);
	PathPane get(int x);
private:
	PathPane **closedNodes;
	int size;
};

#endif /* SORTEDLIST_H_ */
