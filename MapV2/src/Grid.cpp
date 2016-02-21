/*
 * Grid.cpp
 *
 *  Created on: Jan 12, 2016
 *      Author: john
 */

#include "Grid.h"
#include <SDL2/SDL.h>
//#include "GridPane.h"
#include <iostream>
#include <string.h>
#include <math.h>

Grid::Grid(std::vector<std::vector<GridPane> > vec,int paneWidth,int paneHeight,int windowWidth,int windowHeight) {
	this->map = vec;
	this->car = 0;
	this->renderingX = 30;
	this->renderingY = 30;
	this->windowWidth=windowWidth;
	this->windowHeight=windowHeight;
	this->paneRenderingHeight=paneHeight;
	this->paneRenderingWidth=paneWidth;
	this->newPaneHeight=paneHeight;
	this->newPaneWidth=paneWidth;
	this->numberOfPanesXRendered=windowWidth/paneWidth;
	this->numberOfPanesYRendered=windowHeight/paneHeight;
	this->calculateMousePosition(&lastMouseXPosition,&lastMouseYPosition);
}

Grid::Grid(std::vector<std::vector<GridPane> > vec, Car *car,int paneWidth,int paneHeight,int windowWidth,int windowHeight) {
	this->map = vec;
	this->car = car;
	this->renderingX = 10;
	this->renderingY = 10;
	this->windowWidth=windowWidth;
	this->windowHeight=windowHeight;
	this->paneRenderingHeight=paneHeight;
	this->paneRenderingWidth=paneWidth;
	this->newPaneHeight=paneHeight;
	this->newPaneWidth=paneWidth;
	this->numberOfPanesXRendered=windowWidth/paneWidth;
	this->numberOfPanesYRendered=windowHeight/paneHeight;
	this->calculateMousePosition(&lastMouseXPosition,&lastMouseYPosition);
}

Grid::~Grid() {
	delete car;
	map.clear();
	map.resize(0);
}

void Grid::draw(SDL_Renderer* renderer) {
	if(this->paneRenderingHeight!=this->newPaneHeight||this->paneRenderingWidth!=this->newPaneWidth){
		//move the center point to the top-left corner
		this->renderingX=floor(this->car->getX()/this->paneRenderingWidth)+this->renderingX;
		this->renderingY=floor(this->car->getY()/this->paneRenderingHeight)+this->renderingY;
		//scale the panes
		this->numberOfPanesXRendered=ceil((float)this->windowWidth/this->newPaneWidth);
		this->paneRenderingWidth=this->newPaneWidth;
		this->numberOfPanesYRendered=ceil((float)this->windowHeight/this->newPaneHeight);
		this->paneRenderingHeight=this->newPaneHeight;
		//ones the grid has been scaled move the top-left corner to the center point
		this->renderingX=this->renderingX-(this->windowWidth/this->newPaneWidth)/2;
		this->renderingY=this->renderingY-(this->windowHeight/this->newPaneHeight)/2;
		this->car->resize(this->paneRenderingHeight-4);
	}
	for (int x = 0; x < this->numberOfPanesXRendered; x++) {
		for (int y = 0; y < this->numberOfPanesYRendered; y++) {
			if (this->renderingX + x < map.size()&& this->renderingY + y < map[x].size()&& renderingX + x >= 0 && renderingY + y >= 0) {
				this->r.x = x * this->paneRenderingWidth;
				this->r.y = y * this->paneRenderingHeight;
				this->r.w =this->paneRenderingWidth;
				this->r.h =this->paneRenderingHeight;
				SDL_SetRenderDrawColor(renderer,
						map[this->renderingX + x][this->renderingY + y].getRed(),
						map[this->renderingX + x][this->renderingY + y].getGreen(),
						map[this->renderingX + x][this->renderingY + y].getBlue(),
						255);
				SDL_RenderFillRect(renderer, &r);
			}else{

			}
		}
	}
	if (this->car != 0) {
		car->draw(renderer);
	}

}



void Grid::eventUpdate(SDL_Event event) {
	//this->car->update(event);

	const Uint8 *keys = SDL_GetKeyboardState(NULL);
	if (keys[SDL_SCANCODE_A]) {
		this->renderingX -= 1;
	}
	if (keys[SDL_SCANCODE_D]) {
		this->renderingX += 1;
	}

	if (keys[SDL_SCANCODE_W]) {
		this->renderingY -= 1;
	}
	if (keys[SDL_SCANCODE_S]) {
		this->renderingY += 1;
	}
}
void Grid::test(){
	std::cout<<this->windowHeight/this->newPaneHeight<<std::endl;
}


void Grid::addObstacle(int x,int y){
	map[x][y].setRGBColor(255,0,0);
}


void Grid::paintPanes(){
	if (SDL_GetMouseState(NULL, NULL) & SDL_BUTTON(SDL_BUTTON_LEFT)){
		int x;
		int y;
		this->calculateMousePosition(&x,&y);
		int deltaY=y-lastMouseYPosition;
		int deltaX=x-lastMouseXPosition;
		int hypotenuse=sqrt(pow(deltaX, 2) + pow(deltaY, 2));
		int sqr2=1;
		if (hypotenuse > sqr2) {
			for(float i=1;i<hypotenuse+1;i+=sqr2){
				int deltax=(i/hypotenuse)*deltaX;
				int deltay=(i/hypotenuse)*deltaY;
				int x2=lastMouseXPosition+deltax;
				int y2=lastMouseYPosition+deltay;
				this->addObstacle(x2,y2);
			}
		}

		updateMousePosition();
		this->addObstacle(x,y);
	}
}

void Grid::updateMousePosition(){
	int x;
	int y;
	this->calculateMousePosition(&x,&y);
	this->lastMouseXPosition=x;
	this->lastMouseYPosition=y;
}

void Grid::calculateMousePosition(int *x,int *y){
	SDL_GetMouseState(x,y);
	*x=floor(*x/this->paneRenderingWidth)+this->renderingX;
	*y=floor(*y/this->paneRenderingHeight)+this->renderingY;
}

void Grid::mouseEvent(SDL_Event event){
	int x;
	int y;
	switch(event.type){
			case SDL_MOUSEMOTION:
				this->paintPanes();
				break;
			case SDL_MOUSEBUTTONDOWN:
				this->updateMousePosition();
				this->calculateMousePosition(&x,&y);
				this->addObstacle(x,y);
				break;
			case SDL_MOUSEWHEEL:
				if(this->newPaneHeight+event.wheel.y>3&&this->newPaneWidth+event.wheel.y>1){
					this->newPaneWidth+=event.wheel.y;
					this->newPaneHeight+=event.wheel.y;
				}
				break;
		}
}



void Grid::resetGrid() {
	/*panes = new GridPane **[640/10];
	 for (int x = 0; x < 640/10; x++) {
	 panes[x] = new GridPane *[480/10];
	 for(int y=0;y<480/10;y++){
	 panes[x][y] = new GridPane(10,10,8,8);
	 //170+y ,170+y ,255-y
	 //255-y*2,100+x*2,y/(x+1)
	 //hej
	 panes[x][y]->setRGBColor(255-y*2,100+x*2,y/(x+1));
	 }

	 }*/
}

