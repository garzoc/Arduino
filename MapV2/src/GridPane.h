/*
 * GridPane.h
 *
 *  Created on: Jan 11, 2016
 *      Author: john
 */

#ifndef GRIDPANE_H_
#define GRIDPANE_H_

class GridPane {
public:
	GridPane();
	GridPane(int x,int y,int width,int height);
	virtual ~GridPane();
	int getWidth();
	int getHeight();
	//color handling for the panes
	void setRGBColor(int r,int g,int b);
	int getRed();
	int getGreen();
	int getBlue();
	int getX();
	int getY();
private:
	int x;
	int y;
	int height;
	int width;
	int rgbR;
	int rgbG;
	int rgbB;
};

#endif /* GRIDPANE_H_ */
