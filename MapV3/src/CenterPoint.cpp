/*
 * CenterPoint.cpp
 *
 *  Created on: Feb 22, 2016
 *      Author: john
 */

#include "CenterPoint.h"

CenterPoint::CenterPoint(int x, int y, int size) {
	// TODO Auto-generated constructor stub
	this->x = x;
	this->y = y;
	this->size = size;
	this->speed = 5;
}

CenterPoint::~CenterPoint() {
	// TODO Auto-generated destructor stub
}

void CenterPoint::draw(SDL_Renderer* renderer) {
	this->r.x = this->x;
	this->r.y = this->y;
	this->r.w = size;
	this->r.h = size;
	SDL_SetRenderDrawColor(renderer, 255, 0, 255, 255);
	SDL_RenderFillRect(renderer, &r);
}

void CenterPoint::setPosition(int x, int y) {
	this->x = x;
	this->y = y;
}

void CenterPoint::resize(int size) {
	this->size=size;
}

float CenterPoint::getX() {
	return this->x;
}

float CenterPoint::getY() {
	return this->y;
}

