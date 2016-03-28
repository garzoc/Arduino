/*
 * Grid.cpp
 *
 *  Created on: Jan 12, 2016
 *      Author: john
 */

#include "Grid.h"
#include <SDL2/SDL.h>
//#include "GridPane.h"
#include <string.h>
#include <math.h>

Grid::Grid(std::vector<std::vector<GridPane> > vec,int paneWidth,int paneHeight,int windowWidth,int windowHeight) {
	this->map = vec;
	this->centerPoint = 0;
	this->car=0;
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
	keys = SDL_GetKeyboardState(NULL);
}

Grid::Grid(std::vector<std::vector<GridPane> > vec, Car *car,int paneWidth,int paneHeight,int windowWidth,int windowHeight) {
	this->map = vec;
	this->centerPoint = new CenterPoint((windowWidth/2),(windowHeight/2),10);
	this->car=car;
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
	keys = SDL_GetKeyboardState(NULL);
}

Grid::~Grid() {
	delete centerPoint;
	map.clear();
	map.resize(0);
	keys=NULL;
	delete keys;
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
			this->findPath();
			break;
		case SDL_MOUSEWHEEL:
			if(this->newPaneHeight+event.wheel.y>3&&this->newPaneWidth+event.wheel.y>3){
				this->newPaneWidth+=event.wheel.y;
				this->newPaneHeight+=event.wheel.y;
			}
			break;
	}
}

void Grid::eventUpdate(SDL_Event event) {
	//this->CenterPoint->update(event);

	keys = SDL_GetKeyboardState(NULL);
	if (keys[SDL_SCANCODE_A]) {
		this->renderingX -= 1;
		this->car->setRelativePosition(this->paneRenderingWidth,0);
	}
	if (keys[SDL_SCANCODE_D]) {
		this->renderingX += 1;
		this->car->setRelativePosition(-this->paneRenderingWidth,0);
	}

	if (keys[SDL_SCANCODE_W]) {
		this->renderingY -= 1;
		this->car->setRelativePosition(0,this->paneRenderingHeight);
	}
	if (keys[SDL_SCANCODE_S]) {
		this->renderingY += 1;
		this->car->setRelativePosition(0,-this->paneRenderingHeight);
	}

	if (keys[SDL_SCANCODE_T]) {
			this->test();
		}
	/*if (keys[SDL_SCANCODE_P]) {
			//zSDL_Delay(500);
			if (SDL_GetMouseState(NULL, NULL) & SDL_BUTTON(SDL_BUTTON_LEFT)){
				this->findPath();
			}
			}*/

}
void Grid::test(){
	std::cout<<this->paneRenderingWidth<<std::endl;
	std::cout<<floor((this->car->getX()-this->centerPoint->getX())/this->paneRenderingWidth)*this->newPaneWidth<<std::endl;
	for(int i=this->centerPoint->getX()+1;this->car->getX()<i;i-=this->paneRenderingHeight){
		int x=floor(i/this->paneRenderingWidth)+this->renderingX;
		int y=floor(this->centerPoint->getY()/this->paneRenderingHeight)+this->renderingY;
		map[x][y].setRGBColor(255,0,0);
	}
}

void Grid::findPath(){
	keys = SDL_GetKeyboardState(NULL);
	if (keys[SDL_SCANCODE_P]) {
		int mouseX, mouseY;
		this->calculateMousePosition(&mouseX,&mouseY);
		int carX, carY;
		carX=floor(car->getX()/this->paneRenderingWidth)+this->renderingX;
		carY=floor(car->getY()/this->paneRenderingHeight)+this->renderingY;
		Heap heap(10000);
		SortedList list(10000);
		int distanceToNextTarget=0;
		PathPane startNode(map[carX][carY],0,(abs(carX-mouseX)+abs(carY-mouseY))*10);
		//PathPane startNode1(map[carX-1][carY],0,3);
		//PathPane startNode2(map[carX+2][carY],0,3);
		heap.add(startNode);
		list.add(startNode);
		PathPane currentNode(startNode);
		std::cout<<"X "<<startNode.getX()<<" Y "<<startNode.getY()<<" F "<<startNode.getF()<<std::endl;
		int k=0;
		while(((currentNode=PathPane(heap.poll())).getX()!=mouseX||currentNode.getY()!=mouseY)){
			k++;
			for(int x=currentNode.getX()-1;x<currentNode.getX()+2;x++){
				for(int y=currentNode.getY()-1;y<currentNode.getY()+2;y++){
					if(y<map.size()&&x<map[0].size()&&x>=0&&y>=0){
						//compare coordinates to our current position and create new
						//distances. 14 for dioganal movement and 10 for linear.
						distanceToNextTarget=x==currentNode.getX()||y==currentNode.getY()?10:14;
						PathPane node(map[x][y],currentNode.walkedDistance()+distanceToNextTarget,(abs(x-mouseX)+abs(y-mouseY))*10,currentNode);

						if(list.exists(node)<0&&(x!=currentNode.getX()||y!=currentNode.getY())){
							heap.add(node);
						}
					}else{
						std::cout<<"\n"<<" "<<"Error"<<" "<<"OUTSIDE MAP BOUNDRIES"<<std::endl;
					}
				}
			}

			if(list.exists(currentNode)<0){
				//map[currentNode.getX()][currentNode.getY()].setRGBColor(0,0,255);
				list.add(currentNode);
			}
		}


		std::cout<<"SortedList "<<list.getSize()<<std::endl;
		std::cout<<"Heap "<<heap.getSize()<<std::endl;
		std::cout<<"Map "<<map[0].size()*map.size()<<std::endl;


		PathPane** path=currentNode.getPath(new PathPane*[100],0);
		for(int i=0;0!=path[i];i++){
			//std::cout<<"X "<<path[i]->getX()<<" Y "<<path[i]->getY()<<" F "<<path[i]->getF()<<" Getting path \n";
			map[path[i]->getX()][path[i]->getY()].setRGBColor(0,255,0);
			delete path[i];
			//std::cout<<"END:"<<std::endl;
		}
		delete[] path;

	}//end of if statement

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


void Grid::draw(SDL_Renderer* renderer) {
	//chech if we're going to scale
	if(this->paneRenderingHeight!=this->newPaneHeight||this->paneRenderingWidth!=this->newPaneWidth){
		//properly reposition the car
		this->car->setAbsolutePosition(this->centerPoint->getX()+floor((this->car->getX()-this->centerPoint->getX())/this->paneRenderingWidth)*this->newPaneWidth,this->centerPoint->getY()+floor((this->car->getY()-this->centerPoint->getY())/this->paneRenderingHeight)*this->newPaneHeight);
		//move the center point to the top-left corner
		this->renderingX=floor(this->centerPoint->getX()/this->paneRenderingWidth)+this->renderingX;
		this->renderingY=floor(this->centerPoint->getY()/this->paneRenderingHeight)+this->renderingY;
		//scale the panes
		this->numberOfPanesXRendered=ceil((float)this->windowWidth/this->newPaneWidth);
		this->paneRenderingWidth=this->newPaneWidth;
		this->numberOfPanesYRendered=ceil((float)this->windowHeight/this->newPaneHeight);
		this->paneRenderingHeight=this->newPaneHeight;
		//ones the grid has been scaled move the top-left corner to the center points
		this->renderingX=this->renderingX-(this->windowWidth/this->newPaneWidth)/2;
		this->renderingY=this->renderingY-(this->windowHeight/this->newPaneHeight)/2;
		//rezise the center-point to match
		//this->centerPoint->resize(this->paneRenderingHeight-4);
		this->car->resize(this->paneRenderingHeight-4);
	}
	//(this->renderingX%this->paneRenderingWidth)
	//Draw method
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
	if (this->centerPoint != 0) {
		centerPoint->draw(renderer);
	}
	if(this->car!=0){
		car->draw(renderer);
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

