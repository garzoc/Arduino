/*
 * PathPane.cpp
 *
 *  Created on: Mar 25, 2016
 *      Author: john
 */

#include "PathPane.h"

PathPane::PathPane(int paneId,int x, int y, int width, int height,int sourceDistance, int targetDistance):GridPane(paneId,x, y, width,  height){
	// TODO Auto-generated constructor stub
	this->targetDistance=targetDistance;
	this->sourceDistance=sourceDistance;
	this->parent=0;
}

PathPane::PathPane(GridPane pane ,int sourceDistance, int targetDistance):GridPane(pane){
	// TODO Auto-generated constructor stub
	this->targetDistance=targetDistance;
	this->sourceDistance=sourceDistance;
	this->parent=0;
}

PathPane::PathPane(GridPane pane ,int sourceDistance, int targetDistance,PathPane parent):GridPane(pane){
	// TODO Auto-generated constructor stub
	this->targetDistance=targetDistance;
	this->sourceDistance=sourceDistance;
	this->parent=new PathPane(parent);


}

PathPane::~PathPane() {
	// TODO Auto-generated destructor stub
	//some children might be deallocated before this one
	//so reallocate the memory of the lost children and then clear them.
		this->parent=0;
		delete this->parent;

}

int PathPane::getF(){
	return this->sourceDistance+this->targetDistance;
}

int PathPane::walkedDistance(){
	return this->sourceDistance;
}

int PathPane::distanceLeft(){
	return this->targetDistance;
}

PathPane** PathPane::getPath(PathPane** path,int size){
	if(this->parent==0){
		std::cout<<"hej "<<size<<std::endl;
		path[size]=new PathPane(*this);
		path[size+1]=0;
		return path;
	}else{
		this->parent->getPath(path,size+1);
		path[size]=new PathPane(*this);
		return path;
	}

}

