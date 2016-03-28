#include <SDL2/SDL.h>
#include "GridPane.h"
#include "Grid.h"
#include "CenterPoint.h"
#include <iostream>
#include <vector>

int main(int argc, char* argv[]) {
	// Start SDL2
	SDL_Init(SDL_INIT_EVERYTHING);
	int defaultWindowWidth = 640;
	int defaultWindowHeight = 480;
	int defaultPaneWidth = 10;
	int defaultPaneHeight = 10;
	// Create a Window in the middle of the screen
	std::vector<std::vector<GridPane> > vec;
	int id=0;
	for (int x = 0; x < (defaultWindowWidth / defaultPaneWidth) * 3; x++) {
		std::vector<GridPane> row; // Create an empty row
		for (int y = 0; y < (defaultWindowHeight / defaultPaneHeight) * 3;y++) {
			row.push_back(GridPane(id,x, y ,defaultPaneWidth - 1, defaultPaneHeight - 1));
			id++;
			if (x % 2 == 0) {
				if (y % 2 == 0) {
					row[y].setRGBColor(0,0,0);

				} else {
					row[y].setRGBColor(255,255,255);

				}
			} else {
				if (y % 2 == 0) {
					row[y].setRGBColor(255,255,255);

				} else {
					row[y].setRGBColor(0,0,0);

				}
			}
			//row[y].setRGBColor(100,10+y*3,10+x);
		}
		vec.push_back(row); // Add the row to the main vector
	}

	//Create grid
	Grid grid(vec, new Car(defaultWindowWidth / 2, defaultWindowHeight / 2,6),defaultPaneWidth,defaultPaneHeight,defaultWindowWidth,defaultWindowHeight);
	//clear vector
	vec.clear();
	vec.resize(0);
	//create window
	SDL_Window *window = 0;
	window = SDL_CreateWindow("Map",
	SDL_WINDOWPOS_CENTERED,
	SDL_WINDOWPOS_CENTERED, defaultWindowWidth, defaultWindowHeight,
			SDL_WINDOW_SHOWN);
	SDL_Renderer* renderer;
	renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED|SDL_RENDERER_PRESENTVSYNC);
	SDL_Event event;

	while (event.type != SDL_QUIT) {

		while (SDL_PollEvent(&event)) {
			grid.mouseEvent(event);
		}
		SDL_SetRenderDrawColor(renderer, 200, 200, 200, 255);
		SDL_RenderClear(renderer);
		grid.draw(renderer);
		grid.eventUpdate(event);
		SDL_RenderPresent(renderer);

		int tick = SDL_GetTicks();

		int gLastTick = 0;
		if ((tick - gLastTick) > 100) // one frame every 100 milliseconds, IE 10 frames per second
				{
			// set next frame as current frame
			gLastTick = tick;
			SDL_Delay(1);
		}

	}

	// Cleanup and Quit

	SDL_DestroyRenderer(renderer);
	SDL_DestroyWindow(window);
	SDL_Quit();

	return 0;
}

