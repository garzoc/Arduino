/*
 * Car.cpp
 *
 *  Created on: Feb 17, 2016
 *      Author: john
 */

#include "Car.h"

Car::Car(int x, int y,int size) {
	// TODO Auto-generated constructor stub
	this->x = x;
	this->y = y;
	this->size=size;
	this->speed = 5;
}

Car::~Car() {
	// TODO Auto-generated destructor stub
}

void Car::update(SDL_Event event) {

	const Uint8 *keys = SDL_GetKeyboardState(NULL);
	if (keys[SDL_SCANCODE_A]) {
		this->x -= speed;
	}
	if (keys[SDL_SCANCODE_D]) {
		this->x += this->speed;
	}

	if (keys[SDL_SCANCODE_W]) {
		this->y -= this->speed;
	}
	if (keys[SDL_SCANCODE_S]) {
		this->y += this->speed;
	}

}

void Car::draw(SDL_Renderer* renderer) {
	this->r.x = this->x;
	this->r.y = this->y;
	this->r.w = size;
	this->r.h = size;
	SDL_SetRenderDrawColor(renderer, 0, 255, 255, 255);
	SDL_RenderFillRect(renderer, &r);
}

void Car::setPosition(int x, int y){
	this->x=x;
	this->y=y;
}

void Car::resize(int size){
	this->size=size;
}

float Car::getX(){
	return this->x;
}

float Car::getY(){
	return this->y;
}
