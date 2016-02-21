#include "GridPane.h"

GridPane::GridPane() {
	this->x = 0;
	this->y = 0;
	this->width = 0;
	this->height = 0;
	this->rgbR = 0;
	this->rgbG = 0;
	this->rgbB = 0;

}

GridPane::GridPane(int x, int y, int width, int height) {
	this->x = x;
	this->y = y;
	this->width = width;
	this->height = height;
	this->rgbR = 0;
	this->rgbG = 0;
	this->rgbB = 0;
}

int GridPane::getWidth() {
	return this->width;
}

int GridPane::getHeight() {
	return this->height;
}

//color handling for the panes
void GridPane::setRGBColor(int r, int g, int b) {
	this->rgbR = r;
	this->rgbG = g;
	this->rgbB = b;
}

int GridPane::getRed() {
	return this->rgbR;
}

int GridPane::getGreen() {
	return this->rgbG;
}

int GridPane::getBlue() {
	return this->rgbB;
}

int GridPane::getX() {
	return this->x;
}

int GridPane::getY() {
	return this->y;
}

GridPane::~GridPane() {
	// TODO Auto-generated destructor stub
}

