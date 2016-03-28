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
	GridPane(int id,int x,int y,int width,int height);
	GridPane(int x,int y,int width,int height);
	virtual ~GridPane();
	//size
	int getWidth();
	int getHeight();
	//color handling for the panes
	void setRGBColor(int r,int g,int b);
	int getRed();
	int getGreen();
	int getBlue();
	//positioning
	int getX();
	int getY();
	int getID();
private:
	//used in pathfinding for sorting and lookup
	int id;
	//currently not used anywhere
	int x;
	int y;
	int height;
	int width;
	//#############
	//colors
	int rgbR;
	int rgbG;
	int rgbB;
};

#endif /* GRIDPANE_H_ */
