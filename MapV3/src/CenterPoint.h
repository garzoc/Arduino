/*
 * CenterPoint.h
 *
 *  Created on: Feb 22, 2016
 *      Author: john
 */

#ifndef CENTERPOINT_H_
#define CENTERPOINT_H_
#include <SDL2/SDL.h>

class CenterPoint {
public:
	CenterPoint(int x,int y,int size);
	virtual ~CenterPoint();
	void resize(int size);
	void setPosition(int x, int y);
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

#endif /* CENTERPOINT_H_ */
