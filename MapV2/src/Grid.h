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
 #include <iostream>

#include <vector>

class Grid {
public:
	Grid(std::vector< std::vector<GridPane> > vec,int paneWidth,int paneHeight,int windowWidth,int windowHeight);
	Grid(std::vector< std::vector<GridPane> > vec,Car *car,int paneWidth,int paneHeight,int windowWidth,int windowHeight);
	void mouseEvent(SDL_Event event);
	virtual ~Grid();
	//this draws the grid
	void draw(SDL_Renderer* renderer);
	//Reste the grid and it's panes with default colors
	void resetGrid();
	void addObstacle(int x,int y);
	void paintPanes();
	void calculateMousePosition(int *x,int *y);
	void updateMousePosition();
	void eventUpdate(SDL_Event event);
	void test();

private:
	//not supporing changing size in the window once thwe programm is rinnung
	SDL_Rect r;
	double renderingX;
	double renderingY;
	//values for the window sizes
	int windowWidth;
	int windowHeight;
	//CHANGE THESE variables to scale
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
	std::vector< std::vector<GridPane> > map;
	Car *car;
};

#endif /* GRID_H_ */
