/*
 * Car.h
 *
 *  Created on: Feb 17, 2016
 *      Author: john
 */

#ifndef CAR_H_
#define CAR_H_
#include <SDL2/SDL.h>

class Car {
public:
	//Car();
	Car(int x,int y,int size);
	virtual ~Car();
	void update(SDL_Event);
	void resize(int size);
	void setPosition(int x ,int y);
	void draw(SDL_Renderer* renderer);
	float getX();
	float getY();
private:
	SDL_Rect r;
	int size;
	float x;
	float y;
	float speed;
};

#endif /* CAR_H_ */
