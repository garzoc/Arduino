/*
 * PathPane.h
 *
 *  Created on: Mar 25, 2016
 *      Author: john
 */

#ifndef PATHPANE_H_
#define PATHPANE_H_
#include "GridPane.h"
#include <iostream>

class PathPane:public GridPane {
public:
	//PathPane();
	PathPane(int paneId,int x, int y, int width, int height,int sourceDistance, int targetDistance);
	PathPane(GridPane pane,int sourceDistance, int targetDistance);
	PathPane(GridPane pane,int sourceDistance, int targetDistance,PathPane parent);
	int getF();
	int walkedDistance();
	int distanceLeft();
	PathPane** getPath(PathPane** path,int size);
	virtual ~PathPane();
private:
	PathPane *parent;
	//how far is this from the start node
	int sourceDistance;
	//how far away is the target node
	int targetDistance;
};

#endif /* PATHPANE_H_ */
