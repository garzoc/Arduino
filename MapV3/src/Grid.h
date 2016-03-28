/*
 * Grid.h
 *
 *  Created on: Jan 12, 2016
 *      Author: john
 */

#ifndef GRID_H_
#define GRID_H_
#include <SDL2/SDL.h>
#include "GridPane.h"
#include "Car.h"
#include "CenterPoint.h"
#include "PathPane.h"
#include <iostream>
#include "Heap.h"
#include "SortedList.h"
#include <vector>

class Grid {
public:
	Grid(std::vector< std::vector<GridPane> > vec,int paneWidth,int paneHeight,int windowWidth,int windowHeight);
	Grid(std::vector< std::vector<GridPane> > vec,Car *car,int paneWidth,int paneHeight,int windowWidth,int windowHeight);
	//handles events related to the mouse
	void mouseEvent(SDL_Event event);
	virtual ~Grid();
	//this draws the grid
	void draw(SDL_Renderer* renderer);
	//Reste the grid and it's panes with default colors
	void resetGrid();
	//recolor one panes on the specified axis
	void addObstacle(int x,int y);
	//paint several panes with the cursor
	void paintPanes();
	//locates which panes the mouse is on
	void calculateMousePosition(int *x,int *y);
	//update the latest known position of the mouse
	void updateMousePosition();
	void eventUpdate(SDL_Event event);
	//test
	void test();
	//path findin function
	void findPath();

private:
	//not supporing changing size in the window once thwe programm is rinnung
	SDL_Rect r;
	//Rendering offset for the Vector
	double renderingX;
	double renderingY;
	//values for the window sizes
	int windowWidth;
	int windowHeight;
	//CHANGE THESE variables to scale the map
	int newPaneHeight;
	int newPaneWidth;
	//sizes of each individual panes DO NOT CHANGE THESE!
	int paneRenderingHeight;
	int paneRenderingWidth;
	//number of panes rendered on each axis
	int numberOfPanesXRendered;
	int numberOfPanesYRendered;
	//Tracking position of the mouse
	int lastMouseXPosition;
	int lastMouseYPosition;
	//map
	std::vector< std::vector<GridPane> > map;
	//center
	CenterPoint *centerPoint;
	//car
	Car *car;
	const Uint8 *keys;

};

#endif /* GRID_H_ */
